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

import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.utils.Log;

public class JuniorXmlParser  {

	private static int currentTag = 0;

	private static final int TAG_JUNIOR_ID = 1;

	private static final int TAG_JUNIOR_NAME = 2;

	private static final int TAG_JUNIOR_SKILL = 5;
	
	private static final int TAG_FORMATION = 6;
	
	private static final int TAG_AGE = 7;

	private static final int TAG_JUNIOR_SURNAME = 3;

	private static final int TAG_JUNIOR_WEEKS = 4;

//	private static final int TAG_JUNIORS = 0;

	private static final int TAG_JUNIOR = 5;

	private static int tagSwitch = 0;

	private List<Junior> juniors;

	private int teamId;

	private Junior junior;

	private JuniorSkills[] juniorSkills;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			private StringBuilder message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_JUNIOR_ID:
					junior.setId(Integer.valueOf(message.toString()));
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
				case TAG_FORMATION:
					junior.setFormation(Integer.valueOf(message.toString()).intValue());
					break;
				case TAG_AGE:
					juniorSkills[0].setAge(Integer.valueOf(message.toString()).intValue());
				default:
					break;
				}
			}

			// obsluga bledow

			public void endDocument() {
			}

			public void endElement(String namespaceURL, String localName, String qName) {
				currentTag = 0;
				if (localName.equals("junior")) { 
					junior.setSkills(juniorSkills);
					if (junior.getId() != -1) {
						juniors.add(junior);
					}
				}
			}

			public void startDocument() {
				juniors = new ArrayList<Junior>();
			}


			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("juniors")) { 
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { 
							setTeamId(Integer.valueOf(value));
						}
					}
				}

				if (localName.equals("junior")) { 
					tagSwitch = TAG_JUNIOR;

					junior = new Junior();
					junior.setId(-1);
					juniorSkills = new JuniorSkills[1];
					juniorSkills[0] = new JuniorSkills();

				}

				if (tagSwitch == TAG_JUNIOR) {
					if (localName.equals("name")) { 
						currentTag = TAG_JUNIOR_NAME;
					} else if (localName.equals("surname")) { 
						currentTag = TAG_JUNIOR_SURNAME;
					} else if (localName.equals("weeks")) { 
						currentTag = TAG_JUNIOR_WEEKS;
					} else if (localName.equals("skill")) { 
						currentTag = TAG_JUNIOR_SKILL;
					} else if (localName.equals("ID")) { 
						currentTag = TAG_JUNIOR_ID;
					} else if (localName.equals("age")) {
						currentTag = TAG_AGE;
					} else if (localName.equals("formation")) {
						currentTag = TAG_FORMATION;
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
	 *
	 * @see pl.pronux.sokker.downloader.xml.parsers.JuniorXmlParserInterface#getAlJuniors()
	 */
	public List<Junior> getJuniors() {
		return juniors;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
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
