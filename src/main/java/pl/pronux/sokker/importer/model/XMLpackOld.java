package pl.pronux.sokker.importer.model;

import java.io.File;

import pl.pronux.sokker.model.Date;

public class XMLpackOld implements IXMLpack {

	private Date date;
	private boolean complete;
	private File file;
	private boolean imported;

	public Date getDate() {
		return date;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;

	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isImported() {
		return imported;
	}

	public void setImported(boolean imported) {
		this.imported = imported;
	}

}
