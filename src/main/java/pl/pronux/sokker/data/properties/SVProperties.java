package pl.pronux.sokker.data.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SVProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2930553456447297635L;

	private String filename;
	
	public synchronized void loadFile(String filename) throws IOException {
		this.filename = filename;
		this.load(new FileInputStream(filename));
	}

	public synchronized void synchronize() throws IOException {
		if(filename != null) {
			this.store(new FileOutputStream(new File(filename)), "");	 
		}
	}
	
	public synchronized void loadFile(File file) throws IOException {
		this.filename = file.getAbsolutePath();
		this.load(new FileInputStream(file));
	}
	
}
