package pl.pronux.sokker.ui.widgets.shells;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.Fonts;

public class TrainingReportShell extends Shell {

	private Display display;

	private Font fontMain;

	private Monitor monitor;

	private Training training;

	private Tree viewTree;

	public TrainingReportShell(Shell parent, int style, final Training training) {
		super(parent, style);
		
		this.training = training;

		this.display = parent.getDisplay();

		monitor = display.getPrimaryMonitor();

//		parent.setEnabled(false);
		
		fontMain = ConfigBean.getFontMain();

		this.setSize(new org.eclipse.swt.graphics.Point(monitor.getClientArea().width - 100, monitor.getClientArea().height - 100));

		this.setMinimumSize(new org.eclipse.swt.graphics.Point(500, 500));
		this.setLocation(monitor.getClientArea().width / 2 - this.getSize().x / 2, monitor.getClientArea().height / 2 - this.getSize().y / 2);
		this.setFont(fontMain);

		this.setLayout(new FormLayout());

		addBody(this);
		this.layout(true);

		this.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
				}
			}
		});

		setTreeTable(viewTree, training);

	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	private void addBody(final Shell shell) {

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 10);
		formData.height = 30;

		Label labelTitle = new Label(shell, SWT.NONE);
		labelTitle.setLayoutData(formData);
		labelTitle.setText(Messages.getString("training.report")); 
		labelTitle.setAlignment(SWT.CENTER);

		Font font = Fonts.getFont(display, "Arial", 20, SWT.BOLD); 

		labelTitle.setFont(font);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(labelTitle, 5);
		formData.height = 30;

		Label labelDate = new Label(shell, SWT.NONE);
		labelDate.setLayoutData(formData);

		if(training != null) {
			labelDate.setText(String.format("%s: %s", Messages.getString("training.date"), training.getDate().toDateString()));	  
		}
		
		labelDate.setAlignment(SWT.CENTER);

		Font fontDate = Fonts.getFont(display, "Arial", 16, SWT.BOLD); 

		labelDate.setFont(fontDate);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(labelDate, 5);
		formData.height = 30;

		Label labelSeason = new Label(shell, SWT.NONE);
		labelSeason.setLayoutData(formData);

		if( training!= null) {
			labelSeason.setText(String.format("%s %d", Messages.getString("training.week"), training.getDate().getSokkerDate().getWeek()));	  
		}
		
		labelSeason.setAlignment(SWT.CENTER);
		labelSeason.setFont(fontDate);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(labelSeason, 5);
		formData.bottom = new FormAttachment(100, -40);

		addTreeTable(formData);

		Button button1 = new Button(shell, SWT.NONE);
		button1.setText(Messages.getString("button.ok")); 

		formData = new FormData();
		formData.left = new FormAttachment(0, (shell.getClientArea().width / 2) - 60 / 2);
		formData.top = new FormAttachment(viewTree, 5);
		formData.height = 25;
		formData.width = 60;

		button1.setLayoutData(formData);

		button1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				shell.close();
			}
		});
		shell.setDefaultButton(button1);
	}

	private void addTreeTable(FormData formData) {
		viewTree = new Tree(this, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		// viewTree.setHeaderVisible(true);
		viewTree.setLinesVisible(true);
		viewTree.setLayoutData(formData);
		String[] columns = {
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				" ", 
				"" 
		};

		viewTree.setFont(ConfigBean.getFontTable());

		// tworzymy kolumny dla trenerow

		for (int j = 0; j < columns.length; j++) {
			TreeColumn column = new TreeColumn(viewTree, SWT.NONE);

			column.setAlignment(SWT.LEFT);

			column.setText(columns[j]);
			column.setResizable(false);

			if (j > 2) {
				column.setAlignment(SWT.RIGHT);
				if (columns[j].isEmpty()) {
					// column.setWidth(70);
					if (SettingsHandler.IS_LINUX) {
						column.pack();
					}
				} else {
					column.setWidth(40);
					// column.pack();
				}

			} else {
				// column.pack();
				column.setWidth(140);
			}
		}
	}

	private void fillCoachNode(TreeItem treeItem, Training training) {

		TreeItem item = new TreeItem(treeItem, SWT.NONE);
		item.setBackground(treeItem.getParent().getShell().getBackground());
		fillCoachTreeItemVirtualColumns(item);

		if (training.getHeadCoach() != null) {
			item = new TreeItem(treeItem, SWT.NONE);
			Coach coach = training.getHeadCoach();
			fillCoachTreeItem(item, coach, Coach.JOB_HEAD);
		}
		if (training.getJuniorCoach() != null) {
			Coach coach = training.getJuniorCoach();
			item = new TreeItem(treeItem, SWT.NONE);
			fillCoachTreeItem(item, coach, Coach.JOB_JUNIORS);
		}

		if (training.getAssistants().size() > 0) {
			for (Coach coach : training.getAssistants()) {
				item = new TreeItem(treeItem, SWT.NONE);
				fillCoachTreeItem(item, coach, Coach.JOB_ASSISTANT);
			}
		}

	}

	private void fillCoachTreeItem(TreeItem item, Coach coach, int job) {
		int c = 0;

		item.setText(c++, coach.getName());
		item.setText(c++, coach.getSurname());

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

		item.setText(c++, String.valueOf(coach.getStamina()));
		item.setText(c++, String.valueOf(coach.getPace()));
		item.setText(c++, String.valueOf(coach.getTechnique()));
		item.setText(c++, String.valueOf(coach.getPassing()));
		item.setText(c++, String.valueOf(coach.getKeepers()));
		item.setText(c++, String.valueOf(coach.getDefenders()));
		item.setText(c++, String.valueOf(coach.getPlaymakers()));
		item.setText(c++, String.valueOf(coach.getScorers()));
		item.setText(c++, String.valueOf(coach.getGeneralskill()));
	}

	private void fillCoachTreeItemVirtualColumns(TreeItem item) {
		int c = 0;

		item.setText(c++, Messages.getString("table.name")); 
		item.setText(c++, Messages.getString("table.surname")); 
		item.setText(c++, Messages.getString("table.job")); 
		item.setText(c++, Messages.getString("table.stamina")); 
		item.setText(c++, Messages.getString("table.pace")); 
		item.setText(c++, Messages.getString("table.technique")); 
		item.setText(c++, Messages.getString("table.passing")); 
		item.setText(c++, Messages.getString("table.keeper")); 
		item.setText(c++, Messages.getString("table.defender")); 
		item.setText(c++, Messages.getString("table.playmaker")); 
		item.setText(c++, Messages.getString("table.scorer")); 
		item.setText(c++, Messages.getString("table.generallSkill")); 
	}

	private void fillJuniorNode(TreeItem treeItem, Training training) {
		TreeItem item = new TreeItem(treeItem, SWT.NONE);
		item.setBackground(treeItem.getParent().getShell().getBackground());
		fillJuniorTreeItemVirtualColumns(item);
		int max = 0;

		if (training.getJuniors().size() > 0) {
			for (Junior junior : training.getJuniors()) {
				JuniorSkills[] skills = junior.getSkills();
				max = skills.length;
				if (max > 1) {
					for (int i = max - 1; i > 0; i--) {
						if (training.equals(skills[i].getTraining())) {
							if (skills[i].getSkill() - skills[i - 1].getSkill() > 0) {
								item = new TreeItem(treeItem, SWT.NONE);
								fillJuniorTreeItem(item, junior, i);
							} else if (skills[i].getSkill() - skills[i - 1].getSkill() < 0) {
								item = new TreeItem(treeItem, SWT.NONE);
								fillJuniorTreeItem(item, junior, i);
							}
						}
					}
				}
			}
		}

	}

	private void fillJuniorTrainedNode(TreeItem treeItem, Training training) {
		int max = 0;
		TreeItem item = new TreeItem(treeItem, SWT.NONE);
		item.setBackground(treeItem.getParent().getShell().getBackground());
		fillJuniorTrainedTreeItemVirtualColumns(item);

		if (training.getJuniors().size() > 0) {
			for (Junior junior : training.getJuniors()) {
				JuniorSkills[] skills = junior.getSkills();
				max = skills.length;
				if (max > 1) {
					if (skills[max - 1].getWeeks() == 0 && training.equals(skills[max - 1].getTraining())) {
						item = new TreeItem(treeItem, SWT.NONE);
						fillJuniorTrainedTreeItem(item, junior, max);
					}
				}
			}
		}

	}

	private void fillJuniorTrainedTreeItem(TreeItem item, Junior junior, int index) {
		int c = 0;

		item.setText(c++, junior.getName());
		item.setText(c++, junior.getSurname());
		c++;

		item.setText(c++, String.valueOf(junior.getSkills()[index - 1].getSkill()));
	}

	private void fillJuniorTrainedTreeItemVirtualColumns(TreeItem item) {
		int c = 0;

		item.setText(c++, Messages.getString("table.name")); 
		item.setText(c++, Messages.getString("table.surname")); 
		c++;

		item.setText(c++, Messages.getString("table.skill.short")); 
	}

	private void fillJuniorTreeItem(TreeItem item, Junior junior, int index) {
		int c = 0;

		item.setText(c++, junior.getName());
		item.setText(c++, junior.getSurname());
		c++;
		// item.setText(c++, String.valueOf(junior.getSkills()[max -
		// 1].getSkill()));
		if (junior.getSkills()[index].getSkill() - junior.getSkills()[index - 1].getSkill() > 0) {
			item.setBackground(c, ConfigBean.getColorIncrease());	
		} else if(junior.getSkills()[index].getSkill() - junior.getSkills()[index - 1].getSkill() < 0) {
			item.setBackground(c, ConfigBean.getColorDecrease());
		}
		
		item.setText(c++, junior.getSkills()[index].getSkill() + "  " + SVNumberFormat.formatIntegerWithSignZero(junior.getSkills()[index].getSkill() - junior.getSkills()[index - 1].getSkill())); 
		item.setText(c++, String.valueOf(junior.getSkills()[index].getWeeks()));
	}

	private void fillJuniorTreeItemVirtualColumns(TreeItem item) {
		int c = 0;

		item.setText(c++, Messages.getString("table.name")); 
		item.setText(c++, Messages.getString("table.surname")); 
		c++;
		// item.setText(c++, Messages.getString("table.skill"));
		item.setText(c++, Messages.getString("table.skill.short")); 
		item.setText(c++, Messages.getString("table.week.short")); 

	}

	private boolean testPlayerJumps(PlayerSkills before, PlayerSkills now) {
		if (now.getStamina() - before.getStamina() != 0) {
			return true;
		}
		if (now.getPace() - before.getPace() != 0) {
			return true;
		}
		if (now.getTechnique() - before.getTechnique() != 0) {
			return true;
		}
		if (now.getPassing() - before.getPassing() != 0) {
			return true;
		}
		if (now.getKeeper() - before.getKeeper() != 0) {
			return true;
		}
		if (now.getDefender() - before.getDefender() != 0) {
			return true;
		}
		if (now.getPlaymaker() - before.getPlaymaker() != 0) {
			return true;
		}
		if (now.getScorer() - before.getScorer() != 0) {
			return true;
		}
		if (now.getDiscipline() - before.getDiscipline() != 0) {
			return true;
		}
		if (now.getExperience() - before.getExperience() != 0) {
			return true;
		}
		if (now.getTeamwork() - before.getTeamwork() != 0) {
			return true;
		}
		return false;
	}

	private void fillPlayerNode(TreeItem treeItem, Training training) {
		int max = 0;
		TreeItem item = new TreeItem(treeItem, SWT.NONE);
		item.setBackground(treeItem.getParent().getShell().getBackground());
		fillPlayerTreeItemVirtualColumns(item);

		if (training.getPlayers().size() > 0) {
			for (Player player : training.getPlayers()) {
				PlayerSkills[] skills = player.getSkills();
				max = skills.length;
				if (max > 1) {
					for (int i = max - 1; i > 0; i--) {
						if (training.equals(skills[i].getTraining())) {
							if (testPlayerJumps(player.getSkills()[i], player.getSkills()[i - 1])) {
								item = new TreeItem(treeItem, SWT.NONE);
								fillPlayerTreeItem(item, player, i);
								break;
							}
						}
					}
				}
			}
		}

	}

	private void fillPlayerTreeItem(TreeItem item, Player player, int index) {
		int c = 0;
		int diff = 0;
		item.setText(c++, player.getName());
		item.setText(c++, player.getSurname());
		c++;

		for (int i = 4; i < player.getSkills()[index].getStatsTable().length; i++) {
			diff = player.getSkills()[index].getStatsTable()[i] - player.getSkills()[index - 1].getStatsTable()[i];
			if (diff > 0) {
				item.setBackground(c, ConfigBean.getColorIncrease());
				item.setText(c++, player.getSkills()[index].getStatsTable()[i] + "  " + SVNumberFormat.formatIntegerWithSignZero(diff)); 
			} else if (diff < 0) {
				item.setBackground(c, ConfigBean.getColorDecrease());
				item.setText(c++, player.getSkills()[index].getStatsTable()[i] + "  " + SVNumberFormat.formatIntegerWithSignZero(diff)); 
			} else {
				c++;
			}

		}

	}

	private void fillPlayerTreeItemVirtualColumns(TreeItem item) {
		int c = 0;
		item.setText(c++, Messages.getString("table.name")); 
		item.setText(c++, Messages.getString("table.surname")); 
		c++;
		item.setText(c++, Messages.getString("table.stamina")); 
		item.setText(c++, Messages.getString("table.pace")); 
		item.setText(c++, Messages.getString("table.technique")); 
		item.setText(c++, Messages.getString("table.passing")); 
		item.setText(c++, Messages.getString("table.keeper")); 
		item.setText(c++, Messages.getString("table.defender")); 
		item.setText(c++, Messages.getString("table.playmaker")); 
		item.setText(c++, Messages.getString("table.scorer")); 
		item.setText(c++, Messages.getString("table.discipline")); 
		item.setText(c++, Messages.getString("table.experience")); 
		item.setText(c++, Messages.getString("table.teamwork")); 
	}

	private void fillTrainingTable(Tree tree, Training training) {
		// Turn off drawing to avoid flicker
		TreeItem item;
		int c = 0;
		tree.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		tree.removeAll();

		// List the entries using entrySet()
		item = new TreeItem(tree, SWT.NONE);
		c = 0;
		item.setText(c++, Messages.getString("formation")); 
		item.setText(c++, Messages.getString("formation." + training.getFormation())); 

		item = new TreeItem(tree, SWT.NONE);
		c = 0;
		item.setText(c++, Messages.getString("training.type")); 
		item.setText(c++, Messages.getString("training.type." + training.getType())); 

		item = new TreeItem(tree, SWT.NONE);
		c = 0;
		item.setText(c++, Messages.getString("tree.ViewCoaches")); 
		fillCoachNode(item, training);

		item = new TreeItem(tree, SWT.NONE);
		c = 0;
		item.setText(c++, Messages.getString("tree.ViewPlayers")); 
		fillPlayerNode(item, training);

		item = new TreeItem(tree, SWT.NONE);
		c = 0;
		item.setText(c++, Messages.getString("tree.ViewJuniors")); 
		fillJuniorNode(item, training);

		item = new TreeItem(tree, SWT.NONE);
		c = 0;
		item.setText(c++, Messages.getString("tree.ViewJuniorsTrained")); 
		fillJuniorTrainedNode(item, training);
		// for (int i = 0; i < tree.getColumnCount() - 1; i++) {
		// tree.getColumn(i).pack();
		// }
		// tree.update();
		// Turn drawing back on
		for (int i = 0; i < tree.getItemCount(); i++) {
			tree.getItem(i).setExpanded(true);
		}
		tree.setRedraw(true);

		// tree.setRedraw(false);
		// for (int i = 0; i < tree.getColumnCount() - 1; i++) {
		// tree.getColumn(i).pack();
		// tree.getColumn(i).setWidth(tree.getColumn(i).getWidth() + 5);
		// }
		// // for (int i = 0; i < tree.getItemCount(); i++) {
		// // tree.getItem(i).setExpanded(false);
		// // }
		// tree.setRedraw(true);

	}

	private void setTreeTable(Tree tree, Training training) {
		if (training != null) {
			fillTrainingTable(tree, training);
		}
	}

}
