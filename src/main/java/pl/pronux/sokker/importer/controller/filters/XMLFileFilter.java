package pl.pronux.sokker.importer.controller.filters;

import java.io.File;
import java.io.FileFilter;

public class XMLFileFilter implements FileFilter {

	private String name;

	public XMLFileFilter(String name) {
		this.name = name;
	}

	public boolean accept(File file) {
		if (file != null && (file.isDirectory() || (file.getName().endsWith(".xml") && file.getName().contains(name)))) { //$NON-NLS-1$
			return true;
		} else {
			return false;
		}
	}

}
