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
	this.setId(rs.getInt("id_skill")); //$NON-NLS-1$
	this.setIdPlayerFK(rs.getInt("id_player_fk")); //$NON-NLS-1$
	this.setDate(new Date(rs.getLong("millis"))); //$NON-NLS-1$
	this.setAge(rs.getByte("age")); //$NON-NLS-1$
	this.setValue(new Money(rs.getInt("value"))); //$NON-NLS-1$
	this.setSalary(new Money(rs.getInt("salary"))); //$NON-NLS-1$
	this.setForm(rs.getByte("form")); //$NON-NLS-1$
	this.setStamina(rs.getByte("stamina")); //$NON-NLS-1$
	this.setPace(rs.getByte("pace")); //$NON-NLS-1$
	this.setTechnique(rs.getByte("technique")); //$NON-NLS-1$
	this.setPassing(rs.getByte("passing")); //$NON-NLS-1$
	this.setKeeper(rs.getByte("keeper")); //$NON-NLS-1$
	this.setDefender(rs.getByte("defender")); //$NON-NLS-1$
	this.setPlaymaker(rs.getByte("playmaker")); //$NON-NLS-1$
	this.setScorer(rs.getByte("scorer")); //$NON-NLS-1$
	this.setMatches(rs.getInt("matches")); //$NON-NLS-1$
	this.setGoals(rs.getInt("goals")); //$NON-NLS-1$
	this.setAssists(rs.getInt("assists")); //$NON-NLS-1$
	this.setCards(rs.getInt("cards")); //$NON-NLS-1$
	this.setInjurydays(rs.getDouble("injurydays")); //$NON-NLS-1$
	this.setTrainingID(rs.getInt("id_training_fk")); //$NON-NLS-1$
	this.setExperience(rs.getInt("experience")); //$NON-NLS-1$
	this.setTeamwork(rs.getInt("teamwork")); //$NON-NLS-1$
	this.setDiscipline(rs.getInt("discipline")); //$NON-NLS-1$
	this.getDate().getSokkerDate().setDay(rs.getInt("day")); //$NON-NLS-1$
	this.getDate().getSokkerDate().setWeek(rs.getInt("week")); //$NON-NLS-1$
	this.setPassTraining(rs.getBoolean("pass_training")); //$NON-NLS-1$
	this.setSummarySkill();
	return this;
}
}
