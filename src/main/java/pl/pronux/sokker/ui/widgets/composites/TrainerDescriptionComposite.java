package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.ui.widgets.styledtexts.TrainerDescription;
import pl.pronux.sokker.ui.widgets.styledtexts.TrainerDescriptionAdds;

public class TrainerDescriptionComposite extends Composite {

	private TrainerDescription trainerDescription;
	private TrainerDescriptionAdds trainerDescriptionAdds;

	public TrainerDescriptionComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());
		
		FormData formData;
		
		formData = new FormData();
		formData.left = new FormAttachment(0,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(60,0);
		formData.bottom = new FormAttachment(100,0);
		
		trainerDescription = new TrainerDescription(this, SWT.NONE);
		trainerDescription.setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(trainerDescription,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(100,0);
		formData.bottom = new FormAttachment(100,0);
		trainerDescriptionAdds = new TrainerDescriptionAdds(this, SWT.NONE);
		trainerDescriptionAdds.setLayoutData(formData);
	}
	
	public void setInfo(Coach coach) {
		this.trainerDescription.setInfo(coach);
		this.trainerDescriptionAdds.setInfo(coach);
	}

	public String getText() {
		return trainerDescription.getText();
	}
}
