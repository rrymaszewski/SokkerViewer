package pl.pronux.sokker.model;

import java.io.File;

public class GalleryImage {
	public final static String IDENTIFIER = "galleryimage"; //$NON-NLS-1$
	public final static String LOGOS_PATH = System.getProperty("user.dir") + File.separator + "logos" + File.separator; //$NON-NLS-1$ //$NON-NLS-2$
	public final static int IMPORT = 1;
	public final static int LOGOMAKER = 2;

	public final static int PAYMENT_FREE_ALL = 0;
	public final static int PAYMENT_FREE_PLUS = 1;
	public final static int PAYMENT_MONEY_ALL = 2;
	public final static int PAYMENT_MONEY_PLUS = 3;
	public final static int PAYMENT_UNKNOWN = 4;

	private String userID;
	private String name;
	private int type;
	private int payment;
	private int revision;
	private String file;
	private String signature;
	private Date publicationDate;

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public int getPayment() {
		return payment;
	}

	public void setPayment(int payment) {
		this.payment = payment;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String author) {
		this.userID = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
