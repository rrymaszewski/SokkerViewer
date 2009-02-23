package pl.pronux.sokker.importer.controller.filters.sokkerorganizer;

import java.io.File;
import java.io.FileFilter;

public class XMLDateFilter implements FileFilter {

	private int teamID;

	public XMLDateFilter(int teamID) {
		this.teamID = teamID;
	}

	public boolean accept(File file) {
		if (file.isDirectory() || (file.getName().matches(".*_team-" + teamID + "\\.xml$") && file.getName().endsWith(".xml"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
			return true;
		} else {
			return false;
		}
	}

}
