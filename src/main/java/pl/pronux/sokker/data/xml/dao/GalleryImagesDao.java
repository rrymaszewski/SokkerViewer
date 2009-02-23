package pl.pronux.sokker.data.xml.dao;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.data.xml.dto.GalleryXMLParser;
import pl.pronux.sokker.model.GalleryImage;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class GalleryImagesDao {
	public List<GalleryImage> getImages() throws SAXException, IOException {
		List<GalleryImage> images = new ArrayList<GalleryImage>();
		File file = new File(System.getProperty("user.dir") + File.separator + "logos" + File.separator + "images.xml");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if(file.exists()) {
			String xml = OperationOnFile.readFromFile(file);
			GalleryXMLParser parser = new GalleryXMLParser();
			if(xml != null) {
				InputSource input = new InputSource(new StringReader(xml));
					parser.parseXmlSax(input, null);
					Collection<GalleryImage> collection = parser.alImages.values();
					for(GalleryImage image : collection) {
						image.setType(GalleryImage.LOGOMAKER);
						images.add(image);
					}
			}
		}
		return images;
	}
}
