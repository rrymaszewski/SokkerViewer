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

import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.utils.Log;

public class JuniorXmlParser  {

	static int current_tag = 0;

	static final int TAG_junior_id = 1;

	static final int TAG_junior_name = 2;

	static final int TAG_junior_skill = 5;
	
	static final int TAG_formation = 6;
	
	static final int TAG_age = 7;

	static final int TAG_junior_surname = 3;

	static final int TAG_junior_weeks = 4;

	static final int TAG_juniors = 0;

	static final int TAG_junior = 5;

	static int TAG_switch = 0;

	public ArrayList<Junior> alJuniors;

	public int teamID;

	private Junior junior;

	private JuniorSkills[] juniorSkills;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_junior_id:
					junior.setId(Integer.valueOf(message.toString()));
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
				case TAG_formation:
					junior.setFormation(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_age:
					juniorSkills[0].setAge(Integer.valueOf(message.toString()).intValue());
				default:
					break;
				}
			}

			// obsluga bledow

			public void endDocument() {
			}

			public void endElement(String namespaceURL, String localName, String qName) {
				current_tag = 0;
				if (localName.equals("junior")) { //$NON-NLS-1$
					junior.setSkills(juniorSkills);
					if (junior.getId() != -1) {
						alJuniors.add(junior);
					}
				}
			}

			public void startDocument() {
				alJuniors = new ArrayList<Junior>();
			}

			StringBuilder message;

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("juniors")) { //$NON-NLS-1$
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { //$NON-NLS-1$
							teamID = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("junior")) { //$NON-NLS-1$
					TAG_switch = TAG_junior;

					junior = new Junior();
					junior.setId(-1);
					juniorSkills = new JuniorSkills[1];
					juniorSkills[0] = new JuniorSkills();

				}

				if (TAG_switch == TAG_junior) {
					if (localName.equals("name")) { //$NON-NLS-1$
						current_tag = TAG_junior_name;
					} else if (localName.equals("surname")) { //$NON-NLS-1$
						current_tag = TAG_junior_surname;
					} else if (localName.equals("weeks")) { //$NON-NLS-1$
						current_tag = TAG_junior_weeks;
					} else if (localName.equals("skill")) { //$NON-NLS-1$
						current_tag = TAG_junior_skill;
					} else if (localName.equals("ID")) { //$NON-NLS-1$
						current_tag = TAG_junior_id;
					} else if (localName.equals("age")) {
						current_tag = TAG_age;
					} else if (localName.equals("formation")) {
						current_tag = TAG_formation;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new JuniorErrorHandler());

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

	/*
	 * (non-Javadoc)
	 *
	 * @see pl.pronux.sokker.downloader.xml.parsers.JuniorXmlParserInterface#getAlJuniors()
	 */
	public ArrayList<Junior> getAlJuniors() {
		return alJuniors;
	}
}

class JuniorErrorHandler implements ErrorHandler {
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
