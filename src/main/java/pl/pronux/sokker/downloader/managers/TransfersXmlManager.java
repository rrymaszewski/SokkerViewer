package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.TeamManager;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.TransfersXmlParser;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Transfer;

public class TransfersXmlManager extends XmlManager<Transfer> {
	private TransfersXmlParser transfersXmlParser = null;

	private List<Transfer> alTransfers = new ArrayList<Transfer>();

	public TransfersXmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, downloader, currentDay);
	}

	public TransfersXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("transfers", destination, downloader, currentDay); //$NON-NLS-1$
	}
	
	public TransfersXmlManager(String content, Date currentDay, int teamID) {
		super(content, currentDay, teamID);
	}

	@Override
	public void download() throws IOException {
		setContent(downloader.getTransfers());
	}

	@Override
	public void importToSQL() throws SQLException {
		new TeamManager().importTransfers(this.alTransfers);
	}

	public List<Transfer> parseXML() throws SAXException {
		return parseXML(getContent());
	}

	@Override
	public List<Transfer> parseXML(String xml) throws SAXException {
		transfersXmlParser = new TransfersXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			transfersXmlParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			transfersXmlParser.parseXmlSax(input, null);
		}
		this.alTransfers = transfersXmlParser.getAlTransfers();
		return alTransfers;
	}

}
