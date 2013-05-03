package pl.pronux.sokker.ui.widgets.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class JuniorTable extends SVTable<Junior> {
	
	public static final int SKILL = 1;

	public JuniorTable(Composite parent, int style) {
		super(parent, style);
		
		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());

		String[] titles = {
				Messages.getString("table.date"), 
				Messages.getString("table.skill"), 
				Messages.getString("table.week"), 
				Messages.getString("table.coach"), 
				Messages.getString("table.age"),
				"" 
		};

		for (int j = 0; j < titles.length; j++) {
			TableColumn column = new TableColumn(this, SWT.RIGHT);
			column.setText(titles[j]);
			column.setResizable(false);
			column.setMoveable(false);
			if (titles[j].equals(Messages.getString("table.date"))) { 
				column.setWidth(90);
			} else if (titles[j].equals(Messages.getString("table.skill"))) { 
				column.setWidth(50);
			} else if (titles[j].equals(Messages.getString("table.week"))) { 
				column.setWidth(50);
			} else if (titles[j].equals(Messages.getString("table.age"))) { 
				column.setWidth(50);
			} else if (titles[j].isEmpty()) {
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}
		}
	}
	
	public void fill(Junior junior) {
		this.setRedraw(false);
		this.removeAll();

		this.setData(Junior.class.getName(), junior); 

		int maxSkill = 0;
		maxSkill = junior.getSkills().length;
		int[] columns = {
			1
		};

		for (int i = maxSkill - 1; i >= 0; i--) {
			TableItem thisItem = new TableItem(this, SWT.NONE);
			thisItem.setData("date", junior.getSkills()[i].getDate()); 
			thisItem.setText(0, junior.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY).toDateString());
			thisItem.setText(1, String.valueOf(junior.getSkills()[i].getSkill()));
			thisItem.setText(2, String.valueOf(junior.getSkills()[i].getWeeks()));
			if (junior.getSkills()[i].getTraining() != null) {
				Coach coach = junior.getSkills()[i].getTraining().getJuniorCoach();
				if (coach != null) {
					int value = coach.getGeneralskill();
					thisItem.setText(3, String.valueOf(value));
				}
			}
			thisItem.setText(4, String.valueOf(junior.getSkills()[i].getAge()));
		}
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}

		this.getChanges(columns);

		this.setRedraw(true);
	}

}
