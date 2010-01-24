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

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Report;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.utils.Log;

public class ReportsXmlParser {

	static int current_tag = 0;

	static final int TAG_reports = 1;

	static final int TAG_report = 2;

	static final int TAG_reportID = 3;

	static final int TAG_type = 4;

	static final int TAG_playerID = 5;

	static final int TAG_date = 6;

	static final int TAG_value = 7;

	static final int TAG_week = 8;

	static int TAG_switch = 0;

	public ArrayList<Report> alReports;

	public int teamID;

	private Report report;

	public void parseXmlSax(final InputSource input) throws SAXException {
		parseXmlSax(input, null);
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			StringBuilder message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_reportID:
					report.setReportID(Long.valueOf(message.toString()));
					break;
				case TAG_type:
					report.setType(Integer.valueOf(message.toString()));
					break;
				case TAG_playerID:
						report.setPersonID(Integer.valueOf(message.toString()));
						break;
				case TAG_date:
						report.setDate(new Date(message.toString()));
					break;
				case TAG_value:
					report.setValue(Integer.valueOf(message.toString()));
				break;
				case TAG_week:
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
				current_tag = 0;
				if (localName.equals("report")) { //$NON-NLS-1$
					if(report.getReportID() != -1) {
						alReports.add(report);
					}
				}
			}

			public void startDocument() {
				alReports = new ArrayList<Report>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("reports")) { //$NON-NLS-1$
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { //$NON-NLS-1$
							teamID = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("report")) { //$NON-NLS-1$
					TAG_switch = TAG_report;
					report = new Report();
					report.setReportID(-1);
				}
				if (TAG_switch == TAG_report) {
					if (localName.equals("reportID")) { //$NON-NLS-1$
						current_tag = TAG_reportID;
					} else if (localName.equalsIgnoreCase("type")) { //$NON-NLS-1$
						current_tag = TAG_type;
					} else if (localName.equalsIgnoreCase("playerID")) { //$NON-NLS-1$
						current_tag = TAG_playerID;
					} else if (localName.equalsIgnoreCase("date")) { //$NON-NLS-1$
						current_tag = TAG_date;
					} else if (localName.equals("value")) { //$NON-NLS-1$
						current_tag = TAG_value;
					} else if (localName.equals("week")) { //$NON-NLS-1$
						current_tag = TAG_week;
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
			Log.error("Parser Class", e); //$NON-NLS-1$
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
	public ArrayList<Report> getAlReports() {
		return alReports;
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
