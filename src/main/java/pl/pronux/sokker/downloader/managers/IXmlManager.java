package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.xml.sax.SAXException;

public interface IXmlManager<T> {

	public void download() throws IOException, SQLException;

	public void importToSQL() throws SQLException;

	public List<T> parseXML(String xml) throws SAXException;
	
	public List<T> parseXML() throws SAXException;

}