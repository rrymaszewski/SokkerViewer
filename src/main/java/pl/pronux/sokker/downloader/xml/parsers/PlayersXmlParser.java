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

import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.NtSkills;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.utils.Log;

public class PlayersXmlParser  {

	private static int currentTag = 0;

	private static final int TAG_PLAYER = 1;

	private static final int TAG_ID = 2;

	private static final int TAG_NAME = 3;

	private static final int TAG_SURNAME = 4;

	private static final int TAG_COUNTRY_ID = 5;

	private static final int TAG_AGE = 6;

	private static final int TAG_TEAM_ID = 7;

	private static final int TAG_YOUTH_TEAM_ID = 8;

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

	private static final int TAG_INJURY_DAYS = 19;

	private static final int TAG_NATIONAL = 20;

	private static final int TAG_SKILL_FORM = 21;

	private static final int TAG_SKILL_EXPERIENCE = 22;

	private static final int TAG_SKILL_TEAMWORK = 23;

	private static final int TAG_SKILL_DISCIPLINE = 24;

	private static final int TAG_TRANSFER_LIST = 25;

	private static final int TAG_SKILL_STAMINA = 26;

	private static final int TAG_SKILL_PACE = 27;

	private static final int TAG_SKILL_TECHNIQUE = 28;

	private static final int TAG_SKILL_PASSING = 29;

	private static final int TAG_SKILL_KEEPER = 30;

	private static final int TAG_SKILL_DEFENDING = 31;

	private static final int TAG_SKILL_PLAYMAKING = 32;

	private static final int TAG_SKILL_SCORING = 33;
	
	private static final int TAG_HEIGHT = 34;

	private static final int TAG_WEIGHT = 35;
	
	private static final int TAG_BMI = 36;
	
	private static int tagSwitch = 0;

	private List<Player> players;

	private int teamId;

	private Player player;

	private PlayerSkills[] playerSkills;

	private NtSkills[] ntSkills;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			private StringBuilder message;

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_ID:
					player.setId(Integer.valueOf(message.toString()));
					break;
				case TAG_COUNTRY_ID:
					player.setCountryfrom(Integer.valueOf(message.toString()));
					break;
				case TAG_NAME:
					player.setName(message.toString());
					break;
				case TAG_SURNAME:
					player.setSurname(message.toString());
					break;
				case TAG_YOUTH_TEAM_ID:
					player.setYouthTeamId(Integer.valueOf(message.toString()));
					break;
				case TAG_TEAM_ID:
					player.setTeamId(Integer.valueOf(message.toString()));
					break;
				case TAG_NATIONAL:
					player.setNational(Integer.valueOf(message.toString()));
					break;
				case TAG_TRANSFER_LIST:
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
				case TAG_INJURY_DAYS:
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
				case TAG_SKILL_FORM:
					playerSkills[0].setForm(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_DISCIPLINE:
					playerSkills[0].setDiscipline(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_EXPERIENCE:
					playerSkills[0].setExperience(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_TEAMWORK:
					playerSkills[0].setTeamwork(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_STAMINA:
					playerSkills[0].setStamina(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_PACE:
					playerSkills[0].setPace(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_TECHNIQUE:
					playerSkills[0].setTechnique(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_PASSING:
					playerSkills[0].setPassing(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_KEEPER:
					playerSkills[0].setKeeper(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_DEFENDING:
					playerSkills[0].setDefender(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_PLAYMAKING:
					playerSkills[0].setPlaymaker(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_SKILL_SCORING:
					playerSkills[0].setScorer(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_HEIGHT:
					player.setHeight(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_WEIGHT:
					playerSkills[0].setWeight(Double.valueOf(message.toString()).doubleValue());
					break;
				case TAG_BMI:
					playerSkills[0].setBmi(Double.valueOf(message.toString()).doubleValue());
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
				if (localName.equals("player")) { 
					player.setSkills(playerSkills);
					player.setNtSkills(ntSkills);
					if(player.getId() != -1 ) {
						players.add(player);
					}
				}
			}

			public void startDocument() {
				players = new ArrayList<Player>();
			}


			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("players")) { 
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { 
							teamId = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("player")) { 

					tagSwitch = TAG_PLAYER;

					player = new Player();
					player.setId(-1);

					playerSkills = new PlayerSkills[1];
					ntSkills = new NtSkills[1];
					playerSkills[0] = new PlayerSkills();
					ntSkills[0] = new NtSkills();

				}

				if (tagSwitch == TAG_PLAYER) {
					if (localName.equals("ID")) { 
						currentTag = TAG_ID;
					} else if (localName.equals("name")) { 
						currentTag = TAG_NAME;
					} else if (localName.equals("surname")) { 
						currentTag = TAG_SURNAME;
					} else if (localName.equals("countryID")) { 
						currentTag = TAG_COUNTRY_ID;
					} else if (localName.equals("age")) { 
						currentTag = TAG_AGE;
					} else if (localName.equals("teamID")) { 
						currentTag = TAG_TEAM_ID;
					} else if (localName.equals("youthTeamID")) { 
						currentTag = TAG_YOUTH_TEAM_ID;
					} else if (localName.equals("value")) { 
						currentTag = TAG_VALUE;
					} else if (localName.equals("wage")) { 
						currentTag = TAG_WAGE;
					} else if (localName.equals("cards")) { 
						currentTag = TAG_CARDS;
					} else if (localName.equals("goals")) { 
						currentTag = TAG_GOALS;
					} else if (localName.equals("assists")) { 
						currentTag = TAG_ASSISTS;
					} else if (localName.equals("matches")) { 
						currentTag = TAG_MATCHES;
					} else if (localName.equals("ntAssists")) { 
						currentTag = TAG_NTASSISTS;
					} else if (localName.equals("ntCards")) { 
						currentTag = TAG_NTCARDS;
					} else if (localName.equals("ntGoals")) { 
						currentTag = TAG_NTGOALS;
					} else if (localName.equals("ntMatches")) { 
						currentTag = TAG_NTMATCHES;
					} else if (localName.equals("injuryDays")) { 
						currentTag = TAG_INJURY_DAYS;
					} else if (localName.equals("national")) { 
						currentTag = TAG_NATIONAL;
					} else if (localName.equals("skillForm")) { 
						currentTag = TAG_SKILL_FORM;
					} else if (localName.equals("skillExperience")) { 
						currentTag = TAG_SKILL_EXPERIENCE;
					} else if (localName.equals("skillTeamwork")) { 
						currentTag = TAG_SKILL_TEAMWORK;
					} else if (localName.equals("skillDiscipline")) { 
						currentTag = TAG_SKILL_DISCIPLINE;
					} else if (localName.equals("transferList")) { 
						currentTag = TAG_TRANSFER_LIST;
					} else if (localName.equals("skillStamina")) { 
						currentTag = TAG_SKILL_STAMINA;
					} else if (localName.equals("skillPace")) { 
						currentTag = TAG_SKILL_PACE;
					} else if (localName.equals("skillTechnique")) { 
						currentTag = TAG_SKILL_TECHNIQUE;
					} else if (localName.equals("skillPassing")) { 
						currentTag = TAG_SKILL_PASSING;
					} else if (localName.equals("skillKeeper")) { 
						currentTag = TAG_SKILL_KEEPER;
					} else if (localName.equals("skillDefending")) { 
						currentTag = TAG_SKILL_DEFENDING;
					} else if (localName.equals("skillPlaymaking")) { 
						currentTag = TAG_SKILL_PLAYMAKING;
					} else if (localName.equals("skillScoring")) { 
						currentTag = TAG_SKILL_SCORING;
					} else if (localName.equals("height")) {
						currentTag = TAG_HEIGHT;
					} else if (localName.equals("weight")) {
						currentTag = TAG_WEIGHT;
					} else if (localName.equals("BMI")) {
						currentTag = TAG_BMI;
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
			Log.error("Parser Class", e); 
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
	public List<Player> getPlayers() {
		return players;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.downloader.xml.parsers.PlayersXmlParserInterface#getTeamID()
	 */
	public int getTeamId() {
		return teamId;
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
