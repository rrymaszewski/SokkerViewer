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

	private static int currentTag = 0;

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

	private static int tagSwitch = 0;

	private List<Transfer> transfers;

	private int teamId;

	private Transfer transfer;

	public void parseXmlSax(final InputSource input, final String file) throws SAXException {

		class SAXHandler extends DefaultHandler {
			private StringBuilder message;
			public void characters(char ch[], int start, int length) throws SAXException {

				message.append(new String(ch, start, length));

				switch (currentTag) {
				case TAG_ID:
					transfer.setTransferId(Integer.valueOf(message.toString()));
					break;
				case TAG_SELLER_TEAM_ID:
					transfer.setSellerTeamId(Integer.valueOf(message.toString()));
					break;
				case TAG_BUYER_TEAM_ID:
						transfer.setBuyerTeamId(Integer.valueOf(message.toString()));
						break;
				case TAG_SELLER_TEAM_NAME:
						transfer.setSellerTeamName(message.toString());
					break;
				case TAG_BUYER_TEAM_NAME:
					transfer.setBuyerTeamName(message.toString());
				break;
				case TAG_PLAYER_ID:
					transfer.setPlayerId(Integer.valueOf(message.toString()));
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
				currentTag = 0;
				if (localName.equals("transfer")) { 
					if(transfer.getTransferId() != -1) {
						transfers.add(transfer);
					}
				}
			}

			public void startDocument() {
				transfers = new ArrayList<Transfer>();
			}

			public void startElement(String namespaceURL, String localName, String qName, Attributes atts) {

				message = new StringBuilder();
				if (localName.equals("transfers")) { 
					int length = atts.getLength();
					for (int i = 0; i < length; i++) {
						String name = atts.getQName(i);
						String value = atts.getValue(i);
						if (name.equalsIgnoreCase("teamID")) { 
							setTeamId(Integer.valueOf(value));
						}
					}
				}

				if (localName.equals("transfer")) { 
					tagSwitch = TAG_TRANSFER;
					transfer = new Transfer();
					transfer.setTransferId(-1);
					transfer.setBuyerTeamName(""); 
					transfer.setBuyerTeamId(0);
					transfer.setSellerTeamId(0);
					transfer.setSellerTeamName(""); 
				}

				if (tagSwitch == TAG_TRANSFER) {
					if (localName.equals("ID")) { 
						currentTag = TAG_ID;
					} else if (localName.equalsIgnoreCase("sellerTeamID")) { 
						currentTag = TAG_SELLER_TEAM_ID;
					} else if (localName.equalsIgnoreCase("buyerTeamID")) { 
						currentTag = TAG_BUYER_TEAM_ID;
					} else if (localName.equalsIgnoreCase("sellerTeamName")) { 
						currentTag = TAG_SELLER_TEAM_NAME;
					} else if (localName.equals("buyerTeamName")) { 
						currentTag = TAG_BUYER_TEAM_NAME;
					} else if (localName.equals("playerID")) { 
						currentTag = TAG_PLAYER_ID;
					} else if (localName.equals("date")) { 
						currentTag = TAG_DATE;
					} else if (localName.equals("price")) { 
						currentTag = TAG_PRICE;
					} else if (localName.equalsIgnoreCase("playerValue")) { 
						currentTag = TAG_PLAYER_VALUE;
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
			Log.error("Parser Class", e); 
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

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
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
