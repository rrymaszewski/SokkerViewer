package pl.pronux.sokker.importer.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

import pl.pronux.sokker.importer.controller.filters.XMLFilter;
import pl.pronux.sokker.importer.controller.filters.sokkerorganizer.XMLDateFilter;
import pl.pronux.sokker.importer.controller.filters.sokkerorganizer.XMLOldFormatFilter;
import pl.pronux.sokker.importer.model.IXMLpack;
import pl.pronux.sokker.importer.model.XMLpack;
import pl.pronux.sokker.importer.model.XMLpackOld;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class SOPackagesManager extends PackagesManager implements IRunnableWithProgress {

	private File directory;
	private int teamID;
	private ArrayList<IXMLpack> packages;

	public SOPackagesManager(String directory, int teamID) {
		this.teamID = teamID;
		this.directory = new File(directory);
	}

	public SOPackagesManager(File directory, int teamID) {
		this.directory = directory;
		this.teamID = teamID;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		packages = new ArrayList<IXMLpack>();

		ArrayList<File> dateList = OperationOnFile.visitAllDirs(directory, new XMLOldFormatFilter(), new ArrayList<File>(), 1);
		monitor.beginTask(String.format("%s (1/5)", Messages.getString("PackagesManager.build.old")), dateList.size()); //$NON-NLS-1$ //$NON-NLS-2$
		for (File xmlFile : dateList) {
			String date = xmlFile.getName().replaceAll(".xml", ""); //$NON-NLS-1$ //$NON-NLS-2$
			XMLpackOld pack = new XMLpackOld();
			pack.setDate(new Date(date));
			pack.getDate().setSokkerDate(new SokkerDate(pack.getDate().getMillis()));
			pack.setFile(xmlFile);
			pack.setComplete(true);
			packages.add(pack);
			monitor.worked(1);
		}
		
		if (monitor.isCanceled()) {
			return;
		}

		monitor.beginTask(String.format("%s (2/5)", Messages.getString("PackagesManager.build.new.teamid")), 1); //$NON-NLS-1$ //$NON-NLS-2$ 
		dateList = OperationOnFile.getFileChildren(directory, new XMLDateFilter(teamID), new ArrayList<File>(), monitor);
		monitor.worked(1);
		monitor.beginTask(String.format("%s (3/5)", Messages.getString("PackagesManager.build.new.all")), 1); //$NON-NLS-1$ //$NON-NLS-2$ 
		ArrayList<File> fileList = OperationOnFile.getFileChildren(directory, new XMLFilter(".*"), new ArrayList<File>(), monitor); //$NON-NLS-1$
		monitor.worked(1);
		monitor.beginTask(String.format("%s (4/5)", Messages.getString("PackagesManager.build.new")), dateList.size()); //$NON-NLS-1$ //$NON-NLS-2$ 
		for (File dateFile : dateList) {
			monitor.subTask(String.format("%s (%d/%d)", dateFile.getAbsoluteFile().toString(),monitor.getWorked(), monitor.getTotalTime())); //$NON-NLS-1$
			String[] params = dateFile.getName().replaceAll(".xml", "").split("_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			if (params.length == 3 && params[0].matches("[0-9]{4}-[0-9]{2}-[0-9]{2}") && params[1].matches("[0-9]{2}-[0-9]{2}")) { //$NON-NLS-1$ //$NON-NLS-2$
				XMLpack pack = new XMLpack();
				for (File file : fileList) {
					if (file.getName().endsWith(".xml") && file.getName().contains(params[0] + '_' + params[1])) { //$NON-NLS-1$
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
				}

				if (pack.getJuniors() != null && pack.getPlayers() != null && pack.getTeam() != null) {
					pack.setComplete(true);
				} else {
					pack.setComplete(false);
				}
				pack.setDate(new Date(params[0] + "_" + params[1]));//$NON-NLS-1$
				pack.getDate().setSokkerDate(new SokkerDate(pack.getDate().getMillis()));
				pack.setTeamID(teamID);
				packages.add(pack);
			}
			monitor.worked(1);

			if (monitor.isCanceled()) {
				return;
			}
		}
		monitor.beginTask(String.format("%s (5/5)", Messages.getString("PackagesManager.sort")), 1); //$NON-NLS-1$ //$NON-NLS-2$
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
