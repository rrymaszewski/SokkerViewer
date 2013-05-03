package pl.pronux.sokker.ui.widgets.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class OpponentTeamInformationTable extends SVTable<Club> {

	public OpponentTeamInformationTable(Composite parent, int style) {
		super(parent, style);

		TableItem item;
		TableColumn tableColumn;
		this.setLinesVisible(false);
		this.setHeaderVisible(false);
		this.setBackground(parent.getBackground());
		// this.setFont(ConfigBean.getFontTable());
		this.setFont(ConfigBean.getFontMain());

		String[] column = {
				"1", 
				"2", 
		};

		for (int i = 0; i < column.length; i++) {
			tableColumn = new TableColumn(this, SWT.LEFT);
			tableColumn.setText(column[i]);
		}

		String[] firstColumn = {
				Messages.getString("club.owner"), 
				Messages.getString("club.name"), 
				Messages.getString("club.date.created"), 
				Messages.getString("club.rank"), 
		};

		for (int i = 0; i < firstColumn.length; i++) {
			item = new TableItem(this, SWT.NONE);
			item.setText(0, firstColumn[i]);
		}

		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}

		for (int i = 0; i < this.getItemCount(); i++) {
			if ((i % 2) == 0) {
				this.getItem(i).setBackground(ColorResources.getGray());
			}
		}

	}

	public void fill(Club club) {
		if (club == null) {
			return;
		}
		int secondColumn = 1;
		TableItem item;
		int c = 0;
		String text;

		item = this.getItem(c++);
		if (club.getUser() != null && club.getUser().getLogin() != null) {
			text = String.format("%s (%s)", club.getUser().getLogin(), Messages.getString("country." + club.getCountry() + ".name"));   
			item.setText(secondColumn, text);
		} else {
			text = String.format("- (%s)", Messages.getString("country." + club.getCountry() + ".name"));   
			item.setText(secondColumn, text);
		}

		item = this.getItem(c++);
		text = String.format("%s [%d]", club.getClubName().get(club.getClubName().size() - 1).getName(), club.getId()); 
		item.setText(secondColumn, text);

		// item = this.getItem(c++);
		// item.setText(fourthColumn,
		// club.getArena().getAlArenaName().get(club.getArena().getAlArenaName().size()
		// - 1).getArenaName());

		item = this.getItem(c++);
		if (club.getDateCreated() != null) {
			item.setText(secondColumn, club.getDateCreated().toDateTimeString());
		} else {
			item.setText(secondColumn, "-"); 
		}

		item = this.getItem(c++);
		item.setText(secondColumn, String.valueOf(club.getRank().get(0).getRank()));

		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}
	}
}
