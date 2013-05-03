package pl.pronux.sokker.importer.model;

import java.io.File;

public class XMLfile implements Comparable<XMLfile> {

	private String fileName;
	private long timeInMilis;

	public int compareTo(XMLfile xmlFile) {
		if (this.timeInMilis < xmlFile.timeInMilis) {
			return -1;
		} else if (this.timeInMilis > xmlFile.timeInMilis) {
			return 1;
		}
		return 0;
	}

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
		if (file.getName().matches("[0-9]+\\.xml")) { 
			this.timeInMilis = Long
					.valueOf(file.getName().split("\\.")[0]).longValue(); 
		} else {
			this.timeInMilis = file.lastModified();
		}

		this.fileName = file.getPath();
	}
}
