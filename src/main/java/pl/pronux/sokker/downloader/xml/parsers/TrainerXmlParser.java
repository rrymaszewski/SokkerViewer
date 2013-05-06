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

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.utils.Log;

public class TrainerXmlParser {

	private static int currentTag = 0;

//	private static final int TAG_trainers = 1;

	private static final int TAG_TRAINER = 2;

	private static final int TAG_TRAINER_AGE = 3;

	private static final int TAG_TRAINER_COUNTRY_ID = 4;

	private static final int TAG_TRAINER_DEFENDERS = 5;

	private static final int TAG_TRAINER_SKILL_COACH = 6;

	private static final int TAG_TRAINER_ID = 7;

	private static final int TAG_TRAINER_JOB = 8;

	private static final int TAG_TRAINER_KEEPERS = 9;

	private static final int TAG_TRAINER_NAME = 10;

	private static final int TAG_TRAINER_PACE = 11;

	private static final int TAG_TRAINER_PASSING = 12;

	private static final int TAG_TRAINER_PLAYMAKERS = 13;

	private static final int TAG_TRAINER_WAGE = 14;

	private static final int TAG_TRAINER_SCORERS = 15;

	private static final int TAG_TRAINER_SIGNED = 16;

	private static final int TAG_TRAINER_STAMINA = 17;

	private static final int TAG_TRAINER_SURNAME = 18;

	private static final int TAG_TRAINER_TECHNIQUE = 19;

	private static int tagSwitch = 0;

	private List<Coach> coaches;

	private int teamId;

	private Coach trainer;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			private StringBuilder message;

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_TRAINER_AGE:
					trainer.setAge(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_NAME:
					trainer.setName(message.toString());
					/*
					 * Remove white spaces like \t
					 */
					trainer.setName(trainer.getName().replaceAll("\t", ""));  
					break;
				case TAG_TRAINER_SURNAME:
					trainer.setSurname(message.toString());
					/*
					 * Remove white spaces like \t
					 */
					trainer.setSurname(trainer.getSurname().replaceAll("\t", ""));  
					break;

				case TAG_TRAINER_JOB:
					trainer.setJob(Integer.valueOf(message.toString()));
					break;

				case TAG_TRAINER_ID:
					trainer.setId(Integer.valueOf(message.toString()));
					break;
				case TAG_TRAINER_COUNTRY_ID:
					trainer.setCountryfrom(Integer.valueOf(message.toString()));
					break;
				case TAG_TRAINER_WAGE:
					trainer.setSalary(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_TRAINER_SKILL_COACH:
					trainer.setGeneralskill(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_STAMINA:
					trainer.setStamina(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_PACE:
					trainer.setPace(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_TECHNIQUE:
					trainer.setTechnique(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_PASSING:
					trainer.setPassing(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_KEEPERS:
					trainer.setKeepers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_DEFENDERS:
					trainer.setDefenders(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_PLAYMAKERS:
					trainer.setPlaymakers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_SCORERS:
					trainer.setScorers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_TRAINER_SIGNED:
					trainer.setSigned(Integer.valueOf(message.toString()).byteValue());
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
				if (localName.equals("trainer")) { 
					if(trainer.getId() != -1) {
						coaches.add(trainer);
					}
				}
			}

			public void startDocument() {
				coaches = new ArrayList<Coach>();
			}


			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("trainers")) { 
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { 
							setTeamId(Integer.valueOf(value));
						}
					}
				}

				if (localName.equals("trainer")) { 
					tagSwitch = TAG_TRAINER;
					trainer = new Coach();
					trainer.setId(-1);
				}

				if (tagSwitch == TAG_TRAINER) {
					if (localName.equals("name")) { 
						currentTag = TAG_TRAINER_NAME;
					} else if (localName.equalsIgnoreCase("ID")) { 
						currentTag = TAG_TRAINER_ID;
					} else if (localName.equalsIgnoreCase("surname")) { 
						currentTag = TAG_TRAINER_SURNAME;
					} else if (localName.equalsIgnoreCase("job")) { 
						currentTag = TAG_TRAINER_JOB;
					} else if (localName.equals("countryID")) { 
						currentTag = TAG_TRAINER_COUNTRY_ID;
					} else if (localName.equals("age")) { 
						currentTag = TAG_TRAINER_AGE;
					} else if (localName.equals("wage")) { 
						currentTag = TAG_TRAINER_WAGE;
					} else if (localName.equals("skillCoach")) { 
						currentTag = TAG_TRAINER_SKILL_COACH;
					} else if (localName.equalsIgnoreCase("skillStamina")) { 
						currentTag = TAG_TRAINER_STAMINA;
					} else if (localName.equalsIgnoreCase("skillPace")) { 
						currentTag = TAG_TRAINER_PACE;
					} else if (localName.equalsIgnoreCase("skillTechnique")) { 
						currentTag = TAG_TRAINER_TECHNIQUE;
					} else if (localName.equalsIgnoreCase("skillPassing")) { 
						currentTag = TAG_TRAINER_PASSING;
					} else if (localName.equalsIgnoreCase("skillKeeper")) { 
						currentTag = TAG_TRAINER_KEEPERS;
					} else if (localName.equalsIgnoreCase("skillDefending")) { 
						currentTag = TAG_TRAINER_DEFENDERS;
					} else if (localName.equalsIgnoreCase("skillPlaymaking")) { 
						currentTag = TAG_TRAINER_PLAYMAKERS;
					} else if (localName.equalsIgnoreCase("skillScoring")) { 
						currentTag = TAG_TRAINER_SCORERS;
					} else if (localName.equalsIgnoreCase("signedContract")) { 
							currentTag = TAG_TRAINER_SIGNED;
						}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new TrainerErrorHandler());

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
	 * @see pl.pronux.sokker.downloader.xml.parsers.TrainerXmlParserInterface#getAlCoach()
	 */
	public List<Coach> getCoaches() {
		return coaches;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
}

class TrainerErrorHandler implements ErrorHandler {
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
