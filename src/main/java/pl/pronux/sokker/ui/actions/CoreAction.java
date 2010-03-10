package pl.pronux.sokker.ui.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import pl.pronux.sokker.actions.AssistantManager;
import pl.pronux.sokker.actions.ConfigurationManager;
import pl.pronux.sokker.actions.CountriesManager;
import pl.pronux.sokker.actions.LeaguesManager;
import pl.pronux.sokker.actions.MatchesManager;
import pl.pronux.sokker.actions.PlayersManager;
import pl.pronux.sokker.actions.SchedulerManager;
import pl.pronux.sokker.actions.TeamManager;
import pl.pronux.sokker.actions.TrainersManager;
import pl.pronux.sokker.bean.SynchronizerConfiguration;
import pl.pronux.sokker.comparators.CountryComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.downloader.Synchronizer;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.DbProperties;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.model.Transfer;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.utils.Log;
import pl.pronux.sokker.utils.file.Database;

public class CoreAction implements IRunnableWithProgress {

	private boolean update;

	public final static boolean LOCK = false;
	public final static boolean UNLOCK = true;
	public static boolean lock = UNLOCK;

	private MatchesManager matchesManager = MatchesManager.instance();
	private AssistantManager assistantManager = AssistantManager.instance();
	private CountriesManager countriesManager = CountriesManager.instance();
	private ConfigurationManager configurationManager = ConfigurationManager.instance();
	private SchedulerManager schedulerManager = SchedulerManager.instance();
	private TrainersManager trainersManager = TrainersManager.instance();
	private TeamManager teamManager = TeamManager.instance();
	private LeaguesManager leaguesManager = LeaguesManager.instance();
	private PlayersManager playersManager = PlayersManager.instance();

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public CoreAction(boolean update) {
		this.update = update;
	}

	public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		if (lock == LOCK) {
			monitor.interrupt();
			return;
		} else {
			lock = LOCK;
		}
		if (DisplayHandler.getDisplay().isDisposed()) {
			monitor.interrupt();
			return;
		}

		// FIXME: if some players will be removed from then there will be null
		// pointer error

		// try to use some method in viewer to point to main nodes

		// TreeItem[] selectTreeItem = {
		// Cache.getTree().getTopItem()
		// };
		// Cache.getTree().setSelection(selectTreeItem);
		// Cache.getTree().setEnabled(false);

		ViewerHandler.getViewer().clear();

		try {
			SokkerViewerSettings settings = SettingsHandler.getSokkerViewerSettings();
			SQLQuery.setSettings(settings);
			DbProperties dbProperties = null;
			monitor.setTaskName(Messages.getString("CoreAction.database.library.loading")); 
			SynchronizerConfiguration synchronizerConfiguration = new SynchronizerConfiguration();
			if (!SQLQuery.dbExist()) {
				monitor.setTaskName(Messages.getString("progressBar.info.database.initialization")); 
				String dbFile = settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".script"; 
				String dbLogFile = settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + settings.getUsername() + ".log"; 
				String dbPropertiesFile = settings.getBaseDirectory() + File.separator
										  + "db" + File.separator + "db_file_" + settings.getUsername() + ".properties"; 
				try {
					SQLSession.connect();
					SQLSession.beginTransaction();
					SQLQuery.initDB();
					SQLSession.commit();
					SQLSession.endTransaction();
				} catch (SQLException e) {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
						Log.error("Problem during closing database after bad initialization");
					}
					new File(dbFile).delete();
					new File(dbPropertiesFile).delete();
					new File(dbLogFile).delete();
					throw new SVException("DB file error: deleted", e);
				}
				synchronizerConfiguration.checkDownloadAll();
				new Synchronizer(settings, synchronizerConfiguration).run(monitor);
			} else {
				SQLSession.connect();
				monitor.setTaskName(Messages.getString("progressBar.info.database.connection")); //$NON-NLS-1$
				if (SQLQuery.dbPropertiesExist()) {

					configurationManager.updateDbStructure(SV.DB_VERSION);

					dbProperties = configurationManager.getDbProperties();

					if (dbProperties != null) {
						synchronizerConfiguration.setDownloadCountries(dbProperties.isCheckCountries());
						synchronizerConfiguration.setRepairDb(dbProperties.isRepairDB());
						synchronizerConfiguration.setRepairDbJuniorsAge(dbProperties.isCompleteJuniorsAge());
					}

					if (this.isUpdate() || configurationManager.getMaxDate() == null || (dbProperties != null && dbProperties.isCheckDbUpdate())) {
						synchronizerConfiguration.setDownloadBase(true);
					}
					new Synchronizer(settings, synchronizerConfiguration).run(monitor);

					if (synchronizerConfiguration.isDownloadBase()) {
						int counter = dbProperties.getScanCounter();

						String directory = settings.getBaseDirectory() + File.separator + "xml" + File.separator + settings.getUsername();
						if (counter % 30 == 0) {
							File file = new File(directory);
							if (file.exists() && file.listFiles().length > 10000) {
								file.renameTo(new File(settings.getBaseDirectory() + File.separator + "xml" + File.separator + settings.getUsername() + "_"
													   + Calendar.getInstance().getTimeInMillis()));
								new File(settings.getBaseDirectory() + File.separator + "xml" + File.separator + settings.getUsername()).mkdir();
							}
						}
						counter++;
						configurationManager.updateScanCounter(counter);
						dbProperties.setScanCounter(counter);
					}
				} else {
					throw new SVException("Database properties file doesn't exists. Please contact with SV developer!");
				}
			}

			try {
				Database.backup(settings, "autobackup-" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + ".bak"); 
			} catch (IOException ioe) {
				throw new SVException("Synchronizer -> post-autobackup failed", ioe);
			}
			// if (!value.equals("0")) { //$NON-NLS-1$
			//				
			// return;
			// }
			monitor.beginTask(Messages.getString("CoreAction.info"), 17);
			
			monitor.subTask(Messages.getString("progressBar.info.database.connection")); 

			final Date sokkerDate = configurationManager.getMaxDate();
			Cache.setDate(sokkerDate);

			monitor.worked(1);
			monitor.subTask(Messages.getString("statusBar.lastUpdateLabel.text") + " " + sokkerDate.toDateTimeString()); 

			Junior.minimumPop = configurationManager.getJuniorMinimumPop();

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getNotesData")); 

			Cache.setNotes(schedulerManager.getNoteData());

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getClubData")); 

			Cache.setClub(teamManager.getTeam(configurationManager.getTeamID()));

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getCountries"));

			Cache.setCountries(countriesManager.getCountries());
			SVComparator<Country> countryComparator = new CountryComparator();
			countryComparator.setColumn(CountryComparator.NAME);
			countryComparator.setDirection(CountryComparator.ASCENDING);
			Collections.sort(Cache.getCountries(), countryComparator);
			Cache.setCountryMap(new HashMap<Integer, Country>());
			for (Country country : Cache.getCountries()) {
				Cache.getCountryMap().put(country.getCountryID(), country);
			}

			Money.setCurrency(Cache.getCountryMap().get(Cache.getClub().getCountry()).getCurrencyRate());
			Money.setCurrencySymbol(Cache.getCountryMap().get(Cache.getClub().getCountry()).getCurrencyName());

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getTransfersData")); //$NON-NLS-1$

			Cache.setTransfers(teamManager.getTransfers(Cache.getClub()));

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.completeTransfers")); //$NON-NLS-1$

			HashMap<Integer, Transfer> transfersSellMap = new HashMap<Integer, Transfer>();
			HashMap<Integer, Transfer> transfersBuyMap = new HashMap<Integer, Transfer>();
			for (Transfer transfer : Cache.getTransfers()) {
				if (transfer.getSellerTeamID() == Cache.getClub().getId()) {
					transfersSellMap.put(transfer.getPlayerID(), transfer);
				} else {
					transfersBuyMap.put(transfer.getPlayerID(), transfer);
				}
			}

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getCoachesData")); //$NON-NLS-1$

			Cache.setCoachesTrash(trainersManager.getCoachesFromTrashData());
			Cache.setCoaches(trainersManager.getCoachesData());
			Cache.setCoachesFired(trainersManager.getCoachesFiredData());

			ArrayList<Coach> coachDeleted = trainersManager.getCoachesDeletedData();

			// coach map for quick searching
			Cache.setCoachesMap(new HashMap<Integer, Coach>());
			for (Coach coach : Cache.getCoachesFired()) {
				Cache.getCoachesMap().put(coach.getId(), coach);
			}

			for (Coach coach : Cache.getCoachesTrash()) {
				Cache.getCoachesMap().put(coach.getId(), coach);
			}

			for (Coach coach : Cache.getCoaches()) {
				Cache.getCoachesMap().put(coach.getId(), coach);
			}

			for (Coach coach : coachDeleted) {
				Cache.getCoachesMap().put(coach.getId(), coach);
			}

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getTrainingData")); //$NON-NLS-1$

			Cache.setTrainings(teamManager.getTrainingData(Cache.getCoachesMap()));

			Cache.setTrainingsMap(new HashMap<Integer, Training>());
			for (Training training : Cache.getTrainings()) {
				Cache.getTrainingsMap().put(training.getId(), training);
			}

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getJuniorsData")); //$NON-NLS-1$

			Cache.setJuniors(teamManager.getJuniors(Cache.getTrainingsMap()));

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getJuniorsTrainedData")); //$NON-NLS-1$

			Cache.setJuniorsTrained(teamManager.getJuniorsTrained(Cache.getTrainingsMap()));

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getJuniorsFiredData")); //$NON-NLS-1$

			Cache.setJuniorsFired(teamManager.getJuniorsFired(Cache.getTrainingsMap()));
			Cache.setJuniorsTrash(teamManager.getJuniorsFromTrash(Cache.getTrainingsMap()));

			// mapa juniorow do szybkiego wyszukiwania
			HashMap<Integer, Junior> juniorTrainedMap = new HashMap<Integer, Junior>();
			for (Junior junior : Cache.getJuniorsTrained()) {
				juniorTrainedMap.put(junior.getId(), junior);
			}
			for (Junior junior : Cache.getJuniorsTrash()) {
				if (junior.getStatus() == Junior.STATUS_TRASH_TRAINED) {
					juniorTrainedMap.put(junior.getId(), junior);
				}
			}
			Cache.setJuniorsTrainedMap(juniorTrainedMap);

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getPlayersData")); //$NON-NLS-1$

			Cache.setPlayers(playersManager.getPlayers(Cache.getClub(), juniorTrainedMap, Cache.getTrainingsMap(), transfersSellMap, transfersBuyMap));

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getPlayersHistoryData")); //$NON-NLS-1$

			Cache.setPlayersHistory(playersManager.getPlayersHistoryData(Cache.getClub(), juniorTrainedMap, Cache.getTrainingsMap(), transfersSellMap,
																		 transfersBuyMap));
			Cache.setPlayersTrash(playersManager.getPlayersFromTrashData(Cache.getClub(), juniorTrainedMap, Cache.getTrainingsMap(), transfersSellMap,
																		 transfersBuyMap));

			ArrayList<Player> alPlayers = new ArrayList<Player>();
			alPlayers.addAll(Cache.getPlayers());
			alPlayers.addAll(Cache.getPlayersHistory());
			alPlayers.addAll(Cache.getPlayersTrash());

			Cache.setPlayersArchiveMap(playersManager.getPlayersArchive());

			for (Player player : alPlayers) {
				if (Cache.getPlayersArchiveMap().get(player.getId()) == null) {
					Cache.getPlayersArchiveMap().put(player.getId(), new PlayerArchive(player));
				}
			}

			Cache.setReports(teamManager.getReports(Cache.getPlayersArchiveMap(), Cache.getCoachesMap()));

			for (Transfer transfer : Cache.getTransfers()) {
				PlayerArchive player = Cache.getPlayersArchiveMap().get(transfer.getPlayerID());
				if (player != null) {
					transfer.setPlayer(player.toPlayer());
				}
			}

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getAssistantData")); //$NON-NLS-1$

			Cache.setAssistant(assistantManager.getAssistantData());

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getLeaguesData")); //$NON-NLS-1$

			Cache.setClubMap(teamManager.getTeams());

			Cache.setLeaguesMap(leaguesManager.getLeagues());

			Cache.setLeagueSeasons(leaguesManager.getLeagueSeasons(Cache.getLeaguesMap(), Cache.getClubMap()));

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.getMatchesData")); //$NON-NLS-1$

			Cache.setPlayersMap(new HashMap<Integer, Player>());
			for (Player player : alPlayers) {
				Cache.getPlayersMap().put(player.getId(), player);
			}

			Cache.setMatches(matchesManager.getMatches(Cache.getClub(), Cache.getPlayersMap(), Cache.getLeaguesMap(), Cache.getClubMap(), Cache
				.getPlayersArchiveMap()));

			playersManager.calculatePositionForAllPlayer(Cache.getPlayers(), Cache.getAssistant());

			monitor.worked(1);
			monitor.subTask(Messages.getString("progressBar.info.completeYouthTeamId")); //$NON-NLS-1$

			new SetUIAction().run(monitor);
			monitor.done();
			SettingsHandler.setLogged(true);
		} catch (final InvocationTargetException e) {
			// if(e.getCause() instanceof SVSynchronizerCriticalException) {
			// new SVLogger(Level.WARNING, "Downloader -> Synchronizer", e);
			// //$NON-NLS-1$
			// MessageDialog.openErrorMessage(ViewerHandler.getViewer(),
			// e.getCause().getMessage());
			// }
			monitor.interrupt();
			throw e;
		} catch (final SQLException e) {
			SettingsHandler.setLogged(false);
			monitor.interrupt();
			throw new InvocationTargetException(e, "CoreAction SQLException");
		} catch (final ConnectException e) {
			SettingsHandler.setLogged(false);
			monitor.interrupt();
			throw new InvocationTargetException(e, Messages.getString("message.error.connection"));
		} catch (final IOException e) {
			SettingsHandler.setLogged(false);
			monitor.interrupt();
			throw new InvocationTargetException(e, "IOException Bean");
		} catch (ClassNotFoundException e) {
			SettingsHandler.setLogged(false);
			monitor.interrupt();
			throw new InvocationTargetException(e, "ClassNotFound");
		} catch (NumberFormatException e) {
			SettingsHandler.setLogged(false);
			monitor.interrupt();
			throw new InvocationTargetException(e, "NumberFormatExcetion");
		} catch (Exception e) {
			SettingsHandler.setLogged(false);
			monitor.interrupt();
			throw new InvocationTargetException(e, "CoreAction Undefined");
		} finally {
			try {
				SQLSession.close();
			} catch (Exception e) {
				SettingsHandler.setLogged(false);
				throw new InvocationTargetException(e, "CoreAction Finally");
			}
			lock = UNLOCK;
		}
	}

	public void onFinish() {
		try {
			SQLSession.close();
		} catch (SQLException e) {
		}
	}
}