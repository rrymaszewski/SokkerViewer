package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.PersonInterface;
import pl.pronux.sokker.model.Player;

public class PersonsDao {
	private Connection connection;

	public PersonsDao(Connection connection) {
		this.connection = connection;
	}

	public void updatePersonStatus(PersonInterface person) throws SQLException {
		PreparedStatement ps = null;
		if (person instanceof Player) {
			ps = connection.prepareStatement("UPDATE player SET status = ? WHERE id_player = ?"); //$NON-NLS-1$
		} else if (person instanceof Coach) {
			ps = connection.prepareStatement("UPDATE coach SET status = ? WHERE id_coach = ?"); //$NON-NLS-1$
		} else if (person instanceof Junior) {
			ps = connection.prepareStatement("UPDATE junior SET status = ? WHERE id_junior = ?"); //$NON-NLS-1$
		}

		if (ps != null) {
			ps.setInt(1, person.getStatus());
			ps.setInt(2, person.getId());

			ps.executeUpdate();
			ps.close();
		}

	}
}
