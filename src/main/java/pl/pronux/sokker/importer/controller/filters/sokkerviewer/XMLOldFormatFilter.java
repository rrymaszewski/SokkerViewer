package pl.pronux.sokker.importer.controller.filters.sokkerviewer;

import java.io.File;
import java.io.FileFilter;

public class XMLOldFormatFilter implements FileFilter {

	public boolean accept(File file) {
		return file.isDirectory() || file.getName().matches("^[0-9]+.xml$");
	}

}
