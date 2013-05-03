package pl.pronux.sokker.model;

import java.io.Serializable;

public class PlayerSkills implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7253914177113200851L;

	private int experience;

	private int teamwork;

	private int discipline;

	private int cards;

	private int goals;

	private int matches;

	private int assists;

	private double injurydays;

	private int playerId;

	private int id;

	private Date date;

	private byte age;

	private Money value;

	private Money salary;

	private byte form;

	private byte stamina;

	private byte pace;

	private byte technique;

	private byte passing;

	private byte keeper;

	private byte defender;

	private byte playmaker;

	private byte scorer;

	private int trainingId;
	
	private boolean passTraining = true;

	private Training training;

	private int summarySkill;

	public int[] getStatsTable() {
		int[] intTable = {
				value.toInt(),
				salary.toInt(),
				age,
				form,
				stamina,
				pace,
				technique,
				passing,
				keeper,
				defender,
				playmaker,
				scorer,
				discipline,
				experience,
				teamwork
		};
		return intTable;
	}

	public void setSummarySkill() {
		summarySkill = pace + technique + passing + keeper + defender + playmaker + scorer;
	}

	public int getSummarySkill() {
		return summarySkill;
	}


	public byte getAge() {
		return age;
	}

	public void setAge(byte age) {
		this.age = age;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public byte getDefender() {
		return defender;
	}

	public void setDefender(byte defender) {
		this.defender = defender;
	}

	public byte getForm() {
		return form;
	}

	public void setForm(byte form) {
		this.form = form;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public byte getKeeper() {
		return keeper;
	}

	public void setKeeper(byte keeper) {
		this.keeper = keeper;
	}

	public byte getPace() {
		return pace;
	}

	public void setPace(byte pace) {
		this.pace = pace;
	}

	public byte getPassing() {
		return passing;
	}

	public void setPassing(byte passing) {
		this.passing = passing;
	}

	public byte getPlaymaker() {
		return playmaker;
	}

	public void setPlaymaker(byte playmaker) {
		this.playmaker = playmaker;
	}

	public byte getStamina() {
		return stamina;
	}

	public void setStamina(byte stamina) {
		this.stamina = stamina;
	}

	public byte getScorer() {
		return scorer;
	}

	public void setScorer(byte scorer) {
		this.scorer = scorer;
	}

	public byte getTechnique() {
		return technique;
	}

	public void setTechnique(byte technique) {
		this.technique = technique;
	}

	public Money getValue() {
		return value;
	}

	public void setValue(Money value) {
		this.value = value;
	}

	public Money getSalary() {
		return salary;
	}

	public void setSalary(Money salary) {
		this.salary = salary;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getCards() {
		return cards;
	}

	public void setCards(int cards) {
		this.cards = cards;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}

	public double getInjurydays() {
		return injurydays;
	}

	public void setInjurydays(double injurydays) {
		this.injurydays = injurydays;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDiscipline() {
		return discipline;
	}

	public void setDiscipline(int discipline) {
		this.discipline = discipline;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getTeamwork() {
		return teamwork;
	}

	public void setTeamwork(int teamwork) {
		this.teamwork = teamwork;
	}

	public Training getTraining() {
		return training;
	}

	public void setTraining(Training training) {
		this.training = training;
	}

	public int getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(int trainingId) {
		this.trainingId = trainingId;
	}

	public boolean isPassTraining() {
		return passTraining;
	}

	public void setPassTraining(boolean passTraining) {
		this.passTraining = passTraining;
	}

}
