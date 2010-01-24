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

	static int current_tag = 0;

	static final int TAG_arena_capacity = 26;

	static final int TAG_arena_days = 28;

	static final int TAG_arena_location = 29;

	static final int TAG_arena_name = 31;

	static final int TAG_arena_roof = 30;

	static final int TAG_arena_type = 27;

	static final int TAG_club_arenaName = 39;

	static final int TAG_club_country = 34;

	static final int TAG_club_fanclubcount = 37;

	static final int TAG_club_fanclubmood = 38;

	static final int TAG_club_id = 32;

	static final int TAG_club_money = 36;

	static final int TAG_club_name = 33;

	static final int TAG_club_region = 35;

	static final int TAG_coach_age = 46;

	static final int TAG_coach_countryfrom = 45;

	static final int TAG_coach_defenders = 54;

	static final int TAG_coach_generalskill = 48;

	static final int TAG_coach_id = 40;

	static final int TAG_coach_job = 44;

	static final int TAG_coach_keepers = 53;

	static final int TAG_coach_name = 42;

	static final int TAG_coach_pace = 50;

	static final int TAG_coach_passing = 52;

	static final int TAG_coach_playmakers = 55;

	static final int TAG_coach_salary = 47;

	static final int TAG_coach_scorers = 56;

	static final int TAG_coach_signed = 41;

	static final int TAG_coach_stamina = 49;

	static final int TAG_coach_surname = 43;

	static final int TAG_coach_technique = 51;

	static final int TAG_junior_id = 21;

	static final int TAG_junior_name = 22;

	static final int TAG_junior_skill = 25;

	static final int TAG_junior_surname = 23;

	static final int TAG_junior_weeks = 24;

	static final int TAG_player_age = 4;

	static final int TAG_player_assists = 11;

	static final int TAG_player_cards = 7;

	static final int TAG_player_countryfrom = 3;

	static final int TAG_player_defender = 18;

	static final int TAG_player_form = 12;

	static final int TAG_player_goals = 10;

	static final int TAG_player_injurydays = 8;

	static final int TAG_player_keeper = 17;

	static final int TAG_player_matches = 9;

	static final int TAG_player_name = 1;

	static final int TAG_player_pace = 14;

	static final int TAG_player_passing = 16;

	static final int TAG_player_playmaker = 19;

	static final int TAG_player_salary = 6;

	static final int TAG_player_scorer = 20;

	static final int TAG_player_stamina = 13;

	static final int TAG_player_surname = 2;

	static final int TAG_player_technique = 15;

	static final int TAG_player_value = 5;

	static final int TAG_players = 99;

	static final int TAG_sokkerdata = 100;

	static int TAG_switch = 0;

	private ArrayList<Coach> coaches;

	private ArrayList<Junior> juniors;

	private ArrayList<Player> players;

	private Arena arena;

	public Club club;

	private Coach coach;

	private Junior junior;

	private JuniorSkills[] juniorSkills;

	private Player player;

	private PlayerSkills[] playerSkills;

	private ArrayList<ClubSupporters> clubDataSupporters;

	private ArrayList<ClubBudget> clubDataMoney;

	private ArrayList<ClubName> clubName;

	private ArrayList<ClubArenaName> alArenaName;

	private Stand stand;

	private ArrayList<Stand> alStand;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException, IOException {

		class SAXHandler extends DefaultHandler {

			public void characters(char ch[], int start, int length) throws SAXException {
				message.append(new String(ch, start, length));
				switch (current_tag) {
				case TAG_players:
					// TODO jezeli wchodzimy tutaj i sa jakies znaki to ladujemy exception
					break;
				case TAG_sokkerdata:
					if (message.toString().equalsIgnoreCase("empty")) { //$NON-NLS-1$
						throw new SAXException();
					}
					break;
				case TAG_player_name:
					player.setName(message.toString());
					break;
				case TAG_player_surname:
					player.setSurname(message.toString());
					break;
				case TAG_player_countryfrom:
					// player.setCountry(message.toString());
					break;
				case TAG_player_age:
					playerSkills[0].setAge(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_player_value:
					playerSkills[0].setValue(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_player_salary:
					playerSkills[0].setSalary(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_player_cards:
					playerSkills[0].setCards(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_player_injurydays:
					playerSkills[0].setInjurydays(Double.valueOf(message.toString()).doubleValue());
					break;
				case TAG_player_matches:
					playerSkills[0].setMatches(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_player_goals:
					playerSkills[0].setGoals(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_player_assists:
					playerSkills[0].setAssists(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_player_form:
					playerSkills[0].setForm(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_player_stamina:
					playerSkills[0].setStamina(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_player_pace:
					playerSkills[0].setPace(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_player_technique:
					playerSkills[0].setTechnique(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_player_passing:
					playerSkills[0].setPassing(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_player_keeper:
					playerSkills[0].setKeeper(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_player_defender:
					playerSkills[0].setDefender(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_player_playmaker:
					playerSkills[0].setPlaymaker(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_player_scorer:
					playerSkills[0].setScorer(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_junior_name:
					junior.setName(message.toString());
					break;
				case TAG_junior_surname:
					junior.setSurname(message.toString());
					break;
				case TAG_junior_weeks:
					juniorSkills[0].setWeeks(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_junior_skill:
					juniorSkills[0].setSkill(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_club_name:
					club.getClubName().get(0).setName(message.toString());
					break;
				case TAG_club_money:
					club.getClubBudget().get(0).setMoney(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_club_fanclubcount:
					club.getClubSupporters().get(0).setFanclubcount(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_club_fanclubmood:
					club.getClubSupporters().get(0).setFanclubmood(Byte.valueOf(message.toString()).byteValue());
					break;
				case TAG_arena_name:
					alArenaName.get(0).setArenaName(message.toString());
					break;
				case TAG_arena_capacity:
					stand.setCapacity(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_arena_type:
					stand.setType(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_arena_days:
					stand.setConstructionDays(Double.valueOf(message.toString()).doubleValue());
					break;
				case TAG_arena_roof:
					stand.setIsRoof(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_coach_age:
					coach.setAge(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_coach_name:
					coach.setName(message.toString());
					/*
					 * Remove white spaces like \t
					 */
					coach.setName(coach.getName().replaceAll("\t", "")); //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case TAG_coach_surname:
					coach.setSurname(message.toString());
					/*
					 * Remove white spaces like \t
					 */
					coach.setSurname(coach.getSurname().replaceAll("\t", "")); //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case TAG_coach_job:
					String job = message.toString();
					if(job.equalsIgnoreCase("head")) { //$NON-NLS-1$
						coach.setJob(Coach.JOB_HEAD);
					} else if(job.equalsIgnoreCase("juniors")) { //$NON-NLS-1$
						coach.setJob(Coach.JOB_JUNIORS);
					} else if(job.equalsIgnoreCase("none")) { //$NON-NLS-1$
						coach.setJob(Coach.JOB_NONE);
					} else if(job.equalsIgnoreCase("assistant")) { //$NON-NLS-1$
						coach.setJob(Coach.JOB_ASSISTANT);
					}
					break;
				case TAG_coach_salary:
					coach.setSalary(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_coach_generalskill:
					coach.setGeneralskill(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_coach_stamina:
					coach.setStamina(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_coach_pace:
					coach.setPace(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_coach_technique:
					coach.setTechnique(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_coach_passing:
					coach.setPassing(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_coach_keepers:
					coach.setKeepers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_coach_defenders:
					coach.setDefenders(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_coach_playmakers:
					coach.setPlaymakers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_coach_scorers:
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

				current_tag = 0;

				if (localName.equals("player")) { //$NON-NLS-1$
					player.setSkills(playerSkills);
					players.add(player);
				} else if (localName.equals("junior")) { //$NON-NLS-1$
					junior.setSkills(juniorSkills);
					juniors.add(junior);
				} else if (localName.equalsIgnoreCase("stand")) { //$NON-NLS-1$
					alStand.add(stand);
				} else if (localName.equalsIgnoreCase("coach")) { //$NON-NLS-1$
					coaches.add(coach);
				}else if (localName.equalsIgnoreCase("arena")) { //$NON-NLS-1$
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

			StringBuilder message;

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();

				if (localName.equalsIgnoreCase("sokkerdata")) { //$NON-NLS-1$
					TAG_switch = 6;
				}

				if (localName.equalsIgnoreCase("players")) { //$NON-NLS-1$
					TAG_switch = 7;
				}

				if (localName.equals("player")) { //$NON-NLS-1$
					TAG_switch = 1;

					player = new Player();
					playerSkills = new PlayerSkills[1];
					playerSkills[0] = new PlayerSkills();

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("ID")) { //$NON-NLS-1$
							player.setId(Integer.valueOf(value).intValue());
						}
					}
				} else if (localName.equals("junior")) { //$NON-NLS-1$

					TAG_switch = 2;

					junior = new Junior();
					juniorSkills = new JuniorSkills[1];
					juniorSkills[0] = new JuniorSkills();

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("ID")) { //$NON-NLS-1$
							junior.setId(Integer.valueOf(value).intValue());
						}
					}
				} else if (localName.equalsIgnoreCase("team")) { //$NON-NLS-1$

					TAG_switch = 3;
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
						if (name.equalsIgnoreCase("ID")) { //$NON-NLS-1$
							club.setId(Integer.valueOf(value).intValue());
						}
					}

				} else if (localName.equalsIgnoreCase("arena")) { //$NON-NLS-1$

					TAG_switch = 4;
					arena = new Arena();
				} else if (localName.equalsIgnoreCase("coach")) { //$NON-NLS-1$

					TAG_switch = 5;
					coach = new Coach();

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("ID")) { //$NON-NLS-1$
							coach.setId(Integer.valueOf(value).intValue());
						} else if (name.equalsIgnoreCase("signed")) { //$NON-NLS-1$
							coach.setSigned(Integer.valueOf(value).byteValue());
						}
					}
				}

				if (TAG_switch == 1) {
					if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_player_name;
					} else if (localName.equals("surname")) { //$NON-NLS-1$
						current_tag = TAG_player_surname;
					} else if (localName.equals("countryfrom")) { //$NON-NLS-1$

						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("ID")) { //$NON-NLS-1$
								player.setCountryfrom(Integer.valueOf(value).intValue());
							}
						}
						current_tag = TAG_player_countryfrom;
					} else if (localName.equals("age")) { //$NON-NLS-1$
						current_tag = TAG_player_age;
					} else if (localName.equals("value")) { //$NON-NLS-1$
						current_tag = TAG_player_value;
					} else if (localName.equals("salary")) { //$NON-NLS-1$
						current_tag = TAG_player_salary;
					} else if (localName.equals("cards")) { //$NON-NLS-1$
						current_tag = TAG_player_cards;
					} else if (localName.equals("injurydays")) { //$NON-NLS-1$
						current_tag = TAG_player_injurydays;
					} else if (localName.equals("matches")) { //$NON-NLS-1$
						current_tag = TAG_player_matches;
					} else if (localName.equals("goals")) { //$NON-NLS-1$
						current_tag = TAG_player_goals;
					} else if (localName.equals("assists")) { //$NON-NLS-1$
						current_tag = TAG_player_assists;
					} else if (localName.equals("form")) { //$NON-NLS-1$
						current_tag = TAG_player_form;
					} else if (localName.equals("stamina")) { //$NON-NLS-1$
						current_tag = TAG_player_stamina;
					} else if (localName.equals("pace")) { //$NON-NLS-1$
						current_tag = TAG_player_pace;
					} else if (localName.equals("technique")) { //$NON-NLS-1$
						current_tag = TAG_player_technique;
					} else if (localName.equals("passing")) { //$NON-NLS-1$
						current_tag = TAG_player_passing;
					} else if (localName.equals("keeper")) { //$NON-NLS-1$
						current_tag = TAG_player_keeper;
					} else if (localName.equals("defender")) { //$NON-NLS-1$
						current_tag = TAG_player_defender;
					} else if (localName.equals("playmaker")) { //$NON-NLS-1$
						current_tag = TAG_player_playmaker;
					} else if (localName.equals("scorer")) { //$NON-NLS-1$
						current_tag = TAG_player_scorer;
					}
				} else if (TAG_switch == 2) {
					if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_junior_name;
					} else if (localName.equals("surname")) { //$NON-NLS-1$
						current_tag = TAG_junior_surname;
					} else if (localName.equals("weeks")) { //$NON-NLS-1$
						current_tag = TAG_junior_weeks;
					} else if (localName.equals("skill")) { //$NON-NLS-1$
						current_tag = TAG_junior_skill;
					}
				} else if (TAG_switch == 3) {
					if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_club_name;
					} else if (localName.equals("country")) { //$NON-NLS-1$
						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("id")) { //$NON-NLS-1$
								club.setCountry(Integer.valueOf(value).intValue());
							}
						}
					} else if (localName.equals("region")) { //$NON-NLS-1$
						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("id")) { //$NON-NLS-1$
								club.setRegionID(Integer.valueOf(value).intValue());
							}
						}
					} else if (localName.equals("money")) { //$NON-NLS-1$
						current_tag = TAG_club_money;
					} else if (localName.equals("fanclubcount")) { //$NON-NLS-1$
						current_tag = TAG_club_fanclubcount;
					} else if (localName.equals("fanclubmood")) { //$NON-NLS-1$
						current_tag = TAG_club_fanclubmood;
					}
				} else if (TAG_switch == 4) {
					if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_arena_name;
					} else if (localName.equals("stand")) { //$NON-NLS-1$
						stand = new Stand();
						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("location")) { //$NON-NLS-1$
								if(value.equals("N")) { //$NON-NLS-1$
									stand.setLocation(1);
								} else if(value.equals("S")){ //$NON-NLS-1$
									stand.setLocation(2);
								} else if(value.equals("W")){ //$NON-NLS-1$
									stand.setLocation(3);
								} else if(value.equals("E")){ //$NON-NLS-1$
									stand.setLocation(4);
								} else if(value.equals("NW")){ //$NON-NLS-1$
									stand.setLocation(5);
								} else if(value.equals("NE")){ //$NON-NLS-1$
									stand.setLocation(6);
								} else if(value.equals("SW")){ //$NON-NLS-1$
									stand.setLocation(7);
								} else if(value.equals("SE")){ //$NON-NLS-1$
									stand.setLocation(8);
								}
							}
						}
					} else if (localName.equals("capacity")) { //$NON-NLS-1$
						current_tag = TAG_arena_capacity;
					} else if (localName.equals("type")) { //$NON-NLS-1$
						current_tag = TAG_arena_type;
					} else if (localName.equals("days")) { //$NON-NLS-1$
						current_tag = TAG_arena_days;
					} else if (localName.equals("roof")) { //$NON-NLS-1$
						current_tag = TAG_arena_roof;
					}
				} else if (TAG_switch == 5) {
					if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_coach_name;
					} else if (localName.equalsIgnoreCase("surname")) { //$NON-NLS-1$
						current_tag = TAG_coach_surname;
					} else if (localName.equalsIgnoreCase("job")) { //$NON-NLS-1$
						current_tag = TAG_coach_job;
					} else if (localName.equals("countryfrom")) { //$NON-NLS-1$
						int length = atts.getLength();
						for (int i = 0; i < length; i++) {
							String name = atts.getQName(i);
							String value = atts.getValue(i);
							if (name.equalsIgnoreCase("id")) { //$NON-NLS-1$
								coach.setCountryfrom(Integer.valueOf(value).intValue());
							}
						}
					} else if (localName.equals("age")) { //$NON-NLS-1$
						current_tag = TAG_coach_age;
					} else if (localName.equals("salary")) { //$NON-NLS-1$
						current_tag = TAG_coach_salary;
					} else if (localName.equals("generalskill")) { //$NON-NLS-1$
						current_tag = TAG_coach_generalskill;
					} else if (localName.equalsIgnoreCase("stamina")) { //$NON-NLS-1$
						current_tag = TAG_coach_stamina;
					} else if (localName.equalsIgnoreCase("pace")) { //$NON-NLS-1$
						current_tag = TAG_coach_pace;
					} else if (localName.equalsIgnoreCase("technique")) { //$NON-NLS-1$
						current_tag = TAG_coach_technique;
					} else if (localName.equalsIgnoreCase("passing")) { //$NON-NLS-1$
						current_tag = TAG_coach_passing;
					} else if (localName.equalsIgnoreCase("keepers")) { //$NON-NLS-1$
						current_tag = TAG_coach_keepers;
					} else if (localName.equalsIgnoreCase("defenters")) { //$NON-NLS-1$
						current_tag = TAG_coach_defenders;
					} else if (localName.equalsIgnoreCase("playmakers")) { //$NON-NLS-1$
						current_tag = TAG_coach_playmakers;
					} else if (localName.equalsIgnoreCase("scorers")) { //$NON-NLS-1$
						current_tag = TAG_coach_scorers;
					}
				} else if (TAG_switch == 6) {
					current_tag = TAG_sokkerdata;
				} else if (TAG_switch == 7) {
					current_tag = TAG_players;
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
		} catch (IOException e) {
			throw e;
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
