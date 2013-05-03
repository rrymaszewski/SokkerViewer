package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ScrollBar;

import pl.pronux.sokker.model.TeamStats;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.widgets.tables.MatchPlayersTable;

public class MatchPlayersGroup extends Group {

	private MatchPlayersTable matchPlayersTable;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public MatchPlayersGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		this.setLayout(new FormLayout());
		FormData formData;

		this.setText(Messages.getString("team")); 
		this.setForeground(Colors.getBlueDescription());
		this.setFont(ConfigBean.getFontMain());
		
		matchPlayersTable = new MatchPlayersTable(this, SWT.FULL_SELECTION);

		ScrollBar bar = matchPlayersTable.getVerticalBar();

		formData = new FormData();
		formData.left = new FormAttachment(0, bar.getSize().x);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(100, -5);
		matchPlayersTable.setLayoutData(formData);
	}

	public void fill(TeamStats teamStats, String teamName) {
		if (teamStats == null) {
			this.setText(""); 
			// this.headerLabel.update();
			matchPlayersTable.fill(null);
		} else {
			if (teamName == null) {
				this.setText(String.valueOf(teamStats.getTeamId()));
				// this.headerLabel.update();
			} else {
				this.setText(teamName);
				// this.headerLabel.update();
			}
			matchPlayersTable.fill(teamStats.getPlayersStats());
		}

	}

}
