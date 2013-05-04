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
import pl.pronux.sokker.ui.widgets.tables.MatchSummaryTable;

public class MatchSummaryGroup extends Group {

	private MatchSummaryTable summaryTable;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public MatchSummaryGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		this.setLayout(new FormLayout());

		this.setText(Messages.getString("summary")); 
		this.setForeground(Colors.getBlueDescription());
		this.setFont(ConfigBean.getFontMain());
		
		summaryTable = new MatchSummaryTable(this, SWT.NONE);
		ScrollBar bar = summaryTable.getVerticalBar();

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, bar.getSize().x);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(100, 0);

		summaryTable.setLayoutData(formData);
	}

	public void fill(Match match) {
		summaryTable.fill(match);
	}

}
