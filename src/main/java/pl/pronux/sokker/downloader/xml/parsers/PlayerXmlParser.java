package pl.pronux.sokker.downloader.xml.parsers;

import java.io.File;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.NtSkills;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.utils.Log;

public class PlayerXmlParser {

	static int current_tag = 0;

	private static final int TAG_player = 1;

	private static final int TAG_ID = 2;

	private static final int TAG_NAME = 3;

	private static final int TAG_SURNAME = 4;

	private static final int TAG_COUNTRYID = 5;

	private static final int TAG_AGE = 6;

	private static final int TAG_TEAMID = 7;

	private static final int TAG_YOUTHTEAMID = 8;

	private static final int TAG_VALUE = 9;

	private static final int TAG_WAGE = 10;

	private static final int TAG_CARDS = 11;

	private static final int TAG_GOALS = 12;

	private static final int TAG_ASSISTS = 13;

	private static final int TAG_MATCHES = 14;

	private static final int TAG_NTCARDS = 15;

	private static final int TAG_NTGOALS = 16;

	private static final int TAG_NTASSISTS = 17;

	private static final int TAG_NTMATCHES = 18;

	private static final int TAG_INJURYDAYS = 19;

	private static final int TAG_NATIONAL = 20;

	private static final int TAG_SKILLFORM = 21;

	private static final int TAG_SKILLEXPERIENCE = 22;

	private static final int TAG_SKILLTEAMWORK = 23;

	private static final int TAG_SKILLDISCIPLINE = 24;

	private static final int TAG_TRANSFERLIST = 25;

	private static final int TAG_SKILLSTAMINA = 26;

	private static final int TAG_SKILLPACE = 27;

	private static final int TAG_SKILLTECHNIQUE = 28;

	private static final int TAG_SKILLPASSING = 29;

	private static final int TAG_SKILLKEEPER = 30;

	private static final int TAG_SKILLDEFENDING = 31;

	private static final int TAG_SKILLPLAYMAKING = 32;

	private static final int TAG_SKILLSCORING = 33;

	private static final int TAG_HEIGHT = 34;
	
	static int TAG_switch = 0;

	private Player player;

	private PlayerSkills[] playerSkills;

	private NtSkills[] ntSkills;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_ID:
					player.setId(Integer.valueOf(message.toString()));
					break;
				case TAG_COUNTRYID:
					player.setCountryfrom(Integer.valueOf(message.toString()));
					break;
				case TAG_NAME:
					player.setName(message.toString());
					break;
				case TAG_SURNAME:
					player.setSurname(message.toString());
					break;
				case TAG_YOUTHTEAMID:
					player.setYouthTeamID(Integer.valueOf(message.toString()));
					break;
				case TAG_TEAMID:
					player.setTeamID(Integer.valueOf(message.toString()));
					break;
				case TAG_NATIONAL:
					player.setNational(Integer.valueOf(message.toString()));
					break;
				case TAG_TRANSFERLIST:
					player.setTransferList(Integer.valueOf(message.toString()));
					break;
				case TAG_AGE:
					playerSkills[0].setAge(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_WAGE:
					playerSkills[0].setSalary(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_VALUE:
					playerSkills[0].setValue(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_CARDS:
					playerSkills[0].setCards(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_GOALS:
					playerSkills[0].setGoals(Integer.valueOf(message.toString()));
					break;
				case TAG_INJURYDAYS:
					playerSkills[0].setInjurydays(Integer.valueOf(message.toString()));
					break;
				case TAG_ASSISTS:
					playerSkills[0].setAssists(Integer.valueOf(message.toString()));
					break;
				case TAG_MATCHES:
					playerSkills[0].setMatches(Integer.valueOf(message.toString()));
					break;
				case TAG_NTASSISTS:
					ntSkills[0].setNtAssists(Integer.valueOf(message.toString()));
					break;
				case TAG_NTCARDS:
					ntSkills[0].setNtCards(Integer.valueOf(message.toString()));
					break;
				case TAG_NTGOALS:
					ntSkills[0].setNtGoals(Integer.valueOf(message.toString()));
					break;
				case TAG_NTMATCHES:
					ntSkills[0].setNtMatches(Integer.valueOf(message.toString()));
					break;
				case TAG_SKILLFORM:
					playerSkills[0].setForm(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLDISCIPLINE:
					playerSkills[0].setDiscipline(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLEXPERIENCE:
					playerSkills[0].setExperience(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLTEAMWORK:
					playerSkills[0].setTeamwork(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLSTAMINA:
					playerSkills[0].setStamina(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLPACE:
					playerSkills[0].setPace(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLTECHNIQUE:
					playerSkills[0].setTechnique(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLPASSING:
					playerSkills[0].setPassing(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLKEEPER:
					playerSkills[0].setKeeper(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLDEFENDING:
					playerSkills[0].setDefender(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLPLAYMAKING:
					playerSkills[0].setPlaymaker(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILLSCORING:
					playerSkills[0].setScorer(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_HEIGHT:
					player.setHeight(Integer.valueOf(message.toString()).intValue());
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
				if (localName.equals("player")) { //$NON-NLS-1$
					player.setSkills(playerSkills);
					player.setNtSkills(ntSkills);
					if(player.getId() == -1) {
						player = null;
					}
				}
			}

			public void startDocument() {
			}

			StringBuilder message;

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();

				if (localName.equals("player")) { //$NON-NLS-1$

					TAG_switch = TAG_player;

					player = new Player();
					player.setId(-1);

					playerSkills = new PlayerSkills[1];
					ntSkills = new NtSkills[1];
					playerSkills[0] = new PlayerSkills();
					ntSkills[0] = new NtSkills();

				}

				if (TAG_switch == TAG_player) {
					if (localName.equals("ID")) { //$NON-NLS-1$
						current_tag = TAG_ID;
					} else if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_NAME;
					} else if (localName.equals("surname")) { //$NON-NLS-1$
						current_tag = TAG_SURNAME;
					} else if (localName.equals("countryID")) { //$NON-NLS-1$
						current_tag = TAG_COUNTRYID;
					} else if (localName.equals("age")) { //$NON-NLS-1$
						current_tag = TAG_AGE;
					} else if (localName.equals("teamID")) { //$NON-NLS-1$
						current_tag = TAG_TEAMID;
					} else if (localName.equals("youthTeamID")) { //$NON-NLS-1$
						current_tag = TAG_YOUTHTEAMID;
					} else if (localName.equals("value")) { //$NON-NLS-1$
						current_tag = TAG_VALUE;
					} else if (localName.equals("wage")) { //$NON-NLS-1$
						current_tag = TAG_WAGE;
					} else if (localName.equals("cards")) { //$NON-NLS-1$
						current_tag = TAG_CARDS;
					} else if (localName.equals("goals")) { //$NON-NLS-1$
						current_tag = TAG_GOALS;
					} else if (localName.equals("assists")) { //$NON-NLS-1$
						current_tag = TAG_ASSISTS;
					} else if (localName.equals("matches")) { //$NON-NLS-1$
						current_tag = TAG_MATCHES;
					} else if (localName.equals("ntAssists")) { //$NON-NLS-1$
						current_tag = TAG_NTASSISTS;
					} else if (localName.equals("ntCards")) { //$NON-NLS-1$
						current_tag = TAG_NTCARDS;
					} else if (localName.equals("ntGoals")) { //$NON-NLS-1$
						current_tag = TAG_NTGOALS;
					} else if (localName.equals("ntMatches")) { //$NON-NLS-1$
						current_tag = TAG_NTMATCHES;
					} else if (localName.equals("injuryDays")) { //$NON-NLS-1$
						current_tag = TAG_INJURYDAYS;
					} else if (localName.equals("national")) { //$NON-NLS-1$
						current_tag = TAG_NATIONAL;
					} else if (localName.equals("skillForm")) { //$NON-NLS-1$
						current_tag = TAG_SKILLFORM;
					} else if (localName.equals("skillExperience")) { //$NON-NLS-1$
						current_tag = TAG_SKILLEXPERIENCE;
					} else if (localName.equals("skillTeamwork")) { //$NON-NLS-1$
						current_tag = TAG_SKILLTEAMWORK;
					} else if (localName.equals("skillDiscipline")) { //$NON-NLS-1$
						current_tag = TAG_SKILLDISCIPLINE;
					} else if (localName.equals("transferList")) { //$NON-NLS-1$
						current_tag = TAG_TRANSFERLIST;
					} else if (localName.equals("skillStamina")) { //$NON-NLS-1$
						current_tag = TAG_SKILLSTAMINA;
					} else if (localName.equals("skillPace")) { //$NON-NLS-1$
						current_tag = TAG_SKILLPACE;
					} else if (localName.equals("skillTechnique")) { //$NON-NLS-1$
						current_tag = TAG_SKILLTECHNIQUE;
					} else if (localName.equals("skillPassing")) { //$NON-NLS-1$
						current_tag = TAG_SKILLPASSING;
					} else if (localName.equals("skillKeeper")) { //$NON-NLS-1$
						current_tag = TAG_SKILLKEEPER;
					} else if (localName.equals("skillDefending")) { //$NON-NLS-1$
						current_tag = TAG_SKILLDEFENDING;
					} else if (localName.equals("skillPlaymaking")) { //$NON-NLS-1$
						current_tag = TAG_SKILLPLAYMAKING;
					} else if (localName.equals("skillScoring")) { //$NON-NLS-1$
						current_tag = TAG_SKILLSCORING;
					} else if (localName.equals("height")) {
						current_tag = TAG_HEIGHT;
					}

				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new PlayerErrorHandler());

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

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.downloader.xml.parsers.PlayerXmlParserInterface#getPlayer()
	 */
	public Player getPlayer() {
		return this.player;
	}

}

class PlayerErrorHandler implements ErrorHandler {
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
