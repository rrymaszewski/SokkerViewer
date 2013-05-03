package pl.pronux.sokker.ui.widgets.tables;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.CoachComparator;
import pl.pronux.sokker.comparators.CoachFiredComparator;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class CoachFiredTable extends SVTable<Coach> implements IViewSort<Coach> {

	private CoachFiredComparator comparator;
	private List<Coach> coaches;

	public CoachFiredTable(Composite parent, int style) {
		super(parent, style);
		
		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());
		
		comparator = new CoachFiredComparator();
		comparator.setColumn(CoachFiredComparator.SURNAME);
		comparator.setDirection(CoachFiredComparator.ASCENDING);
		
		String[] titles = { 
				"", 
				Messages.getString("table.name"),  
				Messages.getString("table.surname"),  
				Messages.getString("table.salary"),  
				Messages.getString("table.age"),  
				Messages.getString("table.generallSkill"),  
				Messages.getString("table.stamina"), 
				Messages.getString("table.pace"),  
				Messages.getString("table.technique"),  
				Messages.getString("table.passing"),  
				Messages.getString("table.keeper"),  
				Messages.getString("table.defender"),  
				Messages.getString("table.playmaker"),  
				Messages.getString("table.scorer"),  
				"" }; 

		for (int j = 0; j < titles.length; j++) {
			TableColumn column = new TableColumn(this, SWT.NONE);
			if (j > 2) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}

			column.setText(titles[j]);
			column.setResizable(false);
			column.setMoveable(false);

			if (titles[j].isEmpty()) {
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else {
				column.setWidth(40);
			}
		}
		
		this.setSortColumn(this.getColumn(CoachFiredComparator.SURNAME));
		this.setSortDirection(SWT.UP);
		
		final TableColumn[] columns = this.getColumns();

		for (int i = 1; i < columns.length - 1; i++) {
			columns[i].addSelectionListener(new SortTableListener<Coach>(this, comparator));
		}
		
		this.addLabelsListener();
		
	}
	
	public void fill(List<Coach> coaches) {
		// Turn off drawing to avoid flicker
		this.setRedraw(false);
		this.coaches = coaches;
		// We remove all the table entries, sort our
		// rows, then add the entries
		this.remove(0, this.getItemCount() - 1);
		Collections.sort(coaches, comparator);
		
		for (Coach coach : coaches) {
			TableItem item = new TableItem(this, SWT.NONE);
			
			int c = 0;
			item.setData(Coach.class.getName(), coach); 
			item.setImage(c++, FlagsResources.getFlag(coach.getCountryfrom()));
			item.setText(c++, coach.getName());
			item.setText(c++, coach.getSurname());
			item.setText(c++, coach.getSalary().formatIntegerCurrency());
			item.setText(c++, String.valueOf(coach.getAge()));
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
		for(int i = 0 ; i < this.getColumnCount()-1; i++ ) {
			this.getColumn(i).pack();
		}
		// Turn drawing back on
		this.setRedraw(true);
		
	}
	
	public void sort(SVComparator<Coach> comparator) {
		if(coaches != null) {
			Collections.sort(coaches, comparator);
			fill(coaches);
		}
		
	}

	public SVComparator<Coach> getComparator() {
		return comparator;
	}
	
	@Override
	public void setLabel(Label label, int column, TableItem item) {
		if (column >= CoachComparator.GENERAL_SKILL && column <= CoachComparator.SCORERS) {
			Coach coach = (Coach) item.getData(Coach.class.getName());
			switch(column) {
			case CoachComparator.GENERAL_SKILL:
				label.setText(Messages.getString("skill.a" + coach.getGeneralskill()));
				break;
			case CoachComparator.STAMINA:
				label.setText(Messages.getString("skill.a" + coach.getStamina()));
				break;
			case CoachComparator.PACE:
				label.setText(Messages.getString("skill.a" + coach.getPace()));
				break;
			case CoachComparator.TECHNIQUE:
				label.setText(Messages.getString("skill.a" + coach.getTechnique()));
				break;
			case CoachComparator.PASSING:
				label.setText(Messages.getString("skill.a" + coach.getPassing()));
				break;
			case CoachComparator.KEEPERS:
				label.setText(Messages.getString("skill.a" + coach.getKeepers()));
				break;
			case CoachComparator.DEFENDERS:
				label.setText(Messages.getString("skill.a" + coach.getDefenders()));
				break;
			case CoachComparator.PLAYMAKERS:
				label.setText(Messages.getString("skill.a" + coach.getPlaymakers()));
				break;
			case CoachComparator.SCORERS:
				label.setText(Messages.getString("skill.a" + coach.getScorers()));
				break;

			default:
				break;
			}
			label.pack();
		} 

		super.setLabel(label, column, item);
	}

}
