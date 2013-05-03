package pl.pronux.sokker.importer.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private List<IXMLpack> packages;

	public SVPackagesManager(String directory, int teamID) {
		this.teamID = teamID;
		this.directory = new File(directory);
	}

	public SVPackagesManager(File directory, int teamID) {
		this.directory = directory;
		this.teamID = teamID;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		monitor.setTaskName(String.format("%s (1/9)",  Messages.getString("PackagesManager.repair.scan")));  
		List<File> files = OperationOnFile.visitAllDirs(directory, new XMLFilter("players-[0-9]+_[0-9]+_[0-9]+_[0-9]+.xml"), new ArrayList<File>(), 1); 
		monitor.beginTask(String.format("%s (2/9)", Messages.getString("PackagesManager.repair.players")), files.size());   
		for (File file : files) {
			String[] name = file.getName().split("_|-"); 
			file.renameTo(new File(file.getParentFile().getPath() + File.separator + name[0] + "_" + name[1] + "_" + name[2] + "_" + name[3] + "_" + name[4]));    
			monitor.worked(1);
		}
		if (monitor.isCanceled()) {
			return;
		}

		files = OperationOnFile.visitAllDirs(directory, new XMLFilter("teams_[0-9]+_[0-9]+_[0-9]+_[0-9]+.xml"), new ArrayList<File>(), 1); 
		monitor.beginTask(String.format("%s (3/9)" ,Messages.getString("PackagesManager.repair.teams")), files.size());   
		for (File file : files) {
			String[] name = file.getName().split("_"); 
			file.renameTo(new File(file.getParentFile().getPath() + File.separator + "team_" + name[1] + "_" + name[2] + "_" + name[3] + "_" + name[4]));    
			monitor.worked(1);
		}
		if (monitor.isCanceled()) {
			return;
		}

		files = OperationOnFile.visitAllDirs(directory, new XMLFilter("(trainers|transfers|reports|region|juniors|countries)_[0-9]+_[0-9]+_[0-9]+.xml"), new ArrayList<File>(), 1); 
		monitor.beginTask(String.format("%s (4/9)", Messages.getString("PackagesManager.repair.others")), files.size());   
		for (File file : files) {
			String[] name = file.getName().split("_"); 
			if (new File(file.getParentFile().getPath() + File.separator + "team_" + teamID + "_" + name[1] + "_" + name[2] + "_" + name[3]).exists()) {    
				file.renameTo(new File(file.getParentFile().getPath() + File.separator + name[0] + "_" + teamID + "_" + name[1] + "_" + name[2] + "_" + name[3]));    
			}
			monitor.worked(1);
		}
		if (monitor.isCanceled()) {
			return;
		}

		packages = new ArrayList<IXMLpack>();

		List<File> dateList = OperationOnFile.visitAllDirs(directory, new XMLOldFormatFilter(), new ArrayList<File>(), 1);
		monitor.beginTask(String.format("%s (5/9)", Messages.getString("PackagesManager.build.old")), dateList.size());  
		for (File xmlFile : dateList) {
			String date = xmlFile.getName().replaceAll(".xml", "");  
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

		monitor.beginTask(String.format("%s (6/9)", Messages.getString("PackagesManager.build.new.teamid")), 1);   
		dateList = OperationOnFile.getFileChildren(directory, new XMLDateFilter(teamID), new ArrayList<File>(), monitor);
		monitor.worked(1);
		monitor.beginTask(String.format("%s (7/9)", Messages.getString("PackagesManager.build.new.all")), 1);   
		List<File> fileList = OperationOnFile.getFileChildren(directory, new XMLFilter(".*"), new ArrayList<File>(), monitor); 
		monitor.worked(1);

		monitor.beginTask(String.format("%s (8/9)", Messages.getString("PackagesManager.build.new")), dateList.size());   
		for (File dateFile : dateList) {
			monitor.subTask(String.format("%s (%d/%d)", dateFile.getAbsoluteFile().toString(),monitor.getWorked(), monitor.getTotalTime())); 
			String[] params = dateFile.getName().replaceAll(".xml", "").split("_");   

			if (params[4].length() > 12 && params[4].matches("[0-9]+")) { 
				XMLpack pack = new XMLpack();
				for (File file : fileList) {
					if (file.getName().endsWith(".xml") && file.getName().contains(params[1] + '_' + params[2] + '_' + params[3] + '_' + params[4])) { 
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
//					else if (file.getName().endsWith(".xml") && file.getName().contains(params[2] + '_' + params[3] + '_' + params[4])){
//						String name = file.getName();
//						if (name.contains("match_")) { 
//							pack.getMatches().add(file);
//						} else if (name.contains("league")) { 
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
				pack.setTeamId(Integer.valueOf(params[1]));
				packages.add(pack);
			}
			monitor.worked(1);

			if (monitor.isCanceled()) {
				return;
			}
		}
		monitor.beginTask(String.format("%s (9/9)", Messages.getString("PackagesManager.sort")), 1);  
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
