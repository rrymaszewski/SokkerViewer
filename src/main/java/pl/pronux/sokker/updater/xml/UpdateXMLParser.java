package pl.pronux.sokker.updater.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import pl.pronux.sokker.updater.model.Package;

class UpdateErrorHandler implements ErrorHandler {

	public void error(SAXParseException e) throws SAXException {
		throw new SAXException();
	}

	public void fatalError(SAXParseException e) throws SAXException {
		throw new SAXException();
	}

	public void warning(SAXParseException e) throws SAXException {
		// throw new SAXException();
	}
}


public class UpdateXMLParser {

	private static final int TAG_SOKKERDATA = 100;

	private static final int TAG_SOKKERVIEWER = 1;

	private static final int TAG_REVISION = 2;

	private static final int TAG_VERSION = 3;

	private static final int TAG_PACKAGES = 4;

	private static final int TAG_PACKAGE = 5;

	private static final int TAG_NAME = 6;

	private static final int TAG_SIGNATURE = 7;

	private static final int TAG_PATH = 8;

	private static final int TAG_AUTHOR = 9;

	private static final int TAG_LOCALPATH = 10;

	private static final int TAG_FILENAME = 11;

	private static final int TAG_DESCRIPTION = 12;

	private static final int TAG_CHANGELOG = 13;

	private Map<String, Package> packages = new HashMap<String, Package>();

	private int currentTag = 0;

	private int tagSwitch = 0;

	private Package pack;

	private int revision;

	private String version;

	private String changelog;

	public List<Package> compareTo(UpdateXMLParser parser) {
		List<Package> downloadedPackages = new ArrayList<Package>();
		if (this.getRevision() > parser.getRevision()) {
			for (String key : getPackages().keySet()) {
				Package pkgOld = parser.getPackages().get(key);
				Package pkgNew = getPackages().get(key);

				if (pkgOld == null) {
					downloadedPackages.add(pkgNew);
				} else {
					if (pkgOld.getRevision() < pkgNew.getRevision()) {
						downloadedPackages.add(pkgNew);
					}
				}
			}

		}
		return downloadedPackages;
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException, IOException {

		class SAXHandler extends DefaultHandler {

			private StringBuilder message;

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_SOKKERDATA:
					if (message.toString().equalsIgnoreCase("empty")) { 
						throw new SAXException();
					}
					break;
				case TAG_NAME:
					pack.setName(message.toString());
					break;
				case TAG_AUTHOR:
					pack.setAuthor(message.toString());
					break;
				case TAG_FILENAME:
					pack.setFilename(message.toString());
					break;
				case TAG_LOCALPATH:
					pack.setLocalpath(message.toString());
					break;
				case TAG_PATH:
					pack.setPath(message.toString());
					break;
				case TAG_SIGNATURE:
					pack.setSignature(message.toString());
					break;
				case TAG_REVISION:
					setRevision(Integer.valueOf(message.toString()));
					break;
				case TAG_VERSION:
					setVersion(message.toString());
					break;
				case TAG_CHANGELOG:
					setChangelog(message.toString());
					break;
				case TAG_DESCRIPTION:
					pack.setDescription(message.toString());
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

				if (localName.equals("package")) { 
					getPackages().put(pack.getName(), pack);
				}

			}

			/**
			 * xml parsing start
			 */
			public void startDocument() {
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();

				if (localName.equalsIgnoreCase("sokkerviewer")) { 
					tagSwitch = TAG_SOKKERVIEWER;
				}

				if (localName.equalsIgnoreCase("packages")) { 
					tagSwitch = TAG_PACKAGES;
				}

				if (localName.equals("package")) { 
					tagSwitch = TAG_PACKAGE;

					pack = new Package();

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("revision")) { 
							pack.setRevision(Integer.valueOf(value).intValue());
						}
					}
				}

				if (tagSwitch == TAG_PACKAGE) {
					if (localName.equals("name")) { 
						currentTag = TAG_NAME;
					} else if (localName.equals("signature")) { 
						currentTag = TAG_SIGNATURE;
					} else if (localName.equals("path")) { 
						currentTag = TAG_PATH;
					} else if (localName.equals("author")) { 
						currentTag = TAG_AUTHOR;
					} else if (localName.equals("localpath")) { 
						currentTag = TAG_LOCALPATH;
					} else if (localName.equals("filename")) { 
						currentTag = TAG_FILENAME;
					} else if (localName.equals("description")) { 
						currentTag = TAG_DESCRIPTION;
					}
				} else if (tagSwitch == TAG_SOKKERVIEWER) {
					if (localName.equals("revision")) { 
						currentTag = TAG_REVISION;
					} else if (localName.equals("version")) { 
						currentTag = TAG_VERSION;
					} else if (localName.equals("changelog")) { 
						currentTag = TAG_CHANGELOG;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new UpdateErrorHandler());

			parser.parse(input);
		} catch (IOException e) {
			throw e;
		} catch (SAXException e) {
			if (file != null) {
				new File(file).delete();
			}
			throw e;
		}
	}

	public Map<String, Package> getPackages() {
		return packages;
	}

	public void setPackages(Map<String, Package> packages) {
		this.packages = packages;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getChangelog() {
		return changelog;
	}

	public void setChangelog(String changelog) {
		this.changelog = changelog;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}
}
