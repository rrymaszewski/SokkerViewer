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

	private XMLDownloader downloader;
	
	private int teamId;

	public XmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, currentDay);
		this.setDownloader(downloader);
		this.setTeamId(Integer.valueOf(downloader.getTeamId()));
		OperationOnFile.createDirectory(destination);
	}
	
	public XmlManager(String content, Date currentDay, int teamId) {
		super(currentDay);
		this.content = content;
		this.setTeamId(teamId);
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
		return write(getContent(), getDownloader().getTeamId());
	}
	
	protected String getContent() {
		return content;
	}

	protected void setContent(String content) {
		this.content = content;
	}

	public XMLDownloader getDownloader() {
		return downloader;
	}

	public void setDownloader(XMLDownloader downloader) {
		this.downloader = downloader;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
}
