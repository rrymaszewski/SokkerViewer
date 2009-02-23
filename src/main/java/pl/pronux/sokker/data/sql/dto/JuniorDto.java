package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Junior;

public class JuniorDto extends Junior {
/**
	 * 
	 */
	private static final long serialVersionUID = -7260174325740836200L;
private ResultSet rs;

public JuniorDto(ResultSet rs) {
	this.rs = rs;
}

public Junior getJunior() throws SQLException {
	this.setId(rs.getInt("id_junior")); //$NON-NLS-1$
	this.setName(rs.getString("name")); //$NON-NLS-1$
	this.setSurname(rs.getString("surname")); //$NON-NLS-1$
	this.setStatus(rs.getInt("status")); //$NON-NLS-1$
	this.setNote(rs.getString("note")); //$NON-NLS-1$
	return this;
}
}
