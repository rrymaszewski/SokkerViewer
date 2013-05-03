package pl.pronux.sokker.downloader.xml.parsers;

import java.io.File;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import pl.pronux.sokker.model.League;
import pl.pronux.sokker.utils.Log;

public class LeagueInfoXmlParser {

	private static int currentTag = 0;

	private static final int TAG_LEAGUE = 0;

	private static final int TAG_INFO = 1;

	private static final int TAG_LEAGUE_ID = 2;

	private static final int TAG_NAME = 3;

	private static final int TAG_COUNTRY_ID = 4;

	private static final int TAG_DIVISION = 5;

	private static final int TAG_ROUND = 6;

	private static final int TAG_SEASON = 7;

	private static final int TAG_TYPE = 8;

	private static final int TAG_IS_OFFICIAL = 9;

	private static final int TAG_IS_CUP = 10;

	private static final int TAG_USER_ID = 11;

	private static int tagSwitch = 0;

	private League league;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			private StringBuilder message;

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_LEAGUE_ID:
					league.setLeagueId(Integer.parseInt(message.toString()));
					break;
				case TAG_NAME:
					league.setName(message.toString());
					break;
				case TAG_COUNTRY_ID:
					league.setCountryId(Integer.parseInt(message.toString()));
					break;
				case TAG_DIVISION:
					league.setDivision(Integer.parseInt(message.toString()));
					break;
				case TAG_ROUND:
					league.setRound(Integer.parseInt(message.toString()));
					break;
				case TAG_SEASON:
					league.setSeason(Integer.parseInt(message.toString()));
					break;
				case TAG_TYPE:
					league.setType(Integer.parseInt(message.toString()));
					break;
				case TAG_IS_OFFICIAL:
					league.setIsOfficial(Integer.parseInt(message.toString()));
					break;
				case TAG_IS_CUP:
					league.setIsCup(Integer.parseInt(message.toString()));
					break;
				case TAG_USER_ID:
					league.setUserId(Integer.parseInt(message.toString()));
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
			}

			public void startDocument() {
				league = new League();
			}


			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();

				if (localName.equalsIgnoreCase("league")) { 
					tagSwitch = TAG_LEAGUE;
				}

				if (localName.equalsIgnoreCase("info")) { 
					tagSwitch = TAG_INFO;
				}

				if (tagSwitch == TAG_INFO) {
					if (localName.equals("leagueID")) { 
						currentTag = TAG_LEAGUE_ID;
					} else if (localName.equals("name")) { 
						currentTag = TAG_NAME;
					} else if (localName.equals("countryID")) { 
						currentTag = TAG_COUNTRY_ID;
					} else if (localName.equals("division")) { 
						currentTag = TAG_DIVISION;
					} else if (localName.equals("round")) { 
						currentTag = TAG_ROUND;
					} else if (localName.equals("season")) { 
						currentTag = TAG_SEASON;
					} else if (localName.equals("type")) { 
						currentTag = TAG_TYPE;
					} else if (localName.equals("isOfficial")) { 
						currentTag = TAG_IS_OFFICIAL;
					} else if (localName.equals("isCup")) { 
						currentTag = TAG_IS_CUP;
					} else if (localName.equals("userID")) { 
						currentTag = TAG_USER_ID;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new LeagueInfoErrorHandler());

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

}


class LeagueInfoErrorHandler implements ErrorHandler {

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
