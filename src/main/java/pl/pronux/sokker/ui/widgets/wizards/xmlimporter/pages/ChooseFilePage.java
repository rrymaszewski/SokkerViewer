package pl.pronux.sokker.ui.widgets.wizards.xmlimporter.pages;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.importer.controller.PackagesManager;
import pl.pronux.sokker.importer.controller.SOPackagesManager;
import pl.pronux.sokker.importer.controller.SVPackagesManager;
import pl.pronux.sokker.importer.model.IXMLpack;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.widgets.custom.Monitor;
import pl.pronux.sokker.ui.widgets.custom.ProgressBarCustom;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;

public class ChooseFilePage extends Page {

	private ArrayList<IXMLpack> packages;
	private ProgressBarCustom progressBar;
	public static String PAGE_NAME = "CHOOSEFILE_PAGE"; //$NON-NLS-1$
	private Table table;
	private PackagesManager packagesManager;

	public ChooseFilePage(Wizard parent) {
		super(parent, Messages.getString("importer.page.packages.title"), PAGE_NAME); //$NON-NLS-1$
	}

	protected void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 30;
		container.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		progressBar = new ProgressBarCustom(container, SWT.NONE);
		progressBar.setLayoutData(gridData);

		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);

		String[] columns = {
				"date", //$NON-NLS-1$
				"complete", //$NON-NLS-1$
				"" //$NON-NLS-1$
		};

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			;
			column.setText(columns[i]);
		}

		for (int i = 0; i < table.getColumnCount(); i++) {
			if (i == table.getColumnCount() - 1) {
				if (SettingsHandler.OS_TYPE == IPlugin.LINUX) {
					table.getColumn(i).pack();
				}
			} else {
				table.getColumn(i).pack();
			}
		}
		// dialogChanged();
		setContainer(container);
	}

	public void fillTable(ArrayList<IXMLpack> list) {

		table.remove(0, table.getItemCount() - 1);

		for (int i = 0; i < list.size(); i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData("package", list.get(i)); //$NON-NLS-1$
			IXMLpack pack = list.get(i);
			item.setText(new String[] {
					pack.getDate().toDateTimeString(),
					String.valueOf(pack.isComplete())
			});
		}

		for (int i = 0; i < table.getColumnCount() - 1; i++) {
			table.getColumn(i).pack();
		}
	}

	public ArrayList<IXMLpack> getPackages() {
		return packages;
	}

	public ProgressBarCustom getProgressBar() {
		return progressBar;
	}

	public void onEnterPage() {
		getProgressBar().setVisible(true);
		getWizard().getPreviousButton().setEnabled(false);
		getWizard().getNextButton().setEnabled(false);
		final String directory = ((DirectoryPage) getWizard().getPage(DirectoryPage.PAGE_NAME)).getDirectory();
		ApplicationsPage applicationPage = ((ApplicationsPage) getWizard().getPage(ApplicationsPage.PAGE_NAME));
		String application = applicationPage.getApplication();
		
		if(application.equals(ApplicationsPage.SOKKER_VIEWER)) {
			packagesManager = new SVPackagesManager(directory, Cache.getClub().getId());
		} else if(application.equals(ApplicationsPage.SOKKER_ORGANIZER)){
			packagesManager = new SOPackagesManager(directory, Cache.getClub().getId());
		}
			
		try {
			progressBar.run(true, true, packagesManager);
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
		} 

		Thread monitorThread = new Thread(new Runnable() {
			public void run() {
				Monitor monitor = progressBar.getProgressMonitor();
				while (!monitor.isDone() && !monitor.isCanceled()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				if (monitor.isDone() && !monitor.isCanceled()) {
					getWizard().getDisplay().asyncExec(new Runnable() {
						public void run() {
							fillTable(packagesManager.getPackages());
							// getProgressBar().setVisible(false);
							ChooseFilePage.this.setPackages(packagesManager.getPackages());
							getWizard().getNextButton().setEnabled(true);
							getWizard().getNextButton().setText(Messages.getString("button.import")); //$NON-NLS-1$
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
		progressBar.getProgressMonitor().setCanceled(true);
		super.onCancelPage();
	}

	protected void setPackages(ArrayList<IXMLpack> list) {
		this.packages = list;
	}
}
