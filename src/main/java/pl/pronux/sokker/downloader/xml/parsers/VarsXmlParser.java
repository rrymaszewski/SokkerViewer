package pl.pronux.sokker.downloader.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.utils.file.SVLogger;

public class VarsXmlParser {

	static int current_tag = 0;

	static final int TAG_vars = 0;
	static final int TAG_week = 1;
	static final int TAG_day = 2;

	static int TAG_switch = 0;

	public SokkerDate sokkerDate;

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}
	
	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {

			public void characters(char ch[], int start, int length) throws SAXException {
				// System.out.print("Ciag znakow: ");
				// wypisujemy lancuch, zmieniajac znaki tabulacji i konca
				// linii na ich specjalne reprezentacje

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_day:
					sokkerDate.setDay(Integer.valueOf(message.toString()));
					break;
				case TAG_week:
					sokkerDate.setWeek(Integer.valueOf(message.toString()));
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
			}

			public void startDocument() {
			}

			StringBuffer message;

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();

				if (localName.equalsIgnoreCase("vars")) { //$NON-NLS-1$
					TAG_switch = TAG_vars;
					sokkerDate = new SokkerDate();
				}

				if (TAG_switch == 0) {
					if (localName.equals("week")) { //$NON-NLS-1$
						current_tag = TAG_week;
					} else if (localName.equals("day")) { //$NON-NLS-1$
						current_tag = TAG_day;
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
			new SVLogger(Level.WARNING, "Parser Class", e); //$NON-NLS-1$
		} catch (SAXException e) {
			if (file != null) {
				new File(file).delete();
			}
			throw e;
		}
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
