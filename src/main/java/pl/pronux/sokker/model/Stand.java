package pl.pronux.sokker.model;

public class Stand {
	public Stand() {
	}
	
	public Stand(int location, int type, int size, int isRoof, Double constuctionDays) {
		this.location = location;
		this.type = type;
		this.size = size;
		this.isRoof = isRoof;
		this.constructionDays = constuctionDays;
	}
	
	public static final int N = 1;
	public static final int S = 2;
	public static final int W = 3;
	public static final int E = 4;
	public static final int NW = 5;
	public static final int NE = 6;
	public static final int SW = 7;
	public static final int SE = 8;
	
	public static final int TYPE_STANDING = 0;
	public static final int TYPE_TERRACES = 1;
	public static final int TYPE_BENCHES = 2;
	public static final int TYPE_SEATS = 3;
	
	public static final int ROOF_TRUE = 1;
	public static final int ROOF_FALSE = 0;
	
	private int location;

	private int type;

	private int size;

	private int isRoof;

	private Double constructionDays;

	public Double getConstructionDays() {
		return constructionDays;
	}

	public void setConstructionDays(Double constructionDays) {
		this.constructionDays = constructionDays;
	}

	public int getIsRoof() {
		return isRoof;
	}

	public void setIsRoof(int isRoof) {
		this.isRoof = isRoof;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
