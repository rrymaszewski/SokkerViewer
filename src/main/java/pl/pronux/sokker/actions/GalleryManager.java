package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.GalleryDao;
import pl.pronux.sokker.model.GalleryImage;

public class GalleryManager {

	public List<GalleryImage> getGalleryImages() throws SQLException {
		List<GalleryImage> galleryImages = new ArrayList<GalleryImage>();
	
		boolean newConnection = SQLQuery.connect();
		GalleryDao galleryDao = new GalleryDao(SQLSession.getConnection());
		galleryImages = galleryDao.getGalleryImages();
		SQLQuery.close(newConnection);
		return galleryImages;
	}
	
	public void addGalleryImage(GalleryImage galleryImage) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		GalleryDao galleryDao = new GalleryDao(SQLSession.getConnection());
		galleryDao.addGalleryImage(galleryImage);
		SQLQuery.close(newConnection);
	}
}
