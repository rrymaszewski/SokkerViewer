package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.NtSkills;

public class PlayerNtSkillsDto extends NtSkills {

	private ResultSet rs;

	public PlayerNtSkillsDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public NtSkills getNtSkill() throws SQLException {
		this.setNtMatches(rs.getInt("nt_matches")); 
		this.setNtGoals(rs.getInt("nt_goals")); 
		this.setNtAssists(rs.getInt("nt_assists")); 
		this.setNtCards(rs.getInt("nt_cards")); 
		return this;
	}
}
