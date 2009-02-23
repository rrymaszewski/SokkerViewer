package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.model.SokkerDate;

public class JuniorSkillsDto extends JuniorSkills {
	private ResultSet rs;

	public JuniorSkillsDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public JuniorSkills getJuniorSkills() throws SQLException {
		this.setId(rs.getInt("id_skill")); //$NON-NLS-1$
		this.setIdJuniorFK(rs.getInt("id_junior_fk")); //$NON-NLS-1$
		this.setDate(new Date(rs.getLong("millis"))); //$NON-NLS-1$
		this.setWeeks(rs.getInt("weeks")); //$NON-NLS-1$
		this.setSkill(rs.getInt("skill")); //$NON-NLS-1$
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week"))); //$NON-NLS-1$ //$NON-NLS-2$
		return this;
	}
}
