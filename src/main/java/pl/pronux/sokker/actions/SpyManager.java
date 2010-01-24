package pl.pronux.sokker.actions;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import pl.pronux.sokker.comparators.MatchPlayersComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.downloader.managers.MatchXmlManager;
import pl.pronux.sokker.downloader.managers.MatchesTeamXmlManager;
import pl.pronux.sokker.downloader.managers.PlayersXmlManager;
import pl.pronux.sokker.downloader.managers.TeamsXmlManager;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;

public class SpyManager {

	private PlayersManager playersManager = PlayersManager.instance();

	private final static SpyManager _instance = new SpyManager();

	private SpyManager() {
	}

	public static SpyManager instance() {
		return _instance;
	}

	public Club getTeam(int teamID) throws SVException, IOException, SQLException, SAXException {
		XMLDownloader downloader = new XMLDownloader();
		SokkerViewerSettings settings = SettingsHandler.getSokkerViewerSettings();
		ProxySettings proxySettings = settings.getProxySettings();
		String destination = settings.getBaseDirectory() + File.separator + "xml" + File.separator + settings.getUsername(); //$NON-NLS-1$

		downloader.login(settings.getUsername(), settings.getPassword(), proxySettings);

		if (!downloader.getStatus().equals("OK")) { //$NON-NLS-1$
			throw new SVException(Messages.getString("login.error." + downloader.getErrorno())); //$NON-NLS-1$
		}

		TeamsXmlManager teamXMLManager = new TeamsXmlManager(destination, downloader, Cache.getDate());
		PlayersXmlManager playersXMLManager = new PlayersXmlManager(destination, downloader, Cache.getDate());
		MatchesTeamXmlManager matchesTeamXmlManager = new MatchesTeamXmlManager(destination, downloader, Cache.getDate());
		MatchXmlManager matchXmlManager = new MatchXmlManager(destination, downloader, Cache.getDate());

		teamXMLManager.download(String.valueOf(teamID));
		playersXMLManager.download(String.valueOf(teamID));
		matchesTeamXmlManager.download(String.valueOf(teamID));

		Club team = teamXMLManager.parseXML(teamID);
		if (team != null) {
			List<Player> players = playersXMLManager.parseXML(teamID);
			List<Match> matches = matchesTeamXmlManager.parseXML(teamID);
			try {
				SQLSession.connect();
				Map<Integer, Player> playersMap = new HashMap<Integer, Player>();
				for (Player player : players) {
					PlayerArchive playerArchive = new PlayerArchive(player);
					playersManager.addPlayerArchive(playerArchive);
					Cache.getPlayersArchiveMap().put(playerArchive.getId(), playerArchive);
					player.setNote(playersManager.getPlayerArchiveNote(player.getId()));
					playersMap.put(player.getId(), player);
				}

				Map<Integer, PlayerArchive> playerArchive = Cache.getPlayersArchiveMap();

				for (Match match : matches) {
					if (match.getIsFinished() == Match.FINISHED) {
						matchXmlManager.download(match.getMatchID());
					}
				}

				matches = matchXmlManager.parseXML();
				MatchPlayersComparator matchPlayersComparator = new MatchPlayersComparator(MatchPlayersComparator.NUMBER, MatchPlayersComparator.ASCENDING);

				for (Match match : matches) {
					if (match.getAwayTeamID() == team.getId()) {
						match.setAwayTeam(team);
					} else {
						match.setHomeTeam(team);
					}
					double sum = 0;
					double count = 0;
					Collections.sort(match.getHomeTeamStats().getPlayersStats(), matchPlayersComparator);
					Collections.sort(match.getAwayTeamStats().getPlayersStats(), matchPlayersComparator);
					for (PlayerStats playerStats : match.getHomeTeamStats().getPlayersStats()) {
						Player player = playersMap.get(playerStats.getPlayerID());
						if (player != null) {
							playerStats.setPlayer(player);
							if (player.getPlayerMatchStatistics() == null) {
								player.setPlayerMatchStatistics(new ArrayList<PlayerStats>());
							}
							if (playerStats.getTimePlayed() > 0) {
								player.getPlayerMatchStatistics().add(playerStats);
							}
						} else if (playerArchive.get(playerStats.getPlayerID()) != null) {
							playerStats.setPlayer(playerArchive.get(playerStats.getPlayerID()).toPlayer());
						}
						if (playerStats.getNumber() < 12) {
							count++;
							sum += playerStats.getRating();
						}
					}
					match.getHomeTeamStats().setAverageRating(sum / count);

					sum = 0;
					count = 0;
					for (PlayerStats playerStats : match.getAwayTeamStats().getPlayersStats()) {
						Player player = playersMap.get(playerStats.getPlayerID());

						if (player != null) {
							playerStats.setPlayer(player);
							if (player.getPlayerMatchStatistics() == null) {
								player.setPlayerMatchStatistics(new ArrayList<PlayerStats>());
							}
							if (playerStats.getTimePlayed() > 0) {
								player.getPlayerMatchStatistics().add(playerStats);
							}
						} else if (playerArchive.get(playerStats.getPlayerID()) != null) {
							playerStats.setPlayer(playerArchive.get(playerStats.getPlayerID()).toPlayer());
						}
						if (playerStats.getNumber() < 12) {
							count++;
							sum += playerStats.getRating();
						}
					}
					match.getAwayTeamStats().setAverageRating(sum / count);
				}

				for (Player player : players) {
					Integer rating = 0;
					int counter = 0;
					int gk = 0;
					int def = 0;
					int mid = 0;
					int att = 0;
					int max = 0;
					for (PlayerStats playerStats : player.getPlayerMatchStatistics()) {
						if (playerStats.getTimePlayed() > 75) {
							counter++;
							rating += playerStats.getRating();
						}
						switch (playerStats.getFormation()) {
						case PlayerStats.GK:
							gk++;
							break;
						case PlayerStats.DEF:
							def++;
							break;
						case PlayerStats.MID:
							mid++;
							break;
						case PlayerStats.ATT:
							att++;
							break;
						}
					}

					max = gk;
					player.setPreferredPosition(PlayerStats.GK);
					if (def >= max) {
						max = def;
						player.setPreferredPosition(PlayerStats.DEF);
					}
					if (mid >= max) {
						max = mid;
						player.setPreferredPosition(PlayerStats.MID);
					}
					if (att >= max) {
						max = att;
						player.setPreferredPosition(PlayerStats.ATT);
					}

					if (counter > 0) {
						player.setAvgRating(rating / counter);
					} else {
						player.setAvgRating(0);
					}
				}

				team.setPlayers(players);
				team.setMatches(matches);
			} finally {
				SQLSession.close();
			}
		}

		return team;
	}
}
