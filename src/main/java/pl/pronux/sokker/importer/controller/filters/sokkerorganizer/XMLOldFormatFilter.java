package pl.pronux.sokker.importer.controller.filters.sokkerorganizer;

import java.io.File;
import java.io.FileFilter;

public class XMLOldFormatFilter implements FileFilter {

	public boolean accept(File file) {
		if (file.isDirectory() || (file.getName().matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}-[0-9]{2}.xml$") && file.getName().endsWith(".xml")) ) { //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		} else {
			return false;
		}
	}

}
