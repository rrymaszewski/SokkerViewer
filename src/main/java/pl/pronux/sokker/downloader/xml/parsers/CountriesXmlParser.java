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

import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.utils.Log;

public class CountriesXmlParser {

	static int current_tag = 0;
	static final int TAG_COUNTRIES = 1;

	static final int TAG_COUNTRY = 2;

	static final int TAG_COUNTRY_ID =3;

	static final int TAG_NAME = 4;

	static final int TAG_CURRENCY_NAME = 5;

	static final int TAG_CURRENCY_RATE = 6;

	static int TAG_switch = 0;

	public List<Country> countries;

	private Country country;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			StringBuilder message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_COUNTRY_ID:
					country.setCountryID(Integer.valueOf(message.toString()));
					break;
				case TAG_NAME:
					country.setName(message.toString());
					break;
				case TAG_CURRENCY_NAME:
						country.setCurrencyName(message.toString());
						break;
				case TAG_CURRENCY_RATE:
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
					if(country.getCountryID() != -1) {
						countries.add(country);
					}
				}
			}

			public void startDocument() {
				countries = new ArrayList<Country>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("countries")) { //$NON-NLS-1$
				}

				if (localName.equals("country")) { //$NON-NLS-1$
					TAG_switch = TAG_COUNTRY;
					country = new Country();
					country.setCountryID(-1);
				}

				if (TAG_switch == TAG_COUNTRY) {
					if (localName.equals("countryID")) { //$NON-NLS-1$
						current_tag = TAG_COUNTRY_ID;
					} else if (localName.equalsIgnoreCase("name")) { //$NON-NLS-1$
						current_tag = TAG_NAME;
					} else if (localName.equalsIgnoreCase("currencyName")) { //$NON-NLS-1$
						current_tag = TAG_CURRENCY_NAME;
					} else if (localName.equalsIgnoreCase("currencyRate")) { //$NON-NLS-1$
						current_tag = TAG_CURRENCY_RATE;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new CountriesErrorHandler());

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

	public List<Country> getCountries() {
		return countries;
	}
}

class CountriesErrorHandler implements ErrorHandler {
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
