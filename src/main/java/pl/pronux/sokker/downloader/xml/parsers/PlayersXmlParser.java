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

import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.NtSkills;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.utils.file.SVLogger;

public class PlayersXmlParser  {

	static int current_tag = 0;

	final private static int TAG_player = 1;

	final private static int TAG_id = 2;

	final private static int TAG_name = 3;

	final private static int TAG_surname = 4;

	final private static int TAG_countryID = 5;

	final private static int TAG_age = 6;

	final private static int TAG_teamID = 7;

	final private static int TAG_youthTeamID = 8;

	final private static int TAG_value = 9;

	final private static int TAG_wage = 10;

	final private static int TAG_cards = 11;

	final private static int TAG_goals = 12;

	final private static int TAG_assists = 13;

	final private static int TAG_matches = 14;

	final private static int TAG_ntCards = 15;

	final private static int TAG_ntGoals = 16;

	final private static int TAG_ntAssists = 17;

	final private static int TAG_ntMatches = 18;

	final private static int TAG_injuryDays = 19;

	final private static int TAG_national = 20;

	final private static int TAG_skillForm = 21;

	final private static int TAG_skillExperience = 22;

	final private static int TAG_skillTeamwork = 23;

	final private static int TAG_skillDiscipline = 24;

	final private static int TAG_transferList = 25;

	final private static int TAG_skillStamina = 26;

	final private static int TAG_skillPace = 27;

	final private static int TAG_skillTechnique = 28;

	final private static int TAG_skillPassing = 29;

	final private static int TAG_skillKeeper = 30;

	final private static int TAG_skillDefending = 31;

	final private static int TAG_skillPlaymaking = 32;

	final private static int TAG_skillScoring = 33;

	static int TAG_switch = 0;

	public ArrayList<Player> alPlayers;

	public int teamID;

	private Player player;

	private PlayerSkills[] playerSkills;

	private NtSkills[] ntSkills;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_id:
					player.setId(Integer.valueOf(message.toString()));
					break;
				case TAG_countryID:
					player.setCountryfrom(Integer.valueOf(message.toString()));
					break;
				case TAG_name:
					player.setName(message.toString());
					break;
				case TAG_surname:
					player.setSurname(message.toString());
					break;
				case TAG_youthTeamID:
					player.setYouthTeamID(Integer.valueOf(message.toString()));
					break;
				case TAG_teamID:
					player.setTeamID(Integer.valueOf(message.toString()));
					break;
				case TAG_national:
					player.setNational(Integer.valueOf(message.toString()));
					break;
				case TAG_transferList:
					player.setTransferList(Integer.valueOf(message.toString()));
					break;
				case TAG_age:
					playerSkills[0].setAge(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_wage:
					playerSkills[0].setSalary(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_value:
					playerSkills[0].setValue(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_cards:
					playerSkills[0].setCards(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_goals:
					playerSkills[0].setGoals(Integer.valueOf(message.toString()));
					break;
				case TAG_injuryDays:
					playerSkills[0].setInjurydays(Integer.valueOf(message.toString()));
					break;
				case TAG_assists:
					playerSkills[0].setAssists(Integer.valueOf(message.toString()));
					break;
				case TAG_matches:
					playerSkills[0].setMatches(Integer.valueOf(message.toString()));
					break;
				case TAG_ntAssists:
					ntSkills[0].setNtAssists(Integer.valueOf(message.toString()));
					break;
				case TAG_ntCards:
					ntSkills[0].setNtCards(Integer.valueOf(message.toString()));
					break;
				case TAG_ntGoals:
					ntSkills[0].setNtGoals(Integer.valueOf(message.toString()));
					break;
				case TAG_ntMatches:
					ntSkills[0].setNtMatches(Integer.valueOf(message.toString()));
					break;
				case TAG_skillForm:
					playerSkills[0].setForm(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillDiscipline:
					playerSkills[0].setDiscipline(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillExperience:
					playerSkills[0].setExperience(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillTeamwork:
					playerSkills[0].setTeamwork(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillStamina:
					playerSkills[0].setStamina(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillPace:
					playerSkills[0].setPace(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillTechnique:
					playerSkills[0].setTechnique(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillPassing:
					playerSkills[0].setPassing(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillKeeper:
					playerSkills[0].setKeeper(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillDefending:
					playerSkills[0].setDefender(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillPlaymaking:
					playerSkills[0].setPlaymaker(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_skillScoring:
					playerSkills[0].setScorer(Integer.valueOf(message.toString()).byteValue());
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
					if(player.getId() != -1 ) {
						alPlayers.add(player);
					}
				}
			}

			public void startDocument() {
				alPlayers = new ArrayList<Player>();
			}

			StringBuffer message;

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();
				if (localName.equals("players")) { //$NON-NLS-1$
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { //$NON-NLS-1$
							teamID = Integer.valueOf(value);
						}
					}
				}

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
						current_tag = TAG_id;
					} else if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_name;
					} else if (localName.equals("surname")) { //$NON-NLS-1$
						current_tag = TAG_surname;
					} else if (localName.equals("countryID")) { //$NON-NLS-1$
						current_tag = TAG_countryID;
					} else if (localName.equals("age")) { //$NON-NLS-1$
						current_tag = TAG_age;
					} else if (localName.equals("teamID")) { //$NON-NLS-1$
						current_tag = TAG_teamID;
					} else if (localName.equals("youthTeamID")) { //$NON-NLS-1$
						current_tag = TAG_youthTeamID;
					} else if (localName.equals("value")) { //$NON-NLS-1$
						current_tag = TAG_value;
					} else if (localName.equals("wage")) { //$NON-NLS-1$
						current_tag = TAG_wage;
					} else if (localName.equals("cards")) { //$NON-NLS-1$
						current_tag = TAG_cards;
					} else if (localName.equals("goals")) { //$NON-NLS-1$
						current_tag = TAG_goals;
					} else if (localName.equals("assists")) { //$NON-NLS-1$
						current_tag = TAG_assists;
					} else if (localName.equals("matches")) { //$NON-NLS-1$
						current_tag = TAG_matches;
					} else if (localName.equals("ntAssists")) { //$NON-NLS-1$
						current_tag = TAG_ntAssists;
					} else if (localName.equals("ntCards")) { //$NON-NLS-1$
						current_tag = TAG_ntCards;
					} else if (localName.equals("ntGoals")) { //$NON-NLS-1$
						current_tag = TAG_ntGoals;
					} else if (localName.equals("ntMatches")) { //$NON-NLS-1$
						current_tag = TAG_ntMatches;
					} else if (localName.equals("injuryDays")) { //$NON-NLS-1$
						current_tag = TAG_injuryDays;
					} else if (localName.equals("national")) { //$NON-NLS-1$
						current_tag = TAG_national;
					} else if (localName.equals("skillForm")) { //$NON-NLS-1$
						current_tag = TAG_skillForm;
					} else if (localName.equals("skillExperience")) { //$NON-NLS-1$
						current_tag = TAG_skillExperience;
					} else if (localName.equals("skillTeamwork")) { //$NON-NLS-1$
						current_tag = TAG_skillTeamwork;
					} else if (localName.equals("skillDiscipline")) { //$NON-NLS-1$
						current_tag = TAG_skillDiscipline;
					} else if (localName.equals("transferList")) { //$NON-NLS-1$
						current_tag = TAG_transferList;
					} else if (localName.equals("skillStamina")) { //$NON-NLS-1$
						current_tag = TAG_skillStamina;
					} else if (localName.equals("skillPace")) { //$NON-NLS-1$
						current_tag = TAG_skillPace;
					} else if (localName.equals("skillTechnique")) { //$NON-NLS-1$
						current_tag = TAG_skillTechnique;
					} else if (localName.equals("skillPassing")) { //$NON-NLS-1$
						current_tag = TAG_skillPassing;
					} else if (localName.equals("skillKeeper")) { //$NON-NLS-1$
						current_tag = TAG_skillKeeper;
					} else if (localName.equals("skillDefending")) { //$NON-NLS-1$
						current_tag = TAG_skillDefending;
					} else if (localName.equals("skillPlaymaking")) { //$NON-NLS-1$
						current_tag = TAG_skillPlaymaking;
					} else if (localName.equals("skillScoring")) { //$NON-NLS-1$
						current_tag = TAG_skillScoring;
					}

				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new PlayersErrorHandler());

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

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.downloader.xml.parsers.PlayersXmlParserInterface#getAlPlayers()
	 */
	public ArrayList<Player> getAlPlayers() {
		return alPlayers;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.downloader.xml.parsers.PlayersXmlParserInterface#getTeamID()
	 */
	public int getTeamID() {
		return teamID;
	}
}

class PlayersErrorHandler implements ErrorHandler {
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
