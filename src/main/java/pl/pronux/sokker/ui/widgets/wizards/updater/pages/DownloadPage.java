package pl.pronux.sokker.ui.widgets.wizards.updater.pages;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.custom.Monitor;
import pl.pronux.sokker.ui.widgets.custom.ProgressBarCustom;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;
import pl.pronux.sokker.ui.widgets.wizards.updater.UpdaterWizard;
import pl.pronux.sokker.updater.actions.PackagesDownloader;
import pl.pronux.sokker.updater.model.Package;

public class DownloadPage extends Page {
	public static final String PAGE_NAME = "DOWNLOAD_PAGE"; //$NON-NLS-1$
	private ProgressBarCustom progressBar;
	private Label information;
	private String mirror;
	private String osType;
	private String versionType;
	private List<Package> packages;

	public DownloadPage(Wizard parent) {
		super(parent, Messages.getString("updater.page.download"), PAGE_NAME); //$NON-NLS-1$
	}

	@Override
	protected void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;

		progressBar = new ProgressBarCustom(container, SWT.NONE);
		progressBar.setLayoutData(data);

		information = new Label(container, SWT.NONE);
		information.setLayoutData(data);

	}

	@Override
	public void onEnterPage() {

		mirror = ((MirrorsPage) getWizard().getPage(MirrorsPage.PAGE_NAME)).getMirror();
		osType = ((UpdaterWizard) getWizard()).getOsType();
		versionType = ((UpdaterWizard) getWizard()).getVersionType();

		getWizard().getPreviousButton().setEnabled(false);
		getWizard().getNextButton().setEnabled(false);

		packages = ((XMLPage) getWizard().getPage(XMLPage.PAGE_NAME)).getPackagesCollection().getPackages();
		try {
			progressBar.run(true, true, new PackagesDownloader(mirror, versionType, osType, packages));
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
		}

		Thread monitorThread = new Thread(new Runnable() {
			public void run() {
				Monitor monitor = progressBar.getProgressMonitor();
				while (!monitor.isDone() && !monitor.isCanceled() && !monitor.isInterrupted()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
				}
				if (monitor.isDone() && !monitor.isCanceled()) {
					getWizard().getDisplay().asyncExec(new Runnable() {
						public void run() {
							getWizard().getPreviousButton().setEnabled(false);
							getWizard().getNextButton().setEnabled(false);
							getWizard().getFinishButton().setEnabled(true);
							getWizard().getCancelButton().setEnabled(false);
							information.setText(Messages.getString("updater.label.info.finish") + "\n" + Messages.getString("message.information.restart")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						}
					});
				}

				if (monitor.isInterrupted()) {
					getWizard().getDisplay().asyncExec(new Runnable() {
						public void run() {
							getWizard().getPreviousButton().setEnabled(false);
							getWizard().getCancelButton().setEnabled(false);
							getWizard().getNextButton().setEnabled(false);
							getWizard().getFinishButton().setEnabled(true);
						}
					});
				}
			}
		});

		monitorThread.setDaemon(true);
		monitorThread.start();

		super.onEnterPage();
	}
	
	@Override
	public void onCancelPage() {
		progressBar.cancel();
		super.onCancelPage();
	}
}
