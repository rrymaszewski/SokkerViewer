package pl.pronux.sokker.importer.controller.filters;

import java.io.File;
import java.io.FileFilter;

public class XMLFilter implements FileFilter {

	private String name;

	public XMLFilter(String name) {
		this.name = name;
	}

	public boolean accept(File file) {
		return file != null && (file.isDirectory() || (file.getName().endsWith(".xml") && file.getName().matches(name)));
	}

}
