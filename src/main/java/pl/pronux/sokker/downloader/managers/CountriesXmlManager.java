package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.CountriesManager;
import pl.pronux.sokker.actions.ConfigurationManager;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.CountriesXmlParser;
import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.model.Date;

public class CountriesXmlManager extends XmlManager<Country> {

	private ArrayList<Country> alCountries;
	
	private CountriesManager countriesManager = CountriesManager.instance();

	public CountriesXmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, downloader, currentDay);
	}

	public CountriesXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("countries", destination, downloader, currentDay); //$NON-NLS-1$
	}

	public CountriesXmlManager(String content, Date currentDay, int teamID) {
		super(content, currentDay, teamID);
	}
	
	@Override
	public void download() throws IOException {
		setContent(downloader.getCountries());

	}

	@Override
	public void importToSQL() throws SQLException {
		countriesManager.importCountries(this.alCountries);
	}

	public List<Country> parseXML() throws SAXException {
		return parseXML(getContent());
	}

	public void updateDbCountries(boolean b) throws SQLException {
		new ConfigurationManager().updateDbCountry(b);
	}

	@Override
	public List<Country> parseXML(String xml) throws SAXException {
		CountriesXmlParser countriesXmlParser = new CountriesXmlParser();
		InputSource input = new InputSource(new StringReader(xml));

		try {
			countriesXmlParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			countriesXmlParser.parseXmlSax(input, null);
		}
		this.alCountries = countriesXmlParser.getAlCountries();
		return alCountries;
	}

}
