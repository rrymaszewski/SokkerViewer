package pl.pronux.sokker.ui.widgets.wizards.updater.pages;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.custom.Monitor;
import pl.pronux.sokker.ui.widgets.custom.ProgressBarCustom;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;
import pl.pronux.sokker.ui.widgets.wizards.updater.UpdaterWizard;
import pl.pronux.sokker.updater.actions.PackagesXMLDownloader;
import pl.pronux.sokker.updater.model.PackagesCollection;

public class XMLPage extends Page {
	public static final String PAGE_NAME = "XML_PAGE"; 
//	private Text information;
	private String mirror;
	private String osType;
	private String versionType;
	private ProgressBarCustom progressBar;
	private PackagesCollection packagesCollection;

	public XMLPage(Wizard parent) {
		super(parent, Messages.getString("updater.page.xml"), PAGE_NAME); 
	}

	@Override
	protected void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;

		progressBar = new ProgressBarCustom(container, SWT.NONE);
		progressBar.setLayoutData(data);

//		data = new GridData();
//		data.grabExcessVerticalSpace = true;
//		data.verticalAlignment = GridData.FILL;
//		data.grabExcessHorizontalSpace = true;
//		data.horizontalAlignment = GridData.FILL;
//
//		information = new Text(container, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
//		information.setBackground(container.getBackground());
//		information.setLayoutData(data);
	}

	@Override
	public void onEnterPage() {

		mirror = ((MirrorsPage) getWizard().getPage(MirrorsPage.PAGE_NAME)).getMirror();
		osType = ((UpdaterWizard) getWizard()).getOsType();
		versionType = ((UpdaterWizard) getWizard()).getVersionType();

		getWizard().getPreviousButton().setEnabled(false);
		getWizard().getNextButton().setEnabled(false);

		packagesCollection = new PackagesCollection();
		try {
			progressBar.run(true, true, new PackagesXMLDownloader(mirror, versionType, osType, packagesCollection));
		} catch (InterruptedException e) {
			MessageDialog.openErrorMessage(getWizard().getShell(), e.getMessage()); 
		} catch (InvocationTargetException e) {
			MessageDialog.openErrorMessage(getWizard().getShell(), e.getMessage()); 
		}

		Thread monitorThread = new Thread(new Runnable() {
			public void run() {
				Monitor monitor = progressBar.getProgressMonitor();
				while (!monitor.isDone() && !monitor.isCanceled() && !monitor.isInterrupted()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				if (monitor.isDone() && !monitor.isCanceled() && !monitor.isInterrupted()) {
					getWizard().getDisplay().asyncExec(new Runnable() {
						public void run() {
							if(packagesCollection.getPackages().size() > 0) {
								getWizard().getPreviousButton().setEnabled(false);
								getWizard().getNextButton().setEnabled(true);
								MessageDialog.openInformationMessage(getWizard().getShell(), Messages.getString("updater.label.info.successful")); 
//								information.setText(Messages.getString("updater.label.info.successful")); 
							} else {
								getWizard().getPreviousButton().setEnabled(false);
								getWizard().getNextButton().setEnabled(false);
								getWizard().getFinishButton().setEnabled(true);
								getWizard().getCancelButton().setEnabled(false);
								MessageDialog.openInformationMessage(getWizard().getShell(), Messages.getString("updater.label.info.empty")); 
//								information.setText(Messages.getString("updater.label.info.empty")); 
							}
						}
					});
				}
				
				if (monitor.isInterrupted() && monitor.isDone()) {
					getWizard().getDisplay().asyncExec(new Runnable() {
						public void run() {
							MessageDialog.openInformationMessage(getWizard().getShell(), Messages.getString("updater.label.info.empty")); 
//							information.setText(Messages.getString("updater.label.info.empty")); 							
						}
					});
				}

				if (monitor.isInterrupted() && !monitor.isCanceled()) {
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

		monitorThread.setDaemon(false);
		monitorThread.start();

		super.onEnterPage();
	}

	public PackagesCollection getPackagesCollection() {
		return packagesCollection;
	}
	
	@Override
	public void onCancelPage() {
		progressBar.cancel();
		super.onCancelPage();
	}

}
