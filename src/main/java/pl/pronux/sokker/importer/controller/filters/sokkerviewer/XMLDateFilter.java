package pl.pronux.sokker.importer.controller.filters.sokkerviewer;

import java.io.File;
import java.io.FileFilter;

public class XMLDateFilter implements FileFilter {

	private int teamID;

	public XMLDateFilter(int teamID) {
		this.teamID = teamID;
	}

	public boolean accept(File file) {
		if (file.isDirectory() || (file.getName().matches("teams_" + teamID + "_.*") && file.getName().endsWith(".xml")) || (file.getName().matches("team_" + teamID + "_.*") && file.getName().endsWith(".xml"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			return true;
		} else {
			return false;
		}
	}

}
