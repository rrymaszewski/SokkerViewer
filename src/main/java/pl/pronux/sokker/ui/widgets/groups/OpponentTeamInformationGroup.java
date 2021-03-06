package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ScrollBar;

import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.widgets.tables.OpponentTeamInformationTable;

public class OpponentTeamInformationGroup extends Group {

	private OpponentTeamInformationTable clubInfoTable;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public OpponentTeamInformationGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		this.setLayout(new FormLayout());
		
		this.setFont(ConfigBean.getFontMain());
		this.setText(Messages.getString("team.info")); 
		this.setForeground(Colors.getBlueDescription());

		clubInfoTable = new OpponentTeamInformationTable(this, SWT.FULL_SELECTION);

		ScrollBar bar = clubInfoTable.getVerticalBar();

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, bar.getSize().x);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(100, -5);
		clubInfoTable.setLayoutData(formData);
	}

	public void fill(Club club) {
		this.setText(club.getClubName().get(0).getName());
		clubInfoTable.fill(club);
	}

}
