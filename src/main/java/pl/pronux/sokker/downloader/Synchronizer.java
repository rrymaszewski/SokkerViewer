package pl.pronux.sokker.downloader;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.ConfigurationManager;
import pl.pronux.sokker.actions.JuniorsManager;
import pl.pronux.sokker.actions.LeaguesManager;
import pl.pronux.sokker.actions.MatchesManager;
import pl.pronux.sokker.bean.SynchronizerConfiguration;
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
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.exceptions.SVSynchronizerCriticalException;
import pl.pronux.sokker.interfaces.ProgressMonitor;
import pl.pronux.sokker.interfaces.RunnableWithProgress;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.utils.Log;

public class Synchronizer implements RunnableWithProgress {


	private SokkerViewerSettings settings;

	private MatchesManager matchesManager = MatchesManager.getInstance();
	private ConfigurationManager configurationManager = ConfigurationManager.getInstance();
	private JuniorsManager juniorsManager = JuniorsManager.getInstance();
	private LeaguesManager leaguesManager = LeaguesManager.getInstance();

	private SynchronizerConfiguration configuration;

	public static final String ERROR_MESSAGE_NULL = "-4"; 
	public static final String ERROR_RESPONSE_UNKNOWN = "-1"; 
	public static final String ERROR_WRITE = "-3";
	public static final String ERROR_DOWNLOAD = "-2"; 

	public Synchronizer(SokkerViewerSettings settings, SynchronizerConfiguration configuration) {
		this.configuration = configuration;
		this.settings = settings;
	}

	public void run(ProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		if (configuration != null && configuration.isDownloadBase() && monitor != null) {

			monitor.beginTask(Messages.getString("synchronizer.info"), 15); 
			monitor.subTask(Messages.getString("synchronizer.login")); 

			// auth
			XMLDownloader downloader = new XMLDownloader();
			ProxySettings proxySettings = settings.getProxySettings();
			try {
				downloader.login(settings.getUsername(), settings.getPassword(), proxySettings);
			} catch (SVException e) {
				throw new InvocationTargetException(
					new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_MESSAGE_NULL), e)); 
			} catch (IOException e) {
				throw new InvocationTargetException(
					new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_MESSAGE_NULL), e)); 
			}
			String vars;
			if (downloader.getStatus().equals(SokkerAuthentication.OK)) {
				try {
					vars = downloader.getVars();
				} catch (IOException e) {
					throw new InvocationTargetException(
						new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_DOWNLOAD), e)); 
				}
			} else {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("login.error." + downloader.getErrorno()))); 
			}

			VarsXmlParser parser = new VarsXmlParser();
			InputSource input = new InputSource(new StringReader(vars));

			monitor.worked(1);
			try {
				try {
					parser.parseXmlSax(input);
				} catch (SAXException ex) {
					input = new InputSource(new StringReader(XmlManagerUtils.filterCharacters(vars)));
					parser.parseXmlSax(input);
				}
				Date currentDay = new Date(Calendar.getInstance());
				currentDay.setSokkerDate(parser.getSokkerDate());

				int teamID = Integer.valueOf(downloader.getTeamId());
				// write xmls
				String destination = settings.getBaseDirectory() + File.separator + "xml" + File.separator + settings.getUsername(); 

				// init managers
				TransfersXmlManager transfersXmlManager = new TransfersXmlManager(destination, downloader, currentDay);
				TeamsXmlManager teamsXmlManager = new TeamsXmlManager(destination, downloader, currentDay);
				TrainersXmlManager trainersXmlManager = new TrainersXmlManager(destination, downloader, currentDay);
				JuniorsXmlManager juniorsXmlManager = new JuniorsXmlManager(destination, downloader, currentDay);
				ReportsXmlManager reportsXmlManager = new ReportsXmlManager(destination, downloader, currentDay);
				PlayersXmlManager playersXmlManager = new PlayersXmlManager(destination, downloader, currentDay);
				PlayerXmlManager playerXmlManager = new PlayerXmlManager(destination, downloader, currentDay);
				RegionXmlManager regionXmlManager = new RegionXmlManager(destination, downloader, currentDay);
				CountriesXmlManager countriesXmlManager = new CountriesXmlManager(destination, downloader, currentDay);
				SystemXmlManager systemXmlManager = new SystemXmlManager(destination, downloader, currentDay);
				MatchesTeamXmlManager matchesTeamXmlManager = new MatchesTeamXmlManager(destination, downloader, currentDay);
				LeagueXmlManager leagueXmlManager = new LeagueXmlManager(destination, downloader, currentDay);
				MatchXmlManager matchXmlManager = new MatchXmlManager(destination, downloader, currentDay);
				LeagueMatchesXmlManager leagueMatchesXmlManager = new LeagueMatchesXmlManager(destination, downloader, currentDay);

				// download xmls

				if (configuration.isDownloadBase()) {
					monitor.subTask(Messages.getString("synchronizer.download.players")); 
					playersXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.teams")); 
					teamsXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.juniors")); 
					juniorsXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.transfers")); 
					transfersXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.reports")); 
					reportsXmlManager.download();
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.matches")); 
					matchesTeamXmlManager.download();
					monitor.worked(1);
				}

				if (configuration.isDownloadBase() || configuration.isRepairDb()) {
					monitor.subTask(Messages.getString("synchronizer.download.trainers")); 
					trainersXmlManager.download();
					monitor.worked(1);
				}

				if (configuration.isDownloadCountries()) {
					monitor.subTask(Messages.getString("synchronizer.download.countries")); 
					countriesXmlManager.download();
				}

				// parse xmls
				monitor.subTask(Messages.getString("synchronizer.parse")); 
				if (configuration.isDownloadCountries()) {
					countriesXmlManager.parseXML();
					countriesXmlManager.write();
				}

				if (configuration.isDownloadBase() || configuration.isRepairDb()) {
					trainersXmlManager.parseXML();
					trainersXmlManager.write();
				}

				if (configuration.isDownloadBase()) {

					playersXmlManager.parseXML();
					juniorsXmlManager.parseXML();
					transfersXmlManager.parseXML();
					reportsXmlManager.parseXML();
					teamsXmlManager.parseXML();
					monitor.worked(1);
					List<Match> teamMatches = matchesTeamXmlManager.parseXML();

					monitor.subTask(Messages.getString("synchronizer.download.league")); 
					leagueXmlManager.download(teamMatches);
					monitor.worked(1);
					List<League> leagues = leagueXmlManager.parseXML();

					monitor.subTask(Messages.getString("synchronizer.download.league.matches")); 
					leagueMatchesXmlManager.download(leagues);
					List<Match> leagueMatches = leagueMatchesXmlManager.parseXML();
					teamMatches.addAll(leagueMatches);

					List<Match> alNotFinishedMatches = matchesManager.getNotFinishedMatches(teamMatches);
					monitor.worked(1);
					monitor.subTask(Messages.getString("synchronizer.download.matches")); 
					matchXmlManager.download(alNotFinishedMatches);
					matchXmlManager.parseXML();

					monitor.worked(1);
					// download & parse region
					monitor.subTask(Messages.getString("synchronizer.download.region")); 
					regionXmlManager.download(teamsXmlManager.getClub().getRegionId());
					regionXmlManager.parseXML();
					regionXmlManager.write();
					monitor.worked(1);
				}
				monitor.subTask(Messages.getString("synchronizer.write")); 
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

				monitor.subTask(Messages.getString("synchronizer.sql.update")); 
				SQLSession.beginTransaction();

				teamID = configurationManager.getTeamId();
				if (teamID == 0) {
					systemXmlManager.updateDbTeamId(Integer.valueOf(downloader.getTeamId()));
				} else if (Integer.valueOf(downloader.getTeamId()) != teamID) {
					throw new SVException("synchronizer -> teamID != getTeamID"); 
				}

				if (configuration.isRepairDb()) {
					trainersXmlManager.repairCoaches();
					configurationManager.repairDatabase();
				}
				
				if (configuration.isDownloadCountries()) {
					countriesXmlManager.importToSQL();
					countriesXmlManager.updateDbCountries(false);
				}

				if (configuration.isDownloadBase()) {
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

				monitor.subTask(Messages.getString("synchronizer.data.complete")); 

				if (configuration.isDownloadBase()) {
					new MatchXmlManager(destination, downloader, currentDay).completeMatches(teamID, 9);
					playersXmlManager.completeUncompletedPlayers();
					playerXmlManager.completePlayersArchive(50);
					teamsXmlManager.completeClubs();
					leaguesManager.completeLeagueRounds();
				}

				if (configuration.isRepairDbJuniorsAge()) {
					juniorsManager.completeJuniorsAge(currentDay);
				}
				
				SQLSession.commit();
				monitor.worked(1);
			} catch (SQLException e) {
				try {
					SQLSession.rollback();
				} catch (SQLException e1) {
					Log.error("Synchronizer -> SQL Importing Rollback", e1); 
				}
				throw new InvocationTargetException(new SVSynchronizerCriticalException("Synchronizer -> SQL Importing", e)); 
			} catch (IOException e) {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_WRITE), e)); 
			} catch (NullPointerException e) {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_DOWNLOAD), e)); 
			} catch (SVException e) {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("login.error." + Synchronizer.ERROR_DOWNLOAD), e)); 
			} catch (SAXException e) {
				throw new InvocationTargetException(new SVSynchronizerCriticalException(Messages.getString("message.error.login"), e)); 
			} finally {
				try {
					SQLSession.endTransaction();
				} catch (SQLException e) {
					Log.error("Synchronizer -> SQL Importing EndTransaction", e); 
				}
			}
		}
	}

	public void onFinish() {
	}
}
