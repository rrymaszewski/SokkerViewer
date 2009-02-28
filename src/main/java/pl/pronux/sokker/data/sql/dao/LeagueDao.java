package pl.pronux.sokker.data.sql.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.pronux.sokker.data.sql.dto.LeagueDto;
import pl.pronux.sokker.data.sql.dto.LeagueMatchDto;
import pl.pronux.sokker.data.sql.dto.LeagueRoundDto;
import pl.pronux.sokker.data.sql.dto.LeagueSeasonDto;
import pl.pronux.sokker.data.sql.dto.LeagueTeamDto;
import pl.pronux.sokker.data.sql.dto.PlayerArchiveDto;
import pl.pronux.sokker.data.sql.dto.PlayerStatsDto;
import pl.pronux.sokker.data.sql.dto.TeamStatsDto;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.LeagueRound;
import pl.pronux.sokker.model.LeagueSeason;
import pl.pronux.sokker.model.LeagueTeam;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.TeamStats;

public class LeagueDao {

	private Connection connection;

	public LeagueDao(Connection connection) {
		this.connection = connection;
	}

	public void addPlayersStats(int matchID, int teamID, PlayerStats playerStats) throws SQLException {
		PreparedStatement pstm;
		pstm = connection
				.prepareStatement("INSERT INTO PLAYERS_STATS (MATCH_ID, TEAM_ID, PLAYER_ID, NUMBER, FORMATION, TIME_IN, TIME_OUT, YELLOW_CARDS, RED_CARDS, IS_INJURED, GOALS, ASSISTS, FOULS, SHOOTS, RATING, TIME_PLAYING, TIME_DEFENDING) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); //$NON-NLS-1$
		pstm.setInt(1, matchID);
		pstm.setInt(2, teamID);
		pstm.setInt(3, playerStats.getPlayerID());
		pstm.setInt(4, playerStats.getNumber());
		pstm.setInt(5, playerStats.getFormation());
		pstm.setInt(6, playerStats.getTimeIn());
		pstm.setInt(7, playerStats.getTimeOut());
		pstm.setInt(8, playerStats.getYellowCards());
		pstm.setInt(9, playerStats.getRedCards());
		pstm.setInt(10, playerStats.getIsInjured());
		pstm.setInt(11, playerStats.getGoals());
		pstm.setInt(12, playerStats.getAssists());
		pstm.setInt(13, playerStats.getFouls());
		pstm.setInt(14, playerStats.getShoots());
		pstm.setInt(15, playerStats.getRating());
		pstm.setInt(16, playerStats.getTimePlaying());
		pstm.setInt(17, playerStats.getTimeDefending());
		pstm.executeUpdate();
		pstm.close();
	}

	public void addTeamStats(int matchID, TeamStats teamStats) throws SQLException {
		PreparedStatement pstm;
		pstm = connection
				.prepareStatement("INSERT INTO TEAM_STATS (MATCH_ID, TEAM_ID, TIME_ON_HALF, TIME_POSSESSION, OFFSIDES, SHOOTS, FOULS, YELLOW_CARDS, RED_CARDS, TACTIC_NAME, RATING_SCORING, RATING_PASSING, RATING_DEFENDING) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"); //$NON-NLS-1$
		pstm.setInt(1, matchID);
		pstm.setInt(2, teamStats.getTeamID());
		pstm.setInt(3, teamStats.getTimeOnHalf());
		pstm.setInt(4, teamStats.getTimePossession());
		pstm.setInt(5, teamStats.getOffsides());
		pstm.setInt(6, teamStats.getShoots());
		pstm.setInt(7, teamStats.getFouls());
		pstm.setInt(8, teamStats.getYellowCards());
		pstm.setInt(9, teamStats.getRedCards());
		pstm.setString(10, teamStats.getTacticName());
		pstm.setInt(11, teamStats.getRatingScoring());
		pstm.setInt(12, teamStats.getRatingPassing());
		pstm.setInt(13, teamStats.getRatingDefending());

		pstm.executeUpdate();
		pstm.close();
	}

	public List<Integer> getCompletedRounds(League league) throws SQLException {
		List<Integer> alCompletedRounds = new ArrayList<Integer>();
		PreparedStatement ps;

		ps = connection
				.prepareStatement("SELECT round FROM leagues as l left join matches_team as mt on (l.league_id = mt.league_id) where l.league_id = ? and mt.is_finished = 1 group by round having count(round) = 4 order by round asc"); //$NON-NLS-1$
		ps.setInt(1, league.getLeagueID());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			alCompletedRounds.add(rs.getInt("round")); //$NON-NLS-1$
		}
		rs.close();
		ps.close();
		return alCompletedRounds;
	}

//	public ArrayList<League> getLeagues(Map<Integer, Club> clubMap) throws SQLException {
//		ArrayList<League> alLeague = new ArrayList<League>();
//		PreparedStatement ps;
//
//		// ps = conn.prepareStatement("SELECT * FROM leagues WHERE is_cup =
//		// 0 AND
//		// type = 0 AND league_id IN (select league_id FROM leagues as l,
//		// season_round as s, league_team as t WHERE l.league_id =
//		// s.league_id AND
//		// s.season_round_id = t.season_round_id GROUP BY league_id)");
//		ps = connection.prepareStatement("SELECT * FROM leagues"); //$NON-NLS-1$
//		ResultSet rs = ps.executeQuery();
//
//		while (rs.next()) {
//			League league = new LeagueDto(rs).getLeague();
//			if (league.getIsOfficial() == League.OFFICIAL && league.getIsCup() == League.NOT_CUP && league.getType() == League.TYPE_LEAGUE) {
//				league.setLeagueSeasons(getSeason(league, clubMap));
//			}
//
//			alLeague.add(league);
//		}
//		rs.close();
//		ps.close();
//
//		return alLeague;
//	}

	public Map<Integer, League> getLeagues() throws SQLException {
	Map<Integer, League> leaguesMap = new HashMap<Integer, League>();
	PreparedStatement ps;
	ps = connection.prepareStatement("SELECT * FROM leagues"); //$NON-NLS-1$
	ResultSet rs = ps.executeQuery();

	while (rs.next()) {
		League league = new LeagueDto(rs).getLeague();
		
		leaguesMap.put(league.getLeagueID(), league);
	}
	rs.close();
	ps.close();

	return leaguesMap;
}
	public League getLeague(int leagueID) throws SQLException {
		League league = null;
		PreparedStatement ps;

		// ps = conn.prepareStatement("SELECT * FROM leagues WHERE is_cup =
		// 0 AND
		// type = 0 AND league_id IN (select league_id FROM leagues as l,
		// season_round as s, league_team as t WHERE l.league_id =
		// s.league_id AND
		// s.season_round_id = t.season_round_id GROUP BY league_id)");
		ps = connection.prepareStatement("SELECT * FROM leagues WHERE league_id = ?"); //$NON-NLS-1$
		ps.setInt(1, leagueID);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			league = new LeagueDto(rs).getLeague();
		}
		rs.close();
		ps.close();

		return league;
	}

//	public ArrayList<League> getLeaguesByClub(Club club) throws SQLException {
//		ArrayList<League> alLeague = new ArrayList<League>();
//		PreparedStatement ps;
//
//		// ps = conn.prepareStatement("SELECT * FROM leagues WHERE is_cup =
//		// 0 AND
//		// type = 0 AND league_id IN (select league_id FROM leagues as l,
//		// season_round as s, league_team as t WHERE l.league_id =
//		// s.league_id AND
//		// s.season_round_id = t.season_round_id GROUP BY league_id)");
//		ps = connection.prepareStatement("SELECT * FROM leagues"); //$NON-NLS-1$
//		ResultSet rs = ps.executeQuery();
//
//		while (rs.next()) {
//			alLeague.add(new LeagueDto(rs).getLeague());
//		}
//		rs.close();
//		ps.close();
//		return alLeague;
//	}

	public List<LeagueTeam> getLeagueTeam(LeagueRound leagueRound, Map<Integer, Club> clubMap) throws SQLException {
		List<LeagueTeam> alLeagueTeam = new ArrayList<LeagueTeam>();
		PreparedStatement ps;
		int position = 1;

		// ps = connection.prepareStatement("SELECT * FROM league_team WHERE
		// season = ? AND round = ? AND league_id = ? order by points DESC,
		// (goals_scored-goals_lost) DESC, goals_scored DESC,
		// SUBSTRING(rank_total,LENGTH(rank_total)-1)");
		ps = connection
				.prepareStatement("" + //$NON-NLS-1$
						"SELECT l.*,m.home_team_name as team_name, (goals_scored-goals_lost) as distinction, SUBSTRING(rank_total,LENGTH(rank_total)-1) as begin_place FROM league_team as l join matches_team as m on (l.league_id = m.league_id and l.season = m.season and l.round = m.round and l.team_id = m.home_team_id ) WHERE  l.season = ? AND l.round = ? AND l.league_id = ? "//$NON-NLS-1$
						+ 
						"UNION " //$NON-NLS-1$
						+ 
						"(SELECT l.*,m.away_team_name as team_name, (goals_scored-goals_lost) as distinction, SUBSTRING(rank_total,LENGTH(rank_total)-1) as begin_place FROM league_team as l join matches_team as m on (l.league_id = m.league_id and l.season = m.season and l.round = m.round and l.team_id = m.away_team_id ) WHERE  l.season = ? AND l.round = ? AND l.league_id = ?) "//$NON-NLS-1$
						+ 
						"order by points DESC, distinction DESC, goals_scored DESC, begin_place DESC"); //$NON-NLS-1$
		ps.setInt(1, leagueRound.getLeagueSeason().getSeason());
		ps.setInt(2, leagueRound.getRoundNumber());
		ps.setInt(3, leagueRound.getLeagueSeason().getLeagueID());
		ps.setInt(4, leagueRound.getLeagueSeason().getSeason());
		ps.setInt(5, leagueRound.getRoundNumber());
		ps.setInt(6, leagueRound.getLeagueSeason().getLeagueID());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			LeagueTeam leagueTeam = new LeagueTeamDto(rs).getLeagueTeam();
			leagueTeam.setPosition(position++);
			leagueTeam.setClub(clubMap.get(leagueTeam.getTeamID()));
			alLeagueTeam.add(leagueTeam);
		}
		rs.close();
		ps.close();

		return alLeagueTeam;
	}

	public List<Match> getMatches(Club club) throws SQLException {
		List<Match> alMatches = new ArrayList<Match>();
		PreparedStatement ps;

		ps = connection.prepareStatement("SELECT * FROM matches_team WHERE (home_team_id = ? OR away_team_id = ?) AND (date_started >= ? OR is_finished = 0) order by week, day"); //$NON-NLS-1$
		ps.setInt(1, club.getId());
		ps.setInt(2, club.getId());
		ps.setTimestamp(3, club.getDateCreated().getTimestamp());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			alMatches.add(new LeagueMatchDto(rs).getMatch());
		}
		rs.close();
		ps.close();

		return alMatches;
	}

	public List<Match> getMatches(LeagueRound round, Map<Integer, Club> clubMap) throws SQLException {
		List<Match> alMatches = new ArrayList<Match>();
		PreparedStatement ps;

		ps = connection.prepareStatement("SELECT * FROM matches_team WHERE season = ? AND round = ? AND league_id = ? order by match_id"); //$NON-NLS-1$
		ps.setInt(1, round.getLeagueSeason().getSeason());
		ps.setInt(2, round.getRoundNumber());
		ps.setInt(3, round.getLeagueSeason().getLeagueID());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Match match = new LeagueMatchDto(rs).getMatch();

			match.setAwayTeam(clubMap.get(match.getAwayTeamID()));
			match.setHomeTeam(clubMap.get(match.getHomeTeamID()));

			match.setAwayTeamStats(getTeamStats(match, match.getAwayTeamID()));
			if (match.getAwayTeamStats() != null) {
				match.getAwayTeamStats().setPlayersStats(getPlayersStats(match, match.getAwayTeamID()));
			}
			match.setHomeTeamStats(getTeamStats(match, match.getHomeTeamID()));
			if (match.getHomeTeamStats() != null) {
				match.getHomeTeamStats().setPlayersStats(getPlayersStats(match, match.getHomeTeamID()));
			}
			match.setLeague(round.getLeagueSeason().getLeague());
			alMatches.add(match);
		}
		rs.close();
		ps.close();

		return alMatches;
	}

	public int getNumberOfRounds(League league) throws SQLException {
		PreparedStatement ps;
		int numberOfRounds = 0;
		ps = connection
				.prepareStatement("SELECT count(round) as number_of_rounds FROM leagues as l left join matches_team as mt on (l.league_id = mt.league_id) where l.league_id = ? group by round having count(round) = 4"); //$NON-NLS-1$
		ps.setInt(1, league.getLeagueID());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			numberOfRounds = rs.getInt("number_of_rounds"); //$NON-NLS-1$
		}
		rs.close();
		ps.close();
		return numberOfRounds;
	}

	public List<PlayerStats> getPlayersStats(Match match, int teamID) throws SQLException {
		List<PlayerStats> alPlayersStats = new ArrayList<PlayerStats>();
		PreparedStatement ps;

		ps = connection.prepareStatement("SELECT * FROM players_stats as ps LEFT JOIN player_archive as p ON ps.player_id=p.player_id WHERE match_id = ? AND ps.team_id = ? order by number, time_in"); //$NON-NLS-1$
		ps.setInt(1, match.getMatchID());
		ps.setInt(2, teamID);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			PlayerStatsDto playerStats = new PlayerStatsDto(rs).getPlayerStats();
			playerStats.setPlayer(new PlayerArchiveDto(rs).getPlayerArchive().toPlayer());
			alPlayersStats.add(playerStats);
		}
		rs.close();
		ps.close();

		return alPlayersStats;
	}

	public List<PlayerStats> getPlayersStats(Match match, int teamID, Map<Integer, Player> hmPlayers, Map<Integer, PlayerArchive> archivePlayerMap) throws SQLException {
		List<PlayerStats> alPlayersStats = new ArrayList<PlayerStats>();
		PreparedStatement ps;

		ps = connection.prepareStatement("SELECT * FROM players_stats WHERE match_id = ? AND team_id = ? order by number, time_in"); //$NON-NLS-1$
		ps.setInt(1, match.getMatchID());
		ps.setInt(2, teamID);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			PlayerStatsDto playerStats = new PlayerStatsDto(rs).getPlayerStats();

			if (hmPlayers.get(playerStats.getPlayerID()) != null) {
				playerStats.setPlayer(hmPlayers.get(playerStats.getPlayerID()));
			} else if (archivePlayerMap.get(playerStats.getPlayerID()) != null) {
				playerStats.setPlayer(archivePlayerMap.get(playerStats.getPlayerID()).toPlayer());
			}

			playerStats.setMatch(match);
			Player player = hmPlayers.get(playerStats.getPlayerID());
			if (player != null) {
				player.getPlayerMatchStatistics().add(playerStats);
			}
			alPlayersStats.add(playerStats);
		}
		rs.close();
		ps.close();

		return alPlayersStats;
	}

	public List<LeagueRound> getRounds(LeagueSeason leagueSeason, Map<Integer, Club> clubMap) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;
		List<LeagueRound> alRounds = new ArrayList<LeagueRound>();

		ps = connection.prepareStatement("SELECT DISTINCT round FROM matches_team WHERE league_id = ? AND season = ? order by round"); //$NON-NLS-1$
		ps.setInt(1, leagueSeason.getLeagueID());
		ps.setInt(2, leagueSeason.getSeason());
		rs = ps.executeQuery();
		while (rs.next()) {
			LeagueRound round = new LeagueRoundDto(rs).getSeasonRound();
			round.setLeagueSeason(leagueSeason);
			round.setMatches(getMatches(round, clubMap));
			round.setLeagueTeams(getLeagueTeam(round, clubMap));
			alRounds.add(round);
		}
		rs.close();
		ps.close();
		return alRounds;
	}

	public LeagueRound getRound(LeagueSeason leagueSeason, Map<Integer, Club> clubMap) throws SQLException {
		LeagueRound round = new LeagueRound();
		round.setRound(leagueSeason.getRound());
		round.setLeagueSeason(leagueSeason);
		round.setMatches(getMatches(round, clubMap));
		round.setLeagueTeams(getLeagueTeam(round, clubMap));
		return round;
	}

	public List<LeagueRound> getRoundsBySeason(LeagueSeason leagueSeason) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;
		List<LeagueRound> alRounds = new ArrayList<LeagueRound>();

		ps = connection.prepareStatement("SELECT season,league_id FROM season WHERE league_id = ? order by season"); //$NON-NLS-1$
		ps.setInt(1, leagueSeason.getLeagueID());
		rs = ps.executeQuery();
		while (rs.next()) {
			LeagueRound round = new LeagueRoundDto(rs).getSeasonRound();
			round.setLeagueSeason(leagueSeason);
			alRounds.add(round);
		}
		rs.close();
		ps.close();
		return alRounds;
	}

	public List<LeagueSeason> getLeagueSeasons(Map<Integer, League> leaguesMap, Map<Integer, Club> clubMap) throws SQLException {
		List<LeagueSeason> leagueSeasons = new ArrayList<LeagueSeason>();
		PreparedStatement ps;

		// ps = connection.prepareStatement("SELECT DISTINCT
		// season,league_id FROM matches_team WHERE league_id = ? order by
		// season");
		ps = connection.prepareStatement("SELECT DISTINCT m.season,m.league_id, week/16 as raw_season FROM matches_team as m join leagues as l on(m.league_id = l.league_id) WHERE l.is_official = 1 and l.is_cup = 0 and l.type = 0 and m.week = (select min(week) from matches_team where league_id = m.league_id and season = m.season) order by raw_season desc");//$NON-NLS-1$
//				.prepareStatement("SELECT DISTINCT season,league_id, week/16 as raw_season FROM matches_team as m WHERE league_id = ? and week = (select min(week) from matches_team where league_id = m.league_id and season = m.season) order by season desc"); //$NON-NLS-1$

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			LeagueSeason leagueSeason = new LeagueSeasonDto(rs).getLeagueSeason();
			leagueSeason.setLeague(leaguesMap.get(leagueSeason.getLeagueID()));
			leagueSeason.setRounds(getRounds(leagueSeason, clubMap));
			leagueSeasons.add(leagueSeason);
		}
		rs.close();
		ps.close();

		return leagueSeasons;

	}
	
//	public List<LeagueSeason> getSeason(League league, Map<Integer, Club> clubMap) throws SQLException {
//		List<LeagueSeason> leagueSeasons = new ArrayList<LeagueSeason>();
//		PreparedStatement ps;
//
//		// ps = connection.prepareStatement("SELECT DISTINCT
//		// season,league_id FROM matches_team WHERE league_id = ? order by
//		// season");
//		ps = connection
//				.prepareStatement("SELECT DISTINCT season,league_id, week/16 as raw_season FROM matches_team as m WHERE league_id = ? and week = (select min(week) from matches_team where league_id = m.league_id and season = m.season) order by season desc"); //$NON-NLS-1$
//
//		ResultSet rs = ps.executeQuery();
//
//		while (rs.next()) {
//			LeagueSeason leagueSeason = new LeagueSeasonDto(rs).getLeagueSeason();
//			leagueSeason.setLeague(getLeague(leagueSeason.getLeagueID()));
//			leagueSeason.setAlRounds(getRounds(leagueSeason, clubMap));
//			leagueSeasons.add(leagueSeason);
//		}
//		rs.close();
//		ps.close();
//
//		return leagueSeasons;
//
//	}

//	public ArrayList<LeagueSeason> getSeasonByLeague(League league) throws SQLException {
//		ArrayList<LeagueSeason> alLeagueSeason = new ArrayList<LeagueSeason>();
//		PreparedStatement ps;
//
//		ps = connection.prepareStatement("SELECT season,league_id FROM season WHERE league_id = ? order by season"); //$NON-NLS-1$
//		ps.setInt(1, league.getLeagueID());
//		ResultSet rs = ps.executeQuery();
//
//		while (rs.next()) {
//			LeagueSeason leagueSeason = new LeagueSeasonDto(rs).getLeagueSeason();
//			leagueSeason.setLeague(league);
//			alLeagueSeason.add(leagueSeason);
//		}
//		rs.close();
//		ps.close();
//
//		return alLeagueSeason;
//
//	}

	public List<LeagueSeason> getUncompletedRounds() throws SQLException {
		PreparedStatement ps;
		List<LeagueSeason> leagues = new ArrayList<LeagueSeason>();
		ps = connection
				.prepareStatement("select l.league_id, season, round from leagues as l left join matches_team as m on (l.league_id = m.league_id ) where type = 0 and is_official = 1 and is_cup = 0 and m.is_finished = 1 group by l.league_id, season, round having count(round) = 4 except select ll.league_id, season, round from leagues as ll left join league_team as lt on (ll.league_id = lt.league_id ) where type = 0 and is_official = 1 and is_cup = 0 group by ll.league_id, season, round having count(round) = 8 except select lll.league_id, season, round from leagues as lll left join matches_team as lm on (lll.league_id = lm.league_id) where type = 0 and is_official = 1 and is_cup = 0 and round <> 0 and lll.league_id in (select l1.league_id from leagues as l1 left join matches_team as mt1 on (l1.league_id = mt1.league_id) where l1.league_id = lll.league_id and mt1.season = lm.season and type = 0 and is_official = 1 and is_cup = 0 and round <> 0 group by l1.league_id, season having count(*) < 56) "); //$NON-NLS-1$ // and mt1.is_finished = 1 and lll.league_id not in (select distinct league_id from matches_team where week = (select max(week) from matches_team))
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			LeagueSeason league = new LeagueSeason();
			league.setLeagueID(rs.getInt("league_id")); //$NON-NLS-1$
			league.setSeason(rs.getInt("season")); //$NON-NLS-1$
			league.setRound(rs.getInt("round")); //$NON-NLS-1$
			leagues.add(league);
		}
		rs.close();
		ps.close();
		return leagues;
	}

	// public ArrayList<League> getCompletedRounds() {
	// PreparedStatement ps;
	// ArrayList<League> leagues = new ArrayList<League>();
	// try {
	// ps = connection.prepareStatement("select league_id, season, round " +
	// "from leagues as l " +
	// "left join league_team as lt on (l.league_id = lt.league_id ) " +
	// "where type = 0 and is_official = 1 and is_cup = 0 " +
	// "group by league_id, season, round " +
	// "having count(round) = 8 " +
	// "order by league_id, season, round;");
	// ResultSet rs = ps.executeQuery();
	//
	// while (rs.next()) {
	// League league = new LeagueDto(rs).getLeague();
	// leagues.add(league);
	// }
	// rs.close();
	// ps.close();
	// } catch (SQLException e) {
	// new SVLogger(Level.WARNING, "Sql Class", e).showError();
	// }
	// return leagues;
	// }

	public List<Match> getUncompletedLeague(int teamID) throws SQLException {
		List<Match> alMatchesId = new ArrayList<Match>();
		Match match;
		League league;
		PreparedStatement ps;

		ps = connection
				.prepareStatement("select * from leagues as l, matches_team as m " + "where m.league_id = l.league_id and m.is_finished = 1 " + " and l.league_id in (" + "	select l2.league_id from matches_team as m2, leagues as l2" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						+ "	 where m2.is_finished = 1 and l2.league_id = m2.league_id" + "	 and l2.league_id in (" + "		select l3.league_id from matches_team as m3, leagues as l3 where" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ "		l3.is_official = 1 and l3.is_cup = 0 and l3.type = 0 and l3.league_id = m3.league_id and (home_team_id = ? or away_team_id = ?)" + "		and m3.week/16 <> (select max(week)/16 from matches_team)" + "	 )" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ "	 group by l2.league_id, season having count(*) < 56 order by l2.league_id desc, season limit 1" + " )" + " and season in (" + "	select m2.season from matches_team as m2, leagues as l2" + "	 where  m2.is_finished = 1 and l2.league_id = m2.league_id" + "	 and l2.league_id in (" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
						+ "		select distinct m3.league_id from matches_team as m3, leagues as l3 where" + "		l3.is_official = 1 and l3.is_cup = 0 and l3.type = 0 and l3.league_id = m3.league_id and (m3.home_team_id = ? or m3.away_team_id = ?)" //$NON-NLS-1$ //$NON-NLS-2$
						+ "		and m3.week/16 <> (select max(week)/16 from matches_team)" + "	 )" + "	 group by m2.league_id, m2.season having count(*) < 56 order by m2.league_id desc, m2.season limit 1" + " ) order by match_id"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ps.setInt(1, teamID);
		ps.setInt(2, teamID);
		ps.setInt(3, teamID);
		ps.setInt(4, teamID);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			match = new LeagueMatchDto(rs).getMatch();
			league = new LeagueDto(rs).getLeague();
			match.setLeague(league);
			alMatchesId.add(match);
		}
		rs.close();
		ps.close();

		return alMatchesId;
	}

	public TeamStats getTeamStats(Match match, int teamID) throws SQLException {
		PreparedStatement ps;
		TeamStats teamStats = new TeamStats(true);
		ps = connection
				.prepareStatement("SELECT *, (select count(rating) from players_stats as p where t.match_id = p.match_id and t.team_id = p.team_id and number < 12) as players_count,(select sum(rating) from players_stats as p where t.match_id = p.match_id and t.team_id = p.team_id and number < 12) as rating_sum from team_stats as t where t.match_id = ? and t.team_id = ?"); //$NON-NLS-1$
		// ps = SQLSession.getConn().prepareStatement("SELECT * FROM
		// team_stats
		// WHERE match_id = ? AND team_id = ? ");
		ps.setInt(1, match.getMatchID());
		ps.setInt(2, teamID);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			teamStats = new TeamStatsDto(rs).getTeamStats();
			teamStats.setMatch(match);
		}
		rs.close();
		ps.close();

		return teamStats;
	}

	public boolean existsFinishedMatch(int matchID) throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(match_id) FROM matches_team WHERE match_id = ? AND is_finished = 1"); //$NON-NLS-1$
		ps.setInt(1, matchID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) == 1) {
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

	public boolean existsLeague(int leagueID) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(league_id) FROM leagues WHERE league_id = ?"); //$NON-NLS-1$
		ps.setInt(1, leagueID);
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

	public boolean existsLeagueTeam(League league, LeagueTeam team) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(league_id) FROM league_team WHERE league_id = ? AND team_id = ? AND season = ? AND round = ?"); //$NON-NLS-1$
		ps.setInt(1, league.getLeagueID());
		ps.setInt(2, team.getTeamID());
		ps.setInt(3, league.getSeason());
		ps.setInt(4, team.getRound());

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

	public boolean existsMatch(int matchID) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(match_id) FROM matches_team WHERE match_id = ?"); //$NON-NLS-1$
		ps.setLong(1, matchID);
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

	public boolean existsTeamStats(int matchID, int teamID) throws SQLException {

		PreparedStatement ps;
		ps = connection.prepareStatement("SELECT count(match_id) FROM team_stats WHERE match_id = ? AND team_id = ?"); //$NON-NLS-1$
		ps.setLong(1, matchID);
		ps.setInt(2, teamID);
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

	public void addLeague(League league) throws SQLException {
		PreparedStatement pstm;
		pstm = connection.prepareStatement("INSERT INTO  LEAGUES ( LEAGUE_ID , NAME , COUNTRY_ID , DIVISION , TYPE , IS_OFFICIAL , IS_CUP , USER_ID ) VALUES (?,?,?,?,?,?,?,?)"); //$NON-NLS-1$
		pstm.setInt(1, league.getLeagueID());
		pstm.setString(2, league.getName());
		pstm.setInt(3, league.getCountryID());
		pstm.setInt(4, league.getDivision());
		pstm.setInt(5, league.getType());
		pstm.setInt(6, league.getIsOfficial());
		pstm.setInt(7, league.getIsCup());
		pstm.setInt(8, league.getUserID());
		pstm.executeUpdate();
		pstm.close();
	}

	public void addLeagueTeam(League league, LeagueTeam team) throws SQLException {

		PreparedStatement pstm;
		pstm = connection
				.prepareStatement("INSERT INTO LEAGUE_TEAM (TEAM_ID , LEAGUE_ID, SEASON, ROUND , POINTS , WINS , DRAWS , LOSSES , GOALS_SCORED , GOALS_LOST , RANK_TOTAL ) VALUES (?,?,?,?,?,?,?,?,?,?,?)"); //$NON-NLS-1$
		pstm.setInt(1, team.getTeamID());
		pstm.setInt(2, league.getLeagueID());
		pstm.setInt(3, league.getSeason());
		pstm.setInt(4, team.getRound());
		pstm.setInt(5, team.getPoints());
		pstm.setInt(6, team.getWins());
		pstm.setInt(7, team.getDraws());
		pstm.setInt(8, team.getLosses());
		pstm.setInt(9, team.getGoalsScored());
		pstm.setInt(10, team.getGoalsLost());
		pstm.setString(11, team.getRankTotal());
		pstm.executeUpdate();
		pstm.close();
	}

	public void addLeagueTeam(LeagueTeam team) throws SQLException {

		PreparedStatement pstm;
		pstm = connection
				.prepareStatement("INSERT INTO LEAGUE_TEAM (TEAM_ID , LEAGUE_ID, SEASON, ROUND , POINTS , WINS , DRAWS , LOSSES , GOALS_SCORED , GOALS_LOST , RANK_TOTAL ) VALUES (?,?,?,?,?,?,?,?,?,?,?)"); //$NON-NLS-1$
		pstm.setInt(1, team.getTeamID());
		pstm.setInt(2, team.getLeagueID());
		pstm.setInt(3, team.getSeason());
		pstm.setInt(4, team.getRound());
		pstm.setInt(5, team.getPoints());
		pstm.setInt(6, team.getWins());
		pstm.setInt(7, team.getDraws());
		pstm.setInt(8, team.getLosses());
		pstm.setInt(9, team.getGoalsScored());
		pstm.setInt(10, team.getGoalsLost());
		pstm.setString(11, team.getRankTotal());
		pstm.executeUpdate();
		pstm.close();
	}

	public void addMatch(Match match) throws SQLException {
		PreparedStatement pstm;
		pstm = connection
				.prepareStatement("INSERT INTO MATCHES_TEAM ( MATCH_ID, HOME_TEAM_ID, AWAY_TEAM_ID, HOME_TEAM_NAME, AWAY_TEAM_NAME, LEAGUE_ID, ROUND, SEASON, WEEK, DAY, DATE_EXPECTED, DATE_STARTED, HOME_TEAM_SCORE, AWAY_TEAM_SCORE, SUPPORTERS, WEATHER, IS_FINISHED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); //$NON-NLS-1$
		pstm.setInt(1, match.getMatchID());
		pstm.setInt(2, match.getHomeTeamID());
		pstm.setInt(3, match.getAwayTeamID());
		pstm.setString(4, match.getHomeTeamName());
		pstm.setString(5, match.getAwayTeamName());
		pstm.setInt(6, match.getLeagueID());
		pstm.setInt(7, match.getRound());
		pstm.setInt(8, match.getSeason());
		pstm.setInt(9, match.getWeek());
		pstm.setInt(10, match.getDay());
		pstm.setTimestamp(11, match.getDateExpected().getTimestamp());
		pstm.setTimestamp(12, match.getDateStarted().getTimestamp());
		pstm.setInt(13, match.getHomeTeamScore());
		pstm.setInt(14, match.getAwayTeamScore());
		pstm.setInt(15, match.getSupporters());
		pstm.setInt(16, match.getWeather());
		pstm.setInt(17, match.getIsFinished());

		pstm.executeUpdate();
		pstm.close();

	}

	public void updateMatch(Match match) throws SQLException {
		PreparedStatement ps;
		ps = connection
				.prepareStatement("UPDATE matches_team SET date_started=?, home_team_score=?, away_team_score=?, supporters=?, weather=?, is_finished=?, home_team_name=?, away_team_name=?, date_expected = ? WHERE match_id = ?"); //$NON-NLS-1$

		ps.setTimestamp(1, match.getDateStarted().getTimestamp());
		ps.setInt(2, match.getHomeTeamScore());
		ps.setInt(3, match.getAwayTeamScore());
		ps.setInt(4, match.getSupporters());
		ps.setInt(5, match.getWeather());
		ps.setInt(6, match.getIsFinished());
		ps.setString(7, match.getHomeTeamName());
		ps.setString(8, match.getAwayTeamName());
		ps.setTimestamp(9, match.getDateExpected().getTimestamp());
		ps.setInt(10, match.getMatchID());

		ps.executeUpdate();
		ps.close();

	}

	public HashMap<Integer, Integer> getTeamRating(LeagueRound leagueRound) throws SQLException {
		PreparedStatement ps;
		HashMap<Integer, Integer> teamRating = new LinkedHashMap<Integer, Integer>();
		ps = connection.prepareStatement("select team_id, (sum(rating_scoring)+sum(rating_passing)+sum(rating_defending))/(3*?) as team_rating from team_stats where match_id in (select match_id from matches_team where league_id = ? and round <= ? and season = ?) group by team_id order by team_rating desc, team_id limit 10;"); //$NON-NLS-1$
		ps.setInt(1, leagueRound.getLeagueSeason().getLeagueID());
		ps.setInt(2, leagueRound.getRoundNumber());
		ps.setInt(3, leagueRound.getLeagueSeason().getSeason());
		ps.setInt(4, leagueRound.getRoundNumber());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			teamRating.put(rs.getInt("team_id"), rs.getInt("team_rating"));
		}
		rs.close();
		ps.close();
		return teamRating;
	}
	
	public HashMap<Integer, Integer> getAverageTeamRating(LeagueRound leagueRound) throws SQLException {
		PreparedStatement ps;
		HashMap<Integer, Integer> avgTeamRating = new LinkedHashMap<Integer, Integer>();
		ps = connection.prepareStatement("select team_id, avg(rating) as avg_team_rating from players_stats where match_id in (select match_id from matches_team where league_id = ? and round <= ? and season = ?) and rating > 0 group by team_id order by avg_team_rating desc, team_id limit 10;"); //$NON-NLS-1$
		ps.setInt(1, leagueRound.getLeagueSeason().getLeagueID());
		ps.setInt(2, leagueRound.getRoundNumber());
		ps.setInt(3, leagueRound.getLeagueSeason().getSeason());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			avgTeamRating.put(rs.getInt("team_id"), rs.getInt("avg_team_rating"));
		}
		rs.close();
		ps.close();
		return avgTeamRating;
	}
	
	public HashMap<Integer, Integer> getAveragePlayerRating(LeagueRound leagueRound) throws SQLException {
		PreparedStatement ps;
		HashMap<Integer, Integer> avgRating = new LinkedHashMap<Integer, Integer>();
		ps = connection.prepareStatement("select player_id, avg(rating) as avg_rating from players_stats where match_id in (select match_id from matches_team where league_id = ? and round <= ? and season = ?) group by player_id having avg(rating) > 0 order by avg_rating desc, player_id limit 10;"); //$NON-NLS-1$
		ps.setInt(1, leagueRound.getLeagueSeason().getLeagueID());
		ps.setInt(2, leagueRound.getRoundNumber());
		ps.setInt(3, leagueRound.getLeagueSeason().getSeason());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			avgRating.put(rs.getInt("player_id"), rs.getInt("avg_rating"));
		}
		rs.close();
		ps.close();
		return avgRating;
	}
	
	public int getSupporters(LeagueRound leagueRound) throws SQLException {
		PreparedStatement ps;
		int supporters = 0;
		ps = connection.prepareStatement("select avg(supporters) as avg_supporters from matches_team where league_id = ? and round <= ? and season = ?;"); //$NON-NLS-1$
		ps.setInt(1, leagueRound.getLeagueSeason().getLeagueID());
		ps.setInt(2, leagueRound.getRoundNumber());
		ps.setInt(3, leagueRound.getLeagueSeason().getSeason());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			supporters = rs.getInt("avg_supporters");
		}
		rs.close();
		ps.close();
		return supporters;
	}
	
	public HashMap<Integer, Integer> getShoots(LeagueRound leagueRound) throws SQLException {
		PreparedStatement ps;
		HashMap<Integer, Integer> shoots = new LinkedHashMap<Integer, Integer>();
		ps = connection.prepareStatement("select player_id, sum(shoots) as sum_shoots from players_stats where match_id in (select match_id from matches_team where league_id = ? and round <= ? and season = ?) group by player_id having sum(shoots) > 0 order by sum_shoots desc, player_id limit 10;"); //$NON-NLS-1$
		ps.setInt(1, leagueRound.getLeagueSeason().getLeagueID());
		ps.setInt(2, leagueRound.getRoundNumber());
		ps.setInt(3, leagueRound.getLeagueSeason().getSeason());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			shoots.put(rs.getInt("player_id"), rs.getInt("sum_shoots"));
		}
		rs.close();
		ps.close();
		return shoots;
	}
	
	public HashMap<Integer, Integer> getGoals(LeagueRound leagueRound) throws SQLException {
		PreparedStatement ps;
		HashMap<Integer, Integer> goals = new LinkedHashMap<Integer, Integer>();
		ps = connection.prepareStatement("select player_id, sum(goals) as sum_goals from players_stats where match_id in (select match_id from matches_team where league_id = ? and round <= ? and season = ?) group by player_id having sum(goals) > 0 order by sum_goals desc, player_id limit 10;"); //$NON-NLS-1$
		ps.setInt(1, leagueRound.getLeagueSeason().getLeagueID());
		ps.setInt(2, leagueRound.getRoundNumber());
		ps.setInt(3, leagueRound.getLeagueSeason().getSeason());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			goals.put(rs.getInt("player_id"), rs.getInt("sum_goals"));
		}
		rs.close();
		ps.close();
		return goals;
	}
	
	public HashMap<Integer, Integer> getAssists(LeagueRound leagueRound) throws SQLException {
		PreparedStatement ps;
		HashMap<Integer, Integer> assists = new LinkedHashMap<Integer, Integer>();
		ps = connection.prepareStatement("select player_id, sum(assists) as sum_assists from players_stats where match_id in (select match_id from matches_team where league_id = ? and round <= ? and season = ?) group by player_id having sum(assists) > 0 order by sum_assists desc, player_id limit 10;"); //$NON-NLS-1$
		ps.setInt(1, leagueRound.getLeagueSeason().getLeagueID());
		ps.setInt(2, leagueRound.getRoundNumber());
		ps.setInt(3, leagueRound.getLeagueSeason().getSeason());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			assists.put(rs.getInt("player_id"), rs.getInt("sum_assists"));
		}
		rs.close();
		ps.close();
		return assists;
	}
	
	public HashMap<Integer, Integer> getFouls(LeagueRound leagueRound) throws SQLException {
		PreparedStatement ps;
		HashMap<Integer, Integer> fouls = new LinkedHashMap<Integer, Integer>();
		ps = connection.prepareStatement("select player_id, sum(fouls) as sum_fouls from players_stats where match_id in (select match_id from matches_team where league_id = ? and round <= ? and season = ?) group by player_id having sum(fouls) > 0 order by sum_fouls desc, player_id limit 10;"); //$NON-NLS-1$
		ps.setInt(1, leagueRound.getLeagueSeason().getLeagueID());
		ps.setInt(2, leagueRound.getRoundNumber());
		ps.setInt(3, leagueRound.getLeagueSeason().getSeason());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			fouls.put(rs.getInt("player_id"), rs.getInt("sum_fouls"));
		}
		rs.close();
		ps.close();
		return fouls;
	}
}
