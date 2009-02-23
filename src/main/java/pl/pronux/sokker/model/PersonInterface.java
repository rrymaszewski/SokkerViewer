package pl.pronux.sokker.model;

public interface PersonInterface {

	public String getClubName();

	public void setClubName(String clubName);

	public int getId();

	public String getName();

	public void setId(int id);

	public void setName(String name);

	public String getSurname();

	public void setSurname(String surname);

	public int getStatus();

	public void setStatus(int status);
	
	public void moveToTrash();
	
	public void restoreFromTrash();
	
	public void removeFromTrash();
	
}