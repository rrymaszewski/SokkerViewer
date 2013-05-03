package pl.pronux.sokker.downloader.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.TeamStats;
import pl.pronux.sokker.utils.Log;

public class MatchXmlParser {

	private static int currentTag = 0;

	private static final int TAG_MATCH = 1;

	private static final int TAG_INFO = 2;

	private static final int TAG_MATCH_ID = 3;

	private static final int TAG_HOME_TEAM_ID = 4;

	private static final int TAG_AWAY_TEAM_ID = 5;

	private static final int TAG_HOME_TEAM_NAME = 6;

	private static final int TAG_AWAY_TEAM_NAME = 7;

	private static final int TAG_LEAGUE_ID = 8;

	private static final int TAG_ROUND = 9;

	private static final int TAG_SEASON = 10;

	private static final int TAG_WEEK = 11;

	private static final int TAG_DAY = 12;

	private static final int TAG_DATE_EXPECTED = 13;

	private static final int TAG_DATE_STARTED = 14;

	private static final int TAG_HOME_TEAM_SCORE = 15;

	private static final int TAG_AWAY_TEAM_SCORE = 16;

	private static final int TAG_SUPPORTERS = 17;

	private static final int TAG_WEATHER = 18;

	private static final int TAG_IS_FINISHED = 19;

	private static final int TAG_TEAM_STATS = 20;

	private static final int TAG_TEAM_ID = 21;

	private static final int TAG_TIME_ON_HALF = 22;

	private static final int TAG_TIME_POSSESSION = 23;

	private static final int TAG_OFFSIDES = 24;

	private static final int TAG_SHOOTS_INFO = 25;

	private static final int TAG_FOULS_INFO = 26;

	private static final int TAG_YELLOW_CARDS_INFO = 27;

	private static final int TAG_RED_CARDS_INFO = 28;

	private static final int TAG_TACTIC_NAME = 29;

	private static final int TAG_RATING_SCORING = 30;

	private static final int TAG_RATING_PASSING = 31;

	private static final int TAG_RATING_DEFENDING = 32;

	private static final int TAG_PLAYERS_STATS = 33;

	private static final int TAG_PLAYER_STATS = 34;

	private static final int TAG_PLAYER_ID = 35;

	private static final int TAG_NUMBER = 36;

	private static final int TAG_FORMATION = 37;

	private static final int TAG_TIME_IN = 38;

	private static final int TAG_TIME_OUT = 39;

	private static final int TAG_YELLOW_CARDS_PLAYER = 40;

	private static final int TAG_RED_CARDS_PLAYER = 41;

	private static final int TAG_IS_INJURED = 42;

	private static final int TAG_GOALS_PLAYER = 43;

	private static final int TAG_ASSISTS_PLAYER = 44;

	private static final int TAG_SHOOTS_PLAYER = 45;

	private static final int TAG_RATING = 46;

	private static final int TAG_TIME_PLAYING = 47;

	private static final int TAG_TIME_DEFENDING = 48;

	private static final int TAG_FOULS_PLAYER = 49;

	private static int tagSwitch = 0;

	private int teamId;

	private PlayerStats playerStats;

	private Match match;

	private TeamStats homeTeam;

	private TeamStats awayTeam;

	private TeamStats tempTeam;

	private int homeTeamID;

	private int awayTeamID;

	private List<PlayerStats> alPlayersStats;
	
	private StringBuilder message;

	public Match getMatch() {
		return match;
	}

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_MATCH:
					break;
				case TAG_INFO:
					break;
				case TAG_MATCH_ID:
					match.setMatchId(Integer.parseInt(message.toString()));
					break;
				case TAG_HOME_TEAM_ID:
					match.setHomeTeamId(Integer.parseInt(message.toString()));
					homeTeamID = Integer.parseInt(message.toString());
					break;
				case TAG_AWAY_TEAM_ID:
					match.setAwayTeamId(Integer.parseInt(message.toString()));
					awayTeamID = Integer.parseInt(message.toString());
					break;
				case TAG_HOME_TEAM_NAME:
					match.setHomeTeamName(message.toString());
					break;
				case TAG_AWAY_TEAM_NAME:
					match.setAwayTeamName(message.toString());
					break;
				case TAG_LEAGUE_ID:
					match.setLeagueId(Integer.parseInt(message.toString()));
					break;
				case TAG_ROUND:
					match.setRound(Integer.parseInt(message.toString()));
					break;
				case TAG_SEASON:
					match.setSeason(Integer.parseInt(message.toString()));
					break;
				case TAG_WEEK:
					match.setWeek(Integer.parseInt(message.toString()));
					break;
				case TAG_DAY:
					match.setDay(Integer.parseInt(message.toString()));
					break;
				case TAG_DATE_EXPECTED:
					match.setDateExpected(new Date(message.toString()));
					break;
				case TAG_DATE_STARTED:
					match.setDateStarted(new Date(message.toString()));
					break;
				case TAG_HOME_TEAM_SCORE:
					match.setHomeTeamScore(Integer.parseInt(message.toString()));
					break;
				case TAG_AWAY_TEAM_SCORE:
					match.setAwayTeamScore(Integer.parseInt(message.toString()));
					break;
				case TAG_SUPPORTERS:
					match.setSupporters(Integer.parseInt(message.toString()));
					break;
				case TAG_WEATHER:
					match.setWeather(Integer.parseInt(message.toString()));
					break;
				case TAG_IS_FINISHED:
					match.setIsFinished(Integer.parseInt(message.toString()));
					break;
				case TAG_TEAM_STATS:
					break;
				case TAG_TEAM_ID:
					tempTeam.setTeamId(Integer.parseInt(message.toString()));
					break;
				case TAG_TIME_ON_HALF:
					tempTeam.setTimeOnHalf(Integer.parseInt(message.toString()));
					break;
				case TAG_TIME_POSSESSION:
					tempTeam.setTimePossession(Integer.parseInt(message.toString()));
					break;
				case TAG_OFFSIDES:
					tempTeam.setOffsides(Integer.parseInt(message.toString()));
					break;
				case TAG_SHOOTS_INFO:
					tempTeam.setShoots(Integer.parseInt(message.toString()));
					break;
				case TAG_FOULS_INFO:
					tempTeam.setFouls(Integer.parseInt(message.toString()));
					break;
				case TAG_YELLOW_CARDS_INFO:
					tempTeam.setYellowCards(Integer.parseInt(message.toString()));
					break;
				case TAG_RED_CARDS_INFO:
					tempTeam.setRedCards(Integer.parseInt(message.toString()));
					break;
				case TAG_TACTIC_NAME:
					tempTeam.setTacticName(message.toString());
					break;
				case TAG_RATING_SCORING:
					tempTeam.setRatingScoring(Integer.parseInt(message.toString()));
					break;
				case TAG_RATING_PASSING:
					tempTeam.setRatingPassing(Integer.parseInt(message.toString()));
					break;
				case TAG_RATING_DEFENDING:
					tempTeam.setRatingDefending(Integer.parseInt(message.toString()));
					break;
				case TAG_PLAYER_STATS:
					break;
				case TAG_PLAYER_ID:
					playerStats.setPlayerId(Integer.parseInt(message.toString()));
					break;
				case TAG_NUMBER:
					playerStats.setNumber(Integer.parseInt(message.toString()));
					break;
				case TAG_FORMATION:
					playerStats.setFormation(Integer.parseInt(message.toString()));
					break;
				case TAG_TIME_IN:
					playerStats.setTimeIn(Integer.parseInt(message.toString()));
					break;
				case TAG_TIME_OUT:
					playerStats.setTimeOut(Integer.parseInt(message.toString()));
					break;
				case TAG_YELLOW_CARDS_PLAYER:
					playerStats.setYellowCards(Integer.parseInt(message.toString()));
					break;
				case TAG_RED_CARDS_PLAYER:
					playerStats.setRedCards(Integer.parseInt(message.toString()));
					break;
				case TAG_IS_INJURED:
					playerStats.setIsInjured(Integer.parseInt(message.toString()));
					break;
				case TAG_GOALS_PLAYER:
					playerStats.setGoals(Integer.parseInt(message.toString()));
					break;
				case TAG_ASSISTS_PLAYER:
					playerStats.setAssists(Integer.parseInt(message.toString()));
					break;
				case TAG_SHOOTS_PLAYER:
					playerStats.setShoots(Integer.parseInt(message.toString()));
					break;
				case TAG_RATING:
					playerStats.setRating(Integer.parseInt(message.toString()));
					break;
				case TAG_TIME_PLAYING:
					playerStats.setTimePlaying(Integer.parseInt(message.toString()));
					break;
				case TAG_TIME_DEFENDING:
					playerStats.setTimeDefending(Integer.parseInt(message.toString()));
					break;
				case TAG_FOULS_PLAYER:
					playerStats.setFouls(Integer.parseInt(message.toString()));
					break;
				default:
					break;
				}
			}

			// obsluga bledow

			public void endDocument() {
			}

			public void endElement(String namespaceURL, String localName, String qName) {
				currentTag = 0;
				if (localName.equalsIgnoreCase("teamStats")) { 
					if (homeTeamID == tempTeam.getTeamId()) {
						homeTeam = tempTeam;
						match.setHomeTeamStats(homeTeam);
					} else if (awayTeamID == tempTeam.getTeamId()) {
						awayTeam = tempTeam;
						match.setAwayTeamStats(awayTeam);
					}
				} else if (localName.equalsIgnoreCase("match")) { 

				} else if (localName.equalsIgnoreCase("playerStats")) { 
					alPlayersStats.add(playerStats);
				} else if (localName.equalsIgnoreCase("playersStats")) { 
					if (homeTeam.getTeamId() == teamId) {
						homeTeam.setPlayersStats(alPlayersStats);
					} else if (awayTeam.getTeamId() == teamId) {
						awayTeam.setPlayersStats(alPlayersStats);
					}
				}
			}

			public void startDocument() {
				match = new Match();
				awayTeamID = 0;
				homeTeamID = 0;
				homeTeam = new TeamStats();
				awayTeam = new TeamStats();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();

				if (localName.equalsIgnoreCase("match")) { 
					tagSwitch = TAG_MATCH;
				}

				if (localName.equalsIgnoreCase("teamStats")) { 
					tagSwitch = TAG_TEAM_STATS;
					tempTeam = new TeamStats();
				}

				if (localName.equalsIgnoreCase("playersStats")) { 
					tagSwitch = TAG_PLAYERS_STATS;
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { 
							teamId = Integer.parseInt(value);
						}
					}
					alPlayersStats = new ArrayList<PlayerStats>();
				}

				if (localName.equalsIgnoreCase("info")) { 
					tagSwitch = TAG_INFO;
				}

				if (tagSwitch == TAG_INFO) {
					if (localName.equals("matchID")) { 
						currentTag = TAG_MATCH_ID;
					} else if (localName.equals("homeTeamID")) { 
						currentTag = TAG_HOME_TEAM_ID;
					} else if (localName.equals("awayTeamID")) { 
						currentTag = TAG_AWAY_TEAM_ID;
					} else if (localName.equals("homeTeamName")) { 
						currentTag = TAG_HOME_TEAM_NAME;
					} else if (localName.equals("awayTeamName")) { 
						currentTag = TAG_AWAY_TEAM_NAME;
					} else if (localName.equals("leagueID")) { 
						currentTag = TAG_LEAGUE_ID;
					} else if (localName.equals("round")) { 
						currentTag = TAG_ROUND;
					} else if (localName.equals("season")) { 
						currentTag = TAG_SEASON;
					} else if (localName.equals("week")) { 
						currentTag = TAG_WEEK;
					} else if (localName.equals("day")) { 
						currentTag = TAG_DAY;
					} else if (localName.equals("dateExpected")) { 
						currentTag = TAG_DATE_EXPECTED;
					} else if (localName.equals("dateStarted")) { 
						currentTag = TAG_DATE_STARTED;
					} else if (localName.equals("homeTeamScore")) { 
						currentTag = TAG_HOME_TEAM_SCORE;
					} else if (localName.equals("awayTeamScore")) { 
						currentTag = TAG_AWAY_TEAM_SCORE;
					} else if (localName.equals("supporters")) { 
						currentTag = TAG_SUPPORTERS;
					} else if (localName.equals("weather")) { 
						currentTag = TAG_WEATHER;
					} else if (localName.equals("isFinished")) { 
						currentTag = TAG_IS_FINISHED;
					}
				} else if (tagSwitch == TAG_TEAM_STATS) {
					if (localName.equals("teamID")) { 
						currentTag = TAG_TEAM_ID;
					} else if (localName.equals("timeOnHalf")) { 
						currentTag = TAG_TIME_ON_HALF;
					} else if (localName.equals("timePossession")) { 
						currentTag = TAG_TIME_POSSESSION;
					} else if (localName.equals("offsides")) { 
						currentTag = TAG_OFFSIDES;
					} else if (localName.equals("shoots")) { 
						currentTag = TAG_SHOOTS_INFO;
					} else if (localName.equals("fouls")) { 
						currentTag = TAG_FOULS_INFO;
					} else if (localName.equals("yellowCards")) { 
						currentTag = TAG_YELLOW_CARDS_INFO;
					} else if (localName.equals("redCards")) { 
						currentTag = TAG_RED_CARDS_INFO;
					} else if (localName.equals("tacticName")) { 
						currentTag = TAG_TACTIC_NAME;
					} else if (localName.equals("ratingScoring")) { 
						currentTag = TAG_RATING_SCORING;
					} else if (localName.equals("ratingPassing")) { 
						currentTag = TAG_RATING_PASSING;
					} else if (localName.equals("ratingDefending")) { 
						currentTag = TAG_RATING_DEFENDING;
					}
				} else if (tagSwitch == TAG_PLAYERS_STATS) {
					if (localName.equals("playerStats")) { 
						playerStats = new PlayerStats();
					} else if (localName.equals("playerID")) { 
						currentTag = TAG_PLAYER_ID;
					} else if (localName.equals("number")) { 
						currentTag = TAG_NUMBER;
					} else if (localName.equals("formation")) { 
						currentTag = TAG_FORMATION;
					} else if (localName.equals("timeIn")) { 
						currentTag = TAG_TIME_IN;
					} else if (localName.equals("timeOut")) { 
						currentTag = TAG_TIME_OUT;
					} else if (localName.equals("yellowCards")) { 
						currentTag = TAG_YELLOW_CARDS_PLAYER;
					} else if (localName.equals("redCards")) { 
						currentTag = TAG_RED_CARDS_PLAYER;
					} else if (localName.equals("isInjured")) { 
						currentTag = TAG_IS_INJURED;
					} else if (localName.equals("goals")) { 
						currentTag = TAG_GOALS_PLAYER;
					} else if (localName.equals("assists")) { 
						currentTag = TAG_ASSISTS_PLAYER;
					} else if (localName.equals("fouls")) { 
						currentTag = TAG_FOULS_PLAYER;
					} else if (localName.equals("shoots")) { 
						currentTag = TAG_SHOOTS_PLAYER;
					} else if (localName.equals("rating")) { 
						currentTag = TAG_RATING;
					} else if (localName.equals("timePlaying")) { 
						currentTag = TAG_TIME_PLAYING;
					} else if (localName.equals("timeDefending")) { 
						currentTag = TAG_TIME_DEFENDING;
					}
				}

			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new MatchErrorHandler());

			parser.parse(input);
		} catch (IOException e) {
			Log.error("Parser Class", e); 
		} catch (SAXException e) {
			if (file != null) {
				new File(file).delete();
			}
			throw e;
		}
	}
}


class MatchErrorHandler implements ErrorHandler {

	public void warning(SAXParseException e) throws SAXException {
		// throw new SAXException();
	}

	public void error(SAXParseException e) throws SAXException {
		throw e;
	}

	public void fatalError(SAXParseException e) throws SAXException {
		throw e;
	}
}
