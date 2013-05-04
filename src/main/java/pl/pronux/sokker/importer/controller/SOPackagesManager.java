package pl.pronux.sokker.importer.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.pronux.sokker.importer.controller.filters.XMLFilter;
import pl.pronux.sokker.importer.controller.filters.sokkerorganizer.XMLDateFilter;
import pl.pronux.sokker.importer.controller.filters.sokkerorganizer.XMLOldFormatFilter;
import pl.pronux.sokker.importer.model.IXMLpack;
import pl.pronux.sokker.importer.model.XMLpack;
import pl.pronux.sokker.importer.model.XMLpackOld;
import pl.pronux.sokker.interfaces.ProgressMonitor;
import pl.pronux.sokker.interfaces.RunnableWithProgress;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class SOPackagesManager extends PackagesManager implements RunnableWithProgress {

	private File directory;
	private int teamID;
	private List<IXMLpack> packages;

	public SOPackagesManager(String directory, int teamID) {
		this.teamID = teamID;
		this.directory = new File(directory);
	}

	public SOPackagesManager(File directory, int teamID) {
		this.directory = directory;
		this.teamID = teamID;
	}

	public void run(ProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		packages = new ArrayList<IXMLpack>();

		List<File> dateList = OperationOnFile.visitAllDirs(directory, new XMLOldFormatFilter(), new ArrayList<File>(), 1);
		monitor.beginTask(String.format("%s (1/5)", Messages.getString("PackagesManager.build.old")), dateList.size());  
		for (File xmlFile : dateList) {
			String date = xmlFile.getName().replaceAll(".xml", "");  
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

		monitor.beginTask(String.format("%s (2/5)", Messages.getString("PackagesManager.build.new.teamid")), 1);   
		dateList = OperationOnFile.getFileChildren(directory, new XMLDateFilter(teamID), new ArrayList<File>(), monitor);
		monitor.worked(1);
		monitor.beginTask(String.format("%s (3/5)", Messages.getString("PackagesManager.build.new.all")), 1);   
		List<File> fileList = OperationOnFile.getFileChildren(directory, new XMLFilter(".*"), new ArrayList<File>(), monitor); 
		monitor.worked(1);
		monitor.beginTask(String.format("%s (4/5)", Messages.getString("PackagesManager.build.new")), dateList.size());   
		for (File dateFile : dateList) {
			monitor.subTask(String.format("%s (%d/%d)", dateFile.getAbsoluteFile().toString(),monitor.getWorked(), monitor.getTotalTime())); 
			String[] params = dateFile.getName().replaceAll(".xml", "").split("_");   
			
			if (params.length == 3 && params[0].matches("[0-9]{4}-[0-9]{2}-[0-9]{2}") && params[1].matches("[0-9]{2}-[0-9]{2}")) {  
				XMLpack pack = new XMLpack();
				for (File file : fileList) {
					if (file.getName().endsWith(".xml") && file.getName().contains(params[0] + '_' + params[1])) { 
						String name = file.getName();
						if (name.contains("trainers")) { 
							pack.setTrainers(file);
						} else if (name.contains("countries")) { 
							pack.setCountries(file);
						} else if (name.contains("players")) { 
							pack.setPlayers(file);
						} else if (name.contains("juniors")) { 
							pack.setJuniors(file);
						} else if (name.contains("team")) { 
							pack.setTeam(file);
						} else if (name.contains("transfers")) { 
							pack.setTransfers(file);
						} else if (name.contains("region")) { 
							pack.setRegion(file);
						} else if (name.contains("matchesTeam")) { 
							pack.setMatchesTeam(file);
						} else if (name.contains("reports")) { 
							pack.setReports(file);
						} else if (name.contains("region")) { 
							pack.setRegion(file);
						}
					}
				}

				if (pack.getJuniors() != null && pack.getPlayers() != null && pack.getTeam() != null) {
					pack.setComplete(true);
				} else {
					pack.setComplete(false);
				}
				pack.setDate(new Date(params[0] + "_" + params[1]));
				pack.getDate().setSokkerDate(new SokkerDate(pack.getDate().getMillis()));
				pack.setTeamId(teamID);
				packages.add(pack);
			}
			monitor.worked(1);

			if (monitor.isCanceled()) {
				return;
			}
		}
		monitor.beginTask(String.format("%s (5/5)", Messages.getString("PackagesManager.sort")), 1);  
		Collections.sort(packages, new PackageComparator());
		monitor.worked(1);

		monitor.done();
	}

	public List<IXMLpack> getPackages() {
		return packages;
	}

	public void setPackages(List<IXMLpack> packages) {
		this.packages = packages;
	}

	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

}
