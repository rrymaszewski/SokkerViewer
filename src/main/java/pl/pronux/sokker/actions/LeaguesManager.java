package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.bean.LeagueStats;
import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.LeagueDao;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.LeagueRound;
import pl.pronux.sokker.model.LeagueSeason;
import pl.pronux.sokker.model.LeagueTeam;
import pl.pronux.sokker.model.Match;

public final class LeaguesManager {

	private static final int POINTS_FOR_DRAW = 1;
	private static final int POINTS_FOR_LOSS = 0;
	private static final int POINTS_FOR_WIN = 3;
	
	private static LeaguesManager instance = new LeaguesManager();

	private LeaguesManager() {
	}

	public static LeaguesManager getInstance() {
		return instance;
	}

	public void importLeagues(List<League> leagues) throws SQLException {
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		for (League league : leagues) {
			if (!leagueDao.existsLeague(league.getLeagueId())) {
				leagueDao.addLeague(league);
			}
			for (LeagueTeam leagueTeam : league.getLeagueTeams()) {
				if (!leagueDao.existsLeagueTeam(league, leagueTeam)) {
					leagueDao.addLeagueTeam(league, leagueTeam);
				}
			}
		}

	}

	// public ArrayList<League> getLeagues(Map<Integer, Club> clubMap) throws SQLException {
	// ArrayList<League> alLeagues = new ArrayList<League>();
	// boolean newConnection = SQLQuery.connect();
	// LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
	// alLeagues = leagueDao.getLeagues(clubMap);
	// SQLSession.close(newConnection);
	// return alLeagues;
	// }

	public Map<Integer, League> getLeagues() throws SQLException {
		Map<Integer, League> leaguesMap = new HashMap<Integer, League>();
		boolean newConnection = SQLQuery.connect();
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		leaguesMap = leagueDao.getLeagues();
		SQLSession.close(newConnection);
		return leaguesMap;
	}

	public List<LeagueSeason> getLeagueSeasons(Map<Integer, League> leaguesMap, Map<Integer, Club> clubsMap) throws SQLException {
		List<LeagueSeason> leagueSeasons = new ArrayList<LeagueSeason>();
		boolean newConnection = SQLQuery.connect();
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		leagueSeasons = leagueDao.getLeagueSeasons(leaguesMap, clubsMap);
		SQLSession.close(newConnection);
		return leagueSeasons;
	}

	public LeagueStats getLeagueStats(LeagueRound leagueRound) throws SQLException {
		boolean newConnection = SQLQuery.connect();
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		LeagueStats leagueStats = new LeagueStats();
		leagueStats.setAverateTeamRating(leagueDao.getAverageTeamRating(leagueRound));
		leagueStats.setAveragePlayerRating(leagueDao.getAveragePlayerRating(leagueRound));
		leagueStats.setPlayersAssists(leagueDao.getAssists(leagueRound));
		leagueStats.setPlayersFouls(leagueDao.getFouls(leagueRound));
		leagueStats.setPlayersGoals(leagueDao.getGoals(leagueRound));
		leagueStats.setPlayersShoots(leagueDao.getShoots(leagueRound));
		leagueStats.setSupporters(leagueDao.getSupporters(leagueRound));
		leagueStats.setTeamRating(leagueDao.getTeamRating(leagueRound));
		leagueStats.setSupporters(leagueDao.getSupporters(leagueRound));
		SQLSession.close(newConnection);
		return leagueStats;
	}

	public void completeLeagueRounds() throws SQLException {
		List<LeagueSeason> leagues = new ArrayList<LeagueSeason>();
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		leagues = leagueDao.getUncompletedRounds();
		List<LeagueTeam> leagueTeamToComplete = new ArrayList<LeagueTeam>();
		int currentSeason = -1;
		int currentLeagueId = -1;
		List<LeagueRound> rounds = new ArrayList<LeagueRound>();
		for (LeagueSeason leagueSeason : leagues) {
			if (currentSeason != leagueSeason.getSeason() || currentLeagueId != leagueSeason.getLeagueId()) {
				currentSeason = leagueSeason.getSeason();
				currentLeagueId = leagueSeason.getLeagueId();
				rounds = leagueDao.getRounds(leagueSeason, new HashMap<Integer, Club>());
			}

			LeagueRound round = rounds.get(leagueSeason.getRound() - 1);
			if (round.getRoundNumber() == 1) {
				List<Match> matches = round.getMatches();
				round.setLeagueTeams(new ArrayList<LeagueTeam>());
				int place = 8;
				for (Match match : matches) {
					LeagueTeam homeLeagueTeam = new LeagueTeam();
					LeagueTeam awayLeagueTeam = new LeagueTeam();
					homeLeagueTeam.setTeamId(match.getHomeTeamId());
					awayLeagueTeam.setTeamId(match.getAwayTeamId());
					homeLeagueTeam.setGoalsScored(match.getHomeTeamScore());
					awayLeagueTeam.setGoalsScored(match.getAwayTeamScore());
					homeLeagueTeam.setRound(match.getRound());
					awayLeagueTeam.setRound(match.getRound());
					homeLeagueTeam.setSeason(match.getSeason());
					awayLeagueTeam.setSeason(match.getSeason());
					homeLeagueTeam.setLeagueId(match.getLeagueId());
					awayLeagueTeam.setLeagueId(match.getLeagueId());
					if (match.getHomeTeamScore() > match.getAwayTeamScore()) {
						homeLeagueTeam.setPoints(POINTS_FOR_WIN);
						awayLeagueTeam.setPoints(POINTS_FOR_LOSS);
						homeLeagueTeam.setWins(1);
						awayLeagueTeam.setLosses(1);
					} else if (match.getHomeTeamScore() < match.getAwayTeamScore()) {
						homeLeagueTeam.setPoints(POINTS_FOR_LOSS);
						awayLeagueTeam.setPoints(POINTS_FOR_WIN);
						homeLeagueTeam.setLosses(1);
						awayLeagueTeam.setWins(1);
					} else {
						homeLeagueTeam.setPoints(POINTS_FOR_DRAW);
						awayLeagueTeam.setPoints(POINTS_FOR_DRAW);
						homeLeagueTeam.setDraws(1);
						awayLeagueTeam.setDraws(1);
					}
					homeLeagueTeam.setGoalsScored(match.getHomeTeamScore());
					homeLeagueTeam.setGoalsLost(match.getAwayTeamScore());
					awayLeagueTeam.setGoalsScored(match.getAwayTeamScore());
					awayLeagueTeam.setGoalsLost(match.getHomeTeamScore());
					homeLeagueTeam.setBeginPlace(place - 1);
					awayLeagueTeam.setBeginPlace(place);
					if (homeLeagueTeam.getPoints() > 0) {
						homeLeagueTeam
							.setRankTotal(String
								.format(
										"%d%d%03d%03d%03d", homeLeagueTeam.getPoints(), 500 + homeLeagueTeam.getGoalsScored() - homeLeagueTeam.getGoalsLost(), homeLeagueTeam.getGoalsScored(), homeLeagueTeam.getWins(), homeLeagueTeam.getBeginPlace())); 
					} else {
						homeLeagueTeam
							.setRankTotal(String
								.format(
										"%d%03d%03d%03d", 500 + homeLeagueTeam.getGoalsScored() - homeLeagueTeam.getGoalsLost(), homeLeagueTeam.getGoalsScored(), homeLeagueTeam.getWins(), homeLeagueTeam.getBeginPlace())); 
					}
					if (awayLeagueTeam.getPoints() > 0) {
						awayLeagueTeam
							.setRankTotal(String
								.format(
										"%d%d%03d%03d%03d", awayLeagueTeam.getPoints(), 500 + awayLeagueTeam.getGoalsScored() - awayLeagueTeam.getGoalsLost(), awayLeagueTeam.getGoalsScored(), awayLeagueTeam.getWins(), awayLeagueTeam.getBeginPlace())); 
					} else {
						awayLeagueTeam
							.setRankTotal(String
								.format(
										"%d%03d%03d%03d", 500 + awayLeagueTeam.getGoalsScored() - awayLeagueTeam.getGoalsLost(), awayLeagueTeam.getGoalsScored(), awayLeagueTeam.getWins(), awayLeagueTeam.getBeginPlace())); 
					}
					round.getLeagueTeams().add(homeLeagueTeam);
					round.getLeagueTeams().add(awayLeagueTeam);
					place -= 2;
				}
				leagueTeamToComplete.addAll(round.getLeagueTeams());
			} else {

				boolean completed = true;
				LeagueRound previousRound = rounds.get(leagueSeason.getRound() - 2);
				List<Match> matches = round.getMatches();
				for (Match match : matches) {
					if (match.getIsFinished() == Match.NOT_FINISHED) {
						completed = false;
						break;
					}
				}

				if (completed && previousRound.getLeagueTeams().size() == 8) {
					Map<Integer, LeagueTeam> previousRoundMap = new HashMap<Integer, LeagueTeam>();
					for (LeagueTeam leagueTeam : previousRound.getLeagueTeams()) {
						previousRoundMap.put(leagueTeam.getTeamId(), leagueTeam);
					}

					for (Match match : matches) {
						LeagueTeam homeLeagueTeam = new LeagueTeam();
						LeagueTeam awayLeagueTeam = new LeagueTeam();
						LeagueTeam previousHomeLeagueTeam = previousRoundMap.get(match.getHomeTeamId());
						LeagueTeam previousAwayLeagueTeam = previousRoundMap.get(match.getAwayTeamId());
						homeLeagueTeam.setTeamId(match.getHomeTeamId());
						awayLeagueTeam.setTeamId(match.getAwayTeamId());
						homeLeagueTeam.setGoalsScored(match.getHomeTeamScore() + previousHomeLeagueTeam.getGoalsScored());
						awayLeagueTeam.setGoalsScored(match.getAwayTeamScore() + previousAwayLeagueTeam.getGoalsScored());
						homeLeagueTeam.setRound(match.getRound());
						awayLeagueTeam.setRound(match.getRound());
						homeLeagueTeam.setSeason(match.getSeason());
						awayLeagueTeam.setSeason(match.getSeason());
						homeLeagueTeam.setLeagueId(match.getLeagueId());
						awayLeagueTeam.setLeagueId(match.getLeagueId());
						awayLeagueTeam.setLosses(previousAwayLeagueTeam.getLosses());
						awayLeagueTeam.setWins(previousAwayLeagueTeam.getWins());
						awayLeagueTeam.setDraws(previousAwayLeagueTeam.getDraws());
						homeLeagueTeam.setLosses(previousHomeLeagueTeam.getLosses());
						homeLeagueTeam.setWins(previousHomeLeagueTeam.getWins());
						homeLeagueTeam.setDraws(previousHomeLeagueTeam.getDraws());

						if (match.getHomeTeamScore() > match.getAwayTeamScore()) {
							homeLeagueTeam.setPoints(previousHomeLeagueTeam.getPoints() + POINTS_FOR_WIN);
							awayLeagueTeam.setPoints(previousAwayLeagueTeam.getPoints());
							homeLeagueTeam.setWins(previousHomeLeagueTeam.getWins() + 1);
							awayLeagueTeam.setLosses(previousAwayLeagueTeam.getLosses() + 1);
						} else if (match.getHomeTeamScore() < match.getAwayTeamScore()) {
							homeLeagueTeam.setPoints(previousHomeLeagueTeam.getPoints());
							awayLeagueTeam.setPoints(previousAwayLeagueTeam.getPoints() + POINTS_FOR_WIN);
							homeLeagueTeam.setLosses(previousHomeLeagueTeam.getLosses() + 1);
							awayLeagueTeam.setWins(previousAwayLeagueTeam.getWins() + 1);
						} else {
							homeLeagueTeam.setPoints(previousHomeLeagueTeam.getPoints() + POINTS_FOR_DRAW);
							awayLeagueTeam.setPoints(previousAwayLeagueTeam.getPoints() + POINTS_FOR_DRAW);
							homeLeagueTeam.setDraws(previousHomeLeagueTeam.getDraws() + 1);
							awayLeagueTeam.setDraws(previousAwayLeagueTeam.getDraws() + 1);
						}
						homeLeagueTeam.setGoalsScored(match.getHomeTeamScore() + previousHomeLeagueTeam.getGoalsScored());
						homeLeagueTeam.setGoalsLost(match.getAwayTeamScore() + previousHomeLeagueTeam.getGoalsLost());
						awayLeagueTeam.setGoalsScored(match.getAwayTeamScore() + previousAwayLeagueTeam.getGoalsScored());
						awayLeagueTeam.setGoalsLost(match.getHomeTeamScore() + previousAwayLeagueTeam.getGoalsLost());
						homeLeagueTeam.setBeginPlace(previousHomeLeagueTeam.getBeginPlace());
						awayLeagueTeam.setBeginPlace(previousAwayLeagueTeam.getBeginPlace());
						if (homeLeagueTeam.getPoints() > 0) {
							homeLeagueTeam
								.setRankTotal(String
									.format(
											"%d%d%03d%03d%03d", homeLeagueTeam.getPoints(), 500 + homeLeagueTeam.getGoalsScored() - homeLeagueTeam.getGoalsLost(), homeLeagueTeam.getGoalsScored(), homeLeagueTeam.getWins(), homeLeagueTeam.getBeginPlace())); 
						} else {
							homeLeagueTeam
								.setRankTotal(String
									.format(
											"%d%03d%03d%03d", 500 + homeLeagueTeam.getGoalsScored() - homeLeagueTeam.getGoalsLost(), homeLeagueTeam.getGoalsScored(), homeLeagueTeam.getWins(), homeLeagueTeam.getBeginPlace())); 
						}
						if (awayLeagueTeam.getPoints() > 0) {
							awayLeagueTeam
								.setRankTotal(String
									.format(
											"%d%d%03d%03d%03d", awayLeagueTeam.getPoints(), 500 + awayLeagueTeam.getGoalsScored() - awayLeagueTeam.getGoalsLost(), awayLeagueTeam.getGoalsScored(), awayLeagueTeam.getWins(), awayLeagueTeam.getBeginPlace())); 
						} else {
							awayLeagueTeam
								.setRankTotal(String
									.format(
											"%d%03d%03d%03d", 500 + awayLeagueTeam.getGoalsScored() - awayLeagueTeam.getGoalsLost(), awayLeagueTeam.getGoalsScored(), awayLeagueTeam.getWins(), awayLeagueTeam.getBeginPlace())); 
						}
						round.getLeagueTeams().add(homeLeagueTeam);
						round.getLeagueTeams().add(awayLeagueTeam);
					}
				}
				leagueTeamToComplete.addAll(round.getLeagueTeams());
			}
		}

		for (LeagueTeam leagueTeam : leagueTeamToComplete) {
			leagueDao.addLeagueTeam(leagueTeam);
		}

	}
}
