package pl.pronux.sokker.importer.controller.filters.sokkerviewer;

import java.io.File;
import java.io.FileFilter;

public class XMLOldFormatFilter implements FileFilter {

	public boolean accept(File file) {
		if (file.isDirectory() || (file.getName().matches("^[0-9]+.xml$") && file.getName().endsWith(".xml")) ) { //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		} else {
			return false;
		}
	}

}
