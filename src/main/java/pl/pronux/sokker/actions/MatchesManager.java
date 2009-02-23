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

public class MatchesManager {
	public void importMatches(List<Match> matches) throws SQLException {
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		for (Match match : matches) {
			if (leagueDao.existsMatch(match.getMatchID())) {
				leagueDao.updateMatch(match);
			} else {
				leagueDao.addMatch(match);
			}

			if (match.getIsFinished() == 1) {
				if (!leagueDao.existsTeamStats(match.getMatchID(), match.getAwayTeamID()) && match.getAwayTeamStats() != null) {
					leagueDao.addTeamStats(match.getMatchID(), match.getAwayTeamStats());
					List<PlayerStats> stats = match.getAwayTeamStats().getPlayersStats();
					for (PlayerStats playerStats : stats) {
						leagueDao.addPlayersStats(match.getMatchID(), match.getAwayTeamID(), playerStats);
					}
				}
				if (!leagueDao.existsTeamStats(match.getMatchID(), match.getHomeTeamID()) && match.getHomeTeamStats() != null) {
					leagueDao.addTeamStats(match.getMatchID(), match.getHomeTeamStats());
					List<PlayerStats> stats = match.getHomeTeamStats().getPlayersStats();
					for (PlayerStats playerStats : stats) {
						leagueDao.addPlayersStats(match.getMatchID(), match.getHomeTeamID(), playerStats);
					}
				}
			}

		}
	}

	public List<Match> getNotFinishedMatches(List<Match> matches) throws SQLException {

		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());

		List<Match> filteredMatches = new ArrayList<Match>();
		for (Match match : matches) {
			if (!leagueDao.existsFinishedMatch(match.getMatchID())) {
				filteredMatches.add(match);
			}
		}
		return filteredMatches;
	}

	public List<Match> getMatches(Club club, Map<Integer, Player> playersMap, Map<Integer, League> hmLeague, Map<Integer, Club> clubMap, Map<Integer, PlayerArchive> archivePlayerMap) throws SQLException {
		List<Match> matches = new ArrayList<Match>();
		boolean newConnection = SQLQuery.connect();
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		matches = leagueDao.getMatches(club);
		for (Match match : matches) {
			match.setAwayTeam(clubMap.get(match.getAwayTeamID()));
			match.setHomeTeam(clubMap.get(match.getHomeTeamID()));
			match.setLeague(hmLeague.get(match.getLeagueID()));
			match.setAwayTeamStats(leagueDao.getTeamStats(match, match.getAwayTeamID()));
			if (match.getAwayTeamStats() != null) {
				match.getAwayTeamStats().setPlayersStats(leagueDao.getPlayersStats(match, match.getAwayTeamID(), playersMap, archivePlayerMap));
			}
			match.setHomeTeamStats(leagueDao.getTeamStats(match, match.getHomeTeamID()));
			if (match.getHomeTeamStats() != null) {
				match.getHomeTeamStats().setPlayersStats(leagueDao.getPlayersStats(match, match.getHomeTeamID(), playersMap, archivePlayerMap));
			}
		}
		SQLSession.close(newConnection);
		return matches;
	}

	public int importMatch(String matchID) throws SQLException, IOException, SAXException, SVException {
		XMLDownloader downloader = new XMLDownloader();				
		Match match;
		League league;
		String value;
		SokkerViewerSettings settings = SettingsHandler.getSokkerViewerSettings();
		ProxySettings proxySettings = settings.getProxySettings();
		String destination = settings.getBaseDirectory() + File.separator + "xml" + File.separator + settings.getUsername(); //$NON-NLS-1$
		if(proxySettings.isEnabled()) {
			downloader.login(settings.getUsername(), settings.getPassword(), proxySettings.getHostname(), proxySettings.getPort(), proxySettings.getUsername(), proxySettings.getPassword());
		} else {
			downloader.login(settings.getUsername(), settings.getPassword());	
		}

		if (downloader.getStatus().equals("OK")) { //$NON-NLS-1$
			value = "0"; //$NON-NLS-1$
		} else {
			value = downloader.getErrorno();
		}
		// download xmls
		try {
			if (value.equals("0")) { //$NON-NLS-1$
				if (matchID.matches("[0-9]+") || matchID.matches("http://online\\.sokker\\.org/comment\\.php\\?matchID=[0-9]+")) { //$NON-NLS-1$ //$NON-NLS-2$
					SQLSession.connect();
					MatchXmlManager manager = new MatchXmlManager(destination, downloader, Cache.getDate());
					manager.download(Integer.valueOf(matchID.replaceAll("[^0-9]", ""))); //$NON-NLS-1$ //$NON-NLS-2$
					List<Match> matches = manager.parseXML();
					if (matches.get(0) != null) {
						match = matches.get(0);
						if ((match.getAwayTeamID() == Integer.valueOf(downloader.getTeamID()) || match.getHomeTeamID() == Integer.valueOf(downloader.getTeamID())) && match.getDateStarted().compareTo(Cache.getClub().getDateCreated()) > -1 ) {
							manager.write();
							// if(!SQLQuery.ifExistsLeague(manager.getMatch().getLeagueID())) {
							if (Cache.getLeaguesMap().get(match.getLeagueID()) == null) {
								LeagueXmlManager leagueManager = new LeagueXmlManager(destination, downloader, Cache.getDate());
								leagueManager.download(match.getLeagueID());
								List<League> leagues = leagueManager.parseXML();
								leagueManager.importToSQL();
								for (int i = 0; i < leagues.size(); i++) {
									league = leagues.get(i);
//									Cache.getLeagues().add(league);
									Cache.getLeaguesMap().put(league.getLeagueID(), league);
								}
							} else {
								league = Cache.getLeaguesMap().get(match.getLeagueID());
							}

							if (!new LeagueDao(SQLSession.getConnection()).existsFinishedMatch(match.getMatchID())) {
								manager.importToSQL();

								match.setLeague(Cache.getLeaguesMap().get(match.getLeagueID()));
								if (match.getAwayTeamStats() != null) {
									List<PlayerStats> playersStats = match.getAwayTeamStats().getPlayersStats();
									for (PlayerStats playerStats : playersStats) {
										playerStats.setPlayer(Cache.getPlayersMap().get(playerStats.getPlayerID()));
									}
								}
								if (match.getHomeTeamStats() != null) {
									List<PlayerStats> playersStats = match.getHomeTeamStats().getPlayersStats();
									for (PlayerStats playerStats : playersStats) {
										playerStats.setPlayer(Cache.getPlayersMap().get(playerStats.getPlayerID()));
									}
								}

								Cache.getMatches().add(match);

								return 1;
							} else {
								return 3;
							}
						} else {
							return 2;
						}
					} else {
						return 3;	
					}
					
				}
			} else {
				throw new SVException(Messages.getString("login.error." + value)); //$NON-NLS-1$
			}
		} finally {
			SQLSession.close();
		}
		return 2;

	}
}
