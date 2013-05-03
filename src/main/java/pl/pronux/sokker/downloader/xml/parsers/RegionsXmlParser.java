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

import pl.pronux.sokker.model.Region;
import pl.pronux.sokker.utils.Log;

public class RegionsXmlParser {

//	<regions countryID="1">

//	private static final int TAG_regions = 1;
	
	private static final int TAG_REGION = 2;

	private static final int TAG_REGION_ID =3;

	private static final int TAG_NAME = 4;

	private static final int TAG_WEATHER = 5;

	private static int tagSwitch = 0;

	private static int currentTag = 0;

	private int countryId;
	
	private List<Region> regions;

	private Region region;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			private StringBuilder message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_REGION_ID:
					region.setRegionId(Integer.valueOf(message.toString()));
					break;
				case TAG_NAME:
					region.setName(message.toString());
					break;
				case TAG_WEATHER:
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
				currentTag = 0;
				if (localName.equals("region")) { 
					if(region.getRegionId() != -1) {
						regions.add(region);
					}
				}
			}

			public void startDocument() {
				regions = new ArrayList<Region>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("regions")) { 
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("countryID")) { 
							countryId = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("region")) { 
					tagSwitch = TAG_REGION;
					region = new Region();
					region.setRegionId(-1);
				}

				if (tagSwitch == TAG_REGION) {
					if (localName.equals("regionID")) { 
						currentTag = TAG_REGION_ID;
					} else if (localName.equalsIgnoreCase("name")) { 
						currentTag = TAG_NAME;
					} else if (localName.equalsIgnoreCase("weather")) { 
						currentTag = TAG_WEATHER;
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
			Log.error("Parser Class", e); 
		} catch (SAXException e) {
			if (file != null) {
				new File(file).delete();
			}
			throw e;
		}
	}

	public List<Region> getRegions() {
		return regions;
	}

	public int getCountryId() {
		return countryId;
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
