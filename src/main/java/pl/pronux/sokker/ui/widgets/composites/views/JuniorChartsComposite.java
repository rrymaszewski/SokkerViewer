package pl.pronux.sokker.ui.widgets.composites.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorSkills;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.composites.ChartValueComposite;

public class JuniorChartsComposite extends Composite {

	private ChartValueComposite skillGraph;

	private Spinner spinner;

	private Junior junior;

	public JuniorChartsComposite(Composite parent, int style) {
		super(parent, style);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		this.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(gridData);
		composite.setLayout(new RowLayout());

		spinner = new Spinner(composite, SWT.READ_ONLY | SWT.BORDER);
		spinner.setValues(1, 1, 1, 0, 1, 1);

		Button button = new Button(composite, SWT.PUSH);
		button.setText(Messages.getString("button.show")); 
		button.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				fillTable(junior);
			}

		});

		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
//		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 200;
		gridData.grabExcessHorizontalSpace = true;
//		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 2;

		skillGraph = new ChartValueComposite(this, SWT.BORDER);
		skillGraph.setLayoutData(gridData);
	}

	public void fill(Junior junior) {
		JuniorSkills[] skills = junior.getSkills();
		if(skills.length < 2) {
			spinner.setValues(skills.length, 0, skills.length, 0, 1, 10);
		} else {
			spinner.setValues(skills.length, 1, skills.length, 0, 1, 10);	
		}
		

		fillTable(junior);
	}

	private void fillTable(Junior junior) {
		JuniorSkills[] skills = junior.getSkills();

		this.junior = junior;
		List<Date> date = new ArrayList<Date>();
		List<Number> skill = new ArrayList<Number>();
		for (int i = spinner.getMaximum() - spinner.getSelection(); i < spinner.getMaximum(); i++) {
			date.add(skills[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			skill.add(skills[i].getSkill());
		}
		skillGraph.fillGraph(skill, date, SokkerDate.THURSDAY, true, 18, false, Messages.getString("junior.skill")); 
	}

}
