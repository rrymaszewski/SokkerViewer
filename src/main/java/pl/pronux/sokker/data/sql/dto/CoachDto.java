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
		this.setId(rs.getInt("id_coach")); //$NON-NLS-1$
		this.setName(rs.getString("name")); //$NON-NLS-1$
		this.setSurname(rs.getString("surname")); //$NON-NLS-1$
		this.setJob(rs.getInt("job")); //$NON-NLS-1$
		this.setSigned(rs.getByte("signed")); //$NON-NLS-1$
		this.setCountryfrom(rs.getInt("countryfrom")); //$NON-NLS-1$
		this.setAge(rs.getByte("age")); //$NON-NLS-1$
		this.setSalary(new Money(rs.getInt("salary"))); //$NON-NLS-1$
		this.setGeneralskill(rs.getByte("generalskill")); //$NON-NLS-1$
		this.setStamina(rs.getByte("stamina")); //$NON-NLS-1$
		this.setPace(rs.getByte("pace")); //$NON-NLS-1$
		this.setTechnique(rs.getByte("technique")); //$NON-NLS-1$
		this.setPassing(rs.getByte("passing")); //$NON-NLS-1$
		this.setKeepers(rs.getByte("keepers")); //$NON-NLS-1$
		this.setDefenders(rs.getByte("defenders")); //$NON-NLS-1$
		this.setPlaymakers(rs.getByte("playmakers")); //$NON-NLS-1$
		this.setScorers(rs.getByte("scorers")); //$NON-NLS-1$
		this.setStatus(rs.getInt("status")); //$NON-NLS-1$
		this.setNote(rs.getString("note")); //$NON-NLS-1$
		this.setSummarySkill();
		return this;
	}
}
