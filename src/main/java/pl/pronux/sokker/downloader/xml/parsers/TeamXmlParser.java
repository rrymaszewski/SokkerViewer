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
import pl.pronux.sokker.utils.file.SVLogger;

public class TeamXmlParser {

	static int current_tag = 0;

	static final int TAG_teamdata = 0;

	static final int TAG_team = 1;

	static final int TAG_user = 30;

	static final int TAG_userID = 31;

	static final int TAG_login = 32;

	static final int TAG_arena = 20;

	static final int TAG_arena_stand = 21;

	static final int TAG_stand_size = 22;

	static final int TAG_stand_type = 23;

	static final int TAG_stand_days = 24;

	static final int TAG_stand_is_roof = 25;

	static final int TAG_stand_location = 27;

	static final int TAG_arena_name = 28;

	static final int TAG_training_type = 2;

	static final int TAG_training_formation = 3;

	static final int TAG_teamID = 4;

	static final int TAG_name = 5;

	static final int TAG_countryID = 6;

	static final int TAG_regionID = 7;

	static final int TAG_money = 8;

	static final int TAG_fanclubCount = 9;

	static final int TAG_fanclubMood = 10;

	static final int TAG_juniorsMax = 11;

	static final int TAG_date_created = 12;

	static final int TAG_rank = 13;

	static int TAG_switch = 0;

	private ArrayList<Stand> alStand;

	private Arena arena;

	private Training training;

	private Stand stand;

	private User user;

	private ArrayList<ClubArenaName> alArenaName;

	private ArrayList<ClubBudget> alClubMoney;

	private ArrayList<ClubSupporters> alClubFanclub;

	private Club club;

	private ClubSupporters clubFanclub;

	private ClubBudget clubMoney;

	private ClubArenaName clubArenaName;

	private ClubName clubName;

	private ArrayList<ClubName> alClubName;

	private ArrayList<Rank> alRank;

	private Rank rank;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			public void characters(char ch[], int start, int length) throws SAXException {
				// System.out.print("Ciag znakow: ");
				// wypisujemy lancuch, zmieniajac znaki tabulacji i konca
				// linii na ich specjalne reprezentacje

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_stand_size:
					stand.setSize(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_stand_type:
					stand.setType(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_stand_days:
					stand.setConstructionDays(Double.valueOf(message.toString()).doubleValue());
					break;
				case TAG_stand_is_roof:
					stand.setIsRoof(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_stand_location:
					stand.setLocation(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_login:
					user.setLogin(message.toString());
					break;
				case TAG_userID:
					user.setUserID(Integer.valueOf(message.toString()));
					break;
				case TAG_training_formation:
					training.setFormation(Integer.valueOf(message.toString()));
					break;
				case TAG_training_type:
					training.setType(Integer.valueOf(message.toString()));
					break;
				case TAG_arena_name:
					clubArenaName.setArenaName(message.toString());
					break;
				case TAG_teamID:
					club.setId(Integer.valueOf(message.toString()));
					break;
				case TAG_name:
					clubName.setName(message.toString());
					break;
				case TAG_countryID:
					club.setCountry(Integer.valueOf(message.toString()));
					break;
				case TAG_regionID:
					club.setRegionID(Integer.valueOf(message.toString()));
					break;
				case TAG_money:
					clubMoney.setMoney(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_fanclubCount:
					clubFanclub.setFanclubcount(Integer.valueOf(message.toString()));
					break;
				case TAG_fanclubMood:
					clubFanclub.setFanclubmood(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_juniorsMax:
					club.setJuniorsMax(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_date_created:
					club.setDateCreated(new Date(message.toString()));
					break;
				case TAG_rank:
					rank.setRank(Double.valueOf(message.toString()));
					break;
				default:
					break;
				}
			}

			// obsluga bledow

			public void endDocument() {
				if(club.getId() == -1) {
					club = null;
				} else {
					club.setArena(arena);
					club.setClubSupporters(alClubFanclub);
					club.setClubBudget(alClubMoney);
					club.setClubName(alClubName);
					club.setRank(alRank);
				}
			}

			public void endElement(String namespaceURL, String localName, String qName) {
				current_tag = 0;
				if (localName.equalsIgnoreCase("stand")) { //$NON-NLS-1$
					alStand.add(stand);
				} else if (localName.equalsIgnoreCase("arena")) { //$NON-NLS-1$
					arena.setStands(alStand);
				} else if (localName.equalsIgnoreCase("team")) { //$NON-NLS-1$
					alClubFanclub.add(clubFanclub);
					alClubMoney.add(clubMoney);
					alClubName.add(clubName);
					alRank.add(rank);
					alArenaName.add(clubArenaName);
					arena.setAlArenaName(alArenaName);
				}
			}

			public void startDocument() {
				alStand = new ArrayList<Stand>();
				alArenaName = new ArrayList<ClubArenaName>();
				alClubFanclub = new ArrayList<ClubSupporters>();
				alClubMoney = new ArrayList<ClubBudget>();
				alClubName = new ArrayList<ClubName>();
				alRank = new ArrayList<Rank>();

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

			StringBuffer message;

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();

				if (localName.equalsIgnoreCase("arena")) { //$NON-NLS-1$
					TAG_switch = TAG_arena;
				}

				if (localName.equalsIgnoreCase("user")) { //$NON-NLS-1$
					TAG_switch = TAG_user;
				}

				if (localName.equalsIgnoreCase("team")) { //$NON-NLS-1$
					TAG_switch = TAG_team;
				}

				if (TAG_switch == TAG_arena) {
					if (localName.equals("stand")) { //$NON-NLS-1$
						stand = new Stand();
					}
					if (localName.equals("size")) { //$NON-NLS-1$
						current_tag = TAG_stand_size;
					} else if (localName.equals("type")) { //$NON-NLS-1$
						current_tag = TAG_stand_type;
					} else if (localName.equals("constructionDays")) { //$NON-NLS-1$
						current_tag = TAG_stand_days;
					} else if (localName.equals("isRoof")) { //$NON-NLS-1$
						current_tag = TAG_stand_is_roof;
					} else if (localName.equals("location")) { //$NON-NLS-1$
						current_tag = TAG_stand_location;
					}

				} else if (TAG_switch == TAG_user) {
					if (localName.equals("login")) { //$NON-NLS-1$
						current_tag = TAG_login;
					} else if (localName.equals("userID")) { //$NON-NLS-1$
						current_tag = TAG_userID;
					}
				} else if (TAG_switch == TAG_team) {

					if (localName.equalsIgnoreCase("trainingFormation")) { //$NON-NLS-1$
						current_tag = TAG_training_formation;
					} else if (localName.equalsIgnoreCase("trainingType")) { //$NON-NLS-1$
						current_tag = TAG_training_type;
					} else if (localName.equals("arenaName")) { //$NON-NLS-1$
						current_tag = TAG_arena_name;
					} else if (localName.equals("teamID")) { //$NON-NLS-1$
						current_tag = TAG_teamID;
					} else if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_name;
					} else if (localName.equals("countryID")) { //$NON-NLS-1$
						current_tag = TAG_countryID;
					} else if (localName.equals("regionID")) { //$NON-NLS-1$
						current_tag = TAG_regionID;
					} else if (localName.equals("money")) { //$NON-NLS-1$
						current_tag = TAG_money;
					} else if (localName.equals("fanclubCount")) { //$NON-NLS-1$
						current_tag = TAG_fanclubCount;
					} else if (localName.equals("fanclubMood")) { //$NON-NLS-1$
						current_tag = TAG_fanclubMood;
					} else if (localName.equals("juniorsMax")) { //$NON-NLS-1$
						current_tag = TAG_juniorsMax;
					} else if (localName.equals("dateCreated")) { //$NON-NLS-1$
						current_tag = TAG_date_created;
					} else if (localName.equals("rank")) { //$NON-NLS-1$
						current_tag = TAG_rank;
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
			new SVLogger(Level.WARNING, "Parser Class", e); //$NON-NLS-1$
		} catch (SAXException e) {
			if (file != null) {
				new File(file).delete();
			}
			throw e;
		}
	}

	/* (non-Javadoc)
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
