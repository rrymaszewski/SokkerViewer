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
import pl.pronux.sokker.utils.Log;

public class MatchesTeamXmlParser {

	private static int currentTag = 0;

//	private static final int TAG_MATCHES = 1;

	private static final int TAG_MATCH = 2;

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

	private static int tagSwitch = 0;

	private List<Match> matches;

	private int teamId;

	private Match match;

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			private StringBuilder message;

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
				switch (currentTag) {
				case TAG_MATCH_ID:
					match.setMatchId(Integer.parseInt(message.toString()));
					break;
				case TAG_HOME_TEAM_ID:
					match.setHomeTeamId(Integer.parseInt(message.toString()));
					break;
				case TAG_AWAY_TEAM_ID:
					match.setAwayTeamId(Integer.parseInt(message.toString()));
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
				currentTag = 0;
				if (localName.equals("match")) { 
					if (match.getMatchId() != -1) {
						match.setTeamID(teamId);
						matches.add(match);
					}
				}
			}

			public void startDocument() {
				matches = new ArrayList<Match>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {
				message = new StringBuilder();
				if (localName.equals("matches")) { 
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { 
							teamId = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("match")) { 
					tagSwitch = TAG_MATCH;
					match = new Match();
					match.setMatchId(-1);
					match.setHomeTeamId(0);
					match.setAwayTeamId(0);
					match.setHomeTeamName(""); 
					match.setAwayTeamName(""); 
				}

				if (tagSwitch == TAG_MATCH) {
					if (localName.equals("matchID")) { 
						currentTag = TAG_MATCH_ID;
					} else if (localName.equalsIgnoreCase("homeTeamID")) { 
						currentTag = TAG_HOME_TEAM_ID;
					} else if (localName.equalsIgnoreCase("awayTeamID")) { 
						currentTag = TAG_AWAY_TEAM_ID;
					} else if (localName.equalsIgnoreCase("homeTeamName")) { 
						currentTag = TAG_HOME_TEAM_NAME;
					} else if (localName.equals("awayTeamName")) { 
						currentTag = TAG_AWAY_TEAM_NAME;
					} else if (localName.equals("leagueID")) { 
						currentTag = TAG_LEAGUE_ID;
					} else if (localName.equals("round")) { 
						currentTag = TAG_ROUND;
					} else if (localName.equals("season")) { 
						currentTag = TAG_SEASON;
					} else if (localName.equalsIgnoreCase("week")) { 
						currentTag = TAG_WEEK;
					} else if (localName.equalsIgnoreCase("day")) { 
						currentTag = TAG_DAY;
					} else if (localName.equalsIgnoreCase("dateExpected")) { 
						currentTag = TAG_DATE_EXPECTED;
					} else if (localName.equalsIgnoreCase("dateStarted")) { 
						currentTag = TAG_DATE_STARTED;
					} else if (localName.equalsIgnoreCase("homeTeamScore")) { 
						currentTag = TAG_HOME_TEAM_SCORE;
					} else if (localName.equalsIgnoreCase("awayTeamScore")) { 
						currentTag = TAG_AWAY_TEAM_SCORE;
					} else if (localName.equalsIgnoreCase("supporters")) { 
						currentTag = TAG_SUPPORTERS;
					} else if (localName.equalsIgnoreCase("weather")) { 
						currentTag = TAG_WEATHER;
					} else if (localName.equalsIgnoreCase("isFinished")) { 
						currentTag = TAG_IS_FINISHED;
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
			Log.error("Parser Class", e); 
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
	public List<Match> getMatches() {
		return matches;
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
