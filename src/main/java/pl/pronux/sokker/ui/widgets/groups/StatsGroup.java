package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ScrollBar;

import pl.pronux.sokker.model.LeagueRound;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.widgets.tables.LeagueTable;

public class StatsGroup extends Group {
	private LeagueTable leagueTable;
	
	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}
	public StatsGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		this.setLayout(new FormLayout());
		
		this.setText(Messages.getString("table")); 
		this.setForeground(Colors.getBlueDescription());
		
		leagueTable = new LeagueTable(this, SWT.FULL_SELECTION);
		
		ScrollBar bar = leagueTable.getVerticalBar();
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(0,bar.getSize().x);
		formData.top = new FormAttachment(0,5);
		formData.bottom = new FormAttachment(100,-5);
		formData.right = new FormAttachment(100,0);
		
		leagueTable.setLayoutData(formData);
		
	}
	
	public void fill(LeagueRound leagueRound) {
		this.setText(String.format("%s %s %d", Messages.getString("table"),Messages.getString("round"),leagueRound.getRoundNumber()));   
//		this.headerLabel.update();
		leagueTable.fill(leagueRound);
	}

}
