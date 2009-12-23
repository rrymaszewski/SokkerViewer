package pl.pronux.sokker.importer.controller;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.ConfigurationManager;
import pl.pronux.sokker.actions.JuniorsManager;
import pl.pronux.sokker.actions.PlayersManager;
import pl.pronux.sokker.actions.TeamManager;
import pl.pronux.sokker.actions.TrainersManager;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.downloader.managers.JuniorsXmlManager;
import pl.pronux.sokker.downloader.managers.PlayersXmlManager;
import pl.pronux.sokker.downloader.managers.ReportsXmlManager;
import pl.pronux.sokker.downloader.managers.TeamsXmlManager;
import pl.pronux.sokker.downloader.managers.TrainersXmlManager;
import pl.pronux.sokker.downloader.managers.TransfersXmlManager;
import pl.pronux.sokker.downloader.managers.XmlManagerUtils;
import pl.pronux.sokker.downloader.xml.parsers.OldXmlParser;
import pl.pronux.sokker.importer.model.IXMLpack;
import pl.pronux.sokker.importer.model.XMLpack;
import pl.pronux.sokker.importer.model.XMLpackOld;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.Report;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.model.Transfer;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.utils.Log;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class ImportXMLAction implements IRunnableWithProgress {

	private ArrayList<IXMLpack> packages;

	public ImportXMLAction(ArrayList<IXMLpack> packages) {
		this.packages = packages;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		monitor.beginTask(Messages.getString("ImportXMLAction.start"), packages.size()); //$NON-NLS-1$
		try {
			SQLSession.connect();
			ConfigurationManager dbConfiguration = new ConfigurationManager();
			int teamID = dbConfiguration.getTeamID();
			for (IXMLpack child : packages) {
				if (monitor.isCanceled()) {
					throw new InterruptedException();
				}

				monitor.setTaskName(Messages.getString("ImportXMLAction.import")); //$NON-NLS-1$
				monitor.subTask(child.getDate().toDateTimeString());
				if (child instanceof XMLpack) {
					XMLpack pack = (XMLpack) child;
					if (pack.isComplete()) {
						try {
							SQLSession.beginTransaction();
							// if (pack.getCountries() != null) {
							// CountriesXmlManager countriesManager = new
							// CountriesXmlManager(OperationOnFile.readFromFile(pack.getCountries(),
							// "UTF-8"), pack.getDate(), pack.getTeamID());
							// countriesManager.parseXML();
							// countriesManager.importToSQL();
							// }
							TeamsXmlManager teamXmlManager = new TeamsXmlManager(OperationOnFile.readFromFile(pack.getTeam(), "UTF-8"), pack.getDate(), pack.getTeamID()); //$NON-NLS-1$
							PlayersXmlManager playersXmlManager = new PlayersXmlManager(OperationOnFile.readFromFile(pack.getPlayers(), "UTF-8"), pack.getDate(), pack.getTeamID()); //$NON-NLS-1$
							JuniorsXmlManager juniorsXmlManager = new JuniorsXmlManager(OperationOnFile.readFromFile(pack.getJuniors(), "UTF-8"), pack.getDate(), pack.getTeamID()); //$NON-NLS-1$
							TrainersXmlManager trainersManager;
							TransfersXmlManager transfersManager;
							ReportsXmlManager reportsManager;
							List<Coach> trainers;

							Club club = teamXmlManager.parseXML(teamID);
							List<Player> players = playersXmlManager.parseXML();
							List<Junior> juniors = juniorsXmlManager.parseXML();
							
							TeamManager teamManager = new TeamManager();
							PlayersManager playersManager = new PlayersManager();
							JuniorsManager juniorsManager = new JuniorsManager();
							
							if(club.getId() == teamID) {
								if (club.getId() == pack.getTeamID()) {
									teamManager.importerTeam(club, pack.getDate());
								}

								Training training = null;
								
								if (club != null) {
									training = club.getTraining();
									if(pack.getTrainers() != null) {
										 trainersManager = new TrainersXmlManager(OperationOnFile.readFromFile(pack.getTrainers(), "UTF-8"), pack.getDate(), pack.getTeamID()); //$NON-NLS-1$
										 trainers = trainersManager.parseXML();
										 new TrainersManager().importerTrainers(trainers);
										 trainersManager.importCoachesAtTraining(training);
									}
								}
								playersManager.importPlayers(players, training);
								juniorsManager.importJuniors(juniors, training, club.getId());

								if (pack.getReports() != null) {
									reportsManager = new ReportsXmlManager(OperationOnFile.readFromFile(pack.getReports(), "UTF-8"), pack.getDate(), pack.getTeamID()); //$NON-NLS-1$
									List<Report> reports = reportsManager.parseXML();
									teamManager.importerReports(reports);
								}

								// if(pack.getRegion() != null) {
								// CountriesManager countriesManager = new
								// CountriesManager();
								// regionManager = new
								// RegionXmlManager(OperationOnFile.readFromFile(pack.getRegion(),
								// "UTF-8"), pack.getDate(), pack.getTeamID());
								// List<Region> regions =
								// regionManager.parseXML();
								// if(regions.get(0) != null) {
								// countriesManager.importRegion(regions.get(0));
								// }
								// }

								if (pack.getTransfers() != null) {
									transfersManager = new TransfersXmlManager(OperationOnFile.readFromFile(pack.getTransfers(), "UTF-8"), pack.getDate(), pack.getTeamID()); //$NON-NLS-1$
									List<Transfer> transfers = transfersManager.parseXML();
									teamManager.importerTransfers(transfers);
								}
								
//								if(pack.getLeagues().size() > 0) {
//									leagueXmlManager = new LeagueXmlManager();
//									List<String> leagues = new ArrayList<String>();
//									for(File league : pack.getLeagues()) {
//										leagues.add(OperationOnFile.readFromFile(league, "UTF-8"));
//									}
//									leagueXmlManager.parseXML(leagues);
//									leagueXmlManager.importToSQL();
//								}
//								
//								if(pack.getMatches().size() > 0) {
//									matchXmlManager = new MatchXmlManager();
//									List<String> matches = new ArrayList<String>();
//									for(File match : pack.getMatches()) {
//										matches.add(OperationOnFile.readFromFile(match, "UTF-8"));
//									}
//									matchXmlManager.parseXML(matches);
//									matchXmlManager.importToSQL();
//								}
								pack.setImported(true);
							} else {
								pack.setImported(false);	
							}
							
							SQLSession.commit();
							
						} catch (Exception e) {
							pack.setImported(false);
							SQLSession.rollback();
							Log.error(ImportXMLAction.class, "XML Importer ", e); //$NON-NLS-1$
						}
					}
				} else if (child instanceof XMLpackOld) {
					XMLpackOld pack = (XMLpackOld) child;
					try {
						
						//FIXME: data zmiany kodowania z ISO-8859-2 na utf 24.03.2006 
						// BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(importXMLTable.getItem(i).getText(0)), "ISO-8859-2")); //$NON-NLS-1$
						
						SQLSession.beginTransaction();
						OldXmlParser oldXMLParser = new OldXmlParser();
						String xml = OperationOnFile.readFromFile(pack.getFile(), "UTF-8"); //$NON-NLS-1$
						InputSource input = new InputSource(new StringReader(xml));
						try {
							oldXMLParser.parseXmlSax(input, null);
						} catch (SAXException ex) {
							input = new InputSource(new StringReader(XmlManagerUtils.filterCharacters(xml)));
							oldXMLParser.parseXmlSax(input, null);
						}
						Club club = oldXMLParser.getClub();
						if(club.getId() == teamID) {
							TrainersManager trainersManager = new TrainersManager();
							TeamManager teamManager = new TeamManager();
							PlayersManager playersManager = new PlayersManager();
							JuniorsManager juniorsManager = new JuniorsManager();

							trainersManager.importerTrainers(club.getCoaches());
							teamManager.importerTeam(club, pack.getDate());
							Training training = null;
							if (club != null) {
								training = club.getTraining();
								if (( training.getStatus() & Training.NEW_TRAINING ) != 0) {
									trainersManager.importTrainersAtTraining(club.getCoaches(), training);
								} else if ((training.getStatus() & Training.UPDATE_TRAINING) != 0 ) {
									trainersManager.updateTrainersAtTraining(club.getCoaches(), training);
								}
							}
							playersManager.importPlayers(club.getPlayers(), training);
							juniorsManager.importJuniors(club.getJuniors(), training, club.getId());
							
							pack.setImported(true);
						} else {
							pack.setImported(false);
						}
						SQLSession.commit();
					} catch (Exception e) {
						pack.setImported(false);
						SQLSession.rollback();
						Log.error(ImportXMLAction.class, "XML Importer ", e); //$NON-NLS-1$
					}

				}
				monitor.worked(1);
			}
//			new DatabaseConfiguration().updateDbCountry(true);
//			new DatabaseConfiguration().updateDbUpdate(true);
//			SQLSession.commit();
			SQLSession.close();
		} catch (SQLException e) {
			try {
				SQLSession.rollback();
				SQLSession.close();
			} catch (SQLException e1) {
				Log.error(ImportXMLAction.class, "Synchronizer -> SQL Importing Rollback", e1); //$NON-NLS-1$
			}
			Log.error(ImportXMLAction.class, "Synchronizer -> SQL Importing", e); //$NON-NLS-1$
		} finally {
			monitor.done();
		}
	}

	public void onFinish() {
		// TODO Auto-generated method stub
		
	}
}
