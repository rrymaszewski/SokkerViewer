package pl.pronux.sokker.importer.controller.filters.sokkerorganizer;

import java.io.File;
import java.io.FileFilter;

public class XMLDateFilter implements FileFilter {

	private int teamID;

	public XMLDateFilter(int teamID) {
		this.teamID = teamID;
	}

	public boolean accept(File file) {
		return file.isDirectory() || file.getName().matches(".*_team-" + teamID + "\\.xml$"); 
	}

}
