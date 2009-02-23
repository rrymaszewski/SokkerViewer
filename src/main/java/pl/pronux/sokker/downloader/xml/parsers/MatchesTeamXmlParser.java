package pl.pronux.sokker.downloader.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

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
import pl.pronux.sokker.utils.file.SVLogger;

public class MatchesTeamXmlParser {

	static int current_tag = 0;

	static final int TAG_MATCHES = 1;

	static final int TAG_MATCH = 2;

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

	static int TAG_switch = 0;

	private ArrayList<Match> alMatches;

	public int teamID;

	private Match match;

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			StringBuffer message;

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));
				// <matchID>1880256</matchID>
				// <homeTeamID>24872</homeTeamID>
				// <awayTeamID>2551</awayTeamID>
				// <homeTeamName>FC Sulechów</homeTeamName>
				// <awayTeamName>mk Lipno</awayTeamName>
				// <leagueID>263</leagueID>
				// <round>2</round>
				// <season>10</season>
				// <week>178</week>
				// <day>1</day>
				// <dateExpected>2006-12-03 17:13</dateExpected>
				// <dateStarted>2006-12-03 17:13</dateStarted>
				// <homeTeamScore>3</homeTeamScore>
				// <awayTeamScore>4</awayTeamScore>
				// <supporters>10300</supporters>
				// <weather>6</weather>
				// <isFinished>1</isFinished>
				switch (current_tag) {
				case TAG_MATCH_ID:
					match.setMatchID(Integer.parseInt(message.toString()));
					break;
				case TAG_HOME_TEAM_ID:
					match.setHomeTeamID(Integer.parseInt(message.toString()));
					break;
				case TAG_AWAY_TEAM_ID:
					match.setAwayTeamID(Integer.parseInt(message.toString()));
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
				case TAG_DAY:
					match.setDay(Integer.parseInt(message.toString()));
					break;
				case TAG_WEEK:
					match.setWeek(Integer.parseInt(message.toString()));
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
				default:
					break;
				}
			}

			// obsluga bledow

			public void endDocument() {
			}

			public void endElement(String namespaceURL, String localName, String qName) {
				current_tag = 0;
				if (localName.equals("match")) { //$NON-NLS-1$
					if (match.getMatchID() != -1) {
						match.setTeamID(teamID);
						alMatches.add(match);
					}
				}
			}

			public void startDocument() {
				alMatches = new ArrayList<Match>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {
				message = new StringBuffer();
				if (localName.equals("matches")) { //$NON-NLS-1$
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { //$NON-NLS-1$
							teamID = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("match")) { //$NON-NLS-1$
					TAG_switch = TAG_MATCH;
					match = new Match();
					match.setMatchID(-1);
					match.setHomeTeamID(0);
					match.setAwayTeamID(0);
					match.setHomeTeamName(""); //$NON-NLS-1$
					match.setAwayTeamName(""); //$NON-NLS-1$
				}

				if (TAG_switch == TAG_MATCH) {
					if (localName.equals("matchID")) { //$NON-NLS-1$
						current_tag = TAG_MATCH_ID;
					} else if (localName.equalsIgnoreCase("homeTeamID")) { //$NON-NLS-1$
						current_tag = TAG_HOME_TEAM_ID;
					} else if (localName.equalsIgnoreCase("awayTeamID")) { //$NON-NLS-1$
						current_tag = TAG_AWAY_TEAM_ID;
					} else if (localName.equalsIgnoreCase("homeTeamName")) { //$NON-NLS-1$
						current_tag = TAG_HOME_TEAM_NAME;
					} else if (localName.equals("awayTeamName")) { //$NON-NLS-1$
						current_tag = TAG_AWAY_TEAM_NAME;
					} else if (localName.equals("leagueID")) { //$NON-NLS-1$
						current_tag = TAG_LEAGUE_ID;
					} else if (localName.equals("round")) { //$NON-NLS-1$
						current_tag = TAG_ROUND;
					} else if (localName.equals("season")) { //$NON-NLS-1$
						current_tag = TAG_SEASON;
					} else if (localName.equalsIgnoreCase("week")) { //$NON-NLS-1$
						current_tag = TAG_WEEK;
					} else if (localName.equalsIgnoreCase("day")) { //$NON-NLS-1$
						current_tag = TAG_DAY;
					} else if (localName.equalsIgnoreCase("dateExpected")) { //$NON-NLS-1$
						current_tag = TAG_DATE_EXPECTED;
					} else if (localName.equalsIgnoreCase("dateStarted")) { //$NON-NLS-1$
						current_tag = TAG_DATE_STARTED;
					} else if (localName.equalsIgnoreCase("homeTeamScore")) { //$NON-NLS-1$
						current_tag = TAG_HOME_TEAM_SCORE;
					} else if (localName.equalsIgnoreCase("awayTeamScore")) { //$NON-NLS-1$
						current_tag = TAG_AWAY_TEAM_SCORE;
					} else if (localName.equalsIgnoreCase("supporters")) { //$NON-NLS-1$
						current_tag = TAG_SUPPORTERS;
					} else if (localName.equalsIgnoreCase("weather")) { //$NON-NLS-1$
						current_tag = TAG_WEATHER;
					} else if (localName.equalsIgnoreCase("isFinished")) { //$NON-NLS-1$
						current_tag = TAG_IS_FINISHED;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new MatchesTeamErrorHandler());

			parser.parse(input);
		} catch (IOException e) {
			new SVLogger(Level.WARNING, "Parser Class", e); //$NON-NLS-1$
		} catch (SAXException e) {
			if (file != null) {
				new File(file).delete();
			}
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.downloader.xml.parsers.TransferXmlParserInterface#getAlTransfers()
	 */
	public ArrayList<Match> getAlMatches() {
		return alMatches;
	}
}

class MatchesTeamErrorHandler implements ErrorHandler {
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
