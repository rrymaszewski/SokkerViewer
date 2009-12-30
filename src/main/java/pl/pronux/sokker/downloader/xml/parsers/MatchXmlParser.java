package pl.pronux.sokker.downloader.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

	static int current_tag = 0;

	static final int TAG_MATCH = 1;

	static final int TAG_INFO = 2;

	static final int TAG_MATCH_ID = 3;

	static final int TAG_HOME_TEAM_ID = 4;

	static final int TAG_AWAY_TEAM_ID = 5;

	static final int TAG_HOME_TEAM_NAME = 6;

	static final int TAG_AWAY_TEAM_NAME = 7;

	static final int TAG_LEAGUE_ID = 8;

	static final int TAG_ROUND = 9;

	static final int TAG_SEASON = 10;

	static final int TAG_WEEK = 11;

	static final int TAG_DAY = 12;

	static final int TAG_DATE_EXPECTED = 13;

	static final int TAG_DATE_STARTED = 14;

	static final int TAG_HOME_TEAM_SCORE = 15;

	static final int TAG_AWAY_TEAM_SCORE = 16;

	static final int TAG_SUPPORTERS = 17;

	static final int TAG_WEATHER = 18;

	static final int TAG_IS_FINISHED = 19;

	static final int TAG_TEAM_STATS = 20;

	static final int TAG_TEAM_ID = 21;

	static final int TAG_TIME_ON_HALF = 22;

	static final int TAG_TIME_POSSESSION = 23;

	static final int TAG_OFFSIDES = 24;

	static final int TAG_SHOOTS_INFO = 25;

	static final int TAG_FOULS_INFO = 26;

	static final int TAG_YELLOW_CARDS_INFO = 27;

	static final int TAG_RED_CARDS_INFO = 28;

	static final int TAG_TACTIC_NAME = 29;

	static final int TAG_RATING_SCORING = 30;

	static final int TAG_RATING_PASSING = 31;

	static final int TAG_RATING_DEFENDING = 32;

	static final int TAG_PLAYERS_STATS = 33;

	static final int TAG_PLAYER_STATS = 34;

	static final int TAG_PLAYER_ID = 35;

	static final int TAG_NUMBER = 36;

	static final int TAG_FORMATION = 37;

	static final int TAG_TIME_IN = 38;

	static final int TAG_TIME_OUT = 39;

	static final int TAG_YELLOW_CARDS_PLAYER = 40;

	static final int TAG_RED_CARDS_PLAYER = 41;

	static final int TAG_IS_INJURED = 42;

	static final int TAG_GOALS_PLAYER = 43;

	static final int TAG_ASSISTS_PLAYER = 44;

	static final int TAG_SHOOTS_PLAYER = 45;

	static final int TAG_RATING = 46;

	static final int TAG_TIME_PLAYING = 47;

	static final int TAG_TIME_DEFENDING = 48;

	static final int TAG_FOULS_PLAYER = 49;

	static int TAG_switch = 0;

	private int teamID;

	private PlayerStats playerStats;

	private Match match;

	private TeamStats homeTeam;

	private TeamStats awayTeam;

	private TeamStats tempTeam;

	private int homeTeamID;

	private int awayTeamID;

	private ArrayList<PlayerStats> alPlayersStats;

	public Match getMatch() {
		return match;
	}

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			public void characters(char ch[], int start, int length) throws SAXException {
				// System.out.print("Ciag znakow: ");
				// wypisujemy lancuch, zmieniajac znaki tabulacji i konca
				// linii na ich specjalne reprezentacje

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_MATCH:
					break;
				case TAG_INFO:
					break;
				case TAG_MATCH_ID:
					match.setMatchID(Integer.parseInt(message.toString()));
					break;
				case TAG_HOME_TEAM_ID:
					match.setHomeTeamID(Integer.parseInt(message.toString()));
					homeTeamID = Integer.parseInt(message.toString());
					break;
				case TAG_AWAY_TEAM_ID:
					match.setAwayTeamID(Integer.parseInt(message.toString()));
					awayTeamID = Integer.parseInt(message.toString());
					break;
				case TAG_HOME_TEAM_NAME:
					match.setHomeTeamName(message.toString());
					break;
				case TAG_AWAY_TEAM_NAME:
					match.setAwayTeamName(message.toString());
					break;
				case TAG_LEAGUE_ID:
					match.setLeagueID(Integer.parseInt(message.toString()));
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
					tempTeam.setTeamID(Integer.parseInt(message.toString()));
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
					playerStats.setPlayerID(Integer.parseInt(message.toString()));
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
				current_tag = 0;
				if (localName.equalsIgnoreCase("teamStats")) { //$NON-NLS-1$
					if (homeTeamID == tempTeam.getTeamID()) {
						homeTeam = tempTeam;
						match.setHomeTeamStats(homeTeam);
					} else if (awayTeamID == tempTeam.getTeamID()) {
						awayTeam = tempTeam;
						match.setAwayTeamStats(awayTeam);
					}
				} else if (localName.equalsIgnoreCase("match")) { //$NON-NLS-1$

				} else if (localName.equalsIgnoreCase("playerStats")) { //$NON-NLS-1$
					alPlayersStats.add(playerStats);
				} else if (localName.equalsIgnoreCase("playersStats")) { //$NON-NLS-1$
					if (homeTeam.getTeamID() == teamID) {
						homeTeam.setPlayersStats(alPlayersStats);
					} else if (awayTeam.getTeamID() == teamID) {
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

			StringBuilder message;

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();

				if (localName.equalsIgnoreCase("match")) { //$NON-NLS-1$
					TAG_switch = TAG_MATCH;
				}

				if (localName.equalsIgnoreCase("teamStats")) { //$NON-NLS-1$
					TAG_switch = TAG_TEAM_STATS;
					tempTeam = new TeamStats();
				}

				if (localName.equalsIgnoreCase("playersStats")) { //$NON-NLS-1$
					TAG_switch = TAG_PLAYERS_STATS;
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { //$NON-NLS-1$
							teamID = Integer.parseInt(value);
						}
					}
					alPlayersStats = new ArrayList<PlayerStats>();
				}

				if (localName.equalsIgnoreCase("info")) { //$NON-NLS-1$
					TAG_switch = TAG_INFO;
				}

				if (TAG_switch == TAG_INFO) {
					if (localName.equals("matchID")) { //$NON-NLS-1$
						current_tag = TAG_MATCH_ID;
					} else if (localName.equals("homeTeamID")) { //$NON-NLS-1$
						current_tag = TAG_HOME_TEAM_ID;
					} else if (localName.equals("awayTeamID")) { //$NON-NLS-1$
						current_tag = TAG_AWAY_TEAM_ID;
					} else if (localName.equals("homeTeamName")) { //$NON-NLS-1$
						current_tag = TAG_HOME_TEAM_NAME;
					} else if (localName.equals("awayTeamName")) { //$NON-NLS-1$
						current_tag = TAG_AWAY_TEAM_NAME;
					} else if (localName.equals("leagueID")) { //$NON-NLS-1$
						current_tag = TAG_LEAGUE_ID;
					} else if (localName.equals("round")) { //$NON-NLS-1$
						current_tag = TAG_ROUND;
					} else if (localName.equals("season")) { //$NON-NLS-1$
						current_tag = TAG_SEASON;
					} else if (localName.equals("week")) { //$NON-NLS-1$
						current_tag = TAG_WEEK;
					} else if (localName.equals("day")) { //$NON-NLS-1$
						current_tag = TAG_DAY;
					} else if (localName.equals("dateExpected")) { //$NON-NLS-1$
						current_tag = TAG_DATE_EXPECTED;
					} else if (localName.equals("dateStarted")) { //$NON-NLS-1$
						current_tag = TAG_DATE_STARTED;
					} else if (localName.equals("homeTeamScore")) { //$NON-NLS-1$
						current_tag = TAG_HOME_TEAM_SCORE;
					} else if (localName.equals("awayTeamScore")) { //$NON-NLS-1$
						current_tag = TAG_AWAY_TEAM_SCORE;
					} else if (localName.equals("supporters")) { //$NON-NLS-1$
						current_tag = TAG_SUPPORTERS;
					} else if (localName.equals("weather")) { //$NON-NLS-1$
						current_tag = TAG_WEATHER;
					} else if (localName.equals("isFinished")) { //$NON-NLS-1$
						current_tag = TAG_IS_FINISHED;
					}
				} else if (TAG_switch == TAG_TEAM_STATS) {
					if (localName.equals("teamID")) { //$NON-NLS-1$
						current_tag = TAG_TEAM_ID;
					} else if (localName.equals("timeOnHalf")) { //$NON-NLS-1$
						current_tag = TAG_TIME_ON_HALF;
					} else if (localName.equals("timePossession")) { //$NON-NLS-1$
						current_tag = TAG_TIME_POSSESSION;
					} else if (localName.equals("offsides")) { //$NON-NLS-1$
						current_tag = TAG_OFFSIDES;
					} else if (localName.equals("shoots")) { //$NON-NLS-1$
						current_tag = TAG_SHOOTS_INFO;
					} else if (localName.equals("fouls")) { //$NON-NLS-1$
						current_tag = TAG_FOULS_INFO;
					} else if (localName.equals("yellowCards")) { //$NON-NLS-1$
						current_tag = TAG_YELLOW_CARDS_INFO;
					} else if (localName.equals("redCards")) { //$NON-NLS-1$
						current_tag = TAG_RED_CARDS_INFO;
					} else if (localName.equals("tacticName")) { //$NON-NLS-1$
						current_tag = TAG_TACTIC_NAME;
					} else if (localName.equals("ratingScoring")) { //$NON-NLS-1$
						current_tag = TAG_RATING_SCORING;
					} else if (localName.equals("ratingPassing")) { //$NON-NLS-1$
						current_tag = TAG_RATING_PASSING;
					} else if (localName.equals("ratingDefending")) { //$NON-NLS-1$
						current_tag = TAG_RATING_DEFENDING;
					}
				} else if (TAG_switch == TAG_PLAYERS_STATS) {
					if (localName.equals("playerStats")) { //$NON-NLS-1$
						playerStats = new PlayerStats();
					} else if (localName.equals("playerID")) { //$NON-NLS-1$
						current_tag = TAG_PLAYER_ID;
					} else if (localName.equals("number")) { //$NON-NLS-1$
						current_tag = TAG_NUMBER;
					} else if (localName.equals("formation")) { //$NON-NLS-1$
						current_tag = TAG_FORMATION;
					} else if (localName.equals("timeIn")) { //$NON-NLS-1$
						current_tag = TAG_TIME_IN;
					} else if (localName.equals("timeOut")) { //$NON-NLS-1$
						current_tag = TAG_TIME_OUT;
					} else if (localName.equals("yellowCards")) { //$NON-NLS-1$
						current_tag = TAG_YELLOW_CARDS_PLAYER;
					} else if (localName.equals("redCards")) { //$NON-NLS-1$
						current_tag = TAG_RED_CARDS_PLAYER;
					} else if (localName.equals("isInjured")) { //$NON-NLS-1$
						current_tag = TAG_IS_INJURED;
					} else if (localName.equals("goals")) { //$NON-NLS-1$
						current_tag = TAG_GOALS_PLAYER;
					} else if (localName.equals("assists")) { //$NON-NLS-1$
						current_tag = TAG_ASSISTS_PLAYER;
					} else if (localName.equals("fouls")) { //$NON-NLS-1$
						current_tag = TAG_FOULS_PLAYER;
					} else if (localName.equals("shoots")) { //$NON-NLS-1$
						current_tag = TAG_SHOOTS_PLAYER;
					} else if (localName.equals("rating")) { //$NON-NLS-1$
						current_tag = TAG_RATING;
					} else if (localName.equals("timePlaying")) { //$NON-NLS-1$
						current_tag = TAG_TIME_PLAYING;
					} else if (localName.equals("timeDefending")) { //$NON-NLS-1$
						current_tag = TAG_TIME_DEFENDING;
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
			Log.error("Parser Class", e); //$NON-NLS-1$
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
