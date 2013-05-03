package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Money;

public class CoachDto extends Coach {
	/**
	 * 
	 */
	private static final long serialVersionUID = -88488220819871622L;
	private ResultSet rs;

	public CoachDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public Coach getCoach() throws SQLException {
		this.setId(rs.getInt("id_coach")); 
		this.setName(rs.getString("name")); 
		this.setSurname(rs.getString("surname")); 
		this.setJob(rs.getInt("job")); 
		this.setSigned(rs.getByte("signed")); 
		this.setCountryfrom(rs.getInt("countryfrom")); 
		this.setAge(rs.getByte("age")); 
		this.setSalary(new Money(rs.getInt("salary"))); 
		this.setGeneralskill(rs.getByte("generalskill")); 
		this.setStamina(rs.getByte("stamina")); 
		this.setPace(rs.getByte("pace")); 
		this.setTechnique(rs.getByte("technique")); 
		this.setPassing(rs.getByte("passing")); 
		this.setKeepers(rs.getByte("keepers")); 
		this.setDefenders(rs.getByte("defenders")); 
		this.setPlaymakers(rs.getByte("playmakers")); 
		this.setScorers(rs.getByte("scorers")); 
		this.setStatus(rs.getInt("status")); 
		this.setNote(rs.getString("note")); 
		this.setSummarySkill();
		return this;
	}
}
