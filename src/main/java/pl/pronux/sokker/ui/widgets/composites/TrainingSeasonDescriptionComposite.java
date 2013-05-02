package pl.pronux.sokker.ui.widgets.composites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.ui.widgets.styledtexts.TrainingSeasonField;

public class TrainingSeasonDescriptionComposite extends Composite {

	private TrainingSeasonField trainingSeasonDescription;
	private List<TrainingSeasonField> trainingsSeasons;

	public TrainingSeasonDescriptionComposite(Composite parent, int style) {
		super(parent, style);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		this.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;

		trainingsSeasons = new ArrayList<TrainingSeasonField>();
		for (int i = 0; i < 16; i++) {
			trainingSeasonDescription = new TrainingSeasonField(this, SWT.NONE);
			trainingSeasonDescription.setLayoutData(gridData);
			trainingsSeasons.add(trainingSeasonDescription);
		}
	}

	public void setInfo(List<Training> trainings) {
		Map<Integer, Training> trainingsMap = new HashMap<Integer, Training>();
		int season = 0;
		for (Training training : trainings) {
			Date date = training.getDate().getTrainingDate(SokkerDate.THURSDAY);
			if (season == 0) {
				season = date.getSeason().getSeasonNumber();
			}
			
			if (season == date.getSeason().getSeasonNumber()) {
				trainingsMap.put(date.getSeason().getSeasonWeek(), training);
			} 
		}

		for (int i = 0; i < 8; i++) {
			trainingsSeasons.get(i*2).setInfo(trainingsMap.get(i), i);
			trainingsSeasons.get(i*2+1).setInfo(trainingsMap.get(i+8), i+8);
		}
	}

	public String getText() {
		return trainingSeasonDescription.getText();
	}
}
