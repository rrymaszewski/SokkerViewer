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

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.utils.file.SVLogger;

public class TrainerXmlParser {

	static int current_tag = 0;

	static final int TAG_trainers = 1;

	static final int TAG_trainer = 2;

	static final int TAG_trainer_age = 3;

	static final int TAG_trainer_countryID = 4;

	static final int TAG_trainer_defenders = 5;

	static final int TAG_trainer_skillCoach = 6;

	static final int TAG_trainer_id = 7;

	static final int TAG_trainer_job = 8;

	static final int TAG_trainer_keepers = 9;

	static final int TAG_trainer_name = 10;

	static final int TAG_trainer_pace = 11;

	static final int TAG_trainer_passing = 12;

	static final int TAG_trainer_playmakers = 13;

	static final int TAG_trainer_wage = 14;

	static final int TAG_trainer_scorers = 15;

	static final int TAG_trainer_signed = 16;

	static final int TAG_trainer_stamina = 17;

	static final int TAG_trainer_surname = 18;

	static final int TAG_trainer_technique = 19;

	static int TAG_switch = 0;

	private ArrayList<Coach> alCoach;

	public int teamID;

	private Coach trainer;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_trainer_age:
					trainer.setAge(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_name:
					trainer.setName(message.toString());
					/*
					 * Remove white spaces like \t
					 */
					trainer.setName(trainer.getName().replaceAll("\t", "")); //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case TAG_trainer_surname:
					trainer.setSurname(message.toString());
					/*
					 * Remove white spaces like \t
					 */
					trainer.setSurname(trainer.getSurname().replaceAll("\t", "")); //$NON-NLS-1$ //$NON-NLS-2$
					break;

				case TAG_trainer_job:
					trainer.setJob(Integer.valueOf(message.toString()));
					break;

				case TAG_trainer_id:
					trainer.setId(Integer.valueOf(message.toString()));
					break;
				case TAG_trainer_countryID:
					trainer.setCountryfrom(Integer.valueOf(message.toString()));
					break;
				case TAG_trainer_wage:
					trainer.setSalary(new Money(Integer.valueOf(message.toString()).intValue()));
					break;
				case TAG_trainer_skillCoach:
					trainer.setGeneralskill(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_stamina:
					trainer.setStamina(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_pace:
					trainer.setPace(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_technique:
					trainer.setTechnique(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_passing:
					trainer.setPassing(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_keepers:
					trainer.setKeepers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_defenders:
					trainer.setDefenders(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_playmakers:
					trainer.setPlaymakers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_scorers:
					trainer.setScorers(Integer.valueOf(message.toString()).byteValue());
					break;
				case TAG_trainer_signed:
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
				current_tag = 0;
				if (localName.equals("trainer")) { //$NON-NLS-1$
					if(trainer.getId() != -1) {
						alCoach.add(trainer);
					}
				}
			}

			public void startDocument() {
				alCoach = new ArrayList<Coach>();
			}

			StringBuffer message;

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();
				if (localName.equals("trainers")) { //$NON-NLS-1$
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { //$NON-NLS-1$
							teamID = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("trainer")) { //$NON-NLS-1$
					TAG_switch = TAG_trainer;
					trainer = new Coach();
					trainer.setId(-1);
				}

				if (TAG_switch == TAG_trainer) {
					if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_trainer_name;
					} else if (localName.equalsIgnoreCase("ID")) { //$NON-NLS-1$
						current_tag = TAG_trainer_id;
					} else if (localName.equalsIgnoreCase("surname")) { //$NON-NLS-1$
						current_tag = TAG_trainer_surname;
					} else if (localName.equalsIgnoreCase("job")) { //$NON-NLS-1$
						current_tag = TAG_trainer_job;
					} else if (localName.equals("countryID")) { //$NON-NLS-1$
						current_tag = TAG_trainer_countryID;
					} else if (localName.equals("age")) { //$NON-NLS-1$
						current_tag = TAG_trainer_age;
					} else if (localName.equals("wage")) { //$NON-NLS-1$
						current_tag = TAG_trainer_wage;
					} else if (localName.equals("skillCoach")) { //$NON-NLS-1$
						current_tag = TAG_trainer_skillCoach;
					} else if (localName.equalsIgnoreCase("skillStamina")) { //$NON-NLS-1$
						current_tag = TAG_trainer_stamina;
					} else if (localName.equalsIgnoreCase("skillPace")) { //$NON-NLS-1$
						current_tag = TAG_trainer_pace;
					} else if (localName.equalsIgnoreCase("skillTechnique")) { //$NON-NLS-1$
						current_tag = TAG_trainer_technique;
					} else if (localName.equalsIgnoreCase("skillPassing")) { //$NON-NLS-1$
						current_tag = TAG_trainer_passing;
					} else if (localName.equalsIgnoreCase("skillKeeper")) { //$NON-NLS-1$
						current_tag = TAG_trainer_keepers;
					} else if (localName.equalsIgnoreCase("skillDefending")) { //$NON-NLS-1$
						current_tag = TAG_trainer_defenders;
					} else if (localName.equalsIgnoreCase("skillPlaymaking")) { //$NON-NLS-1$
						current_tag = TAG_trainer_playmakers;
					} else if (localName.equalsIgnoreCase("skillScoring")) { //$NON-NLS-1$
						current_tag = TAG_trainer_scorers;
					} else if (localName.equalsIgnoreCase("signedContract")) { //$NON-NLS-1$
							current_tag = TAG_trainer_signed;
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
			new SVLogger(Level.WARNING, "Parser Class", e); //$NON-NLS-1$
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
	public ArrayList<Coach> getAlCoach() {
		return alCoach;
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
