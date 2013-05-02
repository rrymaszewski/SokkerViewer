package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.JuniorsManager;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.JuniorXmlParser;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Training;

public class JuniorsXmlManager extends XmlManager<Junior> {

	private JuniorsManager juniorsManager = JuniorsManager.instance();
	
	private List<Junior> juniors;

	public JuniorsXmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, downloader, currentDay);
	}

	public JuniorsXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("juniors", destination, downloader, currentDay); //$NON-NLS-1$
	}

	public JuniorsXmlManager(String content, Date currentDay, int teamID) {
		super(content, currentDay, teamID);
	}
	
	@Override
	public void download() throws IOException {
		setContent(downloader.getJuniors());
	}

	@Override
	public void importToSQL() throws SQLException {
		// TODO Auto-generated method stub
	}

	public void importToSQL(Training training) throws SQLException {
		juniorsManager.addJuniors(this.juniors, training, teamID);
	}

	public List<Junior> parseXML() throws SAXException {
		return parseXML(getContent());
	}

	@Override
	public List<Junior> parseXML(String xml) throws SAXException {
		JuniorXmlParser juniorXmlParser = new JuniorXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			juniorXmlParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			juniorXmlParser.parseXmlSax(input, null);
		}
		this.juniors = juniorXmlParser.getJuniors();
		return juniors;
	}

}
