package pl.pronux.sokker.importer.model;

import java.io.File;

import pl.pronux.sokker.model.Date;

public class XMLpack implements IXMLpack {
	private File trainers;
	private File players;
	private File juniors;
	private File team;
	private File reports;
	private File countries;
	private File region;
	private File matchesTeam;
	private File transfers;
	private boolean complete;

	private int teamId;

	private Date date;
	private boolean imported;

	public File getTrainers() {
		return trainers;
	}

	public void setTrainers(File trainers) {
		this.trainers = trainers;
	}

	public File getPlayers() {
		return players;
	}

	public void setPlayers(File players) {
		this.players = players;
	}

	public File getJuniors() {
		return juniors;
	}

	public void setJuniors(File juniors) {
		this.juniors = juniors;
	}

	public File getTeam() {
		return team;
	}

	public void setTeam(File team) {
		this.team = team;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public File getReports() {
		return reports;
	}

	public void setReports(File reports) {
		this.reports = reports;
	}

	public File getCountries() {
		return countries;
	}

	public void setCountries(File countries) {
		this.countries = countries;
	}

	public File getRegion() {
		return region;
	}

	public void setRegion(File region) {
		this.region = region;
	}

	public File getMatchesTeam() {
		return matchesTeam;
	}

	public void setMatchesTeam(File matchesTeam) {
		this.matchesTeam = matchesTeam;
	}

	public File getTransfers() {
		return transfers;
	}

	public void setTransfers(File transfers) {
		this.transfers = transfers;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.importer.model.IXMLpack#isComplete()
	 */
	public boolean isComplete() {
		return complete;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.importer.model.IXMLpack#setComplete(boolean)
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isImported() {
		return imported;
	}

	public void setImported(boolean imported) {
		this.imported = imported;
	}

}
