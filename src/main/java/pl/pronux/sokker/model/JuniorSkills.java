package pl.pronux.sokker.model;

public class JuniorSkills {

	private Date date;
	
	private int id;

	private int juniorId;

	private int skill;

	private int weeks;

	private Training training;
	
	private int age = 0;

	public Date getDate() {
		return date;
	}

	public int getJuniorId() {
		return juniorId;
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

	public void setJuniorId(int juniorId) {
		this.juniorId = juniorId;
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

	public void setAge(int age) {
		this.age = age;
	}

	public int getAge() {
		return age;
	}

}
