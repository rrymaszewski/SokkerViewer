package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.LeagueDao;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.LeagueRound;
import pl.pronux.sokker.model.LeagueSeason;
import pl.pronux.sokker.model.LeagueTeam;
import pl.pronux.sokker.model.Match;

public class LeaguesManager {
	public void importLeagues(List<League> leagues) throws SQLException {
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		for (League league : leagues) {
			if (!leagueDao.existsLeague(league.getLeagueID())) {
				leagueDao.addLeague(league);
			}
			for (LeagueTeam leagueTeam : league.getLeagueTeams()) {
				if (!leagueDao.existsLeagueTeam(league, leagueTeam)) {
					leagueDao.addLeagueTeam(league, leagueTeam);
				}
			}
		}

	}

//	public ArrayList<League> getLeagues(Map<Integer, Club> clubMap) throws SQLException {
//		ArrayList<League> alLeagues = new ArrayList<League>();
//		boolean newConnection = SQLQuery.connect();
//		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
//		alLeagues = leagueDao.getLeagues(clubMap);
//		SQLSession.close(newConnection);
//		return alLeagues;
//	}

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

	public void completeLeagueRounds() throws SQLException {
		List<LeagueSeason> leagues = new ArrayList<LeagueSeason>();
		LeagueDao leagueDao = new LeagueDao(SQLSession.getConnection());
		leagues = leagueDao.getUncompletedRounds();
		List<LeagueTeam> leagueTeamToComplete = new ArrayList<LeagueTeam>();
		int currentSeason = -1;
		int currentLeagueID = -1;
		List<LeagueRound> rounds = new ArrayList<LeagueRound>();
		for (LeagueSeason leagueSeason : leagues) {
			if (currentSeason != leagueSeason.getSeason() || currentLeagueID != leagueSeason.getLeagueID()) {
				currentSeason = leagueSeason.getSeason();
				currentLeagueID = leagueSeason.getLeagueID();
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
					homeLeagueTeam.setTeamID(match.getHomeTeamID());
					awayLeagueTeam.setTeamID(match.getAwayTeamID());
					homeLeagueTeam.setGoalsScored(match.getHomeTeamScore());
					awayLeagueTeam.setGoalsScored(match.getAwayTeamScore());
					homeLeagueTeam.setRound(match.getRound());
					awayLeagueTeam.setRound(match.getRound());
					homeLeagueTeam.setSeason(match.getSeason());
					awayLeagueTeam.setSeason(match.getSeason());
					homeLeagueTeam.setLeagueID(match.getLeagueID());
					awayLeagueTeam.setLeagueID(match.getLeagueID());
					if (match.getHomeTeamScore() > match.getAwayTeamScore()) {
						homeLeagueTeam.setPoints(3);
						awayLeagueTeam.setPoints(0);
						homeLeagueTeam.setWins(1);
						awayLeagueTeam.setLosses(1);
					} else if (match.getHomeTeamScore() < match.getAwayTeamScore()) {
						homeLeagueTeam.setPoints(0);
						awayLeagueTeam.setPoints(3);
						homeLeagueTeam.setLosses(1);
						awayLeagueTeam.setWins(1);
					} else {
						homeLeagueTeam.setPoints(1);
						awayLeagueTeam.setPoints(1);
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
						homeLeagueTeam.setRankTotal(String.format("%d%d%03d%03d%03d", homeLeagueTeam.getPoints(), 500 + homeLeagueTeam.getGoalsScored() - homeLeagueTeam.getGoalsLost(), homeLeagueTeam.getGoalsScored(), homeLeagueTeam.getWins(), homeLeagueTeam.getBeginPlace())); //$NON-NLS-1$
					} else {
						homeLeagueTeam.setRankTotal(String.format("%d%03d%03d%03d", 500 + homeLeagueTeam.getGoalsScored() - homeLeagueTeam.getGoalsLost(), homeLeagueTeam.getGoalsScored(), homeLeagueTeam.getWins(), homeLeagueTeam.getBeginPlace())); //$NON-NLS-1$
					}
					if (awayLeagueTeam.getPoints() > 0) {
						awayLeagueTeam.setRankTotal(String.format("%d%d%03d%03d%03d", awayLeagueTeam.getPoints(), 500 + awayLeagueTeam.getGoalsScored() - awayLeagueTeam.getGoalsLost(), awayLeagueTeam.getGoalsScored(), awayLeagueTeam.getWins(), awayLeagueTeam.getBeginPlace())); //$NON-NLS-1$
					} else {
						awayLeagueTeam.setRankTotal(String.format("%d%03d%03d%03d", 500 + awayLeagueTeam.getGoalsScored() - awayLeagueTeam.getGoalsLost(), awayLeagueTeam.getGoalsScored(), awayLeagueTeam.getWins(), awayLeagueTeam.getBeginPlace())); //$NON-NLS-1$
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
				for(Match match : matches) {
					if(match.getIsFinished() == Match.NOT_FINISHED) {
						completed = false;
						break;
					}
				}
				
				if(completed && previousRound.getLeagueTeams().size() == 8) {
					Map<Integer, LeagueTeam> previousRoundMap = new HashMap<Integer, LeagueTeam>();	
					for (LeagueTeam leagueTeam : previousRound.getLeagueTeams()) {
						previousRoundMap.put(leagueTeam.getTeamID(), leagueTeam);
					}
					
					for (Match match : matches) {
						LeagueTeam homeLeagueTeam = new LeagueTeam();
						LeagueTeam awayLeagueTeam = new LeagueTeam();
						LeagueTeam previousHomeLeagueTeam = previousRoundMap.get(match.getHomeTeamID());
						LeagueTeam previousAwayLeagueTeam = previousRoundMap.get(match.getAwayTeamID());
						homeLeagueTeam.setTeamID(match.getHomeTeamID());
						awayLeagueTeam.setTeamID(match.getAwayTeamID());
						homeLeagueTeam.setGoalsScored(match.getHomeTeamScore() + previousHomeLeagueTeam.getGoalsScored());
						awayLeagueTeam.setGoalsScored(match.getAwayTeamScore() + previousAwayLeagueTeam.getGoalsScored());
						homeLeagueTeam.setRound(match.getRound());
						awayLeagueTeam.setRound(match.getRound());
						homeLeagueTeam.setSeason(match.getSeason());
						awayLeagueTeam.setSeason(match.getSeason());
						homeLeagueTeam.setLeagueID(match.getLeagueID());
						awayLeagueTeam.setLeagueID(match.getLeagueID());
						awayLeagueTeam.setLosses(previousAwayLeagueTeam.getLosses());
						awayLeagueTeam.setWins(previousAwayLeagueTeam.getWins());
						awayLeagueTeam.setDraws(previousAwayLeagueTeam.getDraws());
						homeLeagueTeam.setLosses(previousHomeLeagueTeam.getLosses());
						homeLeagueTeam.setWins(previousHomeLeagueTeam.getWins());
						homeLeagueTeam.setDraws(previousHomeLeagueTeam.getDraws());

						if (match.getHomeTeamScore() > match.getAwayTeamScore()) {
							homeLeagueTeam.setPoints(previousHomeLeagueTeam.getPoints() + 3);
							awayLeagueTeam.setPoints(previousAwayLeagueTeam.getPoints());
							homeLeagueTeam.setWins(previousHomeLeagueTeam.getWins() + 1);
							awayLeagueTeam.setLosses(previousAwayLeagueTeam.getLosses() + 1);
						} else if (match.getHomeTeamScore() < match.getAwayTeamScore()) {
							homeLeagueTeam.setPoints(previousHomeLeagueTeam.getPoints());
							awayLeagueTeam.setPoints(previousAwayLeagueTeam.getPoints() + 3);
							homeLeagueTeam.setLosses(previousHomeLeagueTeam.getLosses() + 1);
							awayLeagueTeam.setWins(previousAwayLeagueTeam.getWins() + 1);
						} else {
							homeLeagueTeam.setPoints(previousHomeLeagueTeam.getPoints() + 1);
							awayLeagueTeam.setPoints(previousAwayLeagueTeam.getPoints() + 1);
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
							homeLeagueTeam.setRankTotal(String.format("%d%d%03d%03d%03d", homeLeagueTeam.getPoints(), 500 + homeLeagueTeam.getGoalsScored() - homeLeagueTeam.getGoalsLost(), homeLeagueTeam.getGoalsScored(), homeLeagueTeam.getWins(), homeLeagueTeam.getBeginPlace())); //$NON-NLS-1$
						} else {
							homeLeagueTeam.setRankTotal(String.format("%d%03d%03d%03d", 500 + homeLeagueTeam.getGoalsScored() - homeLeagueTeam.getGoalsLost(), homeLeagueTeam.getGoalsScored(), homeLeagueTeam.getWins(), homeLeagueTeam.getBeginPlace())); //$NON-NLS-1$
						}
						if (awayLeagueTeam.getPoints() > 0) {
							awayLeagueTeam.setRankTotal(String.format("%d%d%03d%03d%03d", awayLeagueTeam.getPoints(), 500 + awayLeagueTeam.getGoalsScored() - awayLeagueTeam.getGoalsLost(), awayLeagueTeam.getGoalsScored(), awayLeagueTeam.getWins(), awayLeagueTeam.getBeginPlace())); //$NON-NLS-1$
						} else {
							awayLeagueTeam.setRankTotal(String.format("%d%03d%03d%03d", 500 + awayLeagueTeam.getGoalsScored() - awayLeagueTeam.getGoalsLost(), awayLeagueTeam.getGoalsScored(), awayLeagueTeam.getWins(), awayLeagueTeam.getBeginPlace())); //$NON-NLS-1$
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
