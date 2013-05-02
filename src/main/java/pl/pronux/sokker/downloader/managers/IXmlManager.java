package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.xml.sax.SAXException;

public interface IXmlManager<T> {

	void download() throws IOException, SQLException;

	void importToSQL() throws SQLException;

	List<T> parseXML(String xml) throws SAXException;
	
	List<T> parseXML() throws SAXException;

}