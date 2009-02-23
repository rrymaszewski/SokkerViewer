package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.GalleryImage;

public class GalleryImageDto extends GalleryImage {
	private ResultSet rs;

	public GalleryImageDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public GalleryImage getGalleryImage() throws SQLException {
		this.setFile(rs.getString("filename")); //$NON-NLS-1$
		this.setName(this.getFile());
		this.setUserID("0"); //$NON-NLS-1$
		this.setPublicationDate(new Date(rs.getLong("upload_date"))); //$NON-NLS-1$
		this.setType(GalleryImage.IMPORT);
		this.setPayment(GalleryImage.PAYMENT_UNKNOWN);
		return this;
	}
}
