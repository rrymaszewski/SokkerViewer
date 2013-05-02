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
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Transfer;
import pl.pronux.sokker.utils.Log;

public class TransfersXmlParser {

	static int current_tag = 0;

//	private static final int TAG_transfers = 1;

	private static final int TAG_TRANSFER = 2;

	private static final int TAG_ID = 3;

	private static final int TAG_SELLER_TEAM_ID = 4;

	private static final int TAG_BUYER_TEAM_ID = 5;

	private static final int TAG_SELLER_TEAM_NAME = 6;

	private static final int TAG_BUYER_TEAM_NAME = 7;

	private static final int TAG_PLAYER_ID = 8;

	private static final int TAG_DATE = 9;

	private static final int TAG_PRICE = 10;

	private static final int TAG_PLAYER_VALUE = 11;

//	private static final int TAG_WARNING = 12;

	static int TAG_switch = 0;

	private List<Transfer> transfers;

	private int teamID;

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
				case TAG_SELLER_TEAM_ID:
					transfer.setSellerTeamID(Integer.valueOf(message.toString()));
					break;
				case TAG_BUYER_TEAM_ID:
						transfer.setBuyerTeamID(Integer.valueOf(message.toString()));
						break;
				case TAG_SELLER_TEAM_NAME:
						transfer.setSellerTeamName(message.toString());
					break;
				case TAG_BUYER_TEAM_NAME:
					transfer.setBuyerTeamName(message.toString());
				break;
				case TAG_PLAYER_ID:
					transfer.setPlayerID(Integer.valueOf(message.toString()));
					break;
				case TAG_PRICE:
					transfer.setPrice(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_PLAYER_VALUE:
					transfer.setPlayerValue(new Money(Integer.valueOf(message.toString())));
					break;
				case TAG_DATE:
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
						transfers.add(transfer);
					}
				}
			}

			public void startDocument() {
				transfers = new ArrayList<Transfer>();
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
					TAG_switch = TAG_TRANSFER;
					transfer = new Transfer();
					transfer.setTransferID(-1);
					transfer.setBuyerTeamName(""); //$NON-NLS-1$
					transfer.setBuyerTeamID(0);
					transfer.setSellerTeamID(0);
					transfer.setSellerTeamName(""); //$NON-NLS-1$
				}

				if (TAG_switch == TAG_TRANSFER) {
					if (localName.equals("ID")) { //$NON-NLS-1$
						current_tag = TAG_ID;
					} else if (localName.equalsIgnoreCase("sellerTeamID")) { //$NON-NLS-1$
						current_tag = TAG_SELLER_TEAM_ID;
					} else if (localName.equalsIgnoreCase("buyerTeamID")) { //$NON-NLS-1$
						current_tag = TAG_BUYER_TEAM_ID;
					} else if (localName.equalsIgnoreCase("sellerTeamName")) { //$NON-NLS-1$
						current_tag = TAG_SELLER_TEAM_NAME;
					} else if (localName.equals("buyerTeamName")) { //$NON-NLS-1$
						current_tag = TAG_BUYER_TEAM_NAME;
					} else if (localName.equals("playerID")) { //$NON-NLS-1$
						current_tag = TAG_PLAYER_ID;
					} else if (localName.equals("date")) { //$NON-NLS-1$
						current_tag = TAG_DATE;
					} else if (localName.equals("price")) { //$NON-NLS-1$
						current_tag = TAG_PRICE;
					} else if (localName.equalsIgnoreCase("playerValue")) { //$NON-NLS-1$
						current_tag = TAG_PLAYER_VALUE;
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
	public List<Transfer> getTransfers() {
		return transfers;
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
