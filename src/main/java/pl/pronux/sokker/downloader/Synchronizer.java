package pl.pronux.sokker.downloader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.ConfigurationManager;
import pl.pronux.sokker.actions.LeaguesManager;
import pl.pronux.sokker.actions.MatchesManager;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.downloader.managers.CountriesXmlManager;
import pl.pronux.sokker.downloader.managers.JuniorsXmlManager;
import pl.pronux.sokker.downloader.managers.LeagueMatchesXmlManager;
import pl.pronux.sokker.downloader.managers.LeagueXmlManager;
import pl.pronux.sokker.downloader.managers.MatchXmlManager;
import pl.pronux.sokker.downloader.managers.MatchesTeamXmlManager;
import pl.pronux.sokker.downloader.managers.PlayerXmlManager;
import pl.pronux.sokker.downloader.managers.PlayersXmlManager;
import pl.pronux.sokker.downloader.managers.RegionXmlManager;
import pl.pronux.sokker.downloader.managers.ReportsXmlManager;
import pl.pronux.sokker.downloader.managers.SystemXmlManager;
import pl.pronux.sokker.downloader.managers.TeamsXmlManager;
import pl.pronux.sokker.downloader.managers.TrainersXmlManager;
import pl.pronux.sokker.downloader.managers.TransfersXmlManager;
import pl.pronux.sokker.downloader.managers.XmlManagerUtils;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.VarsXmlParser;
import pl.pronux.sokker.enums.OperatingSystem;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.exceptions.SVSynchronizerCriticalException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.updater.xml.UpdateXMLParser;
import pl.pronux.sokker.utils.Log;

public class Synchronizer implements IRunnableWithProgress {

	private String vars;

	private SokkerViewerSettings settings;

	private int params;

	final public static int DOWNLOAD_BASE = 1 << 0;

	final public static int DOWNLOAD_COUNTRIES = 1 << 1;

	final public static int REPAIR_DB = 1 << 2;

	final public static int REPAIR_DATABASE = 1 << 3;

	final public static int DOWNLOAD_ALL = DOWNLOAD_BASE | DOWNLOAD_COUNTRIES;

	final public static String ERROR_MESSAGE_NULL = "-4"; //$NON-NLS-1$
	final public static String ERROR_RESPONSE_UNKNOWN = "-1"; //$NON-NLS-1$
	final public static String ERROR_WRITE = "-3";//$NON-NLS-1$
	final public static String ERROR_DOWNLOAD = "-2"; //$NON-NLS-1$

	public Synchronizer(SokkerViewerSettings settings, int params) {
		this.params = params;
		this.settings = settings;
	}

	public Synchronizer(SokkerViewerSettings settings) {
		this.settings = settings;
		this.params = DOWNLOAD_ALL;
	}

	final public static String NO_UPDATES = "NO_UPDATES";//$NON-NLS-1$

	public String getVersion() throws SAXException, IOException {
		String xml;
		String osType = "/windows"; //$NON-NLS-1$
		String version = NO_UPDATES;
		if (SettingsHandler.OS_TYPE == OperatingSystem.WINDOWS) {
			osType = "/windows"; //$NON-NLS-1$
		} else if (SettingsHandler.OS_TYPE == OperatingSystem.LINUX) {
			osType = "/linux"; //$NON-NLS-1$
		} else if (SettingsHandler.OS_TYPE == OperatingSystem.MACOSX) {
			osType = "/mac"; //$NON-NLS-1$
		}

		String query = ""; //$NON-NLS-1$
		if (SV.VERSION_TYPE == SV.TESTING) {
			query = "http://www.rymek.user.icpnet.pl/sv/updates/testing" + osType + "/packages.xml"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			query = "http://www.rymek.user.icpnet.pl/sv/updates/stable" + osType + "/packages.xml"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		ProxySettings proxySettings = settings.getProxySettings();
		xml = new HTMLDownloader(proxySettings).getNormalPage(query);

		UpdateXMLParser parser = new UpdateXMLParser();
		InputSource input = new InputSource(new StringReader(xml));
		parser.parseXmlSax(input, null);

		UpdateXMLParser oldParser = new UpdateXMLParser();
		InputSource inputOld = new InputSource(new FileReader(new File(settings.getBaseDirectory() + File.separator + "packages.xml"))); //$NON-NLS-1$
		oldParser.parseXmlSax(inputOld, null);

		if (parser.revision > oldParser.revision) {
			version = parser.version;
		}
		return version;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		if (params != 0 && (params & Synchronizer.DOWNLOAD_BASE) != 0 && monitor != null) {

			monitor.beginTask(Messages.getString("synchronizer.info"), 15); //$NON-NLS-1$

			// initiate
			InputSource input = null;

			TransfersXmlManager transfersXmlManager = null;
			TeamsXmlManager teamsXmlManager = null;
			TrainersXmlManager trainersXmlManager = null;
			JuniorsXmlManager juniorsXmlManager = null;
			ReportsXmlManager reportsXmlManager = null;
			PlayersXmlManager playersXmlManager = null;
			PlayerXmlManager playerXmlManager = null;
			RegionXmlManager regionXmlManager = null;
			CountriesXmlManager countriesXmlManager = null;
			SystemXmlManager systemXmlManager = null;
			MatchXmlManager matchXmlManager = null;
			MatchesTeamXmlManager matchesTeamXmlManager = null;
			LeagueXmlManager leagueXmlManager = null;
			LeagueMatchesXmlManager leagueMatchesXmlManager = null;

			Date currentDay = new Date(Calendar.getInstance());
			List<Match> teamMatches = new ArrayList<Match>();

			monitor.subTask(Messages.getString("synchronizer.login")); //$NON-NLS-1$

			// auth
			XMLDownloader downloader = new XMLDownloader();
			ProxySettings proxySettings = settings.getProxySettings();
			try {
				if (proxySettings.isEnabled()) {
					downloader.login(settings.getUsername(), settings.getPassword(), proxySettings.getHostname(), proxySettings.getPort(), proxySettings
						.getUsername(), proxySettings.getPassword());
				} else {
					downloader.login(settings.getUsername(), settings.getPassword());
				}
			} catch (SVException e) {
				throw new InvocationTargetException(
					new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_MESSAGE_NULL), e)); //$NON-NLS-1$
			} catch (IOException e) {
				throw new InvocationTargetException(
					new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_MESSAGE_NULL), e)); //$NON-NLS-1$
			}
			if (downloader.getStatus().equals(SokkerAuthentication.OK)) {
				try {
					vars = downloader.getVars();
				} catch (IOException e) {
					throw new InvocationTargetException(
						new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_DOWNLOAD), e)); //$NON-NLS-1$
				}
			} else {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("login.error." + downloader.getErrorno()))); //$NON-NLS-1$
			}

			VarsXmlParser parser = new VarsXmlParser();
			input = new InputSource(new StringReader(vars));

			monitor.worked(1);
			try {
				try {
					parser.parseXmlSax(input);
				} catch (SAXException ex) {
					input = new InputSource(new StringReader(XmlManagerUtils.filterCharacters(vars)));
					parser.parseXmlSax(input);
				}

				currentDay.setSokkerDate(parser.sokkerDate);

				int teamID = Integer.valueOf(downloader.getTeamID());
				// write xmls
				String destination = settings.getBaseDirectory() + File.separator + "xml" + File.separator + settings.getUsername(); //$NON-NLS-1$

				// init managers
				transfersXmlManager = new TransfersXmlManager(destination, downloader, currentDay);
				teamsXmlManager = new TeamsXmlManager(destination, downloader, currentDay);
				trainersXmlManager = new TrainersXmlManager(destination, downloader, currentDay);
				juniorsXmlManager = new JuniorsXmlManager(destination, downloader, currentDay);
				reportsXmlManager = new ReportsXmlManager(destination, downloader, currentDay);
				playersXmlManager = new PlayersXmlManager(destination, downloader, currentDay);
				playerXmlManager = new PlayerXmlManager(destination, downloader, currentDay);
				regionXmlManager = new RegionXmlManager(destination, downloader, currentDay);
				countriesXmlManager = new CountriesXmlManager(destination, downloader, currentDay);
				systemXmlManager = new SystemXmlManager(destination, downloader, currentDay);
				matchesTeamXmlManager = new MatchesTeamXmlManager(destination, downloader, currentDay);
				leagueXmlManager = new LeagueXmlManager(destination, downloader, currentDay);
				matchXmlManager = new MatchXmlManager(destination, downloader, currentDay);
				leagueMatchesXmlManager = new LeagueMatchesXmlManager(destination, downloader, currentDay);

				// download xmls

				if ((params & Synchronizer.DOWNLOAD_BASE) != 0) {
					monitor.subTask(Messages.getString("synchronizer.download.players")); //$NON-NLS-1$
					playersXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.teams")); //$NON-NLS-1$
					teamsXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.juniors")); //$NON-NLS-1$
					juniorsXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.transfers")); //$NON-NLS-1$
					transfersXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.reports")); //$NON-NLS-1$
					reportsXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.matches")); //$NON-NLS-1$
					matchesTeamXmlManager.download();
					monitor.worked(1);
				}

				if ((params & Synchronizer.DOWNLOAD_BASE) != 0 || (params & Synchronizer.REPAIR_DB) != 0) {
					monitor.subTask(Messages.getString("synchronizer.download.trainers")); //$NON-NLS-1$
					trainersXmlManager.download();
					monitor.worked(1);
				}

				if ((params & Synchronizer.DOWNLOAD_COUNTRIES) != 0) {
					monitor.subTask(Messages.getString("synchronizer.download.countries")); //$NON-NLS-1$
					countriesXmlManager.download();
				}

				// parse xmls
				monitor.subTask(Messages.getString("synchronizer.parse")); //$NON-NLS-1$
				if ((params & Synchronizer.DOWNLOAD_COUNTRIES) != 0) {
					countriesXmlManager.parseXML();
					countriesXmlManager.write();
				}

				if ((params & Synchronizer.DOWNLOAD_BASE) != 0 || (params & Synchronizer.REPAIR_DB) != 0) {
					trainersXmlManager.parseXML();
					trainersXmlManager.write();
				}

				if ((params & Synchronizer.DOWNLOAD_BASE) != 0) {

					playersXmlManager.parseXML();
					juniorsXmlManager.parseXML();
					transfersXmlManager.parseXML();
					reportsXmlManager.parseXML();
					teamsXmlManager.parseXML();
					monitor.worked(1);
					teamMatches = matchesTeamXmlManager.parseXML();

					monitor.subTask(Messages.getString("synchronizer.download.league")); //$NON-NLS-1$
					leagueXmlManager.download(teamMatches);
					monitor.worked(1);
					List<League> leagues = leagueXmlManager.parseXML();

					monitor.subTask(Messages.getString("synchronizer.download.league.matches")); //$NON-NLS-1$
					leagueMatchesXmlManager.download(leagues);
					List<Match> leagueMatches = leagueMatchesXmlManager.parseXML();
					teamMatches.addAll(leagueMatches);

					List<Match> alNotFinishedMatches = new MatchesManager().getNotFinishedMatches(teamMatches);
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.matches")); //$NON-NLS-1$
					matchXmlManager.download(alNotFinishedMatches);
					matchXmlManager.parseXML();

					monitor.worked(1);
					// download & parse region
					monitor.subTask(Messages.getString("synchronizer.download.region")); //$NON-NLS-1$
					regionXmlManager.download(teamsXmlManager.getClub().getRegionID());
					regionXmlManager.parseXML();
					regionXmlManager.write();
					monitor.worked(1);
				}
				monitor.subTask(Messages.getString("synchronizer.write")); //$NON-NLS-1$
				// write xmls
				transfersXmlManager.write();
				juniorsXmlManager.write();
				reportsXmlManager.write();
				playersXmlManager.write();
				teamsXmlManager.write();
				matchesTeamXmlManager.write();
				leagueXmlManager.write();
				matchXmlManager.write();
				leagueMatchesXmlManager.write();
				monitor.worked(1);
				// import xmls

				monitor.subTask(Messages.getString("synchronizer.sql.update")); //$NON-NLS-1$
				SQLSession.beginTransaction();

				teamID = new ConfigurationManager().getTeamID();
				if (teamID == 0) {
					systemXmlManager.updateDbTeamID(Integer.valueOf(downloader.getTeamID()));
				} else if (Integer.valueOf(downloader.getTeamID()) != teamID) {
					throw new SVException("synchronizer -> teamID != getTeamID"); //$NON-NLS-1$
				}

				if ((params & Synchronizer.REPAIR_DB) != 0) {
					trainersXmlManager.repairCoaches();
					new ConfigurationManager().repairDatabase();
				}

				if ((params & Synchronizer.DOWNLOAD_COUNTRIES) != 0) {
					countriesXmlManager.importToSQL();
					countriesXmlManager.updateDbCountries(false);
				}

				if ((params & Synchronizer.DOWNLOAD_BASE) != 0) {
					trainersXmlManager.importToSQL();

					systemXmlManager.updateDbDate(currentDay);
					teamsXmlManager.importToSQL();

					Training training = null;
					if (teamsXmlManager.getClub() != null) {
						training = teamsXmlManager.getClub().getTraining();
						trainersXmlManager.importCoachesAtTraining(training);
					}

					playersXmlManager.importToSQL(training);
					juniorsXmlManager.importToSQL(training);
					transfersXmlManager.importToSQL();
					reportsXmlManager.importToSQL();
					regionXmlManager.importToSQL();
					leagueXmlManager.importToSQL();

					matchXmlManager.importToSQL();
					systemXmlManager.updateDbUpdate(false);
				}

				monitor.subTask(Messages.getString("synchronizer.data.complete")); //$NON-NLS-1$

				if ((params & Synchronizer.DOWNLOAD_BASE) != 0) {
					new MatchXmlManager(destination, downloader, currentDay).completeMatches(teamID, 9);
					playersXmlManager.completeYouthTeamId();
					playerXmlManager.completePlayersArchive(50);
					teamsXmlManager.completeClubs();
					new LeaguesManager().completeLeagueRounds();
				}

				SQLSession.commit();
				monitor.worked(1);
			} catch (SQLException e) {
				try {
					SQLSession.rollback();
				} catch (SQLException e1) {
					Log.error("Synchronizer -> SQL Importing Rollback", e1); //$NON-NLS-1$
				}
				throw new InvocationTargetException(new SVSynchronizerCriticalException("Synchronizer -> SQL Importing", e)); //$NON-NLS-1$
			} catch (IOException e) {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_WRITE), e)); //$NON-NLS-1$
			} catch (NullPointerException e) {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_DOWNLOAD), e)); //$NON-NLS-1$
			} catch (SVException e) {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_DOWNLOAD), e)); //$NON-NLS-1$
			} catch (SAXException e) {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("message.error.login"), e)); //$NON-NLS-1$
			}
		}
	}

	public void onFinish() {
	}
}
