package pl.pronux.sokker.downloader.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Rank;
import pl.pronux.sokker.model.Stand;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.model.User;
import pl.pronux.sokker.utils.Log;

public class TeamXmlParser {

	private static int currentTag = 0;

//	private static final int TAG_TEAM_DATA = 0;

	private static final int TAG_TEAM = 1;

	private static final int TAG_USER = 30;

	private static final int TAG_USER_ID = 31;

	private static final int TAG_LOGIN = 32;

	private static final int TAG_ARENA = 20;

//	private static final int TAG_ARENA_STAND = 21;

	private static final int TAG_STAND_SIZE = 22;

	private static final int TAG_STAND_TYPE = 23;

	private static final int TAG_STAND_DAYS = 24;

	private static final int TAG_STAND_IS_ROOF = 25;

	private static final int TAG_STAND_LOCATION = 27;

	private static final int TAG_ARENA_NAME = 28;

	private static final int TAG_TRAINING_TYPE = 2;

	private static final int TAG_TRAINING_FORMATION = 3;

	private static final int TAG_TEAM_ID = 4;

	private static final int TAG_NAME = 5;

	private static final int TAG_COUNTRY_ID = 6;

	private static final int TAG_REGION_ID = 7;

	private static final int TAG_MONEY = 8;

	private static final int TAG_FANCLUB_COUNT = 9;

	private static final int TAG_FANCLUB_MOOD = 10;

	private static final int TAG_JUNIORS_MAX = 11;

	private static final int TAG_DATE_CREATED = 12;

	private static final int TAG_RANK = 13;

	private static int tagSwitch = 0;

	private List<Stand> stands;

	private Arena arena;

	private Training training;

	private Stand stand;

	private User user;

	private List<ClubArenaName> clubArenaNames;

	private List<ClubBudget> clubBudgets;

	private List<ClubSupporters> clubSupporters;

	private Club club;

	private ClubSupporters clubFanclub;

	private ClubBudget clubMoney;

	private ClubArenaName clubArenaName;

	private ClubName clubName;

	private List<ClubName> clubNames;

	private List<Rank> ranks;

	private Rank rank;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			private StringBuilder message;

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_STAND_SIZE:
					stand.setCapacity(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_STAND_TYPE:
					stand.setType(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_STAND_DAYS:
					stand.setConstructionDays(Double.valueOf(message.toString()).doubleValue());
					break;
				case TAG_STAND_IS_ROOF:
					stand.setIsRoof(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_STAND_LOCATION:
					stand.setLocation(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_LOGIN:
					user.setLogin(message.toString());
					break;
				case TAG_USER_ID:
					user.setUserID(Integer.valueOf(message.toString()));
					break;
				case TAG_TRAINING_FORMATION:
					training.setFormation(Integer.valueOf(message.toString()));
					break;
				case TAG_TRAINING_TYPE:
					training.setType(Integer.valueOf(message.toString()));
					break;
				case TAG_ARENA_NAME:
					clubArenaName.setArenaName(message.toString());
					break;
				case TAG_TEAM_ID:
					club.setId(Integer.valueOf(message.toString()));
					break;
				case TAG_NAME:
					clubName.setName(message.toString());
					break;
				case TAG_COUNTRY_ID:
					club.setCountry(Integer.valueOf(message.toString()));
					break;
				case TAG_REGION_ID:
					club.setRegionId(Integer.valueOf(message.toString()));
					break;
				case TAG_MONEY:
					clubMoney.setMoney(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_FANCLUB_COUNT:
					clubFanclub.setFanclubcount(Integer.valueOf(message.toString()));
					break;
				case TAG_FANCLUB_MOOD:
					clubFanclub.setFanclubmood(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_JUNIORS_MAX:
					club.setJuniorsMax(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_DATE_CREATED:
					club.setDateCreated(new Date(message.toString()));
					break;
				case TAG_RANK:
					rank.setRank(Double.valueOf(message.toString()));
					break;
				default:
					break;
				}
			}

			// obsluga bledow

			public void endDocument() {
				if (club.getId() == -1) {
					club = null;
				} else {
					club.setArena(arena);
					club.setClubSupporters(clubSupporters);
					club.setClubBudget(clubBudgets);
					club.setClubName(clubNames);
					club.setRank(ranks);
				}
			}

			public void endElement(String namespaceURL, String localName, String qName) {
				currentTag = 0;
				if (localName.equalsIgnoreCase("stand")) { 
					stands.add(stand);
				} else if (localName.equalsIgnoreCase("arena")) { 

					if (stands.size() < 8) {
						Map<Integer, Stand> standsMap = new HashMap<Integer, Stand>();
						for (Stand stand : stands) {
							standsMap.put(stand.getLocation(), stand);
						}

						for (int i = Stand.N; i <= Stand.SE; i++) {
							if (standsMap.get(i) == null) {
								stands.add(new Stand(i, 0, 100, 0, 0.0));
							}
						}
					}
					arena.setStands(stands);
				} else if (localName.equalsIgnoreCase("team")) { 
					clubSupporters.add(clubFanclub);
					clubBudgets.add(clubMoney);
					clubNames.add(clubName);
					ranks.add(rank);
					clubArenaNames.add(clubArenaName);
					arena.setArenaNames(clubArenaNames);
				}
			}

			public void startDocument() {
				stands = new ArrayList<Stand>();
				clubArenaNames = new ArrayList<ClubArenaName>();
				clubSupporters = new ArrayList<ClubSupporters>();
				clubBudgets = new ArrayList<ClubBudget>();
				clubNames = new ArrayList<ClubName>();
				ranks = new ArrayList<Rank>();

				arena = new Arena();
				training = new Training();
				training.setId(-1);
				club = new Club();
				club.setId(-1);
				user = new User();
				club.setUser(user);
				club.setTraining(training);

				clubFanclub = new ClubSupporters();
				clubMoney = new ClubBudget();
				clubArenaName = new ClubArenaName();
				clubName = new ClubName();
				rank = new Rank();
			}


			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();

				if (localName.equalsIgnoreCase("arena")) { 
					tagSwitch = TAG_ARENA;
				}

				if (localName.equalsIgnoreCase("user")) { 
					tagSwitch = TAG_USER;
				}

				if (localName.equalsIgnoreCase("team")) { 
					tagSwitch = TAG_TEAM;
				}

				if (tagSwitch == TAG_ARENA) {
					if (localName.equals("stand")) { 
						stand = new Stand();
					}
					if (localName.equals("size")) { 
						currentTag = TAG_STAND_SIZE;
					} else if (localName.equals("type")) { 
						currentTag = TAG_STAND_TYPE;
					} else if (localName.equals("constructionDays")) { 
						currentTag = TAG_STAND_DAYS;
					} else if (localName.equals("isRoof")) { 
						currentTag = TAG_STAND_IS_ROOF;
					} else if (localName.equals("location")) { 
						currentTag = TAG_STAND_LOCATION;
					}

				} else if (tagSwitch == TAG_USER) {
					if (localName.equals("login")) { 
						currentTag = TAG_LOGIN;
					} else if (localName.equals("userID")) { 
						currentTag = TAG_USER_ID;
					}
				} else if (tagSwitch == TAG_TEAM) {

					if (localName.equalsIgnoreCase("trainingFormation")) { 
						currentTag = TAG_TRAINING_FORMATION;
					} else if (localName.equalsIgnoreCase("trainingType")) { 
						currentTag = TAG_TRAINING_TYPE;
					} else if (localName.equals("arenaName")) { 
						currentTag = TAG_ARENA_NAME;
					} else if (localName.equals("teamID")) { 
						currentTag = TAG_TEAM_ID;
					} else if (localName.equals("name")) { 
						currentTag = TAG_NAME;
					} else if (localName.equals("countryID")) { 
						currentTag = TAG_COUNTRY_ID;
					} else if (localName.equals("regionID")) { 
						currentTag = TAG_REGION_ID;
					} else if (localName.equals("money")) { 
						currentTag = TAG_MONEY;
					} else if (localName.equals("fanclubCount")) { 
						currentTag = TAG_FANCLUB_COUNT;
					} else if (localName.equals("fanclubMood")) { 
						currentTag = TAG_FANCLUB_MOOD;
					} else if (localName.equals("juniorsMax")) { 
						currentTag = TAG_JUNIORS_MAX;
					} else if (localName.equals("dateCreated")) { 
						currentTag = TAG_DATE_CREATED;
					} else if (localName.equals("rank")) { 
						currentTag = TAG_RANK;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new TeamErrorHandler());

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
	 * @see pl.pronux.sokker.downloader.xml.parsers.TeamXmlParserInterface#getClub()
	 */
	public Club getClub() {
		return club;
	}

}


class TeamErrorHandler implements ErrorHandler {

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
