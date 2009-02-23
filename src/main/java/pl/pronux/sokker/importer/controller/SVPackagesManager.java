package pl.pronux.sokker.importer.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import pl.pronux.sokker.importer.controller.filters.XMLFilter;
import pl.pronux.sokker.importer.controller.filters.sokkerviewer.XMLDateFilter;
import pl.pronux.sokker.importer.controller.filters.sokkerviewer.XMLOldFormatFilter;
import pl.pronux.sokker.importer.model.IXMLpack;
import pl.pronux.sokker.importer.model.XMLpack;
import pl.pronux.sokker.importer.model.XMLpackOld;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class SVPackagesManager extends PackagesManager implements IRunnableWithProgress {

	private File directory;
	private int teamID;
	private ArrayList<IXMLpack> packages;

	public SVPackagesManager(String directory, int teamID) {
		this.teamID = teamID;
		this.directory = new File(directory);
	}

	public SVPackagesManager(File directory, int teamID) {
		this.directory = directory;
		this.teamID = teamID;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		ArrayList<File> files;
		monitor.setTaskName(String.format("%s (1/9)",  Messages.getString("PackagesManager.repair.scan"))); //$NON-NLS-1$ //$NON-NLS-2$
		files = OperationOnFile.visitAllDirs(directory, new XMLFilter("players-[0-9]+_[0-9]+_[0-9]+_[0-9]+.xml"), new ArrayList<File>(), 1); //$NON-NLS-1$
		monitor.beginTask(String.format("%s (2/9)", Messages.getString("PackagesManager.repair.players")), files.size()); //$NON-NLS-1$ //$NON-NLS-2$ 
		for (File file : files) {
			String[] name = file.getName().split("_|-"); //$NON-NLS-1$
			file.renameTo(new File(file.getParentFile().getPath() + File.separator + name[0] + "_" + name[1] + "_" + name[2] + "_" + name[3] + "_" + name[4])); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			monitor.worked(1);
		}
		if (monitor.isCanceled()) {
			return;
		}

		files = OperationOnFile.visitAllDirs(directory, new XMLFilter("teams_[0-9]+_[0-9]+_[0-9]+_[0-9]+.xml"), new ArrayList<File>(), 1); //$NON-NLS-1$
		monitor.beginTask(String.format("%s (3/9)" ,Messages.getString("PackagesManager.repair.teams")), files.size()); //$NON-NLS-1$ //$NON-NLS-2$ 
		for (File file : files) {
			String[] name = file.getName().split("_"); //$NON-NLS-1$
			file.renameTo(new File(file.getParentFile().getPath() + File.separator + "team_" + name[1] + "_" + name[2] + "_" + name[3] + "_" + name[4])); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			monitor.worked(1);
		}
		if (monitor.isCanceled()) {
			return;
		}

		files = OperationOnFile.visitAllDirs(directory, new XMLFilter("(trainers|transfers|reports|region|juniors|countries)_[0-9]+_[0-9]+_[0-9]+.xml"), new ArrayList<File>(), 1); //$NON-NLS-1$
		monitor.beginTask(String.format("%s (4/9)", Messages.getString("PackagesManager.repair.others")), files.size()); //$NON-NLS-1$ //$NON-NLS-2$ 
		for (File file : files) {
			String[] name = file.getName().split("_"); //$NON-NLS-1$
			if (new File(file.getParentFile().getPath() + File.separator + "team_" + teamID + "_" + name[1] + "_" + name[2] + "_" + name[3]).exists()) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				file.renameTo(new File(file.getParentFile().getPath() + File.separator + name[0] + "_" + teamID + "_" + name[1] + "_" + name[2] + "_" + name[3])); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
			monitor.worked(1);
		}
		if (monitor.isCanceled()) {
			return;
		}

		packages = new ArrayList<IXMLpack>();

		ArrayList<File> dateList = OperationOnFile.visitAllDirs(directory, new XMLOldFormatFilter(), new ArrayList<File>(), 1);
		monitor.beginTask(String.format("%s (5/9)", Messages.getString("PackagesManager.build.old")), dateList.size()); //$NON-NLS-1$ //$NON-NLS-2$
		for (File xmlFile : dateList) {
			String date = xmlFile.getName().replaceAll(".xml", ""); //$NON-NLS-1$ //$NON-NLS-2$
			XMLpackOld pack = new XMLpackOld();
			long millis = Long.valueOf(date);
			pack.setDate(new Date(millis));
			pack.getDate().setSokkerDate(new SokkerDate(millis));
			pack.setFile(xmlFile);
			pack.setComplete(true);
			packages.add(pack);
			monitor.worked(1);
		}

		if (monitor.isCanceled()) {
			return;
		}

		monitor.beginTask(String.format("%s (6/9)", Messages.getString("PackagesManager.build.new.teamid")), 1); //$NON-NLS-1$ //$NON-NLS-2$ 
		dateList = OperationOnFile.getFileChildren(directory, new XMLDateFilter(teamID), new ArrayList<File>(), monitor);
		monitor.worked(1);
		monitor.beginTask(String.format("%s (7/9)", Messages.getString("PackagesManager.build.new.all")), 1); //$NON-NLS-1$ //$NON-NLS-2$ 
		ArrayList<File> fileList = OperationOnFile.getFileChildren(directory, new XMLFilter(".*"), new ArrayList<File>(), monitor); //$NON-NLS-1$
		monitor.worked(1);

		monitor.beginTask(String.format("%s (8/9)", Messages.getString("PackagesManager.build.new")), dateList.size()); //$NON-NLS-1$ //$NON-NLS-2$ 
		for (File dateFile : dateList) {
			monitor.subTask(String.format("%s (%d/%d)", dateFile.getAbsoluteFile().toString(),monitor.getWorked(), monitor.getTotalTime())); //$NON-NLS-1$
			String[] params = dateFile.getName().replaceAll(".xml", "").split("_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			if (params[4].length() > 12 && params[4].matches("[0-9]+")) { //$NON-NLS-1$
				XMLpack pack = new XMLpack();
				for (File file : fileList) {
					if (file.getName().endsWith(".xml") && file.getName().contains(params[1] + '_' + params[2] + '_' + params[3] + '_' + params[4])) { //$NON-NLS-1$
						String name = file.getName();
						if (name.contains("trainers")) { //$NON-NLS-1$
							pack.setTrainers(file);
						} else if (name.contains("countries")) { //$NON-NLS-1$
							pack.setCountries(file);
						} else if (name.contains("players")) { //$NON-NLS-1$
							pack.setPlayers(file);
						} else if (name.contains("juniors")) { //$NON-NLS-1$
							pack.setJuniors(file);
						} else if (name.contains("team")) { //$NON-NLS-1$
							pack.setTeam(file);
						} else if (name.contains("transfers")) { //$NON-NLS-1$
							pack.setTransfers(file);
						} else if (name.contains("region")) { //$NON-NLS-1$
							pack.setRegion(file);
						} else if (name.contains("matchesTeam")) { //$NON-NLS-1$
							pack.setMatchesTeam(file);
						} else if (name.contains("reports")) { //$NON-NLS-1$
							pack.setReports(file);
						} else if (name.contains("region")) { //$NON-NLS-1$
							pack.setRegion(file);
						}
					}
//					else if (file.getName().endsWith(".xml") && file.getName().contains(params[2] + '_' + params[3] + '_' + params[4])){
//						String name = file.getName();
//						if (name.contains("match_")) { //$NON-NLS-1$
//							pack.getMatches().add(file);
//						} else if (name.contains("league")) { //$NON-NLS-1$
//							pack.getLeagues().add(file);
//						}
//					}
				}

				if (pack.getTrainers() != null && pack.getJuniors() != null && pack.getPlayers() != null && pack.getTeam() != null) {
					pack.setComplete(true);
				} else {
					pack.setComplete(false);
				}

				pack.setDate(new Date(Long.valueOf(params[4])));
				pack.getDate().setSokkerDate(new SokkerDate());
				pack.getDate().getSokkerDate().setDay(Integer.valueOf(params[3]));
				pack.getDate().getSokkerDate().setWeek(Integer.valueOf(params[2]));
				pack.setTeamID(Integer.valueOf(params[1]));
				packages.add(pack);
			}
			monitor.worked(1);

			if (monitor.isCanceled()) {
				return;
			}
		}
		monitor.beginTask(String.format("%s (9/9)", Messages.getString("PackagesManager.sort")), 1); //$NON-NLS-1$ //$NON-NLS-2$
		Collections.sort(packages, new PackageComparator());
		monitor.worked(1);

		monitor.done();
	}

	public ArrayList<IXMLpack> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<IXMLpack> packages) {
		this.packages = packages;
	}

}
