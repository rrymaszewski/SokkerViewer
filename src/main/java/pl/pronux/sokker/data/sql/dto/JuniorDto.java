package pl.pronux.sokker.data.sql.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Junior;

public class JuniorDto extends Junior {

	private static final long serialVersionUID = -7260174325740836200L;
	private ResultSet rs;

	public JuniorDto(ResultSet rs) {
		this.rs = rs;
	}

	public Junior getJunior() throws SQLException {
		this.setId(rs.getInt("id_junior"));
		this.setName(rs.getString("name"));
		this.setSurname(rs.getString("surname"));
		this.setStatus(rs.getInt("status"));
		this.setNote(rs.getString("note"));
		this.setFormation(rs.getInt("formation"));
		return this;
	}
}
