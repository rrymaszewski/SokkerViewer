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
		this.setId(rs.getInt("id_skill"));
		this.setIdJuniorFK(rs.getInt("id_junior_fk"));
		this.setDate(new Date(rs.getLong("millis")));
		this.setWeeks(rs.getInt("weeks"));
		this.setSkill(rs.getInt("skill"));
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week")));
		this.setAge(rs.getInt("age"));
		return this;
	}
}
