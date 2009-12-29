package pl.pronux.sokker.actions;

import java.sql.SQLException;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.JuniorsDao;
import pl.pronux.sokker.data.sql.dao.PersonsDao;
import pl.pronux.sokker.data.sql.dao.PlayersArchiveDao;
import pl.pronux.sokker.data.sql.dao.PlayersDao;
import pl.pronux.sokker.data.sql.dao.TrainersDao;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Person;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerArchive;

public class PersonsManager {

	public static void restorePersonFromTrash(Person person) throws SQLException {
		boolean newConnection = false;
		try {
			if (SQLSession.getConnection().isClosed()) {
				SQLSession.connect();
				newConnection = true;
			}

			if(person.getStatus() >= 10 && person.getStatus() < 20) {
				person.restoreFromTrash();
				new PersonsDao(SQLSession.getConnection()).updatePersonStatus(person);
		
				if (person instanceof Junior) {
		
					Junior junior = (Junior) person;
					Cache.getJuniorsTrash().remove(junior);
		
					if (person.getStatus() == Junior.STATUS_SACKED) {
						Cache.getJuniorsFired().add(junior);
					} else if (person.getStatus() == Junior.STATUS_TRAINED) {
						Cache.getJuniorsTrained().add(junior);
					}
				} else if (person instanceof Player) {
					Cache.getPlayersHistory().add((Player) person);
					Cache.getPlayersTrash().remove((Player) person);
				} else if (person instanceof Coach) {
					Cache.getCoachesFired().add((Coach) person);
					Cache.getCoachesTrash().remove((Coach) person);
				}
			}
	
			if (newConnection) {
				SQLSession.close();
			}
		} catch (SQLException e) {
			SQLSession.close();
			throw e;
		}
	}

	public static void movePersonToTrash(Person person) throws SQLException {
		boolean newConnection = false;
		try {
			if (SQLSession.getConnection().isClosed()) {
				SQLSession.connect();
				newConnection = true;
			}

			if(person.getStatus() < 10) {

				if (person instanceof Junior) {
					Junior junior = (Junior) person;
					Cache.getJuniorsTrash().add(junior);
					if (junior.getStatus() == Junior.STATUS_SACKED) {
						Cache.getJuniorsFired().remove(junior);
					} else if (junior.getStatus() == Junior.STATUS_TRAINED) {
						Cache.getJuniorsTrained().remove(junior);
					}
				} else if (person instanceof Player) {
					Player player = (Player) person;
					Cache.getPlayersTrash().add(player);
					Cache.getPlayersHistory().remove(player);
				} else if (person instanceof Coach) {
					Coach coach = (Coach) person;
					Cache.getCoachesTrash().add(coach);
					Cache.getCoachesFired().remove(coach);
				}
		
				person.moveToTrash();
				new PersonsDao(SQLSession.getConnection()).updatePersonStatus(person);
			}
	
			if (newConnection) {
				SQLSession.close();
			}
		} catch (SQLException e) {
			SQLSession.close();
			throw e;
		}
	}

	public static void removePersonFromTrash(Person person) throws SQLException {
		boolean newConnection = false;
		try {
			if (SQLSession.getConnection().isClosed()) {
				SQLSession.connect();
				newConnection = true;
			}

			if(person.getStatus() >= 10 && person.getStatus() < 20) {
				if (person instanceof Junior) {
					Cache.getJuniorsTrash().remove(person);
				} else if (person instanceof Player) {
					Cache.getPlayersTrash().remove(person);
				} else if (person instanceof Coach) {
					Cache.getCoachesTrash().remove(person);
				}
		
				person.removeFromTrash();
				new PersonsDao(SQLSession.getConnection()).updatePersonStatus(person);
			}
	
			if (newConnection) {
				SQLSession.close();
			}
		} catch (SQLException e) {
			SQLSession.close();
			throw e;
		}
	}

	public static void updatePersonNote(Person person) throws SQLException {
		try {
			SQLSession.connect();
			if (person instanceof Player) {
				new PlayersDao(SQLSession.getConnection()).updatePlayerNote(person);
			} else if (person instanceof Junior) {
				new JuniorsDao(SQLSession.getConnection()).updateJuniorNote(person);
			} else if (person instanceof Coach) {
				new TrainersDao(SQLSession.getConnection()).updateCoachNote(person);
			} else if (person instanceof PlayerArchive) {
				new PlayersArchiveDao(SQLSession.getConnection()).updatePlayerArchiveNote(person);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLSession.close();
		}
	
	}

}
