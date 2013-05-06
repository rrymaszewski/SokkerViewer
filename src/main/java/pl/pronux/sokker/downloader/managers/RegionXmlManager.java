package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.CountriesManager;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.RegionXmlParser;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Region;

public class RegionXmlManager extends XmlManager<Region> {

	private Region region;
	private List<Region> regions = new ArrayList<Region>();

	private CountriesManager countriesManager = CountriesManager.getInstance();

	public RegionXmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, downloader, currentDay);
	}

	public RegionXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("region", destination, downloader, currentDay); 
	}

	public RegionXmlManager(String content, Date currentDay, int teamID) {
		super(content, currentDay, teamID);
	}

	@Override
	public void download() throws IOException {
		// TODO Auto-generated method stub

	}

	public void download(int regionId) throws IOException {
		setContent(getDownloader().getRegion(String.valueOf(regionId)));
	}

	@Override
	public void importToSQL() throws SQLException {
		countriesManager.importRegion(this.region);
	}

	public List<Region> parseXML() throws SAXException {
		return parseXML(getContent());
	}

	@Override
	public List<Region> parseXML(String xml) throws SAXException {
		RegionXmlParser regionXMLParser = new RegionXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			regionXMLParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			regionXMLParser.parseXmlSax(input, null);
		}
		this.region = regionXMLParser.getRegion();

		regions.add(region);

		return regions;
	}

}
