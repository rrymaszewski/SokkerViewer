package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.pronux.sokker.data.sql.dto.ReportDto;
import pl.pronux.sokker.model.Report;

public class ReportsDao {

	private Connection connection;

	public ReportsDao(Connection connection) {
		this.connection = connection;
	}

	public void addReport(Report report) throws SQLException {
		PreparedStatement pstm = connection.prepareStatement("INSERT INTO reports(id_report,type,player_id ,millis ,week ,value ) VALUES (?,?,?,?,?,?)"); 
		pstm.setLong(1, report.getReportId());
		pstm.setInt(2, report.getType());
		pstm.setLong(3, report.getPersonId());
		pstm.setLong(4, report.getDate().getMillis());
		pstm.setInt(5, report.getDate().getSokkerDate().getWeek());
		pstm.setInt(6, report.getValue());
		pstm.executeUpdate();
		pstm.close();
	}

	public boolean existsReport(long reportId) throws SQLException {

		PreparedStatement ps = connection.prepareStatement("SELECT count(id_report) FROM reports WHERE id_report = ?"); 
		ps.setLong(1, reportId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0) {
				rs.close();
				ps.close();
				return true;
			} else {
				rs.close();
				ps.close();
				return false;
			}
		}

		return false;
	}

	public List<Report> getReports() throws SQLException {
		List<Report> reports = new ArrayList<Report>();
		PreparedStatement ps = connection.prepareStatement("SELECT id_report, type, value, player_id, millis, week, checked FROM reports order by id_report desc"); 
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Report report = new ReportDto(rs).getReport();
			reports.add(report);
		}
		rs.close();
		ps.close();
		return reports;
	}

	public void checkReports() throws SQLException {
		PreparedStatement pstm = connection.prepareStatement("UPDATE reports SET checked = true WHERE checked = false"); 
		pstm.executeUpdate();
		pstm.close();
	}
}
