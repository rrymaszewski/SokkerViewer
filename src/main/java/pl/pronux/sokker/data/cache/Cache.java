package pl.pronux.sokker.data.cache;

import java.util.List;
import java.util.Map;

import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.LeagueSeason;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.Note;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.model.Report;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.model.Transfer;

public class Cache {

	private static List<Country> countries;

	private static int[][] assistant;

	private static Club club;

	private static List<Coach> coaches;

	private static List<Coach> coachesFired;

	private static Map<Integer, Coach> coachesMap;

	private static List<Coach> coachesTrash;

	private static Date date;

	private static Map<Integer, Country> countryMap;

	private static Map<Integer, League> leaguesMap;

	private static Map<Integer, Player> playersMap;

	private static List<Junior> juniors;

	private static List<Junior> juniorsFired;

	private static List<Junior> juniorsTrained;

	private static Map<Integer, Junior> juniorsTrainedMap;

	private static List<Junior> juniorsTrash;

	private static List<Match> matches;

	private static List<Note> notes;

	private static List<Player> players;

	private static List<Player> playersHistory;

	private static List<Player> playersTrash;

	private static List<Training> trainings;

	private static List<LeagueSeason> leagueSeasons;

	private static Map<Integer, Training> trainingsMap;

	private static Map<Integer, PlayerArchive> playersArchiveMap;

	private static List<Transfer> transfers;

	private static Map<Integer, Club> clubMap;

	private static List<Report> reports;

	public static List<Country> getCountries() {
		return countries;
	}

	public static int[][] getAssistant() {
		return assistant;
	}

	public static Club getClub() {
		return club;
	}

	public static List<Coach> getCoaches() {
		return coaches;
	}

	public static List<Coach> getCoachesFired() {
		return coachesFired;
	}

	public static Map<Integer, Coach> getCoachesMap() {
		return coachesMap;
	}

	public static List<Coach> getCoachesTrash() {
		return coachesTrash;
	}

	public static Date getDate() {
		return date;
	}

	public static Map<Integer, Country> getCountryMap() {
		return countryMap;
	}

	public static Map<Integer, League> getLeaguesMap() {
		return leaguesMap;
	}

	public static Map<Integer, Player> getPlayersMap() {
		return playersMap;
	}

	public static List<Junior> getJuniors() {
		return juniors;
	}

	public static List<Junior> getJuniorsFired() {
		return juniorsFired;
	}

	public static List<Junior> getJuniorsTrained() {
		return juniorsTrained;
	}

	public static Map<Integer, Junior> getJuniorsTrainedMap() {
		return juniorsTrainedMap;
	}

	public static List<Junior> getJuniorsTrash() {
		return juniorsTrash;
	}

	public static List<Match> getMatches() {
		return matches;
	}

	public static List<Note> getNotes() {
		return notes;
	}

	public static List<Player> getPlayers() {
		return players;
	}

	public static List<Player> getPlayersHistory() {
		return playersHistory;
	}

	public static List<Player> getPlayersTrash() {
		return playersTrash;
	}

	public static List<Training> getTrainings() {
		return trainings;
	}

	public static Map<Integer, Training> getTrainingsMap() {
		return trainingsMap;
	}

	public static List<Transfer> getTransfers() {
		return transfers;
	}

	public static void setCountries(List<Country> countries) {
		Cache.countries = countries;
	}

	public static void setAssistant(int[][] assistant) {
		Cache.assistant = assistant;
	}

	public static void setClub(Club club) {
		Cache.club = club;
	}

	public static void setCoaches(List<Coach> coach) {
		Cache.coaches = coach;
	}

	public static void setCoachesFired(List<Coach> coachFired) {
		Cache.coachesFired = coachFired;
	}

	public static void setCoachesMap(Map<Integer, Coach> coachMap) {
		Cache.coachesMap = coachMap;
	}

	public static void setCoachesTrash(List<Coach> coachTrash) {
		Cache.coachesTrash = coachTrash;
	}

	public static void setDate(Date sokkerDate) {
		Cache.date = sokkerDate;
	}

	public static void setCountryMap(Map<Integer, Country> hmCountry) {
		Cache.countryMap = hmCountry;
	}

	public static void setLeaguesMap(Map<Integer, League> leaguesMap) {
		Cache.leaguesMap = leaguesMap;
	}

	public static void setPlayersMap(Map<Integer, Player> hmPlayers) {
		Cache.playersMap = hmPlayers;
	}

	public static void setJuniors(List<Junior> junior) {
		Cache.juniors = junior;
	}

	public static void setJuniorsFired(List<Junior> juniorFired) {
		Cache.juniorsFired = juniorFired;
	}

	public static void setJuniorsTrained(List<Junior> juniorTrained) {
		Cache.juniorsTrained = juniorTrained;
	}

	public static void setJuniorsTrainedMap(Map<Integer, Junior> juniorTrainedMap) {
		Cache.juniorsTrainedMap = juniorTrainedMap;
	}

	public static void setJuniorsTrash(List<Junior> juniorTrash) {
		Cache.juniorsTrash = juniorTrash;
	}

	public static void setMatches(List<Match> matches) {
		Cache.matches = matches;
	}

	public static void setNotes(List<Note> notes) {
		Cache.notes = notes;
	}

	public static void setPlayers(List<Player> player) {
		Cache.players = player;
	}

	public static void setPlayersHistory(List<Player> playerHistory) {
		Cache.playersHistory = playerHistory;
	}

	public static void setPlayersTrash(List<Player> playerTrash) {
		Cache.playersTrash = playerTrash;
	}

	public static void setTrainings(List<Training> trainingList) {
		Cache.trainings = trainingList;
	}

	public static void setTrainingsMap(Map<Integer, Training> trainingMap) {
		Cache.trainingsMap = trainingMap;
	}

	public static void setTransfers(List<Transfer> transfers) {
		Cache.transfers = transfers;
	}

	public static Map<Integer, Club> getClubMap() {
		return clubMap;
	}

	public static void setClubMap(Map<Integer, Club> clubMap) {
		Cache.clubMap = clubMap;
	}

	public static Map<Integer, PlayerArchive> getPlayersArchiveMap() {
		return playersArchiveMap;
	}

	public static void setPlayersArchiveMap(Map<Integer, PlayerArchive> playerArchiveMap) {
		Cache.playersArchiveMap = playerArchiveMap;
	}

	public static void setReports(List<Report> reports) {
		Cache.reports = reports;
	}

	public static List<Report> getReports() {
		return reports;
	}

	public static List<LeagueSeason> getLeagueSeasons() {
		return leagueSeasons;
	}

	public static void setLeagueSeasons(List<LeagueSeason> leagueSeasons) {
		Cache.leagueSeasons = leagueSeasons;
	}

}
