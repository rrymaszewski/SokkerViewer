package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.xml.sax.SAXException;

import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.utils.file.OperationOnFile;

public abstract class XmlManager<T> extends XmlManagerUtils implements IXmlManager<T> {

	private String content;

	protected XMLDownloader downloader;
	
	protected int teamID;

	public XmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, currentDay);
		this.downloader = downloader;
		this.teamID = Integer.valueOf(downloader.getTeamID());
		OperationOnFile.createDirectory(destination);
	}
	
	public XmlManager(String content, Date currentDay, int teamID) {
		super(currentDay);
		this.content = content;
		this.teamID = teamID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.downloader.managers.IXmlManager#download(pl.pronux.sokker.downloader.XMLDownloader)
	 */
	public abstract void download() throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.downloader.managers.IXmlManager#importToSQL()
	 */
	public abstract void importToSQL() throws SQLException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.downloader.managers.IXmlManager#parseXML()
	 */
	public abstract List<T> parseXML(String xml) throws SAXException;
	
	public abstract List<T> parseXML() throws SAXException;
	
	public boolean write() throws IOException {
		return write(getContent(), downloader.getTeamID());
	}
	
	protected String getContent() {
		return content;
	}

	protected void setContent(String content) {
		this.content = content;
	}
	
}
