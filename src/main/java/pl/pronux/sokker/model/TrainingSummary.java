package pl.pronux.sokker.model;

public class TrainingSummary {
	private int trainedSkillsPops;
	private int trainedSkillsFalls;
	private int allSkillsPops;
	private int allSkillsFalls;
	private int staminaPops;
	private int staminaFalls;
	private int juniorsPops;
	private int juniorsFalls;

	public int getTrainedSkillsPops() {
		return trainedSkillsPops;
	}

	public void setTrainedSkillsPops(int trainedSkillsPops) {
		this.trainedSkillsPops = trainedSkillsPops;
	}

	public int getTrainedSkillsFalls() {
		return trainedSkillsFalls;
	}

	public void setTrainedSkillsFalls(int trainedSkillsFalls) {
		this.trainedSkillsFalls = trainedSkillsFalls;
	}

	public int getAllSkillsPops() {
		return allSkillsPops;
	}

	public void setAllSkillsPops(int allSkillsPops) {
		this.allSkillsPops = allSkillsPops;
	}

	public int getAllSkillsFalls() {
		return allSkillsFalls;
	}

	public void setAllSkillsFalls(int allSkillsFalls) {
		this.allSkillsFalls = allSkillsFalls;
	}

	public int getOthersSkillsPops() {
		return this.getAllSkillsPops() - this.getTrainedSkillsPops();
	}

	public int getOthersSkillsFalls() {
		return this.getAllSkillsFalls() - this.getTrainedSkillsFalls();
	}

	public int getStaminaPops() {
		return staminaPops;
	}

	public void setStaminaPops(int staminaPops) {
		this.staminaPops = staminaPops;
	}

	public int getStaminaFalls() {
		return staminaFalls;
	}

	public void setStaminaFalls(int staminaFalls) {
		this.staminaFalls = staminaFalls;
	}

	
	public int getJuniorsPops() {
		return juniorsPops;
	}

	
	public void setJuniorsPops(int juniorsPops) {
		this.juniorsPops = juniorsPops;
	}

	
	public int getJuniorsFalls() {
		return juniorsFalls;
	}

	
	public void setJuniorsFalls(int juniorsFalls) {
		this.juniorsFalls = juniorsFalls;
	}
}
