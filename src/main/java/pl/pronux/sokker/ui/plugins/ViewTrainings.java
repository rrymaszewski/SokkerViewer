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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.TeamManager;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.DescriptionDoubleComposite;
import pl.pronux.sokker.ui.widgets.menus.TrainingsMenu;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.shells.TrainingReportShell;

public class ViewTrainings implements IPlugin {

	private Composite composite;

	private TreeItem treeItem;

	private Sash sashHorizontal;

	private FormData sashFormData;

	private FormData viewFormData;

	private FormData descriptionFormData;

	private DescriptionDoubleComposite descriptionComposite;

	private Tree viewTree;

	private ArrayList<Training> trainingList;

	private TrainingsMenu menuPopUp;

	private Menu menuClear;

	public void clear() {

	}

	private void initComposite() {
		sashHorizontal = new Sash(composite, SWT.HORIZONTAL | SWT.NONE);

		sashFormData = new FormData();
		sashFormData.top = new FormAttachment(0, 230);
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

		viewTree = new Tree(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		viewTree.setHeaderVisible(true);
		viewTree.setLinesVisible(true);
		viewTree.setLayoutData(viewFormData);

		descriptionFormData = new FormData();
		descriptionFormData.top = new FormAttachment(0, 0);
		descriptionFormData.right = new FormAttachment(100, 0);
		descriptionFormData.left = new FormAttachment(0, 0);
		descriptionFormData.bottom = new FormAttachment(sashHorizontal, 0);

		descriptionComposite = new DescriptionDoubleComposite(composite, SWT.BORDER);
		descriptionComposite.setLayoutData(descriptionFormData);
		descriptionComposite.setVisible(true);
		descriptionComposite.setFont(ConfigBean.getFontDescription());

		descriptionComposite.setLeftDescriptionStringFormat("%-25s%-15s\r\n");
		descriptionComposite.setLeftFirstColumnSize(25);
		descriptionComposite.setLeftSecondColumnSize(15);

		descriptionComposite.setRightDescriptionStringFormat("%-25s%-15s\r\n");
		descriptionComposite.setRightFirstColumnSize(25);
		descriptionComposite.setRightSecondColumnSize(15);
	}

	public Composite getComposite() {
		return composite;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
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
		trainingList = new ArrayList<Training>();
//		addComposite();
		addTreeTable();
		menuPopUp = new TrainingsMenu(composite.getShell(), SWT.POP_UP);
		menuClear = new Menu(composite.getShell(), SWT.POP_UP);

		composite.layout(true);
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
//		this.confProperties = confProperties;

	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewTrainings"));

		this.treeItem.setImage(ImageResources.getImageResources("training_ico.png"));

	}

	public void set() {
		
		ViewerHandler.getViewer().addListener(IEvents.REFRESH_TRAININGS, new Listener() {

			public void handleEvent(Event arg0) {
				fillTrainingTable(viewTree, trainingList);
			}
		});
		
		trainingList = Cache.getTrainings();
		fillTrainingTable(viewTree, trainingList);
		if(trainingList.size() > 0) {
			if(trainingList.get(0).isReported()) {
				final Training training = trainingList.get(0);
				composite.getDisplay().asyncExec(new Runnable() {
					public void run() {
//						if(IViewComposite.OS_TYPE == IViewComposite.LINUX) {
//							new TrainingReportShell(composite.getShell(), SWT.TOOL, training);
//						} else {
//							new TrainingReportShell(composite.getShell(), SWT.ON_TOP, training);
//						}
//						new TrainingReportShell(composite.getShell(), SWT.CLOSE | SWT.MAX, training);
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
	}

//	private void addComposite() {
//		FormData formData = new FormData();
//		formData.left = new FormAttachment(0, 0);
//		formData.right = new FormAttachment(100, 0);
//		formData.top = new FormAttachment(0, 0);
//		formData.bottom = new FormAttachment(100, 0);
//
//		tabComposite = new TabComposite(composite, SWT.NONE);
//		tabComposite.setVisible(true);
//		tabComposite.setLayoutData(formData);
//
//	}

	private void addTreeTable() {

		String[] columns = {
				Messages.getString("table.date"),
				Messages.getString("table.formation"),
				Messages.getString("table.trainingType"),
				Messages.getString("table.coach.head"),
				Messages.getString("table.coach.juniors"),
				Messages.getString("table.coach.assistants"),
				" ",
				" ",
				" ",
				" ",
				" ",
				" ",
				"" };

		viewTree.setFont(ConfigBean.getFontTable());

		// tworzymy kolumny dla trenerow

		for (int j = 0; j < columns.length; j++) {
			TreeColumn column = new TreeColumn(viewTree, SWT.NONE);

			column.setAlignment(SWT.LEFT);

			column.setText(columns[j]);
			column.setResizable(false);

			if(j > 2) {
				if (columns[j].equals("")) {
				// column.setWidth(70);
					if (SettingsHandler.OS_TYPE == IPlugin.LINUX) {
						column.pack();
					}
				} else {
					column.setWidth(40);
//					column.pack();
				}

			} else {
				column.setWidth(120);
//				column.pack();
			}

//			if (columns[j].equals("")) {
//				// column.setWidth(70);
//				if (IViewComposite.OS_TYPE == IViewComposite.LINUX) {
//					column.pack();
//				}
//			} else {
////				column.setWidth(140);
////				 column.pack();
//			}
		}

		viewTree.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);

				TreeItem item = viewTree.getItem(pt);

				if(item != null) {
						if(item.getData(Coach.IDENTIFIER)!=null) {
							setStatsCoachInfo((Coach)item.getData(Coach.IDENTIFIER), descriptionComposite);
						} else if(item.getData(Training.IDENTIFIER)!=null) {
							setStatsTrainingInfo((Training)item.getData(Training.IDENTIFIER), descriptionComposite);
							
							if (event.button == 3) {
								treeItem.getParent().setMenu(menuPopUp);
								treeItem.getParent().getMenu().setData(Training.IDENTIFIER, (Training)item.getData(Training.IDENTIFIER));
								treeItem.getParent().getMenu().setVisible(true);
							} else {
								treeItem.getParent().setMenu(menuClear);
								treeItem.getParent().getMenu().setData(Training.IDENTIFIER, null);
								treeItem.getParent().getMenu().setVisible(true);
							}
						} else {
							descriptionComposite.clearAll();
						}
				}
			}
		});

		viewTree.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {

				Point pt = new Point(event.x, event.y);
				TreeItem item = viewTree.getItem(pt);
				if (item != null) {
					if(item.getData(Training.IDENTIFIER)!=null) {
						
						new TrainingReportShell(viewTree.getShell(), SWT.CLOSE | SWT.PRIMARY_MODAL, (Training)item.getData(Training.IDENTIFIER)).open();
					}
				}
			}
		});
	}

	private void fillTreeItem(TreeItem item, Coach coach, int job) {
		int c = 0;

		switch (job) {
		case Coach.JOB_HEAD:
			item.setText(c++, Messages.getString("coach.job.head.short"));
			break;
		case Coach.JOB_JUNIORS:
			item.setText(c++, Messages.getString("coach.job.juniors.short"));
			break;
		case Coach.JOB_ASSISTANT:
			item.setText(c++, Messages.getString("coach.job.assistant.short"));
			break;
		default:
			return;
		}
		item.setData(Coach.IDENTIFIER, coach);

		item.setText(c++, coach.getName());
		item.setText(c++, coach.getSurname());

		item.setText(c++, String.valueOf(coach.getGeneralskill()));
		item.setText(c++, String.valueOf(coach.getStamina()));
		item.setText(c++, String.valueOf(coach.getPace()));
		item.setText(c++, String.valueOf(coach.getTechnique()));
		item.setText(c++, String.valueOf(coach.getPassing()));
		item.setText(c++, String.valueOf(coach.getKeepers()));
		item.setText(c++, String.valueOf(coach.getDefenders()));
		item.setText(c++, String.valueOf(coach.getPlaymakers()));
		item.setText(c++, String.valueOf(coach.getScorers()));
	}

	private void fillTreeItemVirtualColumns(TreeItem item) {
		int c = 0;

		item.setData(Coach.IDENTIFIER, null);

		item.setText(c++, Messages.getString("table.job"));
		item.setText(c++, Messages.getString("table.name"));
		item.setText(c++, Messages.getString("table.surname"));
		item.setText(c++, Messages.getString("table.generallSkill"));
		item.setText(c++, Messages.getString("table.stamina"));
		item.setText(c++, Messages.getString("table.pace"));
		item.setText(c++, Messages.getString("table.technique"));
		item.setText(c++, Messages.getString("table.passing"));
		item.setText(c++, Messages.getString("table.keeper"));
		item.setText(c++, Messages.getString("table.defender"));
		item.setText(c++, Messages.getString("table.playmaker"));
		item.setText(c++, Messages.getString("table.scorer"));

	}

	private void setStatsCoachInfo(Coach coach, DescriptionDoubleComposite description) {

		description.clearAll();

		String[][] values;

		values = new String[7][2];
		values[0][0] = Messages.getString("coach.id");
		values[1][0] = Messages.getString("coach.name");
		values[2][0] = Messages.getString("coach.surname");
		values[3][0] = Messages.getString("coach.general");
		values[4][0] = Messages.getString("coach.age");
		values[5][0] = Messages.getString("coach.country");
		values[6][0] = Messages.getString("coach.salary");

		values[0][1] = String.valueOf(coach.getId());
		values[1][1] = coach.getName();
		values[2][1] = coach.getSurname();
		values[3][1] = Messages.getString("skill.a" + coach.getGeneralskill()) + " [" + coach.getGeneralskill() + "]";
		values[4][1] = String.valueOf(coach.getAge());
		values[5][1] = Messages.getString("country." + coach.getCountryfrom() + ".name");
		values[6][1] = coach.getSalary().formatDoubleCurrencySymbol();


		for (int i = 0; i < values.length; i++) {
			description.addLeftText(values[i]);
		}

		// statsLeft.setStyleRanges(leftStyle);

		values = new String[10][2];
		values[0][0] = Messages.getString("coach.training");
		values[1][0] = Messages.getString("coach.stamina");
		values[2][0] = Messages.getString("coach.pace");
		values[3][0] = Messages.getString("coach.technique");
		values[4][0] = Messages.getString("coach.passing");
		values[5][0] = Messages.getString("coach.keeper");
		values[6][0] = Messages.getString("coach.defender");
		values[7][0] = Messages.getString("coach.playmaker");
		values[8][0] = Messages.getString("coach.scorer");
		values[9][0] = Messages.getString("coach.general");
		values[0][1] = "";
		values[1][1] = Messages.getString("skill.a" + coach.getStamina()) + " [" + coach.getStamina() + "]";
		values[2][1] = Messages.getString("skill.a" + coach.getPace()) + " [" + coach.getPace() + "]";
		values[3][1] = Messages.getString("skill.a" + coach.getTechnique()) + " [" + coach.getTechnique() + "]";
		values[4][1] = Messages.getString("skill.a" + coach.getPassing()) + " [" + coach.getPassing() + "]";
		values[5][1] = Messages.getString("skill.a" + coach.getKeepers()) + " [" + coach.getKeepers() + "]";
		values[6][1] = Messages.getString("skill.a" + coach.getDefenders()) + " [" + coach.getDefenders() + "]";
		values[7][1] = Messages.getString("skill.a" + coach.getPlaymakers()) + " [" + coach.getPlaymakers() + "]";
		values[8][1] = Messages.getString("skill.a" + coach.getScorers()) + " [" + coach.getScorers() + "]";
		values[9][1] = "[" + coach.getSummarySkill() + "]";

		for (int i = 0; i < values.length; i++) {
			description.addRightText(values[i]);
		}
	}


	private void setStatsTrainingInfo(Training training, DescriptionDoubleComposite description) {

		int c = 0;
		description.clearAll();

		String[][] values;

		values = new String[12][2];
		values[c++][0] = Messages.getString("training.id");
		values[c++][0] = Messages.getString("training.date");
		values[c++][0] = Messages.getString("training.date.insert");
		values[c++][0] = Messages.getString("training.week");
		values[c++][0] = Messages.getString("formation");
		values[c++][0] = Messages.getString("training.type");
		values[c++][0] = Messages.getString("coach.job.head");
		values[c++][0] = Messages.getString("training.assistants");
		values[c++][0] = Messages.getString("coach.job.juniors");
		values[c++][0] = "";
		values[c++][0] = "";
		values[c++][0] = "";

		int textLeftSize = 0;
		c = 0;
		int j = 0;


		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = String.valueOf(training.getId());
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = training.getDate().getTrainingDate(SokkerDate.THURSDAY).toDateString();
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = training.getDate().toDateString();
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;


		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = String.valueOf(training.getDate().getTrainingDate(SokkerDate.THURSDAY).getSokkerDate().getSeasonWeek());
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);


		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = Messages.getString("formation." + training.getFormation());
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = Messages.getString("training.type." + training.getType());
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = getHeadCoachStats(training);

		if (training.getHeadCoach() != null) {
			description.leftStyleText(textLeftSize + getIndexOfTrainedSkill(training), getLengthOfTrainedSkill(training), composite.getDisplay().getSystemColor(SWT.COLOR_BLACK), composite.getDisplay().getSystemColor(SWT.COLOR_WHITE), SWT.NONE);
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = getAssistantsStats(training);
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		if(training.getJuniorCoach() != null) {
			values[c][j] = Messages.getString("skill.a" + training.getJuniorCoach().getGeneralskill()) + " [" + training.getJuniorCoach().getGeneralskill() + "]";
		} else {
			values[c][j] = "-";
		}
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = "";
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = "";
		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(values[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(values[c][j++]);
		values[c][j] = "";

		for (int i = 0; i < values.length; i++) {
			description.addLeftText(values[i]);
		}

		description.setLeftColor();


	}

	private String getHeadCoachStats(Training training) {
		if(training.getHeadCoach() != null) {
			Coach coach = training.getHeadCoach();
			return coach.getGeneralskill() + " " + coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " " + coach.getKeepers() + " " + coach.getDefenders() + " " + coach.getPlaymakers() + " " + coach.getScorers();
		} else {
			return "-";
		}
	}

	private int getLengthOfTrainedSkill(Training training) {
		if(training.getHeadCoach() != null) {
			Coach coach = training.getHeadCoach();
			switch(training.getType()) {
			case Training.TYPE_STAMINA:
				return String.valueOf(coach.getStamina()).length();
			case Training.TYPE_KEEPER:
				return String.valueOf(coach.getKeepers()).length();
			case Training.TYPE_PLAYMAKING:
				return String.valueOf(coach.getPlaymakers()).length();
			case Training.TYPE_PASSING:
				return String.valueOf(coach.getPassing()).length();
			case Training.TYPE_TECHNIQUE:
				return String.valueOf(coach.getTechnique()).length();
			case Training.TYPE_DEFENDING:
				return String.valueOf(coach.getDefenders()).length();
			case Training.TYPE_STRIKER:
				return String.valueOf(coach.getScorers()).length();
			case Training.TYPE_PACE:
				return String.valueOf(coach.getPace()).length();
			}
		}

		return 0;
	}

	private int getIndexOfTrainedSkill(Training training) {
		if(training.getHeadCoach() != null) {
			Coach coach = training.getHeadCoach();
			String position = String.valueOf(coach.getGeneralskill()) + " ";
			switch(training.getType()) {
			case Training.TYPE_STAMINA:
				return position.length();
			case Training.TYPE_KEEPER:
				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " ";
				return position.length();
			case Training.TYPE_PLAYMAKING:
				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " " + coach.getKeepers() + " " + coach.getDefenders() + " ";
				return position.length();
			case Training.TYPE_PASSING:
				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " ";
				return position.length();
			case Training.TYPE_TECHNIQUE:
				position += coach.getStamina() + " " + coach.getPace() + " " ;
				return position.length();
			case Training.TYPE_DEFENDING:
				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " " + coach.getKeepers() + " ";
				return position.length();
			case Training.TYPE_STRIKER:
				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " " + coach.getKeepers() + " " + coach.getDefenders() + " " + coach.getPlaymakers() + " ";
				return position.length();
			case Training.TYPE_PACE:
				position += coach.getStamina() + " ";
				return position.length();
			}
		}

		return 0;
	}

	private String getAssistantsStats(Training training) {
		String assistants = "";
		if(training.getAlAssistants().size() > 0) {
			for(Coach coach : training.getAlAssistants()) {
				assistants += coach.getGeneralskill() + " ";
			}
			return assistants;

		} else {
			return "-";
		}
	}

	private void fillTrainingNode(TreeItem treeItem, Training training) {

		treeItem.getParent().setRedraw(false);

		TreeItem item = new TreeItem(treeItem, SWT.NONE);
		item.setBackground(treeItem.getParent().getShell().getBackground());
		fillTreeItemVirtualColumns(item);

		if (training.getHeadCoach() != null) {
			item = new TreeItem(treeItem, SWT.NONE);
			Coach coach = training.getHeadCoach();
			fillTreeItem(item, coach, Coach.JOB_HEAD);
		}
		if (training.getJuniorCoach() != null) {
			Coach coach = training.getJuniorCoach();
			item = new TreeItem(treeItem, SWT.NONE);
			fillTreeItem(item, coach, Coach.JOB_JUNIORS);
		}

		if (training.getAlAssistants().size() > 0) {
			for (Coach coach : training.getAlAssistants()) {
				item = new TreeItem(treeItem, SWT.NONE);
				fillTreeItem(item, coach, Coach.JOB_ASSISTANT);
			}
		}

		treeItem.getParent().setRedraw(true);
	}

	private void fillTrainingTable(Tree tree, ArrayList<Training> trainingList) {
		// Turn off drawing to avoid flicker
		tree.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		tree.removeAll();

		// List the entries using entrySet()
		for (Training training : trainingList) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			int c = 0;
			item.setData(Training.IDENTIFIER, training);

			item.setText(c++, training.getDate().getTrainingDate(SokkerDate.THURSDAY).toDateString());
			if(training.getType() == Training.TYPE_PACE || training.getType() == Training.TYPE_STAMINA) {
				item.setText(c++, Messages.getString("formation." + Training.FORMATION_ALL));
			} else {
				item.setText(c++, Messages.getString("formation." + training.getFormation()));
			}
			item.setText(c++, Messages.getString("training.type." + training.getType()));
			if (training.getHeadCoach() != null) {
				item.setText(c++, "1");
			} else {
				item.setText(c++, "0");
			}
			if (training.getJuniorCoach() != null) {
				item.setText(c++, "1");
			} else {
				item.setText(c++, "0");
			}
			item.setText(c++, String.valueOf(training.getAlAssistants().size()));

			fillTrainingNode(item, training);
		}

		tree.setRedraw(true);
	}

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
