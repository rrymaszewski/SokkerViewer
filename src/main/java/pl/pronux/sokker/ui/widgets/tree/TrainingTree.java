package pl.pronux.sokker.ui.widgets.tree;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.enums.OperatingSystem;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;

public class TrainingTree extends Tree {

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public TrainingTree(Composite parent, int style) {
		super(parent, style);

		this.setHeaderVisible(true);
		this.setLinesVisible(true);

		String[] columns = { Messages.getString("table.date"), Messages.getString("table.formation"), Messages.getString("table.trainingType"), Messages.getString("table.coach.head"),
				Messages.getString("table.coach.juniors"), Messages.getString("table.coach.assistants"), " ", " ", " ", " ", " ", " ", "" };

		this.setFont(ConfigBean.getFontTable());

		// tworzymy kolumny dla trenerow

		for (int j = 0; j < columns.length; j++) {
			TreeColumn column = new TreeColumn(this, SWT.NONE);

			column.setAlignment(SWT.LEFT);
			column.setText(columns[j]);
			column.setResizable(false);

			if (j > 2) {
				if (columns[j].isEmpty()) {
					if (SettingsHandler.OS_TYPE == OperatingSystem.LINUX) {
						column.pack();
					}
				} else {
					column.setWidth(40);
				}
			} else {
				column.setWidth(120);
			}
		}
	}

	public void fillTrainingTable(List<Training> trainings) {
		// Turn off drawing to avoid flicker
		this.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		this.removeAll();

		// List the entries using entrySet()
		for (Training training : trainings) {
			TreeItem item = new TreeItem(this, SWT.NONE);
			int c = 0;
			item.setData(Training.IDENTIFIER, training);

			item.setText(c++, training.getDate().getTrainingDate(SokkerDate.THURSDAY).toDateString());
			if (training.getType() == Training.TYPE_PACE || training.getType() == Training.TYPE_STAMINA) {
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
			item.setText(c++, String.valueOf(training.getAssistants().size()));

			fillTrainingNode(item, training);
		}
		this.setRedraw(true);
	}

	private void fillTrainingNode(TreeItem treeItem, Training training) {

		treeItem.getParent().setRedraw(false);

		TreeItem item = new TreeItem(treeItem, SWT.NONE);
		item.setBackground(Colors.getGray());
		fillTreeItemVirtualColumns(item);

		if (training.getHeadCoach() != null) {
			item = new TreeItem(treeItem, SWT.NONE);
			item.setBackground(ColorResources.getColor(236, 213, 202));
			Coach coach = training.getHeadCoach();
			fillTreeItem(item, coach, Coach.JOB_HEAD, training);
		}
		if (training.getJuniorCoach() != null) {
			Coach coach = training.getJuniorCoach();
			item = new TreeItem(treeItem, SWT.NONE);
			item.setBackground(ColorResources.getColor(237, 240, 225));
			fillTreeItem(item, coach, Coach.JOB_JUNIORS, training);
		}

		if (training.getAssistants().size() > 0) {
			for (Coach coach : training.getAssistants()) {
				item = new TreeItem(treeItem, SWT.NONE);
				item.setBackground(ColorResources.getColor(252, 250, 245));
				fillTreeItem(item, coach, Coach.JOB_ASSISTANT, training);
			}
		}

		treeItem.getParent().setRedraw(true);
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

	private void fillTreeItem(TreeItem item, Coach coach, int job, Training training) {
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

		if (job != Coach.JOB_HEAD) {
			item.setFont(3, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
		} else {
			switch (training.getType()) {
			case Training.TYPE_STAMINA:
				item.setFont(4, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
				break;
			case Training.TYPE_PACE:
				item.setFont(5, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
				break;
			case Training.TYPE_TECHNIQUE:
				item.setFont(6, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
				break;
			case Training.TYPE_PASSING:
				item.setFont(7, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
				break;
			case Training.TYPE_KEEPER:
				item.setFont(8, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
				break;
			case Training.TYPE_DEFENDING:
				item.setFont(9, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
				break;
			case Training.TYPE_PLAYMAKING:
				item.setFont(10, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
				break;
			case Training.TYPE_STRIKER:
				item.setFont(11, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
				break;
			default:
				break;
			}
		}
	}
}
