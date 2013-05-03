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

import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.utils.Log;

public class MatchesLeagueXmlParser {

	private static int currentTag = 0;

//	private static final int TAG_MATCHES = 1;

	private static final int TAG_MATCH = 2;

	private static final int TAG_MATCH_ID = 3;

	private static final int TAG_HOME_TEAM_ID = 4;

	private static final int TAG_AWAY_TEAM_ID = 5;

	private static final int TAG_LEAGUE_ID = 6;

	private static final int TAG_ROUND = 7;

	private static final int TAG_SEASON = 8;

	private static final int TAG_HOME_TEAM_SCORE = 9;

	private static final int TAG_AWAY_TEAM_SCORE = 10;

	private static final int TAG_IS_FINISHED = 11;

	private static int tagSwitch = 0;

	private List<Match> matches;

	private int teamId;

	private Match match;

	private Integer leagueId;

	private Integer season;

	private Integer round;

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			private StringBuilder message;

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

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
				case TAG_LEAGUE_ID:
					match.setLeagueId(Integer.parseInt(message.toString()));
					break;
				case TAG_ROUND:
					match.setRound(Integer.parseInt(message.toString()));
					break;
				case TAG_SEASON:
					match.setSeason(Integer.parseInt(message.toString()));
					break;
				case TAG_HOME_TEAM_SCORE:
					match.setHomeTeamScore(Integer.parseInt(message.toString()));
					break;
				case TAG_AWAY_TEAM_SCORE:
					match.setAwayTeamScore(Integer.parseInt(message.toString()));
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
						match.setSeason(season);
						match.setLeagueId(leagueId);
						match.setRound(round);
						if (match.getLeagueId() != -1 && match.getRound() != -1 && match.getSeason() != -1) {
							matches.add(match);
						}

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
						if (name.equalsIgnoreCase("leagueID")) { 
							leagueId = Integer.valueOf(value);
						}
						if (name.equalsIgnoreCase("season")) { 
							season = Integer.valueOf(value);
						}
						if (name.equalsIgnoreCase("round")) { 
							round = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("match")) { 
					tagSwitch = TAG_MATCH;
					match = new Match();
					match.setMatchId(-1);
					match.setHomeTeamId(0);
					match.setAwayTeamId(0);
					match.setSeason(-1);
					match.setLeagueId(-1);
					match.setRound(-1);
				}

				if (tagSwitch == TAG_MATCH) {
					if (localName.equals("matchID")) { 
						currentTag = TAG_MATCH_ID;
					} else if (localName.equalsIgnoreCase("homeTeamID")) { 
						currentTag = TAG_HOME_TEAM_ID;
					} else if (localName.equalsIgnoreCase("awayTeamID")) { 
						currentTag = TAG_AWAY_TEAM_ID;
					} else if (localName.equalsIgnoreCase("homeTeamScore")) { 
						currentTag = TAG_HOME_TEAM_SCORE;
					} else if (localName.equalsIgnoreCase("awayTeamScore")) { 
						currentTag = TAG_AWAY_TEAM_SCORE;
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
			parser.setErrorHandler(new MatchesLeagueErrorHandler());

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

class MatchesLeagueErrorHandler implements ErrorHandler {
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
