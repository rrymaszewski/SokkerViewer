package pl.pronux.sokker.importer.model;

import java.io.File;

public class XMLfile implements Comparable<Object> {
	public int compareTo(Object o) {
		if (this.timeInMilis < ((XMLfile) o).timeInMilis) {
			return -1;
		} else if (this.timeInMilis> ((XMLfile) o).timeInMilis) {
			return 1;
		}
		return 0;
	}
	String fileName;
	long timeInMilis;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getTimeInMilis() {
		return timeInMilis;
	}

	public void setTimeInMilis(long timeInMilis) {
		this.timeInMilis = timeInMilis;
	}

	public XMLfile(File file) {
		if(file.getName().matches("[0-9]+\\.xml")) { //$NON-NLS-1$
			this.timeInMilis = Long.valueOf(file.getName().split("\\.")[0]).longValue(); //$NON-NLS-1$
		} else {
			this.timeInMilis = file.lastModified();
		}

		this.fileName = file.getPath();
	}
}
