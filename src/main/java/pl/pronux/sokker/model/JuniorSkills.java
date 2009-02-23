package pl.pronux.sokker.model;

public class JuniorSkills {

	Date date;
	
	int id;

	int idJuniorFK;

	int skill;

	int weeks;

	Training training;

	public Date getDate() {
		return date;
	}

	public int getIdJuniorFK() {
		return idJuniorFK;
	}

	public int getSkill() {
		return skill;
	}

	public int getWeeks() {
		return weeks;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setIdJuniorFK(int id_junior_fk) {
		this.idJuniorFK = id_junior_fk;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public void setWeeks(int weeks) {
		this.weeks = weeks;
	}

	public Training getTraining() {
		return training;
	}

	public void setTraining(Training training) {
		this.training = training;
	}
	
	public Coach getTrainer() {
		if(training != null) {
			return training.getJuniorCoach();
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
