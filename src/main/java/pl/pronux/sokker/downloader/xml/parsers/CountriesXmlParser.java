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

	private static int currentTag = 0;
//	private static final int TAG_COUNTRIES = 1;

	private static final int TAG_COUNTRY = 2;

	private static final int TAG_COUNTRY_ID =3;

	private static final int TAG_NAME = 4;

	private static final int TAG_CURRENCY_NAME = 5;

	private static final int TAG_CURRENCY_RATE = 6;

	private static int tagSwitch = 0;

	private List<Country> countries;

	private Country country;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			private StringBuilder message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_COUNTRY_ID:
					country.setCountryId(Integer.valueOf(message.toString()));
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
				currentTag = 0;
				if (localName.equals("country")) { 
					if(country.getCountryId() != -1) {
						countries.add(country);
					}
				}
			}

			public void startDocument() {
				countries = new ArrayList<Country>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("countries")) { 
				}

				if (localName.equals("country")) { 
					tagSwitch = TAG_COUNTRY;
					country = new Country();
					country.setCountryId(-1);
				}

				if (tagSwitch == TAG_COUNTRY) {
					if (localName.equals("countryID")) { 
						currentTag = TAG_COUNTRY_ID;
					} else if (localName.equalsIgnoreCase("name")) { 
						currentTag = TAG_NAME;
					} else if (localName.equalsIgnoreCase("currencyName")) { 
						currentTag = TAG_CURRENCY_NAME;
					} else if (localName.equalsIgnoreCase("currencyRate")) { 
						currentTag = TAG_CURRENCY_RATE;
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
			Log.error("Parser Class", e); 
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
