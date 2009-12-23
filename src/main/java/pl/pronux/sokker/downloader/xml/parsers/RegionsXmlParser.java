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

import pl.pronux.sokker.model.Region;
import pl.pronux.sokker.utils.Log;

public class RegionsXmlParser {

//	<regions countryID="1">


	static int current_tag = 0;
	static final int TAG_regions = 1;

	int countryID;
	static final int TAG_region = 2;

	static final int TAG_regionID =3;

	static final int TAG_name = 4;

	static final int TAG_weather = 5;

	static int TAG_switch = 0;

	public ArrayList<Region> alRegions;

	private Region region;

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
					if(region.getRegionID() != -1) {
						alRegions.add(region);
					}
				}
			}

			public void startDocument() {
				alRegions = new ArrayList<Region>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();
				if (localName.equals("regions")) { //$NON-NLS-1$
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("countryID")) { //$NON-NLS-1$
							countryID = Integer.valueOf(value);
						}
					}
				}

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
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new RegionsErrorHandler());

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

	public ArrayList<Region> getAlRegions() {
		return alRegions;
	}

	public int getCountryID() {
		return countryID;
	}
}

class RegionsErrorHandler implements ErrorHandler {
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
