package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.pronux.sokker.data.sql.dto.GalleryImageDto;
import pl.pronux.sokker.model.GalleryImage;

public class GalleryDao {
	private Connection connection;

	public GalleryDao(Connection connection) {
		this.connection = connection;
	}
	
	public void addGalleryImage(GalleryImage galleryImage) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("INSERT INTO gallery(filename,upload_date) VALUES (?,?)"); //$NON-NLS-1$
		pstm.setString(1, galleryImage.getFile());
		pstm.setLong(2, galleryImage.getPublicationDate().getMillis());
		pstm.executeUpdate();
		pstm.close();

	}

	public List<GalleryImage> getGalleryImages() throws SQLException {
		GalleryImage galleryImage;
		List<GalleryImage> galleryImages = new ArrayList<GalleryImage>();
		PreparedStatement ps;

		ps = connection.prepareStatement("SELECT image_id, filename, upload_date FROM gallery"); //$NON-NLS-1$
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			galleryImage = new GalleryImageDto(rs).getGalleryImage();
			galleryImages.add(galleryImage);
		}
		rs.close();
		ps.close();
		return galleryImages;
	}
}
