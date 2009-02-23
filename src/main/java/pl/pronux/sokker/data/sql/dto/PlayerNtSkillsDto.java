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
		this.setNtMatches(rs.getInt("nt_matches")); //$NON-NLS-1$
		this.setNtGoals(rs.getInt("nt_goals")); //$NON-NLS-1$
		this.setNtAssists(rs.getInt("nt_assists")); //$NON-NLS-1$
		this.setNtCards(rs.getInt("nt_cards")); //$NON-NLS-1$
		return this;
	}
}
