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

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Report;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.utils.Log;

public class ReportsXmlParser {

	private static int currentTag = 0;

//	private static final int TAG_reports = 1;

	private static final int TAG_REPORT = 2;

	private static final int TAG_REPORT_ID = 3;

	private static final int TAG_TYPE = 4;

	private static final int TAG_PLAYER_ID = 5;

	private static final int TAG_DATE = 6;

	private static final int TAG_VALUE = 7;

	private static final int TAG_WEEK = 8;

	private static int tagSwitch = 0;

	private List<Report> reports;

	private int teamId;

	private Report report;

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			private StringBuilder message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_REPORT_ID:
					report.setReportId(Long.valueOf(message.toString()));
					break;
				case TAG_TYPE:
					report.setType(Integer.valueOf(message.toString()));
					break;
				case TAG_PLAYER_ID:
						report.setPersonId(Integer.valueOf(message.toString()));
						break;
				case TAG_DATE:
						report.setDate(new Date(message.toString()));
					break;
				case TAG_VALUE:
					report.setValue(Integer.valueOf(message.toString()));
				break;
				case TAG_WEEK:
					report.getDate().setSokkerDate(new SokkerDate(0,Integer.valueOf(message.toString())));
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
				if (localName.equals("report")) { 
					if(report.getReportId() != -1) {
						reports.add(report);
					}
				}
			}

			public void startDocument() {
				reports = new ArrayList<Report>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("reports")) { 
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { 
							setTeamId(Integer.valueOf(value));
						}
					}
				}

				if (localName.equals("report")) { 
					tagSwitch = TAG_REPORT;
					report = new Report();
					report.setReportId(-1);
				}
				if (tagSwitch == TAG_REPORT) {
					if (localName.equals("reportID")) { 
						currentTag = TAG_REPORT_ID;
					} else if (localName.equalsIgnoreCase("type")) { 
						currentTag = TAG_TYPE;
					} else if (localName.equalsIgnoreCase("playerID")) { 
						currentTag = TAG_PLAYER_ID;
					} else if (localName.equalsIgnoreCase("date")) { 
						currentTag = TAG_DATE;
					} else if (localName.equals("value")) { 
						currentTag = TAG_VALUE;
					} else if (localName.equals("week")) { 
						currentTag = TAG_WEEK;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new ReportsErrorHandler());

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

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.downloader.xml.parsers.ReportsXmlParserInterface#getAlReports()
	 */
	public List<Report> getReports() {
		return reports;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
}

class ReportsErrorHandler implements ErrorHandler {
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
