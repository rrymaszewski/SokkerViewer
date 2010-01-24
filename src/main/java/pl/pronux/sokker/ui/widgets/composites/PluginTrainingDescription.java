package pl.pronux.sokker.ui.widgets.composites;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Training;

public class PluginTrainingDescription extends Composite {

	private TrainerDescriptionComposite trainerDescription;
	private TrainingDescriptionComposite trainingDescription;
	private Composite currentComposite;
	private TrainingSeasonDescriptionComposite trainingSeasonDescription;
	
	public PluginTrainingDescription(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());
		FormData formData;
		
		formData = new FormData();
		formData.left = new FormAttachment(0,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(100,0);
		formData.bottom = new FormAttachment(100,0);
		
		trainerDescription = new TrainerDescriptionComposite(this, SWT.NONE);
		trainerDescription.setLayoutData(formData);
		trainerDescription.setVisible(false);
		
		trainingDescription = new TrainingDescriptionComposite(this, SWT.NONE);
		trainingDescription.setLayoutData(formData);
		trainingDescription.setVisible(false);
	
		trainingSeasonDescription = new TrainingSeasonDescriptionComposite(this, SWT.NONE);
		trainingSeasonDescription.setLayoutData(formData);
		trainingSeasonDescription.setVisible(false);
	}
	
	public void setInfo(List<Training> trainings) {
		trainingSeasonDescription.setInfo(trainings);
		show(trainingSeasonDescription);
	}
	
	public void setInfo(Coach coach) {
		trainerDescription.setInfo(coach);
		show(trainerDescription);
	}
	
	public void setInfo(Training training) {
		trainingDescription.setInfo(training);
		show(trainingDescription);
	}
	
	private void show(Composite composite) {
		if(currentComposite == null) {
			currentComposite = composite;
		}
		currentComposite.setVisible(false);
		currentComposite = composite;
		currentComposite.setVisible(true);
	}

	public void clear() {
		if(currentComposite != null) {
			currentComposite.setVisible(false);	
		}
	}
}
