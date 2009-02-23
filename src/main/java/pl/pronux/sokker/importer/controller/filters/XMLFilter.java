package pl.pronux.sokker.importer.controller.filters;

import java.io.File;
import java.io.FileFilter;

public class XMLFilter implements FileFilter {

	private String name;

	public XMLFilter(String name) {
		this.name = name;
	}

	public boolean accept(File file) {
		if (file != null && (file.isDirectory() || (file.getName().endsWith(".xml") && file.getName().matches(name)))) { //$NON-NLS-1$
			return true;
		} else {
			return false;
		}
	}

}
