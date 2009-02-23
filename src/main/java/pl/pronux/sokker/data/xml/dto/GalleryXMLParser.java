package pl.pronux.sokker.data.xml.dto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.GalleryImage;

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

public class GalleryXMLParser {

	final static int TAG_SOKKERDATA = 100;

	final static int TAG_SOKKERVIEWER = 1;

	final static int TAG_REVISION = 2;

	final static int TAG_IMAGES = 3;

	final static int TAG_IMAGE = 4;

	final static int TAG_NAME = 5;

	final static int TAG_FILE = 6;

	final static int TAG_USERID = 7;

	final static int TAG_PAYMENT = 8;
	
	final static int TAG_SIGNATURE = 9;

	final static int TAG_PUBLICATION_DATE = 10;
	
	public Map<String, GalleryImage> alImages = new HashMap<String, GalleryImage>();

	private int currentTag = 0;

	private int tagSwitch = 0;

	private GalleryImage galleryImage;

	public int revision;

	public ArrayList<GalleryImage> compareTo(GalleryXMLParser parser) {
		ArrayList<GalleryImage> downloadImages = new ArrayList<GalleryImage>();
		if (this.revision > parser.revision) {
			Set<String> e = alImages.keySet();
			for(String key : e) {
				GalleryImage pkgOld = parser.alImages.get(key);
				GalleryImage pkgNew = alImages.get(key);

				if (pkgOld == null) {
					downloadImages.add(pkgNew);
				} else {
					if (pkgOld.getRevision() < pkgNew.getRevision()) {
						downloadImages.add(pkgNew);
					}
				}
			}

		}
		return downloadImages;
	}

	public void parseXmlSax(final InputSource input, final String file) throws SAXException, IOException {

		class SAXHandler extends DefaultHandler {

			StringBuffer message;

			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_SOKKERDATA:
					if (message.toString().equalsIgnoreCase("empty")) { //$NON-NLS-1$
						throw new SAXException();
					}
					break;
				case TAG_NAME:
					galleryImage.setName(message.toString());
					break;
				case TAG_USERID:
					galleryImage.setUserID(message.toString());
					break;
				case TAG_PAYMENT:
					galleryImage.setPayment(Integer.valueOf(message.toString()));
					break;
				case TAG_FILE:
					galleryImage.setFile(message.toString());
					break;
				case TAG_REVISION:
					revision = Integer.valueOf(message.toString());
					break;
				case TAG_SIGNATURE:
					galleryImage.setSignature(message.toString());
					break;
				case TAG_PUBLICATION_DATE:
					galleryImage.setPublicationDate(new Date(message.toString()));
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

				if (localName.equals("image")) { //$NON-NLS-1$
					alImages.put(galleryImage.getName(), galleryImage);
				}

			}

			/**
			 * xml parsing start
			 */
			public void startDocument() {
				alImages = new Hashtable<String, GalleryImage>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuffer();

				if (localName.equalsIgnoreCase("sokkerviewer")) { //$NON-NLS-1$
					tagSwitch = TAG_SOKKERVIEWER;
				}

				if (localName.equalsIgnoreCase("images")) { //$NON-NLS-1$
					tagSwitch = TAG_IMAGES;
				}

				if (localName.equals("image")) { //$NON-NLS-1$
					tagSwitch = TAG_IMAGE;

					galleryImage = new GalleryImage();

					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("revision")) { //$NON-NLS-1$
							galleryImage.setRevision(Integer.valueOf(value).intValue());
						}
					}
				}

				if (tagSwitch == TAG_IMAGE) {
					if (localName.equals("name")) { //$NON-NLS-1$
						currentTag = TAG_NAME;
					} else if (localName.equals("file")) { //$NON-NLS-1$
						currentTag = TAG_FILE;
					} else if (localName.equals("userID")) { //$NON-NLS-1$
						currentTag = TAG_USERID;
					} else if (localName.equals("payment")) { //$NON-NLS-1$
						currentTag = TAG_PAYMENT;
					} else if (localName.equals("signature")) { //$NON-NLS-1$
						currentTag = TAG_SIGNATURE;
					} else if (localName.equals("publicationDate")) { //$NON-NLS-1$
						currentTag = TAG_PUBLICATION_DATE;
					}
				} else if (tagSwitch == TAG_SOKKERVIEWER) {
					if (localName.equals("revision")) { //$NON-NLS-1$
						currentTag = TAG_REVISION;
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
