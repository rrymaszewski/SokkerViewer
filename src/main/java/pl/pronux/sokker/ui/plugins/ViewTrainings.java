package pl.pronux.sokker.ui.plugins;

import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.ConfigurationManager;
import pl.pronux.sokker.actions.TeamManager;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.model.TrainingConfiguration;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.PluginTrainingDescription;
import pl.pronux.sokker.ui.widgets.composites.TrainingConfigurationAddsComposite;
import pl.pronux.sokker.ui.widgets.composites.TrainingConfigurationComposite;
import pl.pronux.sokker.ui.widgets.menus.TrainingsMenu;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.shells.TrainingReportShell;
import pl.pronux.sokker.ui.widgets.tree.TrainingTree;

public class ViewTrainings implements IPlugin {
	private class Configure implements IViewConfigure {

		private Composite composite;
		private TreeItem treeItem;
		private TrainingConfiguration trainingConfiguration;
		private TrainingConfigurationComposite trainingConfigurationComposite;
		private TrainingConfigurationAddsComposite trainingAddsConfigurationComposite;
		private boolean initialized;

		public void applyChanges() {
			if(initialized) {
				trainingConfiguration.setGeneralSettings(trainingConfigurationComposite.getGeneralSettings());
				trainingConfiguration.getGeneralSettings().put(TrainingConfiguration.ENABLED, String.valueOf(trainingAddsConfigurationComposite.getEnabledButton()));
				trainingConfiguration.setSettings(trainingAddsConfigurationComposite.getSettings());
				try {
					new ConfigurationManager().setTrainingConfiguration(trainingConfiguration);
				} catch (SQLException e) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrainings", e);
				}
			}
		}

		public void clear() {
		}

		public void dispose() {
		}

		public Composite getComposite() {
			return this.composite;
		}

		public TreeItem getTreeItem() {
			return this.treeItem;
		}

		public void init(Composite composite) {
			this.composite = composite;
			this.composite.setLayout(new FormLayout());
			FormData formData = new FormData();
			formData.left = new FormAttachment(0,5);
			formData.top = new FormAttachment(0,5);
			formData.right = new FormAttachment(100, -5);
			formData.height = 150;
			
			trainingConfigurationComposite = new TrainingConfigurationComposite(composite, SWT.NONE);
			trainingConfigurationComposite.setLayoutData(formData);
			trainingConfigurationComposite.setEnabled(false);
			
			formData = new FormData();
			formData.left = new FormAttachment(0,0);
			formData.top = new FormAttachment(trainingConfigurationComposite,0);
			formData.right = new FormAttachment(100, 0);
			
			Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			separator.setLayoutData(formData);
			
			formData = new FormData();
			formData.left = new FormAttachment(0,5);
			formData.top = new FormAttachment(separator,5);
			formData.right = new FormAttachment(100, -5);
			formData.bottom = new FormAttachment(100, -5);
			
			trainingAddsConfigurationComposite = new TrainingConfigurationAddsComposite(composite, SWT.NONE);
			trainingAddsConfigurationComposite.setLayoutData(formData);
			trainingAddsConfigurationComposite.setEnabled(false);
		}

		public void restoreDefaultChanges() {
			if (initialized) {
				trainingConfigurationComposite.set(trainingConfiguration);
				trainingAddsConfigurationComposite.set(trainingConfiguration);
			}
		}

		public void set() {
			initialized = true;
			trainingConfiguration = Cache.getConfiguration().getTrainingConfiguration();
			trainingConfigurationComposite.set(trainingConfiguration);
			trainingAddsConfigurationComposite.set(trainingConfiguration);
			trainingAddsConfigurationComposite.setEnabled(true);
			trainingConfigurationComposite.setEnabled(true);
		}

		public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		}

		public void setTreeItem(TreeItem treeItem) {
			this.treeItem = treeItem;
			this.treeItem.setText(Messages.getString("tree.ViewTrainings"));
		}
	}

	private Composite composite;

	private TreeItem treeItem;

	private Sash sashHorizontal;

	private FormData sashFormData;

	private FormData viewFormData;

	private FormData descriptionFormData;

	private PluginTrainingDescription descriptionComposite;

	private TrainingTree trainingTree;

	private ArrayList<Training> trainings;

	private TrainingsMenu menuPopUp;

	private Menu menuClear;

	public void clear() {
	}

	private void initComposite() {
		sashHorizontal = new Sash(composite, SWT.HORIZONTAL | SWT.NONE);

		sashFormData = new FormData();
		sashFormData.top = new FormAttachment(0, 250);
		sashFormData.right = new FormAttachment(100, 0);
		sashFormData.left = new FormAttachment(0, 0);

		sashHorizontal.setLayoutData(sashFormData);
		sashHorizontal.setVisible(true);

		sashHorizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				((FormData) sashHorizontal.getLayoutData()).top = new FormAttachment(0, event.y);
				sashHorizontal.getParent().layout();
			}
		});

		viewFormData = new FormData();
		viewFormData.top = new FormAttachment(sashHorizontal, 0);
		viewFormData.right = new FormAttachment(100, 0);
		viewFormData.left = new FormAttachment(0, 0);
		viewFormData.bottom = new FormAttachment(100, 0);

		trainingTree = new TrainingTree(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		trainingTree.setLayoutData(viewFormData);

		descriptionFormData = new FormData();
		descriptionFormData.top = new FormAttachment(0, 0);
		descriptionFormData.right = new FormAttachment(100, 0);
		descriptionFormData.left = new FormAttachment(0, 0);
		descriptionFormData.bottom = new FormAttachment(sashHorizontal, 0);

		descriptionComposite = new PluginTrainingDescription(composite, SWT.BORDER);
		descriptionComposite.setLayoutData(descriptionFormData);
	}

	public Composite getComposite() {
		return composite;
	}

	public IViewConfigure getConfigureComposite() {
		return new Configure();
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoTrash");
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		composite.setLayout(new FormLayout());

		initComposite();
		trainings = new ArrayList<Training>();
		menuPopUp = new TrainingsMenu(composite.getShell(), SWT.POP_UP);
		menuClear = new Menu(composite.getShell(), SWT.POP_UP);
		composite.layout(true);
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewTrainings"));
		this.treeItem.setImage(ImageResources.getImageResources("training_ico.png"));
	}

	public void set() {
		ViewerHandler.getViewer().addListener(IEvents.REFRESH_TRAININGS, new Listener() {
			public void handleEvent(Event arg0) {
				trainingTree.fillTrainingTable(trainings);
			}
		});

		trainings = Cache.getTrainings();
		trainingTree.fillTrainingTable(trainings);
		
		if (trainings.size() > 0) {
			if (trainings.get(0).isReported()) {
				final Training training = trainings.get(0);
				composite.getDisplay().asyncExec(new Runnable() {
					public void run() {
						new TrainingReportShell(composite.getShell(), SWT.CLOSE | SWT.PRIMARY_MODAL, training).open();
						try {
							new TeamManager().updateReportedTrainings(training);
						} catch (SQLException e) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewTraining -> update reported training", e);
						}
					}
				});
			}
		}
		
		treeItem.getParent().addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = treeItem.getParent().getItem(pt);
				if(treeItem.equals(item)) {
					descriptionComposite.setInfo(trainings);
				}
			}
		});
		
		trainingTree.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = trainingTree.getItem(pt);

				if (item != null) {
					if (item.getData(Coach.IDENTIFIER) != null) {
						descriptionComposite.setInfo((Coach) item.getData(Coach.IDENTIFIER));
					} else if (item.getData(Training.IDENTIFIER) != null) {
						descriptionComposite.setInfo((Training) item.getData(Training.IDENTIFIER));
						if (event.button == 3) {
							treeItem.getParent().setMenu(menuPopUp);
							treeItem.getParent().getMenu().setData(Training.IDENTIFIER, (Training) item.getData(Training.IDENTIFIER));
							treeItem.getParent().getMenu().setVisible(true);
						} else {
							treeItem.getParent().setMenu(menuClear);
							treeItem.getParent().getMenu().setData(Training.IDENTIFIER, null);
							treeItem.getParent().getMenu().setVisible(true);
						}
					} else {
						descriptionComposite.setInfo(trainings);
					}
				}
			}
		});
		
		trainingTree.addListener(SWT.KeyUp, new Listener() {

			public void handleEvent(Event event) {
				TreeItem item = null;
				for (int i = 0; i < ViewTrainings.this.trainingTree.getSelection().length; i++) {
					item = ViewTrainings.this.trainingTree.getSelection()[i];
				}

				if (item != null) {
					if (item.getData(Coach.IDENTIFIER) != null) {
						descriptionComposite.setInfo((Coach) item.getData(Coach.IDENTIFIER));
					} else if (item.getData(Training.IDENTIFIER) != null) {
						descriptionComposite.setInfo((Training) item.getData(Training.IDENTIFIER));
						if (event.button == 3) {
							treeItem.getParent().setMenu(menuPopUp);
							treeItem.getParent().getMenu().setData(Training.IDENTIFIER, (Training) item.getData(Training.IDENTIFIER));
							treeItem.getParent().getMenu().setVisible(true);
						} else {
							treeItem.getParent().setMenu(menuClear);
							treeItem.getParent().getMenu().setData(Training.IDENTIFIER, null);
							treeItem.getParent().getMenu().setVisible(true);
						}
					} else {
						descriptionComposite.setInfo(trainings);
					}
				}
			}
		});
		
		trainingTree.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {

				Point pt = new Point(event.x, event.y);
				TreeItem item = trainingTree.getItem(pt);
				if (item != null) {
					if (item.getData(Training.IDENTIFIER) != null) {
						new TrainingReportShell(trainingTree.getShell(), SWT.CLOSE | SWT.PRIMARY_MODAL, (Training) item.getData(Training.IDENTIFIER)).open();
					}
				}
			}
		});
	}

	public void dispose() {
	}

	public void setSvBean(SvBean svBean) {
	}

	public void reload() {
	}
}
