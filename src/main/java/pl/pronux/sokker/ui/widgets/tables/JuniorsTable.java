package pl.pronux.sokker.ui.widgets.tables;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.JuniorsComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;

public class JuniorsTable extends SVTable<Junior> implements ISort {

	private JuniorsComparator comparator;

	public JuniorsTable(Composite parent, int style) {
		super(parent, style);
		
		comparator = new JuniorsComparator();
		comparator.setColumn(JuniorsComparator.WEEKS);
		comparator.setDirection(JuniorsComparator.ASCENDING);
		
		this.setLinesVisible(true);

		this.setHeaderVisible(true);

		this.setFont(ConfigBean.getFontTable());

	
		String[] titles = {
				Messages.getString("table.name"), //$NON-NLS-1$
				Messages.getString("table.surname"), //$NON-NLS-1$
				Messages.getString("table.skill"), //$NON-NLS-1$
				Messages.getString("table.week") + ARROW_UP, //$NON-NLS-1$
				Messages.getString("junior.table.weeks.withoutJump"), //$NON-NLS-1$
				Messages.getString("junior.averageJumps"), //$NON-NLS-1$
				Messages.getString("junior.skill.begin.short"), //$NON-NLS-1$
				Messages.getString("junior.table.estimated.level.short"), //$NON-NLS-1$
				Messages.getString("junior.table.jumps"), //$NON-NLS-1$
				Messages.getString("table.age"), //$NON-NLS-1$
				Messages.getString("junior.exit.date"), //$NON-NLS-1$
				Messages.getString("junior.table.money.spent"), //$NON-NLS-1$
				Messages.getString("junior.table.money.left"), //$NON-NLS-1$
				Messages.getString("junior.table.money.all"), //$NON-NLS-1$
				Messages.getString("table.note.short"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};
		
		for (int j = 0; j < titles.length; j++) {
			TableColumn column = new TableColumn(this, SWT.NONE);
			if (j > 1) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}

			column.setText(titles[j]);
			column.setResizable(false);
			column.setMoveable(false);
			// if (titles[j].equals(langProperties.getProperty("table.skill"))) {
			// column.setWidth(50);
			// } else if (titles[j].equals(langProperties.getProperty("table.week")))
			// {
			// column.setWidth(50);
			// } else
			if (titles[j].equals("")) { //$NON-NLS-1$

				// potrzebne do dopelnienia tabel w Linuxie
				if (SettingsHandler.OS_TYPE == IPlugin.LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}
		}
		
		this.getColumn(JuniorsComparator.BEGIN_LEVEL).setToolTipText(Messages.getString("junior.skill.begin")); //$NON-NLS-1$
		this.getColumn(JuniorsComparator.ESTIMATED_LEVEL).setToolTipText(Messages.getString("junior.table.estimated.level")); //$NON-NLS-1$

		this.addLabelsListener();
	}
	
	public void fill(List<Junior> juniors) {
		int maxSkill = 0;
		// Turn off drawing to avoid flicker
		this.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		this.removeAll();
		Collections.sort(juniors, comparator);
		for (Junior junior : juniors) {
			maxSkill = junior.getSkills().length - 1;
			TableItem item = new TableItem(this, SWT.NONE);
			int c = 0;
			item.setData(Junior.IDENTIFIER, junior);
			// item.setData("id", junior.getId());
			item.setImage(FlagsResources.getFlag(Cache.getClub().getCountry()));
			item.setText(c++, junior.getName());
			item.setText(c++, junior.getSurname());
			item.setText(c++, String.valueOf(junior.getSkills()[maxSkill].getSkill()));
			item.setText(c++, String.valueOf(junior.getSkills()[maxSkill].getWeeks()));
			item.setText(c++, String.valueOf(junior.getWeeksWithoutJump()));
			if(junior.getAveragePops() == 0) {
				item.setText(c++, "-"); //$NON-NLS-1$
			} else {
				if(junior.getPops() > 1) {
					item.setText(c++, SVNumberFormat.formatDouble(junior.getAveragePops()));	
				} else {
					item.setForeground(c, ColorResources.getDarkGray());
					item.setText(c++, "~" + SVNumberFormat.formatDouble(junior.getAveragePops())); //$NON-NLS-1$
				}
				
			}
			
			item.setText(c++, String.valueOf(junior.getSkills()[0].getSkill()));
			
			if(junior.getPops() > 1 || junior.getEstimatedSkill() == junior.getSkills()[maxSkill].getSkill() ) {
				item.setText(c++, String.valueOf(junior.getEstimatedSkill()));
			} else {
				item.setForeground(c, ColorResources.getDarkGray());
				item.setText(c++, "~" + junior.getEstimatedSkill()); //$NON-NLS-1$
				
			}
			item.setText(c++, String.valueOf(junior.getPops()));
			item.setText(c++, SVNumberFormat.formatIntegerWithSignZero(junior.getEstimatedAge()));
			item.setText(c++, junior.getEndDate().toDateString());
			item.setText(c++, junior.getMoneySpent().formatIntegerCurrencySymbol());
			item.setText(c++, junior.getRestMoneyToSpend().formatIntegerCurrencySymbol());
//			item.setText(c++, Money.formatIntegerCurrency((junior.getSkills()[0].getWeeks() + 1) * juniorCost.toInt()));
			item.setText(c++, junior.getAllMoneyToSpend().formatIntegerCurrencySymbol());
			
			
			if (junior.getNote() != null) {
				if (junior.getNote().equals("")) { //$NON-NLS-1$
					c++;
				} else {
					item.setImage(c++, ImageResources.getImageResources("note.png")); //$NON-NLS-1$
				}
			}

			if (maxSkill > 0) {
				this.getChanges(junior.getSkills()[maxSkill].getSkill(), junior.getSkills()[maxSkill - 1].getSkill(), item, 2);
			} else {
				item.setBackground(ConfigBean.getColorNewTableObject());
			}
		}

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
//			table.getColumn(i).setWidth(table.getColumn(i).getWidth() + 15);
		}
		// Turn drawing back on
		this.setRedraw(true);
	}
	
	@Override
	public JuniorsComparator getComparator() {
		return this.comparator;
	}

	@Override
	public void setLabel(Label label, int column, TableItem item) {
		if (column == JuniorsComparator.NOTE) {
			Junior junior = (Junior) item.getData(Junior.IDENTIFIER); 
			if (junior.getNote() != null && !junior.getNote().equals("")) { //$NON-NLS-1$
				label.setText(junior.getNote());
				int minSizeX = 200;
				int minSizeY = 80;
				int maxSizeX = 400;
				int maxSizeY = 200;

				Point size = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				if (size.x < minSizeX) {
					size.x = minSizeX;
				}
				if (size.y < minSizeY) {
					size.y = minSizeY;
				}
				if (size.x > maxSizeX) {
					size.x = maxSizeX;
				}
				if (size.y > maxSizeY) {
					size.y = maxSizeY;
				}

				label.setSize(size);
			}
		}
		super.setLabel(label, column, item);
	}
}
