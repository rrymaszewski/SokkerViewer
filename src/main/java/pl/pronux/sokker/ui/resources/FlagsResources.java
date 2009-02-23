package pl.pronux.sokker.ui.resources;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import pl.pronux.sokker.ui.handlers.DisplayHandler;

final public class FlagsResources {
	private static final Map<Integer, Image> cache = new HashMap<Integer, Image>();

	public final static int EMPTY_FLAG = 500;
	public final static int QUESTION_FLAG = 501;
	private static final String IMAGE_PATH = "/flags/"; //$NON-NLS-1$

	static {
		DisplayHandler.getDisplay().disposeExec(new Runnable() {
			public void run() {
				if (!cache.isEmpty()) {
					for (Image image : cache.values()) {
						if (!image.isDisposed()) {
							image.dispose();
						}
					}
				}
			}
		});
	}

	private FlagsResources() {
	}

	public static Image getFlag(int idCountry) {
		Image image = cache.get(idCountry);
		if (image == null || image.isDisposed()) {
			image = loadImage(idCountry + ".png"); //$NON-NLS-1$
			if (image == null) {
				image = getFlag(QUESTION_FLAG);
			}
			cache.put(idCountry, image);
		}
		return image;
	}

	public static Image getFlagLight(int idCountry) {
		Image image;
		image = cache.get(-idCountry);
		
		if (image == null || image.isDisposed()) {
			image = loadImageLight(idCountry + ".png"); //$NON-NLS-1$
			if (image == null) {
				image = getFlagLight(QUESTION_FLAG);
			}

			cache.put(-idCountry, image);

		}
		return image;
	}

	public static Image getFlagVeryLight(int idCountry) {
		Image image;
			image = cache.get(-idCountry);
		
		if (image == null || image.isDisposed()) {
			image = loadImageVeryLight(idCountry + ".png"); //$NON-NLS-1$
			if (image == null) {
				image = getFlagVeryLight(QUESTION_FLAG);
			}
			cache.put(-idCountry, image);
		}
		return image;
	}

	private static Image loadImageLight(String filename) {
		Image image = null;
		ImageData id = null;
		InputStream is;
		is = FlagsResources.class.getResourceAsStream(IMAGE_PATH + filename);
		if (is != null) {
			id = new ImageData(is);
			if (id != null) {
				for (int y = 0; y < id.height; y++) {
					for (int x = 0; x < id.width; x++) {
						id.setAlpha(x, y, 128);
					}
				}
				image = new Image(DisplayHandler.getDisplay(), id);
			}
		}
		return image;
	}

	private static Image loadImageVeryLight(String filename) {
		Image image = null;
		ImageData id = null;
		InputStream is;
		is = FlagsResources.class.getResourceAsStream(IMAGE_PATH + filename);
		if (is != null) {
			id = new ImageData(is);
			if (id != null) {
				for (int y = 0; y < id.height; y++) {
					for (int x = 0; x < id.width; x++) {
						id.setAlpha(x, y, 64);
					}
				}
				image = new Image(DisplayHandler.getDisplay(), id);
			}
		}
		return image;
	}

	private static Image loadImage(String filename) {
		Image image = null;
		InputStream is;
		is = FlagsResources.class.getResourceAsStream(IMAGE_PATH + filename);
		if (is != null) {
			image = new Image(DisplayHandler.getDisplay(), is);
		}
		return image;
	}

}
