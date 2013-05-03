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

import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.utils.Log;

public class VarsXmlParser {

	private static int currentTag = 0;

	private static final int TAG_VARS = 0;
	private static final int TAG_WEEK = 1;
	private static final int TAG_DAY = 2;

	private static int tagSwitch = 0;

	private SokkerDate sokkerDate;

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			private StringBuilder message;

			public void characters(char ch[], int start, int length) throws SAXException {
				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_DAY:
					getSokkerDate().setDay(Integer.valueOf(message.toString()));
					break;
				case TAG_WEEK:
					getSokkerDate().setWeek(Integer.valueOf(message.toString()));
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
			}


			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();

				if (localName.equalsIgnoreCase("vars")) { 
					tagSwitch = TAG_VARS;
					setSokkerDate(new SokkerDate());
				}

				if (tagSwitch == 0) {
					if (localName.equals("week")) { 
						currentTag = TAG_WEEK;
					} else if (localName.equals("day")) { 
						currentTag = TAG_DAY;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new VarsErrorHandler());

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

	public SokkerDate getSokkerDate() {
		return sokkerDate;
	}

	public void setSokkerDate(SokkerDate sokkerDate) {
		this.sokkerDate = sokkerDate;
	}

}


class VarsErrorHandler implements ErrorHandler {

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
