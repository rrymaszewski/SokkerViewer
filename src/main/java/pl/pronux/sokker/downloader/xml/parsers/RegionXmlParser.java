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

import pl.pronux.sokker.model.Region;
import pl.pronux.sokker.utils.Log;

public class RegionXmlParser {

	static int current_tag = 0;

	static final int TAG_region = 1;

	static final int TAG_regionID =2;

	static final int TAG_name = 3;

	static final int TAG_weather = 4;

	static final int TAG_countryID = 5;

	static int TAG_switch = 0;

	private Region region;

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			StringBuffer message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_regionID:
					region.setRegionID(Integer.valueOf(message.toString()));
					break;
				case TAG_name:
					region.setName(message.toString());
					break;
				case TAG_weather:
						region.setWeather(Integer.valueOf(message.toString()));
						break;
				case TAG_countryID:
					region.setIdCountryFK(Integer.valueOf(message.toString()));
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
				if (localName.equals("region")) { //$NON-NLS-1$
					if(region.getRegionID() == -1) {
						region = null;
					}
				}
			}

			public void startDocument() {
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();

				if (localName.equals("region")) { //$NON-NLS-1$
					TAG_switch = TAG_region;
					region = new Region();
					region.setRegionID(-1);
				}

				if (TAG_switch == TAG_region) {
					if (localName.equals("regionID")) { //$NON-NLS-1$
						current_tag = TAG_regionID;
					} else if (localName.equalsIgnoreCase("name")) { //$NON-NLS-1$
						current_tag = TAG_name;
					} else if (localName.equalsIgnoreCase("weather")) { //$NON-NLS-1$
						current_tag = TAG_weather;
					} else if (localName.equalsIgnoreCase("countryID")) { //$NON-NLS-1$
						current_tag = TAG_countryID;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new RegionErrorHandler());

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

	public Region getRegion() {
		return region;
	}
}

class RegionErrorHandler implements ErrorHandler {
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
