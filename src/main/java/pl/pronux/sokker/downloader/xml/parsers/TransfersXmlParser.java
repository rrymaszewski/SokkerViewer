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
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Transfer;
import pl.pronux.sokker.utils.Log;

public class TransfersXmlParser {

	static int current_tag = 0;

	static final int TAG_transfers = 1;

	static final int TAG_transfer = 2;

	static final int TAG_ID = 3;

	static final int TAG_sellerTeamID = 4;

	static final int TAG_buyerTeamID = 5;

	static final int TAG_sellerTeamName = 6;

	static final int TAG_buyerTeamName = 7;

	static final int TAG_playerID = 8;

	static final int TAG_date = 9;

	static final int TAG_price = 10;

	static final int TAG_playerValue = 11;

	static final int TAG_warning = 12;

	static int TAG_switch = 0;

	private ArrayList<Transfer> alTransfers;

	public int teamID;

	private Transfer transfer;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			StringBuilder message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (current_tag) {
				case TAG_ID:
					transfer.setTransferID(Integer.valueOf(message.toString()));
					break;
				case TAG_sellerTeamID:
					transfer.setSellerTeamID(Integer.valueOf(message.toString()));
					break;
				case TAG_buyerTeamID:
						transfer.setBuyerTeamID(Integer.valueOf(message.toString()));
						break;
				case TAG_sellerTeamName:
						transfer.setSellerTeamName(message.toString());
					break;
				case TAG_buyerTeamName:
					transfer.setBuyerTeamName(message.toString());
				break;
				case TAG_playerID:
					transfer.setPlayerID(Integer.valueOf(message.toString()));
					break;
				case TAG_price:
					transfer.setPrice(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_playerValue:
					transfer.setPlayerValue(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_date:
					transfer.setDate(new Date(message.toString()));
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
				if (localName.equals("transfer")) { //$NON-NLS-1$
					if(transfer.getTransferID() != -1) {
						alTransfers.add(transfer);
					}
				}
			}

			public void startDocument() {
				alTransfers = new ArrayList<Transfer>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("transfers")) { //$NON-NLS-1$
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { //$NON-NLS-1$
							teamID = Integer.valueOf(value);
						}
					}
				}

				if (localName.equals("transfer")) { //$NON-NLS-1$
					TAG_switch = TAG_transfer;
					transfer = new Transfer();
					transfer.setTransferID(-1);
					transfer.setBuyerTeamName(""); //$NON-NLS-1$
					transfer.setBuyerTeamID(0);
					transfer.setSellerTeamID(0);
					transfer.setSellerTeamName(""); //$NON-NLS-1$
				}

				if (TAG_switch == TAG_transfer) {
					if (localName.equals("ID")) { //$NON-NLS-1$
						current_tag = TAG_ID;
					} else if (localName.equalsIgnoreCase("sellerTeamID")) { //$NON-NLS-1$
						current_tag = TAG_sellerTeamID;
					} else if (localName.equalsIgnoreCase("buyerTeamID")) { //$NON-NLS-1$
						current_tag = TAG_buyerTeamID;
					} else if (localName.equalsIgnoreCase("sellerTeamName")) { //$NON-NLS-1$
						current_tag = TAG_sellerTeamName;
					} else if (localName.equals("buyerTeamName")) { //$NON-NLS-1$
						current_tag = TAG_buyerTeamName;
					} else if (localName.equals("playerID")) { //$NON-NLS-1$
						current_tag = TAG_playerID;
					} else if (localName.equals("date")) { //$NON-NLS-1$
						current_tag = TAG_date;
					} else if (localName.equals("price")) { //$NON-NLS-1$
						current_tag = TAG_price;
					} else if (localName.equalsIgnoreCase("playerValue")) { //$NON-NLS-1$
						current_tag = TAG_playerValue;
					}
				}
			}

		} // SAXHandler

		XMLReader parser;
		try {
			parser = XMLReaderFactory.createXMLReader();
			SAXHandler handler = new SAXHandler();
			parser.setContentHandler(handler);
			parser.setErrorHandler(new TransfersErrorHandler());

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
	 * @see pl.pronux.sokker.downloader.xml.parsers.TransferXmlParserInterface#getAlTransfers()
	 */
	public ArrayList<Transfer> getAlTransfers() {
		return alTransfers;
	}
}

class TransfersErrorHandler implements ErrorHandler {
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
