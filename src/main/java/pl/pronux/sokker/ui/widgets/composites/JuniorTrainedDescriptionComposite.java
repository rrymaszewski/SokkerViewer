package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.ui.widgets.styledtexts.JuniorTrainedAdditionsDescriptionStyledText;
import pl.pronux.sokker.ui.widgets.styledtexts.JuniorTrainedDescriptionStyledText;

public class JuniorTrainedDescriptionComposite extends Composite {

	private JuniorTrainedDescriptionStyledText juniorDescription;
	private JuniorTrainedAdditionsDescriptionStyledText juniorAdditionsDescription;

	public JuniorTrainedDescriptionComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());
		this.setVisible(false);
		FormData formData;
		
		formData = new FormData();
		formData.left = new FormAttachment(0,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(60,0);
		formData.bottom = new FormAttachment(100,0);
		
		juniorDescription = new JuniorTrainedDescriptionStyledText(this, SWT.NONE);
		juniorDescription.setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(juniorDescription,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(100,0);
		formData.bottom = new FormAttachment(100,0);
		juniorAdditionsDescription = new JuniorTrainedAdditionsDescriptionStyledText(this, SWT.NONE);
		juniorAdditionsDescription.setLayoutData(formData);
	}
	
	public void setStatsJuniorInfo(Junior junior) {
		this.juniorDescription.setStatsJuniorInfo(junior);
		this.juniorAdditionsDescription.setStatsJuniorInfo(junior);
	}

	public void setStatsJuniorInfo(Junior player, int index) {
		this.juniorDescription.setStatsJuniorInfo(player, index);
		this.juniorAdditionsDescription.setStatsJuniorInfo(player, index);
	}
	
	public String getText() {
		return juniorDescription.getText();
	}
}
