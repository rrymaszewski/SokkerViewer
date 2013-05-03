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

public class JuniorTrainedTable extends SVTable<Junior> {

	public static final int SKILL = 1;
	
	public JuniorTrainedTable(Composite parent, int style) {
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
			if (titles[j].isEmpty()) {
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}
		}
		
	}

	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}

	public void fill(Junior junior) {
		this.setRedraw(false);
		this.setData(Junior.class.getName(), junior); 

		this.remove(0, this.getItemCount()-1);
		int maxSkill = 0;
		maxSkill = junior.getSkills().length;
		int[] columns = {
			1
		};

		for (int i = maxSkill - 1; i >= 0; i--) {
			TableItem juniorTableItem = new TableItem(this, SWT.NONE);
			juniorTableItem.setData("date", junior.getSkills()[i].getDate()); 
			juniorTableItem.setText(0, junior.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY).toDateString());
			juniorTableItem.setText(1, String.valueOf(junior.getSkills()[i].getSkill()));
			juniorTableItem.setText(2, String.valueOf(junior.getSkills()[i].getWeeks()));
			if (junior.getSkills()[i].getTraining() != null) {
				Coach coach = junior.getSkills()[i].getTraining().getJuniorCoach();
				if (coach != null) {
					int value = coach.getGeneralskill();
					juniorTableItem.setText(3, String.valueOf(value));
				}
			}
			juniorTableItem.setText(4, String.valueOf(junior.getSkills()[i].getAge()));
		}

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
		}
		this.getChanges(columns);
		this.setRedraw(true);
	}
}
