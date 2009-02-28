package pl.pronux.sokker.ui.widgets.composites;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import pl.pronux.sokker.model.TrainingConfiguration;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class TrainingConfigurationComposite extends Composite {

	private Map<String, Spinner> spinners = new HashMap<String , Spinner>();

	public TrainingConfigurationComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		this.setLayout(layout);
		this.setFont(ConfigBean.getFontMain());

		ModifyListener listener = new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				int friendly = spinners.get(TrainingConfiguration.FRIENDLY).getSelection();
				int official = spinners.get(TrainingConfiguration.OFFICIAL).getSelection();
				int official_friendly = spinners.get(TrainingConfiguration.OFFICIAL_FRIENDLY).getSelection();
				int official_cup = spinners.get(TrainingConfiguration.OFFICIAL_CUP).getSelection();
				int national = spinners.get(TrainingConfiguration.NATIONAL).getSelection();
				
				if(friendly > official) {
					spinners.get(TrainingConfiguration.OFFICIAL).setSelection(friendly);
				}
				
				if(official > official_friendly) {
					spinners.get(TrainingConfiguration.OFFICIAL_FRIENDLY).setSelection(official);
				}

				if(official > official_cup) {
					spinners.get(TrainingConfiguration.OFFICIAL_CUP).setSelection(official);
				}
				
				if(friendly > official_friendly) {
					spinners.get(TrainingConfiguration.OFFICIAL_FRIENDLY).setSelection(official);
				}
				
				if(national > official) {
					spinners.get(TrainingConfiguration.OFFICIAL).setSelection(national);
				}
				
				if(national > friendly) {
					spinners.get(TrainingConfiguration.FRIENDLY).setSelection(national);
				}
				
			}
		};
		
		Label label;
		Spinner spinner;

		GridData labeldata = new GridData();
//		labeldata.grabExcessHorizontalSpace = true;
		labeldata.widthHint = 350;
		labeldata.horizontalAlignment = GridData.FILL;

		GridData spinnerdata = new GridData();
		spinnerdata.widthHint = 30;

		label = new Label(this, SWT.NONE);
		label.setLayoutData(labeldata);
		label.setText(Messages.getString("training.configuration.official"));
		label.setFont(this.getFont());

		spinner = new Spinner(this, SWT.READ_ONLY);
		spinner.setLayoutData(spinnerdata);
		spinner.setMinimum(0);
		spinner.setMaximum(999);
		spinner.setDigits(2);
		spinner.setIncrement(1);
		spinners.put(TrainingConfiguration.OFFICIAL, spinner);
		spinner.addModifyListener(listener);
		spinner.setFont(this.getFont());

		label = new Label(this, SWT.NONE);
		label.setLayoutData(labeldata);
		label.setText(Messages.getString("training.configuration.friendly"));
		label.setFont(this.getFont());
		
		spinner = new Spinner(this, SWT.READ_ONLY);
		spinner.setLayoutData(spinnerdata);
		spinner.setMinimum(0);
		spinner.setMaximum(999);
		spinner.setDigits(2);
		spinner.setIncrement(1);
		spinners.put(TrainingConfiguration.FRIENDLY, spinner);
		spinner.addModifyListener(listener);
		spinner.setFont(this.getFont());
		
		label = new Label(this, SWT.NONE);
		label.setLayoutData(labeldata);
		label.setText(Messages.getString("training.configuration.official_friendly"));
		label.setFont(this.getFont());
		
		spinner = new Spinner(this, SWT.READ_ONLY);
		spinner.setLayoutData(spinnerdata);
		spinner.setMinimum(0);
		spinner.setMaximum(999);
		spinner.setDigits(2);
		spinner.setIncrement(1);
		spinners.put(TrainingConfiguration.OFFICIAL_FRIENDLY, spinner);
		spinner.addModifyListener(listener);
		spinner.setFont(this.getFont());
		
		label = new Label(this, SWT.NONE);
		label.setLayoutData(labeldata);
		label.setText(Messages.getString("training.configuration.official_cup"));
		label.setFont(this.getFont());
		
		spinner = new Spinner(this, SWT.READ_ONLY);
		spinner.setLayoutData(spinnerdata);
		spinner.setMinimum(0);
		spinner.setMaximum(999);
		spinner.setDigits(2);
		spinner.setIncrement(1);
		spinners.put(TrainingConfiguration.OFFICIAL_CUP, spinner);
		spinner.addModifyListener(listener);
		spinner.setFont(this.getFont());
		
		label = new Label(this, SWT.NONE);
		label.setLayoutData(labeldata);
		label.setText(Messages.getString("training.configuration.national"));
		label.setFont(this.getFont());
		
		spinner = new Spinner(this, SWT.READ_ONLY);
		spinner.setLayoutData(spinnerdata);
		spinner.setMinimum(0);
		spinner.setMaximum(999);
		spinner.setDigits(2);
		spinner.setIncrement(1);
		spinners.put(TrainingConfiguration.NATIONAL, spinner);
		spinner.addModifyListener(listener);
		spinner.setFont(this.getFont());
	}

	public void set(TrainingConfiguration trainingConfiguration) {
		Map<String, String> map = trainingConfiguration.getGeneralSettings();
		spinners.get(TrainingConfiguration.NATIONAL).setSelection(Integer.valueOf(map.get(TrainingConfiguration.NATIONAL))); //20
		spinners.get(TrainingConfiguration.FRIENDLY).setSelection(Integer.valueOf(map.get(TrainingConfiguration.FRIENDLY))); //80
		spinners.get(TrainingConfiguration.OFFICIAL).setSelection(Integer.valueOf(map.get(TrainingConfiguration.OFFICIAL))); //100
		spinners.get(TrainingConfiguration.OFFICIAL_FRIENDLY).setSelection(Integer.valueOf(map.get(TrainingConfiguration.OFFICIAL_FRIENDLY))); // 115
		spinners.get(TrainingConfiguration.OFFICIAL_CUP).setSelection(Integer.valueOf(map.get(TrainingConfiguration.OFFICIAL_CUP))); //120
	}
	
	public Map<String, String> getGeneralSettings() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(TrainingConfiguration.NATIONAL, String.valueOf(spinners.get(TrainingConfiguration.NATIONAL).getSelection()));
		map.put(TrainingConfiguration.FRIENDLY, String.valueOf(spinners.get(TrainingConfiguration.FRIENDLY).getSelection()));
		map.put(TrainingConfiguration.OFFICIAL, String.valueOf(spinners.get(TrainingConfiguration.OFFICIAL).getSelection()));
		map.put(TrainingConfiguration.OFFICIAL_FRIENDLY, String.valueOf(spinners.get(TrainingConfiguration.OFFICIAL_FRIENDLY).getSelection()));
		map.put(TrainingConfiguration.OFFICIAL_CUP, String.valueOf(spinners.get(TrainingConfiguration.OFFICIAL_CUP).getSelection()));
		return map;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		spinners.get(TrainingConfiguration.NATIONAL).setEnabled(enabled);
		spinners.get(TrainingConfiguration.FRIENDLY).setEnabled(enabled);
		spinners.get(TrainingConfiguration.OFFICIAL).setEnabled(enabled);
		spinners.get(TrainingConfiguration.OFFICIAL_FRIENDLY).setEnabled(enabled);
		spinners.get(TrainingConfiguration.OFFICIAL_CUP).setEnabled(enabled);
		super.setEnabled(enabled);
	}
}
