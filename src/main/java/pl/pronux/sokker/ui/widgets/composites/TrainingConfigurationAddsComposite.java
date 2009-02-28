package pl.pronux.sokker.ui.widgets.composites;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.model.TrainingConfiguration;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.widgets.groups.TrainingConfigurationGroup;
import pl.pronux.sokker.ui.widgets.tables.TrainingConfigurationTable;

public class TrainingConfigurationAddsComposite extends Composite {

	private HashMap<Integer, TrainingConfigurationGroup> trainings;
	private Button activeButton;

	public TrainingConfigurationAddsComposite(Composite parent, int style) {
		super(parent, style);
		this.setFont(ConfigBean.getFontMain());
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		this.setLayout(layout);
		trainings = new HashMap<Integer, TrainingConfigurationGroup>();
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.heightHint = 125;

		GridData layoutDataSpan = new GridData();
		layoutDataSpan.grabExcessHorizontalSpace = true;
		layoutDataSpan.horizontalAlignment = GridData.FILL;
		layoutDataSpan.horizontalSpan = 2;

		activeButton = new Button(this, SWT.CHECK);
		activeButton.setText(Messages.getString("training.configuration.active"));
		activeButton.setLayoutData(layoutDataSpan);
		activeButton.setFont(this.getFont());

		activeButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.widget instanceof Button) {
					Button button = (Button) event.widget;
					if (button.getSelection()) {
						for (TrainingConfigurationGroup trainingGroup : trainings.values()) {
							trainingGroup.setEnabled(true);
						}
					} else {
						for (TrainingConfigurationGroup trainingGroup : trainings.values()) {
							trainingGroup.setEnabled(false);
						}
					}
				}
			}
		});

		TrainingConfigurationGroup trainingGroup = new TrainingConfigurationGroup(this, SWT.NONE);
		trainingGroup.setLayoutData(layoutData);
		trainingGroup.setText(Messages.getString("training.configuration.technique"));
		trainings.put(Training.TYPE_TECHNIQUE, trainingGroup);

		trainingGroup = new TrainingConfigurationGroup(this, SWT.NONE);
		trainingGroup.setLayoutData(layoutData);
		trainingGroup.setText(Messages.getString("training.configuration.defenders"));
		trainings.put(Training.TYPE_DEFENDING, trainingGroup);

		trainingGroup = new TrainingConfigurationGroup(this, SWT.NONE);
		trainingGroup.setLayoutData(layoutData);
		trainingGroup.setText(Messages.getString("training.configuration.passing"));
		trainings.put(Training.TYPE_PASSING, trainingGroup);

		trainingGroup = new TrainingConfigurationGroup(this, SWT.NONE);
		trainingGroup.setLayoutData(layoutData);
		trainingGroup.setText(Messages.getString("training.configuration.playmakers"));
		trainings.put(Training.TYPE_PLAYMAKING, trainingGroup);

		trainingGroup = new TrainingConfigurationGroup(this, SWT.NONE);
		trainingGroup.setLayoutData(layoutData);
		trainingGroup.setText(Messages.getString("training.configuration.strikers"));
		trainings.put(Training.TYPE_STRIKER, trainingGroup);

		trainingGroup = new TrainingConfigurationGroup(this, SWT.NONE);
		trainingGroup.setLayoutData(layoutData);
		trainingGroup.setText(Messages.getString("training.configuration.keepers"));
		trainings.put(Training.TYPE_KEEPER, trainingGroup);

		trainingGroup = new TrainingConfigurationGroup(this, SWT.NONE);
		trainingGroup.setLayoutData(layoutData);
		trainingGroup.setText(Messages.getString("training.configuration.pace"));
		trainings.put(Training.TYPE_PACE, trainingGroup);
		trainingGroup.setType(TrainingConfigurationTable.TYPE_ALL);

		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(layoutDataSpan);
		label.setText(String.format("* %s", Messages.getString("training.configuration.efficiency")));
		label.setForeground(ColorResources.getColor(0, 128, 128));
		label.setFont(this.getFont());

		label = new Label(this, SWT.NONE);
		label.setLayoutData(layoutDataSpan);
		label.setText(String.format("* %s", Messages.getString("training.configuration.formation")));
		label.setForeground(ColorResources.getColor(8, 61, 132));
		label.setFont(this.getFont());

		if (activeButton.getSelection()) {
			for (TrainingConfigurationGroup training : trainings.values()) {
				training.setEnabled(true);
			}
		} else {
			for (TrainingConfigurationGroup training : trainings.values()) {
				training.setEnabled(false);
			}
		}
	}

	public void set(TrainingConfiguration trainingConfiguration) {
		Map<Integer, Integer[][]> map = trainingConfiguration.getSettings();
		activeButton.setSelection(trainingConfiguration.getEnabled());
		if (activeButton.getSelection()) {
			for (TrainingConfigurationGroup training : trainings.values()) {
				training.setEnabled(true);
			}
		} else {
			for (TrainingConfigurationGroup training : trainings.values()) {
				training.setEnabled(false);
			}
		}
		for (Integer key : trainings.keySet()) {
			trainings.get(key).fill(map.get(key));
		}
	}
	
	public boolean getEnabledButton() {
		return activeButton.getSelection();
	}
	
	public Map<Integer, Integer[][]> getSettings() {
		Map<Integer, Integer[][]> map = new HashMap<Integer, Integer[][]>();
		for (Integer key : trainings.keySet()) {
			map.put(key, trainings.get(key).getValues());
		}
		return map;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		activeButton.setEnabled(enabled);
		super.setEnabled(enabled);
	}
}
