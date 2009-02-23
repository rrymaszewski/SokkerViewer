package pl.pronux.sokker.updater.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

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

	final static int TAG_SOKKERDATA = 100;

	final static int TAG_SOKKERVIEWER = 1;

	final static int TAG_REVISION = 2;

	final static int TAG_VERSION = 3;

	final static int TAG_PACKAGES = 4;

	final static int TAG_PACKAGE = 5;

	final static int TAG_NAME = 6;

	final static int TAG_SIGNATURE = 7;

	final static int TAG_PATH = 8;

	final static int TAG_AUTHOR = 9;

	final static int TAG_LOCALPATH = 10;

	final static int TAG_FILENAME = 11;

	final static int TAG_DESCRIPTION = 12;

	final static int TAG_CHANGELOG = 13;

	public Hashtable<String, Package> alPackages = new Hashtable<String, Package>();

	private int currentTag = 0;

	private int tagSwitch = 0;

	private Package pack;

	public int revision;

	public String version;

	public String changelog;

	public ArrayList<Package> compareTo(UpdateXMLParser parser) {
		ArrayList<Package> downloadedPackages = new ArrayList<Package>();
		if (this.revision > parser.revision) {
			Enumeration<String> e = alPackages.keys();
			String key;
			while (e.hasMoreElements()) {
				key = e.nextElement();
				Package pkgOld = parser.alPackages.get(key);
				Package pkgNew = alPackages.get(key);

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

			StringBuffer message;

			public void characters(char ch[], int start, int length) throws SAXException {
				// System.out.print("Ciag znakow: ");
				// wypisujemy lancuch, zmieniajac znaki tabulacji i konca
				// linii na ich specjalne reprezentacje

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_SOKKERDATA:
					if (message.toString().equalsIgnoreCase("empty")) { //$NON-NLS-1$
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
					revision = Integer.valueOf(message.toString());
					break;
				case TAG_VERSION:
					version = message.toString();
					break;
				case TAG_CHANGELOG:
					changelog = message.toString();
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

				if (localName.equals("package")) { //$NON-NLS-1$
					alPackages.put(pack.getName(), pack);
				}

			}

			/**
			 * xml parsing start
			 */
			public void startDocument() {
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();

				if (localName.equalsIgnoreCase("sokkerviewer")) { //$NON-NLS-1$
					tagSwitch = TAG_SOKKERVIEWER;
				}

				if (localName.equalsIgnoreCase("packages")) { //$NON-NLS-1$
					tagSwitch = TAG_PACKAGES;
				}

				if (localName.equals("package")) { //$NON-NLS-1$
					tagSwitch = TAG_PACKAGE;

					pack = new Package();

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("revision")) { //$NON-NLS-1$
							pack.setRevision(Integer.valueOf(value).intValue());
						}
					}
				}

				if (tagSwitch == TAG_PACKAGE) {
					if (localName.equals("name")) { //$NON-NLS-1$
						currentTag = TAG_NAME;
					} else if (localName.equals("signature")) { //$NON-NLS-1$
						currentTag = TAG_SIGNATURE;
					} else if (localName.equals("path")) { //$NON-NLS-1$
						currentTag = TAG_PATH;
					} else if (localName.equals("author")) { //$NON-NLS-1$
						currentTag = TAG_AUTHOR;
					} else if (localName.equals("localpath")) { //$NON-NLS-1$
						currentTag = TAG_LOCALPATH;
					} else if (localName.equals("filename")) { //$NON-NLS-1$
						currentTag = TAG_FILENAME;
					} else if (localName.equals("description")) { //$NON-NLS-1$
						currentTag = TAG_DESCRIPTION;
					}
				} else if (tagSwitch == TAG_SOKKERVIEWER) {
					if (localName.equals("revision")) { //$NON-NLS-1$
						currentTag = TAG_REVISION;
					} else if (localName.equals("version")) { //$NON-NLS-1$
						currentTag = TAG_VERSION;
					} else if (localName.equals("changelog")) { //$NON-NLS-1$
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
}
