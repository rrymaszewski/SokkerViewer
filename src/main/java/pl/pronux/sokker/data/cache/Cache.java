package pl.pronux.sokker.data.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Configuration;
import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.GalleryImage;
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

	private static ArrayList<Coach> coaches;

	private static ArrayList<Coach> coachesFired;

	private static HashMap<Integer, Coach> coachesMap;

	private static ArrayList<Coach> coachesTrash;

	private static Date date;

	private static HashMap<Integer, Country> countryMap;

	private static Map<Integer, League> leaguesMap;

	private static HashMap<Integer, Player> playersMap;

	private static List<Junior> juniors;

	private static List<Junior> juniorsFired;

	private static List<Junior> juniorsTrained;

	private static HashMap<Integer, Junior> juniorsTrainedMap;

	private static List<Junior> juniorsTrash;

	private static List<Match> matches;

	private static ArrayList<Note> notes;

	private static ArrayList<Player> players;

	private static ArrayList<Player> playersHistory;

	private static ArrayList<Player> playersTrash;

	private static ArrayList<Training> trainings;
	
	private static List<LeagueSeason> leagueSeasons;

	private static HashMap<Integer, Training> trainingsMap;

	private static HashMap<Integer, PlayerArchive> playersArchiveMap;

	private static ArrayList<Transfer> transfers;

	private static Map<Integer, Club> clubMap;

	private static List<Report> reports;
	
	private static List<GalleryImage> galleryImages;
	
	private static Configuration configuration;
	
	public static List<GalleryImage> getGalleryImages() {
		return galleryImages;
	}

	public static void setGalleryImages(List<GalleryImage> galleryImages) {
		Cache.galleryImages = galleryImages;
	}

	public static List<Country> getCountries() {
		return countries;
	}

	public static int[][] getAssistant() {
		return assistant;
	}

	public static Club getClub() {
		return club;
	}

	public static ArrayList<Coach> getCoaches() {
		return coaches;
	}

	public static ArrayList<Coach> getCoachesFired() {
		return coachesFired;
	}

	public static HashMap<Integer, Coach> getCoachesMap() {
		return coachesMap;
	}

	public static ArrayList<Coach> getCoachesTrash() {
		return coachesTrash;
	}

	public static Date getDate() {
		return date;
	}

	public static HashMap<Integer, Country> getCountryMap() {
		return countryMap;
	}

	public static Map<Integer, League> getLeaguesMap() {
		return leaguesMap;
	}

	public static HashMap<Integer, Player> getPlayersMap() {
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

	public static HashMap<Integer, Junior> getJuniorsTrainedMap() {
		return juniorsTrainedMap;
	}

	public static List<Junior> getJuniorsTrash() {
		return juniorsTrash;
	}

	public static List<Match> getMatches() {
		return matches;
	}

	public static ArrayList<Note> getNotes() {
		return notes;
	}

	public static ArrayList<Player> getPlayers() {
		return players;
	}

	public static ArrayList<Player> getPlayersHistory() {
		return playersHistory;
	}

	public static ArrayList<Player> getPlayersTrash() {
		return playersTrash;
	}

	public static ArrayList<Training> getTrainings() {
		return trainings;
	}

	public static HashMap<Integer, Training> getTrainingsMap() {
		return trainingsMap;
	}

	public static ArrayList<Transfer> getTransfers() {
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

	public static void setCoaches(ArrayList<Coach> coach) {
		Cache.coaches = coach;
	}

	public static void setCoachesFired(ArrayList<Coach> coachFired) {
		Cache.coachesFired = coachFired;
	}

	public static void setCoachesMap(HashMap<Integer, Coach> coachMap) {
		Cache.coachesMap = coachMap;
	}

	public static void setCoachesTrash(ArrayList<Coach> coachTrash) {
		Cache.coachesTrash = coachTrash;
	}

	public static void setDate(Date sokkerDate) {
		Cache.date = sokkerDate;
	}

	public static void setCountryMap(HashMap<Integer, Country> hmCountry) {
		Cache.countryMap = hmCountry;
	}

	public static void setLeaguesMap(Map<Integer, League> leaguesMap) {
		Cache.leaguesMap = leaguesMap;
	}

	public static void setPlayersMap(HashMap<Integer, Player> hmPlayers) {
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

	public static void setJuniorsTrainedMap(HashMap<Integer, Junior> juniorTrainedMap) {
		Cache.juniorsTrainedMap = juniorTrainedMap;
	}

	public static void setJuniorsTrash(List<Junior> juniorTrash) {
		Cache.juniorsTrash = juniorTrash;
	}

	public static void setMatches(List<Match> matches) {
		Cache.matches = matches;
	}

	public static void setNotes(ArrayList<Note> notes) {
		Cache.notes = notes;
	}

	public static void setPlayers(ArrayList<Player> player) {
		Cache.players = player;
	}

	public static void setPlayersHistory(ArrayList<Player> playerHistory) {
		Cache.playersHistory = playerHistory;
	}

	public static void setPlayersTrash(ArrayList<Player> playerTrash) {
		Cache.playersTrash = playerTrash;
	}

	public static void setTrainings(ArrayList<Training> trainingList) {
		Cache.trainings = trainingList;
	}

	public static void setTrainingsMap(HashMap<Integer, Training> trainingMap) {
		Cache.trainingsMap = trainingMap;
	}

	public static void setTransfers(ArrayList<Transfer> transfers) {
		Cache.transfers = transfers;
	}

	public static Map<Integer, Club> getClubMap() {
		return clubMap;
	}

	public static void setClubMap(Map<Integer, Club> clubMap) {
		Cache.clubMap = clubMap;
	}

	public static HashMap<Integer, PlayerArchive> getPlayersArchiveMap() {
		return playersArchiveMap;
	}

	public static void setPlayersArchiveMap(HashMap<Integer, PlayerArchive> playerArchiveMap) {
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

	public static Configuration getConfiguration() {
		return configuration;
	}

	public static void setConfiguration(Configuration configuration) {
		Cache.configuration = configuration;
	}

}
