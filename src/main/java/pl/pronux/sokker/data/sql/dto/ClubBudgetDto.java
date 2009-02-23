package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.ClubBudget;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SokkerDate;

public class ClubBudgetDto extends ClubBudget {
	private ResultSet rs;

	public ClubBudgetDto(ResultSet rs) {
		this.rs = rs;
	}
	
	public ClubBudget getClubBudget() throws SQLException {
		this.setId(rs.getInt("id_data")); //$NON-NLS-1$
		this.setDate(new Date(rs.getLong("millis"))); //$NON-NLS-1$
		this.setMoney(new Money(rs.getInt("money"))); //$NON-NLS-1$
		this.getDate().setSokkerDate(new SokkerDate(rs.getInt("day"), rs.getInt("week"))); //$NON-NLS-1$ //$NON-NLS-2$
		return this;
	}
}
