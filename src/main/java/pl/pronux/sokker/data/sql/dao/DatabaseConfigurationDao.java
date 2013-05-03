package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.DbProperties;
import pl.pronux.sokker.model.SokkerDate;

public class DatabaseConfigurationDao {

	private Connection connection;

	public DatabaseConfigurationDao(Connection connection) {
		this.connection = connection;
	}

	public void setDbRepair(boolean b) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE system SET repair_db = ?"); 
		ps.setBoolean(1, b);
		ps.executeUpdate();
		ps.close();
	}
	
	public void setDbRepairJuniorsAge(boolean b) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE system SET repair_juniors_age = ?"); 
		ps.setBoolean(1, b);
		ps.executeUpdate();
		ps.close();
	}

	public void setDbCountry(boolean b) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE system SET check_countries = ?"); 
		ps.setBoolean(1, b);
		ps.executeUpdate();
		ps.close();
	}
	
	public void updateScanCounter(int scanCounter) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE system SET scan_counter = ?"); 
		ps.setInt(1, scanCounter);
		ps.executeUpdate();
		ps.close();
	}

	public void setDbDate(Date date) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE system SET last_modification_millis = ?, last_modification_sk_week = ?, last_modification_sk_day = ?"); 
		ps.setLong(1, date.getMillis());
		ps.setLong(2, date.getSokkerDate().getWeek());
		ps.setLong(3, date.getSokkerDate().getDay());
		ps.executeUpdate();
		ps.close();
	}

	public void setDbUpdate(boolean b) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE system SET check_update_db = ?"); 
		ps.setBoolean(1, b);
		ps.executeUpdate();
		ps.close();
	}

	public void setDBVersion(int dbVersion) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE system SET " + "version = ?");  
		ps.setInt(1, dbVersion);
		ps.executeUpdate();
		ps.close();
	}

	public DbProperties getDbProperties() throws SQLException {
		DbProperties dbProperties = null;
		PreparedStatement ps = connection.prepareStatement("SELECT version, last_modification_millis, last_modification_sk_day, last_modification_sk_week, check_countries, check_update_db, repair_db, scan_counter, complete_juniors_age FROM system");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			dbProperties = new DbProperties();
			dbProperties.setDbVersion(rs.getInt("version"));
			Date date = new Date(rs.getLong("last_modification_millis"));
			date.setSokkerDate(new SokkerDate(rs.getInt("last_modification_sk_day"), rs.getInt("last_modification_sk_week")));
			dbProperties.setLastModification(date);
			dbProperties.setCheckCountries(rs.getBoolean("check_countries"));
			dbProperties.setCheckDbUpdate(rs.getBoolean("check_update_db"));
			dbProperties.setRepairDB(rs.getBoolean("repair_db"));
			dbProperties.setScanCounter(rs.getInt("scan_counter"));
			dbProperties.setCompleteJuniorsAge(rs.getBoolean("complete_juniors_age"));
		}
		rs.close();
		ps.close();
		return dbProperties;
	}

	public Date getMaxDate() throws SQLException {
		Date date = null;
		PreparedStatement ps = connection
				.prepareStatement("SELECT c.last_modification_millis, c.last_modification_sk_day, c.last_modification_sk_week FROM system c WHERE c.last_modification_millis IN (SELECT max(last_modification_millis) FROM system)"); 
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			date  = new Date(rs.getLong(1));
			date.setSokkerDate(new SokkerDate(rs.getInt(2), rs.getInt(3)));
		}
		rs.close();
		ps.close();
		return date;
	}

	public int getTeamId() throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT team_id from system "); 
		int teamId = 0;
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			teamId = rs.getInt("team_id"); 
		}
		rs.close();
		ps.close();
		return teamId;
	}

	public void setDBTeamId(int teamId) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE system SET " + "team_id = ?");  
		ps.setInt(1, teamId);
		ps.executeUpdate();
		ps.close();
	}

	public void setJuniorMinimumPop(double pop) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE system SET " + "junior_minimum_pop = ?");  
		ps.setDouble(1, pop);
		ps.executeUpdate();
		ps.close();
	}

	public double getJuniorMinimumPop() throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT junior_minimum_pop from system "); 
		double pop = 0;
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			pop = rs.getDouble("junior_minimum_pop"); 
		}
		rs.close();
		ps.close();
		return pop;
	}

	public int checkDBVersion() throws SQLException {
		int version = 0;
		PreparedStatement ps = connection.prepareStatement("SELECT version FROM system"); 
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			version = rs.getInt(1);
		}
		rs.close();
		ps.close();
		return version;
	}
}
