package pl.pronux.sokker.actions;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.LeagueDao;
import pl.pronux.sokker.downloader.managers.LeagueXmlManager;
import pl.pronux.sokker.downloader.managers.MatchXmlManager;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;

public final class MatchesManager {
	
	public static final int OK = 0;
	public static final int ERROR_DOESNT_CONTAIN = 2;
	public static final int ERROR_ALREADY_EXIST = 3;

	private static MatchesManager instance = new MatchesManager();

	private MatchesManager() {
	}

	public static MatchesManager getInstance() {
		return instance;
	}

	public void importMatches(List<Match> matches) throws SQLException {
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		for (Match match : matches) {
			if (leagueDao.existsMatch(match.getMatchId())) {
				leagueDao.updateMatch(match);
			} else {
				leagueDao.addMatch(match);
			}

			if (match.getIsFinished() == Match.FINISHED) {
				if (!leagueDao.existsTeamStats(match.getMatchId(), match.getAwayTeamId()) && match.getAwayTeamStats() != null) {
					leagueDao.addTeamStats(match.getMatchId(), match.getAwayTeamStats());
					List<PlayerStats> stats = match.getAwayTeamStats().getPlayersStats();
					for (PlayerStats playerStats : stats) {
						leagueDao.addPlayersStats(match.getMatchId(), match.getAwayTeamId(), playerStats);
					}
				}
				if (!leagueDao.existsTeamStats(match.getMatchId(), match.getHomeTeamId()) && match.getHomeTeamStats() != null) {
					leagueDao.addTeamStats(match.getMatchId(), match.getHomeTeamStats());
					List<PlayerStats> stats = match.getHomeTeamStats().getPlayersStats();
					for (PlayerStats playerStats : stats) {
						leagueDao.addPlayersStats(match.getMatchId(), match.getHomeTeamId(), playerStats);
					}
				}
			}

		}
	}

	public List<Match> getNotFinishedMatches(List<Match> matches) throws SQLException {

		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());

		List<Match> filteredMatches = new ArrayList<Match>();
		for (Match match : matches) {
			if (!leagueDao.existsFinishedMatch(match.getMatchId())) {
				filteredMatches.add(match);
			}
		}
		return filteredMatches;
	}

	public List<Match> getMatches(Club club, Map<Integer, Player> playersMap, Map<Integer, League> hmLeague, Map<Integer, Club> clubMap,
		Map<Integer, PlayerArchive> archivePlayerMap) throws SQLException {
		List<Match> matches = new ArrayList<Match>();
		boolean newConnection = SQLQuery.connect();
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		matches = leagueDao.getMatches(club);
		for (Match match : matches) {
			match.setAwayTeam(clubMap.get(match.getAwayTeamId()));
			match.setHomeTeam(clubMap.get(match.getHomeTeamId()));
			match.setLeague(hmLeague.get(match.getLeagueId()));
			match.setAwayTeamStats(leagueDao.getTeamStats(match, match.getAwayTeamId()));
			if (match.getAwayTeamStats() != null) {
				match.getAwayTeamStats().setPlayersStats(leagueDao.getPlayersStats(match, match.getAwayTeamId(), playersMap, archivePlayerMap));
			}
			match.setHomeTeamStats(leagueDao.getTeamStats(match, match.getHomeTeamId()));
			if (match.getHomeTeamStats() != null) {
				match.getHomeTeamStats().setPlayersStats(leagueDao.getPlayersStats(match, match.getHomeTeamId(), playersMap, archivePlayerMap));
			}
		}
		SQLSession.close(newConnection);
		return matches;
	}

	public int importMatch(String matchId) throws SQLException, IOException, SAXException, SVException {
		XMLDownloader downloader = new XMLDownloader();
		League league;
		SokkerViewerSettings settings = SettingsHandler.getSokkerViewerSettings();
		ProxySettings proxySettings = settings.getProxySettings();
		String destination = settings.getBaseDirectory() + File.separator + "xml" + File.separator + settings.getUsername(); 
		downloader.login(settings.getUsername(), settings.getPassword(), proxySettings);

		// download xmls
		try {
			if (downloader.getStatus().equals("OK")) { 
				if (matchId.matches("[0-9]+") || matchId.matches("http://online\\.sokker\\.org/comment\\.php\\?matchID=[0-9]+")) {  
					SQLSession.connect();
					MatchXmlManager manager = new MatchXmlManager(destination, downloader, Cache.getDate());
					manager.download(Integer.valueOf(matchId.replaceAll("[^0-9]", "")));  
					List<Match> matches = manager.parseXML();
					if (matches.get(0) != null) {
						Match match = matches.get(0);
						if ((match.getAwayTeamId() == Integer.valueOf(downloader.getTeamId()) || match.getHomeTeamId() == Integer.valueOf(downloader
							.getTeamId()))
							&& match.getDateStarted().compareTo(Cache.getClub().getDateCreated()) > -1) {
							manager.write();
							// if(!SQLQuery.ifExistsLeague(manager.getMatch().getLeagueID())) {
							if (Cache.getLeaguesMap().get(match.getLeagueId()) == null) {
								LeagueXmlManager leagueManager = new LeagueXmlManager(destination, downloader, Cache.getDate());
								leagueManager.download(match.getLeagueId());
								List<League> leagues = leagueManager.parseXML();
								leagueManager.importToSQL();
								for (int i = 0; i < leagues.size(); i++) {
									league = leagues.get(i);
									// Cache.getLeagues().add(league);
									Cache.getLeaguesMap().put(league.getLeagueId(), league);
								}
							} else {
								league = Cache.getLeaguesMap().get(match.getLeagueId());
							}

							if (!new LeagueDao(SQLSession.getConnection()).existsFinishedMatch(match.getMatchId())) {
								manager.importToSQL();

								match.setLeague(Cache.getLeaguesMap().get(match.getLeagueId()));
								if (match.getAwayTeamStats() != null) {
									List<PlayerStats> playersStats = match.getAwayTeamStats().getPlayersStats();
									for (PlayerStats playerStats : playersStats) {
										playerStats.setPlayer(Cache.getPlayersMap().get(playerStats.getPlayerId()));
									}
								}
								if (match.getHomeTeamStats() != null) {
									List<PlayerStats> playersStats = match.getHomeTeamStats().getPlayersStats();
									for (PlayerStats playerStats : playersStats) {
										playerStats.setPlayer(Cache.getPlayersMap().get(playerStats.getPlayerId()));
									}
								}

								Cache.getMatches().add(match);

								return OK;
							} else {
								return ERROR_ALREADY_EXIST;
							}
						} else {
							return ERROR_DOESNT_CONTAIN;
						}
					} else {
						return ERROR_DOESNT_CONTAIN;
					}

				}
			} else {
				throw new SVException(Messages.getString("login.error." + downloader.getErrorno())); 
			}
		} finally {
			SQLSession.close();
		}
		return ERROR_DOESNT_CONTAIN;
	}
}