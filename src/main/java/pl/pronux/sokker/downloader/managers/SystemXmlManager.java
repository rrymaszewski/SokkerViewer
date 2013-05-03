package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.ConfigurationManager;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.model.Date;

public class SystemXmlManager extends XmlManager<Object> {
	
	private ConfigurationManager databaseConfigurationManager = ConfigurationManager.getInstance();
	
	public SystemXmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, downloader, currentDay);
	}

	public SystemXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("system", destination, downloader, currentDay); 
	}
	
	@Override
	public void download() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void importToSQL() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Object> parseXML(String xml) throws SAXException {
		// TODO Auto-generated method stub
		return null;
		
	}

	public List<Object> parseXML() throws SAXException {
		// TODO Auto-generated method stub
		return parseXML(null);
	}

	public void updateDbDate(Date date) throws SQLException {
		databaseConfigurationManager.updateDbDate(date);
	}

	public void updateDbUpdate(boolean b) throws SQLException {
		databaseConfigurationManager.updateDbUpdate(b);
	}
	
	public void updateDbTeamId(int teamId) throws SQLException {
		databaseConfigurationManager.updateDbTeamId(teamId);
	}

}
