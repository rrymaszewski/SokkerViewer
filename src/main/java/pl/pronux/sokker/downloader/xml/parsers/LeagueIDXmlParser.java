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

import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.LeagueTeam;
import pl.pronux.sokker.utils.Log;

public class LeagueIDXmlParser {

	static int current_tag = 0;

	static final int TAG_LEAGUE = 0;

	static final int TAG_INFO = 1;

	static final int TAG_LEAGUE_ID = 2;

	static final int TAG_NAME = 3;

	static final int TAG_COUNTRY_ID = 4;

	static final int TAG_DIVISION = 5;

	static final int TAG_ROUND = 6;

	static final int TAG_SEASON = 7;

	static final int TAG_TYPE = 8;

	static final int TAG_IS_OFFICIAL = 9;

	static final int TAG_IS_CUP = 10;

	static final int TAG_USER_ID = 11;

	static final int TAG_TEAMS = 20;

	static final int TAG_TEAM = 21;

	static final int TAG_ROUND_TEAM = 30;

	static final int TAG_TEAM_ID = 22;

	static final int TAG_POINTS = 23;

	static final int TAG_WINS = 24;

	static final int TAG_DRAWS = 25;

	static final int TAG_LOOSES = 26;

	static final int TAG_GOALS_SCORED = 27;

	static final int TAG_GOALS_LOST = 28;

	static final int TAG_RANK_TOTAL = 29;

	static int TAG_switch = 0;

	private League league;

	private ArrayList<LeagueTeam> alTeams;

	public LeagueTeam team;
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
				case TAG_LEAGUE_ID:
					league.setLeagueID(Integer.parseInt(message.toString()));
					break;
				case TAG_NAME:
					league.setName(message.toString());
					break;
				case TAG_COUNTRY_ID:
					league.setCountryID(Integer.parseInt(message.toString()));
					break;
				case TAG_DIVISION:
					league.setDivision(Integer.parseInt(message.toString()));
					break;
				case TAG_ROUND:
					league.setRound(Integer.parseInt(message.toString()));
					break;
				case TAG_SEASON:
					league.setSeason(Integer.parseInt(message.toString()));
					break;
				case TAG_TYPE:
					league.setType(Integer.parseInt(message.toString()));
					break;
				case TAG_IS_OFFICIAL:
					league.setIsOfficial(Integer.parseInt(message.toString()));
					break;
				case TAG_IS_CUP:
					league.setIsCup(Integer.parseInt(message.toString()));
					break;
				case TAG_USER_ID:
					league.setUserID(Integer.parseInt(message.toString()));
					break;
				case TAG_TEAM_ID:
					team.setTeamID(Integer.parseInt(message.toString()));
					break;
				case TAG_ROUND_TEAM:
					team.setRound(Integer.parseInt(message.toString()));
					break;
				case TAG_WINS:
					team.setWins(Integer.parseInt(message.toString()));
					break;
				case TAG_DRAWS:
					team.setDraws(Integer.parseInt(message.toString()));
					break;
				case TAG_LOOSES:
					team.setLosses(Integer.parseInt(message.toString()));
					break;
				case TAG_GOALS_SCORED:
					team.setGoalsScored(Integer.parseInt(message.toString()));
					break;
				case TAG_GOALS_LOST:
					team.setGoalsLost(Integer.parseInt(message.toString()));
					break;
				case TAG_POINTS:
					team.setPoints(Integer.parseInt(message.toString()));
					break;
				case TAG_RANK_TOTAL:
					team.setRankTotal(message.toString());
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
				if (localName.equalsIgnoreCase("team")) { //$NON-NLS-1$
					alTeams.add(team);
				} else if (localName.equalsIgnoreCase("teams")) { //$NON-NLS-1$
					league.setLeagueTeams(alTeams);
				}
			}

			public void startDocument() {
				league = new League();
				league.setCountryID(1);
				alTeams = new ArrayList<LeagueTeam>();

			}

			StringBuffer message;

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();

				if (localName.equalsIgnoreCase("teams")) { //$NON-NLS-1$
					TAG_switch = TAG_TEAMS;
				}

				if (localName.equalsIgnoreCase("league")) { //$NON-NLS-1$
					TAG_switch = TAG_LEAGUE;
				}

				if (localName.equalsIgnoreCase("info")) { //$NON-NLS-1$
					TAG_switch = TAG_INFO;
				}

				if (TAG_switch == TAG_INFO) {
					if (localName.equals("leagueID")) { //$NON-NLS-1$
						current_tag = TAG_LEAGUE_ID;
					} else if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_NAME;
					} else if (localName.equals("countryID")) { //$NON-NLS-1$
						current_tag = TAG_COUNTRY_ID;
					} else if (localName.equals("division")) { //$NON-NLS-1$
						current_tag = TAG_DIVISION;
					} else if (localName.equals("round")) { //$NON-NLS-1$
						current_tag = TAG_ROUND;
					} else if (localName.equals("season")) { //$NON-NLS-1$
						current_tag = TAG_SEASON;
					} else if (localName.equals("type")) { //$NON-NLS-1$
						current_tag = TAG_TYPE;
					} else if (localName.equals("isOfficial")) { //$NON-NLS-1$
						current_tag = TAG_IS_OFFICIAL;
					} else if (localName.equals("isCup")) { //$NON-NLS-1$
						current_tag = TAG_IS_CUP;
					} else if (localName.equals("userID")) { //$NON-NLS-1$
						current_tag = TAG_USER_ID;
					}

				} else if (TAG_switch == TAG_TEAMS) {
					if (localName.equals("team")) { //$NON-NLS-1$
						team = new LeagueTeam();
					} else if (localName.equalsIgnoreCase("teamID")) { //$NON-NLS-1$
						current_tag = TAG_TEAM_ID;
					} else if (localName.equalsIgnoreCase("round")) { //$NON-NLS-1$
						current_tag = TAG_ROUND_TEAM;
					} else if (localName.equals("wins")) { //$NON-NLS-1$
						current_tag = TAG_WINS;
					} else if (localName.equals("draws")) { //$NON-NLS-1$
						current_tag = TAG_DRAWS;
					} else if (localName.equals("losses")) { //$NON-NLS-1$
						current_tag = TAG_LOOSES;
					} else if (localName.equals("goalsScored")) { //$NON-NLS-1$
						current_tag = TAG_GOALS_SCORED;
					} else if (localName.equals("goalsLost")) { //$NON-NLS-1$
						current_tag = TAG_GOALS_LOST;
					} else if (localName.equals("rankTotal")) { //$NON-NLS-1$
						current_tag = TAG_RANK_TOTAL;
					}  else if (localName.equals("points")) { //$NON-NLS-1$
						current_tag = TAG_POINTS;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new LeagueErrorHandler());

			parser.parse(input);
		} catch (IOException e) {
			Log.error(LeagueIDXmlParser.class, "Parser Class", e); //$NON-NLS-1$
		} catch (SAXException e) {
			if (file != null) {
				new File(file).delete();
			}
			throw e;
		}
	}
	public League getLeague() {
		league.setLeagueTeams(alTeams);
		return league;
	}

}

class LeagueErrorHandler implements ErrorHandler {
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
