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

import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.utils.Log;

public class CountryXmlParser {

	static int current_tag = 0;
	static final int TAG_country = 1;

	static final int TAG_countryID =2;

	static final int TAG_name = 3;

	static final int TAG_currencyName = 4;

	static final int TAG_currencyRate = 5;

	static int TAG_switch = 0;

	private Country country;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			StringBuffer message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_countryID:
					country.setCountryID(Integer.valueOf(message.toString()));
					break;
				case TAG_name:
					country.setName(message.toString());
					break;
				case TAG_currencyName:
						country.setCurrencyName(message.toString());
						break;
				case TAG_currencyRate:
						country.setCurrencyRate(Double.valueOf(message.toString()));
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
				if (localName.equals("country")) { //$NON-NLS-1$
					if(country.getCountryID() == -1) {
						country = null;
					}
				}
			}

			public void startDocument() {
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();

				if (localName.equals("country")) { //$NON-NLS-1$
					TAG_switch = TAG_country;
					country = new Country();
					country.setCountryID(-1);
				}

				if (TAG_switch == TAG_country) {
					if (localName.equals("countryID")) { //$NON-NLS-1$
						current_tag = TAG_countryID;
					} else if (localName.equalsIgnoreCase("name")) { //$NON-NLS-1$
						current_tag = TAG_name;
					} else if (localName.equalsIgnoreCase("currencyName")) { //$NON-NLS-1$
						current_tag = TAG_currencyName;
					} else if (localName.equalsIgnoreCase("currencyRate")) { //$NON-NLS-1$
						current_tag = TAG_currencyRate;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new CountryErrorHandler());

			parser.parse(input);
		} catch (IOException e) {
			Log.error(CountryXmlParser.class, "Parser Class", e); //$NON-NLS-1$
		} catch (SAXException e) {
			if (file != null) {
				new File(file).delete();
			}
			throw e;
		}
	}

	public Country getCountry() {
		return country;
	}
}

class CountryErrorHandler implements ErrorHandler {
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
