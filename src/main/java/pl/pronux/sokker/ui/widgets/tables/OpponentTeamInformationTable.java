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
//		this.setFont(ConfigBean.getFontTable());
		this.setFont(ConfigBean.getFontMain());

		String[] column = {
				"1", //$NON-NLS-1$
				"2", //$NON-NLS-1$
				"3", //$NON-NLS-1$
				"4", //$NON-NLS-1$
		};

		for (int i = 0; i < column.length; i++) {
			tableColumn = new TableColumn(this, SWT.LEFT);
			tableColumn.setText(column[i]);
		}

		String[] firstColumn = {
				Messages.getString("club.owner"), //$NON-NLS-1$
				Messages.getString("club.id"), //$NON-NLS-1$
				Messages.getString("club.name"), //$NON-NLS-1$
				Messages.getString("club.date.created"), //$NON-NLS-1$
		};
		
		String[] thirdColumn = {
				Messages.getString("club.rank"), //$NON-NLS-1$
				Messages.getString("club.country"), //$NON-NLS-1$
				Messages.getString("club.arenaname"), //$NON-NLS-1$
				""
		};

		for (int i = 0; i < firstColumn.length; i++) {
			item = new TableItem(this, SWT.NONE);
			item.setText(0, firstColumn[i]);
			item.setText(2, thirdColumn[i]);
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
		if(club == null) {
			return;
		}
		int secondColumn = 1;
		int fourthColumn = 3;
		
		TableItem item;
		
		int c = 0;

		item = this.getItem(c);
		if(club.getUser() != null && club.getUser().getLogin() != null) {
			item.setText(secondColumn, club.getUser().getLogin());	
		}
		
		item = this.getItem(c++);
		item.setText(fourthColumn, String.valueOf(club.getRank().get(0).getRank()));

		item = this.getItem(c);
		item.setText(secondColumn, String.valueOf(club.getId()));

		item = this.getItem(c++);
		item.setText(fourthColumn, Messages.getString("country." + club.getCountry() + ".name")); //$NON-NLS-1$ //$NON-NLS-2$
		
		item = this.getItem(c);
		item.setText(secondColumn, club.getClubName().get(club.getClubName().size() - 1).getName());

		item = this.getItem(c++);
		item.setText(fourthColumn, club.getArena().getAlArenaName().get(club.getArena().getAlArenaName().size() - 1).getArenaName());
		
		item = this.getItem(c);
		if(club.getDateCreated() != null) {
			item.setText(secondColumn, club.getDateCreated().toDateTimeString());
		} else {
			item.setText(secondColumn, "-"); //$NON-NLS-1$
		}







		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}
	}

}
