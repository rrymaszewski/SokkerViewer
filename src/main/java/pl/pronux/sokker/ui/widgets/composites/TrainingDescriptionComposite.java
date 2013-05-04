package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.ui.widgets.styledtexts.TrainingDescription;
import pl.pronux.sokker.ui.widgets.styledtexts.TrainingDescriptionAdds;

public class TrainingDescriptionComposite extends Composite {

	private TrainingDescription trainingDescription;
	private TrainingDescriptionAdds trainingDescriptionAdds;

	public TrainingDescriptionComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(0,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(60,0);
		formData.bottom = new FormAttachment(100,0);
		
		trainingDescription = new TrainingDescription(this, SWT.NONE);
		trainingDescription.setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(trainingDescription,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(100,0);
		formData.bottom = new FormAttachment(100,0);
		trainingDescriptionAdds = new TrainingDescriptionAdds(this, SWT.NONE);
		trainingDescriptionAdds.setLayoutData(formData);
	}
	
	public void setInfo(Training training) {
		trainingDescription.setInfo(training);
		trainingDescriptionAdds.setInfo(training);
	}
	
	public String getText() {
		return trainingDescription.getText();
	}
}
