package pl.pronux.sokker.model;

import java.math.BigDecimal;

public class Stand {
	public static final int N = 1;
	public static final int S = 2;
	public static final int W = 3;
	public static final int E = 4;
	public static final int NW = 5;
	public static final int NE = 6;
	public static final int SW = 7;
	public static final int SE = 8;
	
	public static final int FIELD = 10;
	
	public static final int TYPE_STANDING = 0;
	public static final int TYPE_TERRACES = 1;
	public static final int TYPE_BENCHES = 2;
	public static final int TYPE_SEATS = 3;
	
	public static final int ROOF_TRUE = 1;
	public static final int ROOF_FALSE = 0;
	
	public static final int COST_PROJECT = 100000;
	public static final int COST_ROOF = 400;
	public static final int COST_SEATS = 180;
	public static final int COST_BENCHES = 50;
	public static final int COST_DISSASEMBLY_ROOF = 20;
	public static final int COST_DISSASEMBLY_SEATS = 10;
	public static final int COST_DISSASEMBLY_BENCHES = 10;
	public static final int COST_DISSASEMBLY_FASTENING = 10;
	public static final int COST_FASTENING = 100;
	public static final int COST_TERRACES = 100;
	
	public static final int PLACE_MAINTENANCE = 3;
	
	public static final int SEATS_WITHOUT_ROOF_ALONG_LINE_EARNS = 30;
	public static final int SEATS_WITHOUT_ROOF_BEHIND_GOAL_EARNS = 25;
	public static final int BENCHES_WITHOUT_ROOF_EARNS = 25;
	public static final int TERRACES_WITHOUT_ROOF_EARNS = 16;
	public static final int STANDING_EARNS = 8;
	public static final int SEATS_WITH_ROOF_ALONG_LINE_EARNS = 35;
	public static final int SEATS_WITH_ROOF_BEHIND_GOAL_EARNS = 30;
	public static final int BENCHES_WITH_ROOF_ALONG_LINE_EARNS = 30;
	public static final int BENCHES_WITH_ROOF_BEHIND_GOAL_EARNS = 25;
	public static final int TERRACES_WITH_ROOF_EARNS = 20;
	
	private int location;

	private int type;

	private int capacity;

	private int isRoof;

	private Double constructionDays;

	public Stand() {
	}
	
	public Stand(int location, int type, int size, int isRoof, Double constuctionDays) {
		this.location = location;
		this.type = type;
		this.capacity = size;
		this.isRoof = isRoof;
		this.constructionDays = constuctionDays;
	}
	
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

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int size) {
		this.capacity = size;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getIncome() {
		int income = 0;
		if (this.getIsRoof() == Stand.ROOF_TRUE) {
			switch (this.getType()) {
			case Stand.TYPE_TERRACES:
				income += this.getCapacity() * Stand.TERRACES_WITH_ROOF_EARNS;
				break;
			case Stand.TYPE_BENCHES:
				if (this.getLocation() == Stand.N || this.getLocation() == Stand.S) {
					income += this.getCapacity() * Stand.BENCHES_WITH_ROOF_ALONG_LINE_EARNS;
				} else {
					income += this.getCapacity() * Stand.BENCHES_WITH_ROOF_BEHIND_GOAL_EARNS;
				}
				break;
			case Stand.TYPE_SEATS:
				if (this.getLocation() == Stand.N || this.getLocation() == Stand.S) {
					income += this.getCapacity() * Stand.SEATS_WITH_ROOF_ALONG_LINE_EARNS;
				} else {
					income += this.getCapacity() * Stand.SEATS_WITH_ROOF_BEHIND_GOAL_EARNS;
				}
				break;
			}
		} else {
			switch (this.getType()) {
			case Stand.TYPE_STANDING:
				income += this.getCapacity() * Stand.STANDING_EARNS;
				break;
			case Stand.TYPE_TERRACES:
				income += this.getCapacity() * Stand.TERRACES_WITHOUT_ROOF_EARNS;
				break;
			case Stand.TYPE_BENCHES:
				income += this.getCapacity() * Stand.BENCHES_WITHOUT_ROOF_EARNS;
				break;
			case Stand.TYPE_SEATS:
				if (this.getLocation() == Stand.N || this.getLocation() == Stand.S) {
					income += this.getCapacity() * Stand.SEATS_WITHOUT_ROOF_ALONG_LINE_EARNS;
				} else {
					income += this.getCapacity() * Stand.SEATS_WITHOUT_ROOF_BEHIND_GOAL_EARNS;
				}
				break;
			}
		}
		return income;
	}
	
	public int getCostOfTerracesRebuild(int newSize) {
		if(this.getType() == Stand.TYPE_STANDING) {
			return Double.valueOf((Stand.COST_TERRACES * newSize + 0.005 * newSize * newSize)).intValue();	
		} else {
			if(newSize > this.getCapacity()) {
				return Double.valueOf((Stand.COST_TERRACES * newSize + 0.005 * newSize * newSize) - (Stand.COST_TERRACES * this.getCapacity() + 0.005 * this.getCapacity() * this.getCapacity())).intValue();
			} else {
				return new BigDecimal(((Stand.COST_TERRACES * this.getCapacity() + 0.005 * this.getCapacity() * this.getCapacity()) - (Stand.COST_TERRACES * newSize + 0.005 * newSize * newSize)) / 5.0).setScale(0, BigDecimal.ROUND_UP).intValue();	
			}
		}
	}
	
	public int getCost() {
		if(this.getType() != Stand.TYPE_STANDING) {
			return this.getCapacity() * Stand.PLACE_MAINTENANCE;
		}
		return 0;
	}
}
