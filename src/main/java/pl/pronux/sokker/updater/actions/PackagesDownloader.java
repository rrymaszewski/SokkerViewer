package pl.pronux.sokker.updater.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import pl.pronux.sokker.actions.SettingsManager;
import pl.pronux.sokker.downloader.HTMLDownloader;
import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.ProgressMonitor;
import pl.pronux.sokker.interfaces.RunnableWithProgress;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.updater.model.Package;
import pl.pronux.sokker.utils.file.OperationOnFile;
import pl.pronux.sokker.utils.security.Crypto;

public class PackagesDownloader implements RunnableWithProgress {
	
	private static final String SERVER_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjI8H/0thn2jwRH/hWOGeK3bsCDoB7TJ9HyKlZT2jD8vA//CPcou4UPCk8HLPDVlbv5aETe572t+wz5En6k24wEjsO4H5cQicZ3aPM5tYDc1Abn0UsttZ7DJvCqHQqTP2xruNZ4LstYHm1WQG8CfT0aDUsK10Mlly0ZiSlmVunhQIDAQAB"; 

	private SettingsManager settingsManager = SettingsManager.getInstance();
	
	private String mirror;
	private String versionType;
	private List<Package> packages;

	public PackagesDownloader(String mirror, String versionType, List<Package> packages) {
		this.mirror = mirror;
		this.versionType = versionType;
		this.packages = packages;

	}

	public void run(ProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			monitor.beginTask(String.format("%s (1/5)", Messages.getString("updater.label.package.download")), 1);  
			downloadPackages(mirror + versionType, System.getProperty("user.dir") + File.separator + "tmp", packages, monitor);  

			monitor.beginTask(String.format("%s (2/5)", Messages.getString("updater.label.package.verify")), 1);  
			PublicKey pubk = Crypto.convertByteArrayToPublicKey(Crypto.decodeBase64(SERVER_PUBLIC_KEY), "RSA"); 
			verifyPackages(packages, pubk, monitor);

			monitor.beginTask(String.format("%s (3/5)", Messages.getString("updater.label.package.unzip")), 1);  
			unzipPackages(packages, monitor);

			monitor.beginTask(String.format("%s (4/5)", Messages.getString("updater.label.package.copy")), 1);  
			OperationOnFile.copyDirectory(new File(System.getProperty("user.dir") + File.separator + "tmp"), new File(System.getProperty("user.dir")));   

			SettingsHandler.getSokkerViewerSettings().setCheckProperties(true);
			settingsManager.updateSettings(SettingsHandler.getSokkerViewerSettings());

			monitor.beginTask(String.format("%s (5/5)", Messages.getString("updater.label.package.clean")), 1);  
			OperationOnFile.cleanDir(new File(System.getProperty("user.dir") + File.separator + "tmp"));  
			monitor.setTaskName(""); 
			monitor.done();
		} catch (SVException e) {
			throw new InvocationTargetException(e);
		} catch (FileNotFoundException e) {
			throw new InvocationTargetException(e);
		} catch (IOException e) {
			throw new InvocationTargetException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new InvocationTargetException(e);
		} catch (InvalidKeySpecException e) {
			throw new InvocationTargetException(e);
		} catch (BadArgumentException e) {
			throw new InvocationTargetException(e);
		}

	}

	private void unzipPackages(List<Package> packages, ProgressMonitor monitor) throws SVException {
		monitor.setTotalTime(packages.size());
		for (Package pkg : packages) {
			monitor.subTask(pkg.getFilename());
			try {
				pkg.unzip();
				if (pkg.getFilename().matches("[a-zA-Z0-9.-]+.zip")) { 
					new File(pkg.getRootDirectory() + pkg.getLocalpath() + pkg.getFilename()).delete();
				}

			} catch (IOException e) {
				throw new SVException(Messages.getString("updater.label.info.unzip"), e); 
			}
		}
	}

	private void downloadPackages(final String mirror, String tempDirectory, List<Package> packages, ProgressMonitor monitor) throws IOException, SVException {
		monitor.setTotalTime(packages.size());
		HTMLDownloader htmlDownloader;
			for (Package pkg : packages) {
				try {
					ProxySettings proxySettings = SettingsHandler.getSokkerViewerSettings().getProxySettings();

					htmlDownloader = new HTMLDownloader(proxySettings);
					
					htmlDownloader.downloadPackage(mirror + pkg.getPath(), tempDirectory + File.separator + pkg.getLocalpath(), pkg.getFilename(), monitor);
				} catch (IOException ex) {
					throw new SVException(String.format("%s %s", Messages.getString("updater.label.info.download"), pkg.getFilename()), ex	);  
				}
				monitor.worked(1);
			}
	}

	private void verifyPackages(List<Package> packages, PublicKey pubk, ProgressMonitor monitor) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, SVException, BadArgumentException {

		monitor.setTotalTime(packages.size());
		for (Package pkg : packages) {
			monitor.subTask(pkg.getFilename());
			pkg.setRootDirectory("tmp"); 
			if (!pkg.verify(pubk)) {
				throw new SVException(String.format("%s %s", Messages.getString("updater.label.info.verify.package"),pkg.getFilename()));  
			}
			monitor.worked(1);
		}
	}

	public void onFinish() {
	}

}
