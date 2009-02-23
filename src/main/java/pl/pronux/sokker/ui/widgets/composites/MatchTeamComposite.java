package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.TeamStats;
import pl.pronux.sokker.ui.widgets.tables.MatchPlayersDetailsTable;

public class MatchTeamComposite extends TabCompositeImpl<TeamStats> {

	private MatchPlayersDetailsTable matchPlayersTable;

	public MatchTeamComposite(Composite parent, int style) {
		super(parent, style);
		
		this.setLayout(new FormLayout());
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(0 ,10);
		formData.top = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -10);
		formData.bottom = new FormAttachment(100, -5);
		
		matchPlayersTable = new MatchPlayersDetailsTable(this, SWT.FULL_SELECTION);
		matchPlayersTable.setLayoutData(formData);
	}

	public void fill(TeamStats teamStats) {
		matchPlayersTable.fill(teamStats.getPlayersStats());
	}
	

}
