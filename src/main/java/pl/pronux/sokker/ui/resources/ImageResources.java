package pl.pronux.sokker.ui.resources;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import org.eclipse.swt.graphics.Image;

import pl.pronux.sokker.ui.handlers.DisplayHandler;

final public class ImageResources {
	private static final HashMap<String, Image> cache = new HashMap<String, Image>();

	private static final String IMAGE_PATH = "/images/"; //$NON-NLS-1$

	static {
		DisplayHandler.getDisplay().disposeExec(new Runnable() {
			public void run() {
				if (!cache.isEmpty()) {
					for (Image image : cache.values()) {
						if(image != null) {
							if(!image.isDisposed()) {
								image.dispose();
							}
						}
					}
				}
			}
		});
	}
	
	private ImageResources() {
	}

	public static Image getImageResources(String filename) {
		Image image = cache.get(filename);
		if(image == null || image.isDisposed()) {
			image = loadImage(filename);
			cache.put(filename, image);
		}
		return image;
	}
	
	public static Image getImageFile(String file) {
		Image image = cache.get(file);
		if(image == null || image.isDisposed()) {
			image = loadImageFile(file);
			cache.put(file, image);
		}
		return image;
		
	}
	
	private static Image loadImageFile(String file) {
		Image image = null;
		if(file!=null) {
			if(new File(file).exists()) {
				image = new Image(DisplayHandler.getDisplay(), file);	
			}
		}
		return image;
	}

	private static Image loadImage(String filename) {
		Image image = null;
		InputStream is = ImageResources.class.getResourceAsStream(IMAGE_PATH + filename);
		if (is != null) {
			image = new Image(DisplayHandler.getDisplay(), is);
		}
		return image;
	}

}
