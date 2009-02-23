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

import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.utils.file.SVLogger;

public class MatchesLeagueXmlParser {

	static int current_tag = 0;

	static final int TAG_MATCHES = 1;

	static final int TAG_MATCH = 2;

	static final int TAG_MATCH_ID = 3;

	static final int TAG_HOME_TEAM_ID = 4;

	static final int TAG_AWAY_TEAM_ID = 5;

	static final int TAG_LEAGUE_ID = 6;

	static final int TAG_ROUND = 7;

	static final int TAG_SEASON = 8;

	static final int TAG_HOME_TEAM_SCORE = 9;

	static final int TAG_AWAY_TEAM_SCORE = 10;

	static final int TAG_IS_FINISHED = 11;

	static int TAG_switch = 0;

	private ArrayList<Match> alMatches;

	public int teamID;

	private Match match;

	private Integer leagueID;

	private Integer season;

	private Integer round;

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			StringBuffer message;

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

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
				case TAG_LEAGUE_ID:
					match.setLeagueID(Integer.parseInt(message.toString()));
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
				current_tag = 0;
				if (localName.equals("match")) { //$NON-NLS-1$
					if (match.getMatchID() != -1) {
						match.setSeason(season);
						match.setLeagueID(leagueID);
						match.setRound(round);
						if (match.getLeagueID() != -1 && match.getRound() != -1 && match.getSeason() != -1) {
							alMatches.add(match);
						}

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
						if (name.equalsIgnoreCase("leagueID")) { //$NON-NLS-1$
							leagueID = Integer.valueOf(value);
						}
						if (name.equalsIgnoreCase("season")) { //$NON-NLS-1$
							season = Integer.valueOf(value);
						}
						if (name.equalsIgnoreCase("round")) { //$NON-NLS-1$
							round = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("match")) { //$NON-NLS-1$
					TAG_switch = TAG_MATCH;
					match = new Match();
					match.setMatchID(-1);
					match.setHomeTeamID(0);
					match.setAwayTeamID(0);
					match.setSeason(-1);
					match.setLeagueID(-1);
					match.setRound(-1);
				}

				if (TAG_switch == TAG_MATCH) {
					if (localName.equals("matchID")) { //$NON-NLS-1$
						current_tag = TAG_MATCH_ID;
					} else if (localName.equalsIgnoreCase("homeTeamID")) { //$NON-NLS-1$
						current_tag = TAG_HOME_TEAM_ID;
					} else if (localName.equalsIgnoreCase("awayTeamID")) { //$NON-NLS-1$
						current_tag = TAG_AWAY_TEAM_ID;
					} else if (localName.equalsIgnoreCase("homeTeamScore")) { //$NON-NLS-1$
						current_tag = TAG_HOME_TEAM_SCORE;
					} else if (localName.equalsIgnoreCase("awayTeamScore")) { //$NON-NLS-1$
						current_tag = TAG_AWAY_TEAM_SCORE;
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
			parser.setErrorHandler(new MatchesLeagueErrorHandler());

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
