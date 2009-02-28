package pl.pronux.sokker.ui.widgets.groups;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ScrollBar;

import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.widgets.tables.MatchInfoTable;

public class MatchInfoGroup extends Group {

	private MatchInfoTable matchInfoTable;

	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}
	public MatchInfoGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		this.setLayout(new FormLayout());
		FormData formData;
		this.setFont(ConfigBean.getFontMain());
		
		this.setText(Messages.getString("match.information")); //$NON-NLS-1$
		this.setForeground(Colors.getBlueDescription());

		matchInfoTable = new MatchInfoTable(this, SWT.FULL_SELECTION);
		
		ScrollBar bar = matchInfoTable.getVerticalBar();
		
		formData = new FormData();
		formData.left = new FormAttachment(0,bar.getSize().x);
		formData.top = new FormAttachment(0,5);
		formData.bottom = new FormAttachment(100,-5);
		formData.right = new FormAttachment(100,-5);
		matchInfoTable.setLayoutData(formData);
		
	}
	
	public void fill(Match match) {
		matchInfoTable.fill(match);
	}
}
