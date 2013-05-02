package pl.pronux.sokker.ui.widgets.wizards.xmlimporter.pages;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.importer.controller.ImportXMLAction;
import pl.pronux.sokker.importer.model.IXMLpack;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.events.UpdateEvent;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.widgets.custom.Monitor;
import pl.pronux.sokker.ui.widgets.custom.ProgressBarCustom;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;

public class ImportPage extends Page {

	private ProgressBarCustom progressBar;
	private Button detailsButton;
	private Table table;
	private CLabel informationLabel;
	public static String PAGE_NAME = "IMPORT_PAGE"; //$NON-NLS-1$

	public ImportPage(Wizard parent) {
		super(parent, Messages.getString("importer.page.import.title"), PAGE_NAME); //$NON-NLS-1$
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
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 30;
		
		informationLabel = new CLabel(container, SWT.NONE);
		informationLabel.setText(Messages.getString("importer.page.import.processing")); //$NON-NLS-1$
		informationLabel.setLayoutData(gridData);
		
		detailsButton = new Button(container, SWT.NONE);
		detailsButton.setEnabled(false);
		detailsButton.setText(Messages.getString("button.details")); //$NON-NLS-1$
		detailsButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				table.setVisible(!table.getVisible());
			}
		});
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setVisible(false);
		
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
				if (SettingsHandler.IS_LINUX) {
					table.getColumn(i).pack();
				}
			} else {
				table.getColumn(i).pack();
			}
		}

		setContainer(container);
	}

	public ProgressBarCustom getProgressBar() {
		return progressBar;
	}

	public void onEnterPage() {
		getProgressBar().setVisible(true);
		getWizard().getPreviousButton().setEnabled(false);
		getWizard().getNextButton().setEnabled(false);
		List<IXMLpack> packages = ((ChooseFilePage) getWizard().getPage(ChooseFilePage.PAGE_NAME)).getPackages();
		try {
			progressBar.run(true, true, new ImportXMLAction(packages));
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
		}
		
		Thread monitorThread = new Thread(new Runnable() {
			public void run() {
				Monitor monitor = progressBar.getProgressMonitor();
				while(!monitor.isDone() && !monitor.isCanceled()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				if(monitor.isDone() && !monitor.isCanceled()) {
					getWizard().getDisplay().asyncExec(new Runnable() {
						public void run() {
							getWizard().getNextButton().setText(Messages.getString("button.next")); //$NON-NLS-1$
							getWizard().getPreviousButton().setEnabled(false);
							getWizard().getCancelButton().setEnabled(false);
							getWizard().getNextButton().setEnabled(false);
							getWizard().getFinishButton().setEnabled(true);
							detailsButton.setEnabled(true);
							List<IXMLpack> packages = ((ChooseFilePage) getWizard().getPage(ChooseFilePage.PAGE_NAME)).getPackages();
							fillTable(packages);
							int imported = 0;
							int failed = 0;
							for(IXMLpack pack : packages) {
								if(pack.isImported()) {
									imported++;
								} else {
									failed++;
								}
							}
							informationLabel.setText(String.format("%s %d %s %d %s %d", Messages.getString("importer.page.import.all"), packages.size(), Messages.getString("importer.page.import.correct"), imported, Messages.getString("importer.page.import.failed"), failed)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
						}
					});
				}
			}
		});
		
		monitorThread.setDaemon(true);
		monitorThread.start();
		
		super.onEnterPage();
	}
	
	private void fillTable(List<IXMLpack> list) {

		table.remove(0, table.getItemCount() - 1);

		for (int i = 0; i < list.size(); i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData("package", list.get(i)); //$NON-NLS-1$
			IXMLpack pack = list.get(i);
			item.setText(0, pack.getDate().toDateTimeString());
			
			if(pack.isImported()) {
				item.setText(1, Messages.getString("importer.page.import.ok")); //$NON-NLS-1$
				item.setForeground(1, ColorResources.getDarkGreen());	
			} else {
				item.setText(1, Messages.getString("importer.page.import.failed")); //$NON-NLS-1$
				item.setForeground(1, ColorResources.getRed());
			}
		}

		for (int i = 0; i < table.getColumnCount() - 1; i++) {
			table.getColumn(i).pack();
		}
	}
	
	@Override
	public void onCancelPage() {
		progressBar.cancel();
		super.onCancelPage();
	}
	
	@Override
	public void onFinishPage() {
		ViewerHandler.getViewer().notifyListeners(IEvents.LOAD_DATA, new UpdateEvent(false));
		super.onFinishPage();
	}
	
}
