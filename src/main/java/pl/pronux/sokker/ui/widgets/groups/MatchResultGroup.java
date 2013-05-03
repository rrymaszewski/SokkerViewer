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
import pl.pronux.sokker.ui.widgets.tables.MatchResultTable;

public class MatchResultGroup extends Group {

	private MatchResultTable resultsTable;
	
	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}
	public MatchResultGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		this.setLayout(new FormLayout());

		FormData formData;
		
		this.setText(Messages.getString("match")); 
		this.setForeground(Colors.getBlueDescription());
		this.setFont(ConfigBean.getFontMain());
		
		resultsTable = new MatchResultTable(this, SWT.NONE);
		
		ScrollBar bar = resultsTable.getVerticalBar();
		
		formData = new FormData();
		formData.left = new FormAttachment(0,bar.getSize().x);
		formData.top = new FormAttachment(0,5);
		formData.bottom = new FormAttachment(100,-5);
		formData.right = new FormAttachment(100,0);
		
		resultsTable.setLayoutData(formData);
		
	}
	
	public void fill(Match match) {
		this.setText(String.format("%s: %d", Messages.getString("match"), match.getMatchId()));  
//		this.headerLabel.update();
		resultsTable.fill(match);
	}



}
