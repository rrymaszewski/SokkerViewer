package pl.pronux.sokker.model;

import java.util.ArrayList;

public class Arena {
	
	ArrayList<ClubArenaName> alArenaName;

	ArrayList<Stand> stands;

	public ArrayList<Stand> getStands() {
		return stands;
	}

	public void setStands(ArrayList<Stand> stands) {
		this.stands = stands;
	}

	public ArrayList<ClubArenaName> getAlArenaName() {
		return alArenaName;
	}

	public void setAlArenaName(ArrayList<ClubArenaName> alArenaName) {
		this.alArenaName = alArenaName;
	}

}
