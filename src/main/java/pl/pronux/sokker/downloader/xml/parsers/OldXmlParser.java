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

import pl.pronux.sokker.model.Arena;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.ClubArenaName;
import pl.pronux.sokker.model.ClubBudget;
import pl.pronux.sokker.model.ClubName;
import pl.pronux.sokker.model.ClubSupporters;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.Rank;
import pl.pronux.sokker.model.Stand;
import pl.pronux.sokker.model.Training;

public class OldXmlParser {

	private static int currentTag = 0;

	private static final int TAG_ARENA_CAPACITY = 26;

	private static final int TAG_ARENA_DAYS = 28;

//	private static final int TAG_ARENA_LOCATION = 29;

	private static final int TAG_ARENA_NAME = 31;

	private static final int TAG_ARENA_ROOF = 30;

	private static final int TAG_ARENA_TYPE = 27;

//	private static final int TAG_CLUB_ARENANAME = 39;

//	private static final int TAG_CLUB_COUNTRY = 34;

	private static final int TAG_CLUB_FANCLUBCOUNT = 37;

	private static final int TAG_CLUB_FANCLUBMOOD = 38;

//	private static final int TAG_CLUB_ID = 32;

	private static final int TAG_CLUB_MONEY = 36;

	private static final int TAG_CLUB_NAME = 33;

//	private static final int TAG_CLUB_REGION = 35;

	private static final int TAG_COACH_AGE = 46;

//	private static final int TAG_COACH_COUNTRYFROM = 45;

	private static final int TAG_COACH_DEFENDERS = 54;

	private static final int TAG_COACH_GENERALSKILL = 48;

//	private static final int TAG_COACH_ID = 40;

	private static final int TAG_COACH_JOB = 44;

	private static final int TAG_COACH_KEEPERS = 53;

	private static final int TAG_COACH_NAME = 42;

	private static final int TAG_COACH_PACE = 50;

	private static final int TAG_COACH_PASSING = 52;

	private static final int TAG_COACH_PLAYMAKERS = 55;

	private static final int TAG_COACH_SALARY = 47;

	private static final int TAG_COACH_SCORERS = 56;

//	private static final int TAG_COACH_SIGNED = 41;

	private static final int TAG_COACH_STAMINA = 49;

	private static final int TAG_COACH_SURNAME = 43;

	private static final int TAG_COACH_TECHNIQUE = 51;

//	private static final int TAG_JUNIOR_ID = 21;

	private static final int TAG_JUNIOR_NAME = 22;

	private static final int TAG_JUNIOR_SKILL = 25;

	private static final int TAG_JUNIOR_SURNAME = 23;

	private static final int TAG_JUNIOR_WEEKS = 24;

	private static final int TAG_PLAYER_AGE = 4;

	private static final int TAG_PLAYER_ASSISTS = 11;

	private static final int TAG_PLAYER_CARDS = 7;

	private static final int TAG_PLAYER_COUNTRYFROM = 3;

	private static final int TAG_PLAYER_DEFENDER = 18;

	private static final int TAG_PLAYER_FORM = 12;

	private static final int TAG_PLAYER_GOALS = 10;

	private static final int TAG_PLAYER_INJURYDAYS = 8;

	private static final int TAG_PLAYER_KEEPER = 17;

	private static final int TAG_PLAYER_MATCHES = 9;

	private static final int TAG_PLAYER_NAME = 1;

	private static final int TAG_PLAYER_PACE = 14;

	private static final int TAG_PLAYER_PASSING = 16;

	private static final int TAG_PLAYER_PLAYMAKER = 19;

	private static final int TAG_PLAYER_SALARY = 6;

	private static final int TAG_PLAYER_SCORER = 20;

	private static final int TAG_PLAYER_STAMINA = 13;

	private static final int TAG_PLAYER_SURNAME = 2;

	private static final int TAG_PLAYER_TECHNIQUE = 15;

	private static final int TAG_PLAYER_VALUE = 5;

	private static final int TAG_PLAYERS = 99;

	private static final int TAG_SOKKERDATA = 100;

	private static int tagSwitch = 0;

	private List<Coach> coaches;

	private List<Junior> juniors;

	private List<Player> players;

	private Arena arena;

	private Club club;

	private Coach coach;

	private Junior junior;

	private JuniorSkills[] juniorSkills;

	private Player player;

	private PlayerSkills[] playerSkills;

	private List<ClubSupporters> clubDataSupporters;

	private List<ClubBudget> clubDataMoney;

	private List<ClubName> clubName;

	private List<ClubArenaName> alArenaName;

	private Stand stand;

	private List<Stand> alStand;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException, IOException {


		class SAXHandler extends DefaultHandler {

			private StringBuilder message;

			public void characters(char ch[], int start, int length) throws SAXException {
				message.append(new String(ch, start, length));
				switch (currentTag) {
				case TAG_PLAYERS:
					// TODO jezeli wchodzimy tutaj i sa jakies znaki to ladujemy exception
					break;
				case TAG_SOKKERDATA:
					if (message.toString().equalsIgnoreCase("empty")) { 
						throw new SAXException();
					}
					break;
				case TAG_PLAYER_NAME:
					player.setName(message.toString());
					break;
				case TAG_PLAYER_SURNAME:
					player.setSurname(message.toString());
					break;
				case TAG_PLAYER_COUNTRYFROM:
					// player.setCountry(message.toString());
					break;
				case TAG_PLAYER_AGE:
					playerSkills[0].setAge(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_PLAYER_VALUE:
					playerSkills[0].setValue(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_PLAYER_SALARY:
					playerSkills[0].setSalary(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_PLAYER_CARDS:
					playerSkills[0].setCards(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_PLAYER_INJURYDAYS:
					playerSkills[0].setInjurydays(Double.valueOf(message.toString()).doubleValue());
					break;
				case TAG_PLAYER_MATCHES:
					playerSkills[0].setMatches(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_PLAYER_GOALS:
					playerSkills[0].setGoals(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_PLAYER_ASSISTS:
					playerSkills[0].setAssists(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_PLAYER_FORM:
					playerSkills[0].setForm(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_PLAYER_STAMINA:
					playerSkills[0].setStamina(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_PLAYER_PACE:
					playerSkills[0].setPace(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_PLAYER_TECHNIQUE:
					playerSkills[0].setTechnique(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_PLAYER_PASSING:
					playerSkills[0].setPassing(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_PLAYER_KEEPER:
					playerSkills[0].setKeeper(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_PLAYER_DEFENDER:
					playerSkills[0].setDefender(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_PLAYER_PLAYMAKER:
					playerSkills[0].setPlaymaker(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_PLAYER_SCORER:
					playerSkills[0].setScorer(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_JUNIOR_NAME:
					junior.setName(message.toString());
					break;
				case TAG_JUNIOR_SURNAME:
					junior.setSurname(message.toString());
					break;
				case TAG_JUNIOR_WEEKS:
					juniorSkills[0].setWeeks(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_JUNIOR_SKILL:
					juniorSkills[0].setSkill(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_CLUB_NAME:
					club.getClubName().get(0).setName(message.toString());
					break;
				case TAG_CLUB_MONEY:
					club.getClubBudget().get(0).setMoney(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_CLUB_FANCLUBCOUNT:
					club.getClubSupporters().get(0).setFanclubcount(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_CLUB_FANCLUBMOOD:
					club.getClubSupporters().get(0).setFanclubmood(Byte.valueOf(message.toString()).byteValue());
					break;
				case TAG_ARENA_NAME:
					alArenaName.get(0).setArenaName(message.toString());
					break;
				case TAG_ARENA_CAPACITY:
					stand.setCapacity(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_ARENA_TYPE:
					stand.setType(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_ARENA_DAYS:
					stand.setConstructionDays(Double.valueOf(message.toString()).doubleValue());
					break;
				case TAG_ARENA_ROOF:
					stand.setIsRoof(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_COACH_AGE:
					coach.setAge(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_COACH_NAME:
					coach.setName(message.toString());
					/*
					 * Remove white spaces like \t
					 */
					coach.setName(coach.getName().replaceAll("\t", ""));  
					break;
				case TAG_COACH_SURNAME:
					coach.setSurname(message.toString());
					/*
					 * Remove white spaces like \t
					 */
					coach.setSurname(coach.getSurname().replaceAll("\t", ""));  
					break;
				case TAG_COACH_JOB:
					String job = message.toString();
					if(job.equalsIgnoreCase("head")) { 
						coach.setJob(Coach.JOB_HEAD);
					} else if(job.equalsIgnoreCase("juniors")) { 
						coach.setJob(Coach.JOB_JUNIORS);
					} else if(job.equalsIgnoreCase("none")) { 
						coach.setJob(Coach.JOB_NONE);
					} else if(job.equalsIgnoreCase("assistant")) { 
						coach.setJob(Coach.JOB_ASSISTANT);
					}
					break;
				case TAG_COACH_SALARY:
					coach.setSalary(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_COACH_GENERALSKILL:
					coach.setGeneralskill(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_COACH_STAMINA:
					coach.setStamina(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_COACH_PACE:
					coach.setPace(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_COACH_TECHNIQUE:
					coach.setTechnique(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_COACH_PASSING:
					coach.setPassing(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_COACH_KEEPERS:
					coach.setKeepers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_COACH_DEFENDERS:
					coach.setDefenders(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_COACH_PLAYMAKERS:
					coach.setPlaymakers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_COACH_SCORERS:
					coach.setScorers(Integer.valueOf(message.toString()).byteValue());
					break;
				default:
					break;
				}
			}

			// obsluga bledow

			public void endDocument() {
				Training training = new Training();
				training.setJuniors(juniors);
				training.setPlayers(players);
				training.setType(Training.TYPE_UNKNOWN);
				training.setFormation(Training.FORMATION_ALL);
				training.setAssistants(new ArrayList<Coach>());
				for(Coach coach : coaches) {
					if(coach.getJob() == Coach.JOB_HEAD) {
						training.setHeadCoach(coach);
					} else if(coach.getJob() == Coach.JOB_JUNIORS) {
						training.setJuniorCoach(coach);
					} else if(coach.getJob() == Coach.JOB_ASSISTANT) {
						training.getAssistants().add(coach);
					}
				}
				club.setRank(new ArrayList<Rank>());
				club.setJuniors(juniors);
				club.setPlayers(players);
				club.setCoaches(coaches);
				club.setArena(arena);
				club.setClubBudget(clubDataMoney);
				club.setClubName(clubName);
				club.setClubSupporters(clubDataSupporters);
				club.setTraining(training);
			}

			public void endElement(String namespaceURL, String localName, String qName) {

				currentTag = 0;

				if (localName.equals("player")) { 
					player.setSkills(playerSkills);
					players.add(player);
				} else if (localName.equals("junior")) { 
					junior.setSkills(juniorSkills);
					juniors.add(junior);
				} else if (localName.equalsIgnoreCase("stand")) { 
					alStand.add(stand);
				} else if (localName.equalsIgnoreCase("coach")) { 
					coaches.add(coach);
				}else if (localName.equalsIgnoreCase("arena")) { 
					arena.setArenaNames(alArenaName);
					arena.setStands(alStand);
				}

			}

			public void startDocument() {
				players = new ArrayList<Player>();
				juniors = new ArrayList<Junior>();
				coaches = new ArrayList<Coach>();
				alStand = new ArrayList<Stand>();
			}


			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();

				if (localName.equalsIgnoreCase("sokkerdata")) { 
					tagSwitch = 6;
				}

				if (localName.equalsIgnoreCase("players")) { 
					tagSwitch = 7;
				}

				if (localName.equals("player")) { 
					tagSwitch = 1;

					player = new Player();
					playerSkills = new PlayerSkills[1];
					playerSkills[0] = new PlayerSkills();

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("ID")) { 
							player.setId(Integer.valueOf(value).intValue());
						}
					}
				} else if (localName.equals("junior")) { 

					tagSwitch = 2;

					junior = new Junior();
					juniorSkills = new JuniorSkills[1];
					juniorSkills[0] = new JuniorSkills();

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("ID")) { 
							junior.setId(Integer.valueOf(value).intValue());
						}
					}
				} else if (localName.equalsIgnoreCase("team")) { 

					tagSwitch = 3;
					club = new Club();
					clubDataSupporters = new ArrayList<ClubSupporters>();
					clubDataSupporters.add(new ClubSupporters());

					clubDataMoney = new ArrayList<ClubBudget>();
					clubDataMoney.add(new ClubBudget());

					clubName = new ArrayList<ClubName>();
					clubName.add(new ClubName());

					alArenaName = new ArrayList<ClubArenaName>();
					alArenaName.add(new ClubArenaName());

					club.setClubSupporters(clubDataSupporters);
					club.setClubBudget(clubDataMoney);
					club.setClubName(clubName);

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("ID")) { 
							club.setId(Integer.valueOf(value).intValue());
						}
					}

				} else if (localName.equalsIgnoreCase("arena")) { 

					tagSwitch = 4;
					arena = new Arena();
				} else if (localName.equalsIgnoreCase("coach")) { 

					tagSwitch = 5;
					coach = new Coach();

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("ID")) { 
							coach.setId(Integer.valueOf(value).intValue());
						} else if (name.equalsIgnoreCase("signed")) { 
							coach.setSigned(Integer.valueOf(value).byteValue());
						}
					}
				}

				if (tagSwitch == 1) {
					if (localName.equals("name")) { 
						currentTag = TAG_PLAYER_NAME;
					} else if (localName.equals("surname")) { 
						currentTag = TAG_PLAYER_SURNAME;
					} else if (localName.equals("countryfrom")) { 

						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("ID")) { 
								player.setCountryfrom(Integer.valueOf(value).intValue());
							}
						}
						currentTag = TAG_PLAYER_COUNTRYFROM;
					} else if (localName.equals("age")) { 
						currentTag = TAG_PLAYER_AGE;
					} else if (localName.equals("value")) { 
						currentTag = TAG_PLAYER_VALUE;
					} else if (localName.equals("salary")) { 
						currentTag = TAG_PLAYER_SALARY;
					} else if (localName.equals("cards")) { 
						currentTag = TAG_PLAYER_CARDS;
					} else if (localName.equals("injurydays")) { 
						currentTag = TAG_PLAYER_INJURYDAYS;
					} else if (localName.equals("matches")) { 
						currentTag = TAG_PLAYER_MATCHES;
					} else if (localName.equals("goals")) { 
						currentTag = TAG_PLAYER_GOALS;
					} else if (localName.equals("assists")) { 
						currentTag = TAG_PLAYER_ASSISTS;
					} else if (localName.equals("form")) { 
						currentTag = TAG_PLAYER_FORM;
					} else if (localName.equals("stamina")) { 
						currentTag = TAG_PLAYER_STAMINA;
					} else if (localName.equals("pace")) { 
						currentTag = TAG_PLAYER_PACE;
					} else if (localName.equals("technique")) { 
						currentTag = TAG_PLAYER_TECHNIQUE;
					} else if (localName.equals("passing")) { 
						currentTag = TAG_PLAYER_PASSING;
					} else if (localName.equals("keeper")) { 
						currentTag = TAG_PLAYER_KEEPER;
					} else if (localName.equals("defender")) { 
						currentTag = TAG_PLAYER_DEFENDER;
					} else if (localName.equals("playmaker")) { 
						currentTag = TAG_PLAYER_PLAYMAKER;
					} else if (localName.equals("scorer")) { 
						currentTag = TAG_PLAYER_SCORER;
					}
				} else if (tagSwitch == 2) {
					if (localName.equals("name")) { 
						currentTag = TAG_JUNIOR_NAME;
					} else if (localName.equals("surname")) { 
						currentTag = TAG_JUNIOR_SURNAME;
					} else if (localName.equals("weeks")) { 
						currentTag = TAG_JUNIOR_WEEKS;
					} else if (localName.equals("skill")) { 
						currentTag = TAG_JUNIOR_SKILL;
					}
				} else if (tagSwitch == 3) {
					if (localName.equals("name")) { 
						currentTag = TAG_CLUB_NAME;
					} else if (localName.equals("country")) { 
						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("id")) { 
								club.setCountry(Integer.valueOf(value).intValue());
							}
						}
					} else if (localName.equals("region")) { 
						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("id")) { 
								club.setRegionId(Integer.valueOf(value).intValue());
							}
						}
					} else if (localName.equals("money")) { 
						currentTag = TAG_CLUB_MONEY;
					} else if (localName.equals("fanclubcount")) { 
						currentTag = TAG_CLUB_FANCLUBCOUNT;
					} else if (localName.equals("fanclubmood")) { 
						currentTag = TAG_CLUB_FANCLUBMOOD;
					}
				} else if (tagSwitch == 4) {
					if (localName.equals("name")) { 
						currentTag = TAG_ARENA_NAME;
					} else if (localName.equals("stand")) { 
						stand = new Stand();
						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("location")) { 
								if(value.equals("N")) { 
									stand.setLocation(1);
								} else if(value.equals("S")){ 
									stand.setLocation(2);
								} else if(value.equals("W")){ 
									stand.setLocation(3);
								} else if(value.equals("E")){ 
									stand.setLocation(4);
								} else if(value.equals("NW")){ 
									stand.setLocation(5);
								} else if(value.equals("NE")){ 
									stand.setLocation(6);
								} else if(value.equals("SW")){ 
									stand.setLocation(7);
								} else if(value.equals("SE")){ 
									stand.setLocation(8);
								}
							}
						}
					} else if (localName.equals("capacity")) { 
						currentTag = TAG_ARENA_CAPACITY;
					} else if (localName.equals("type")) { 
						currentTag = TAG_ARENA_TYPE;
					} else if (localName.equals("days")) { 
						currentTag = TAG_ARENA_DAYS;
					} else if (localName.equals("roof")) { 
						currentTag = TAG_ARENA_ROOF;
					}
				} else if (tagSwitch == 5) {
					if (localName.equals("name")) { 
						currentTag = TAG_COACH_NAME;
					} else if (localName.equalsIgnoreCase("surname")) { 
						currentTag = TAG_COACH_SURNAME;
					} else if (localName.equalsIgnoreCase("job")) { 
						currentTag = TAG_COACH_JOB;
					} else if (localName.equals("countryfrom")) { 
						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("id")) { 
								coach.setCountryfrom(Integer.valueOf(value).intValue());
							}
						}
					} else if (localName.equals("age")) { 
						currentTag = TAG_COACH_AGE;
					} else if (localName.equals("salary")) { 
						currentTag = TAG_COACH_SALARY;
					} else if (localName.equals("generalskill")) { 
						currentTag = TAG_COACH_GENERALSKILL;
					} else if (localName.equalsIgnoreCase("stamina")) { 
						currentTag = TAG_COACH_STAMINA;
					} else if (localName.equalsIgnoreCase("pace")) { 
						currentTag = TAG_COACH_PACE;
					} else if (localName.equalsIgnoreCase("technique")) { 
						currentTag = TAG_COACH_TECHNIQUE;
					} else if (localName.equalsIgnoreCase("passing")) { 
						currentTag = TAG_COACH_PASSING;
					} else if (localName.equalsIgnoreCase("keepers")) { 
						currentTag = TAG_COACH_KEEPERS;
					} else if (localName.equalsIgnoreCase("defenters")) { 
						currentTag = TAG_COACH_DEFENDERS;
					} else if (localName.equalsIgnoreCase("playmakers")) { 
						currentTag = TAG_COACH_PLAYMAKERS;
					} else if (localName.equalsIgnoreCase("scorers")) { 
						currentTag = TAG_COACH_SCORERS;
					}
				} else if (tagSwitch == 6) {
					currentTag = TAG_SOKKERDATA;
				} else if (tagSwitch == 7) {
					currentTag = TAG_PLAYERS;
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new MyErrorHandler());
			parser.parse(input);
		} catch (SAXException e) {
			if (file != null) {
				new File(file).delete();
			}
			throw e;
		}
	}

	public Club getClub() {
		return club;
	}
}

class MyErrorHandler implements ErrorHandler {
	public void warning(SAXParseException e) throws SAXException {
		// throw new SAXException();
	}

	public void error(SAXParseException e) throws SAXException {
		throw new SAXException();
	}

	public void fatalError(SAXParseException e) throws SAXException {
		throw new SAXException();
	}
}
