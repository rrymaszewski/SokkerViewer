package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.PlayerSkills;

public class PlayerSkillsDto extends PlayerSkills {
/**
	 * 
	 */
	private static final long serialVersionUID = 5822858020573099194L;
private ResultSet rs;

public PlayerSkillsDto(ResultSet rs) {
	this.rs = rs;
}

public PlayerSkills getPlayerSkills() throws SQLException {
	this.setId(rs.getInt("id_skill")); 
	this.setPlayerId(rs.getInt("id_player_fk")); 
	this.setDate(new Date(rs.getLong("millis"))); 
	this.setAge(rs.getByte("age")); 
	this.setValue(new Money(rs.getInt("value"))); 
	this.setSalary(new Money(rs.getInt("salary"))); 
	this.setForm(rs.getByte("form")); 
	this.setStamina(rs.getByte("stamina")); 
	this.setPace(rs.getByte("pace")); 
	this.setTechnique(rs.getByte("technique")); 
	this.setPassing(rs.getByte("passing")); 
	this.setKeeper(rs.getByte("keeper")); 
	this.setDefender(rs.getByte("defender")); 
	this.setPlaymaker(rs.getByte("playmaker")); 
	this.setScorer(rs.getByte("scorer")); 
	this.setMatches(rs.getInt("matches")); 
	this.setGoals(rs.getInt("goals")); 
	this.setAssists(rs.getInt("assists")); 
	this.setCards(rs.getInt("cards")); 
	this.setInjurydays(rs.getDouble("injurydays")); 
	this.setTrainingId(rs.getInt("id_training_fk")); 
	this.setExperience(rs.getInt("experience")); 
	this.setTeamwork(rs.getInt("teamwork")); 
	this.setDiscipline(rs.getInt("discipline")); 
	this.getDate().getSokkerDate().setDay(rs.getInt("day")); 
	this.getDate().getSokkerDate().setWeek(rs.getInt("week")); 
	this.setPassTraining(rs.getBoolean("pass_training")); 
	this.setWeight(rs.getDouble("weight"));
	this.setBmi(rs.getDouble("bmi"));
	this.setSummarySkill();
	return this;
}
}
