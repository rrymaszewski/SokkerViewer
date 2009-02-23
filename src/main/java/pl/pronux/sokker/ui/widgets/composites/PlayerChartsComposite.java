package pl.pronux.sokker.ui.widgets.composites;

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
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.resources.Messages;

public class PlayerChartsComposite extends Composite {

	private ChartValueComposite staminaGraph;

	private ChartValueComposite paceGraph;

	private ChartValueComposite techniqueGraph;

	private ChartValueComposite passingGraph;

	private ChartValueComposite keeperGraph;

	private ChartValueComposite defenderGraph;

	private ChartValueComposite playmakerGraph;

	private ChartValueComposite scorerGraph;

	private Spinner spinner;

	private Player player;

	public PlayerChartsComposite(Composite parent, int style) {
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
		button.setText(Messages.getString("button.show")); //$NON-NLS-1$
		button.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				fillTable(player);
			}

		});

		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		staminaGraph = new ChartValueComposite(this, SWT.BORDER);
		staminaGraph.setLayoutData(gridData);

		paceGraph = new ChartValueComposite(this, SWT.BORDER);
		paceGraph.setLayoutData(gridData);

		techniqueGraph = new ChartValueComposite(this, SWT.BORDER);
		techniqueGraph.setLayoutData(gridData);

		passingGraph = new ChartValueComposite(this, SWT.BORDER);
		passingGraph.setLayoutData(gridData);

		keeperGraph = new ChartValueComposite(this, SWT.BORDER);
		keeperGraph.setLayoutData(gridData);

		defenderGraph = new ChartValueComposite(this, SWT.BORDER);
		defenderGraph.setLayoutData(gridData);

		playmakerGraph = new ChartValueComposite(this, SWT.BORDER);
		playmakerGraph.setLayoutData(gridData);

		scorerGraph = new ChartValueComposite(this, SWT.BORDER);
		scorerGraph.setLayoutData(gridData);
	}

	public void fill(Player player) {
		PlayerSkills[] skills = player.getSkills();
		if(skills.length < 2) {
			spinner.setValues(skills.length, 0, skills.length, 0, 1, 10);
		} else {
			spinner.setValues(skills.length, 1, skills.length, 0, 1, 10);	
		}
		

		fillTable(player);
	}

	private void fillTable(Player player) {
		PlayerSkills[] skills = player.getSkills();

		this.player = player;
		List<Date> date = new ArrayList<Date>();
		List<Number> stamina = new ArrayList<Number>();
		List<Number> form = new ArrayList<Number>();
		List<Number> pace = new ArrayList<Number>();
		List<Number> technique = new ArrayList<Number>();
		List<Number> passing = new ArrayList<Number>();
		List<Number> keeper = new ArrayList<Number>();
		List<Number> defender = new ArrayList<Number>();
		List<Number> playmaker = new ArrayList<Number>();
		List<Number> scorer = new ArrayList<Number>();
		for (int i = spinner.getMaximum() - spinner.getSelection(); i < spinner.getMaximum(); i++) {
			date.add(skills[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			form.add(skills[i].getForm());
			stamina.add(skills[i].getStamina());
			pace.add(skills[i].getPace());
			technique.add(skills[i].getTechnique());
			passing.add(skills[i].getPassing());
			keeper.add(skills[i].getKeeper());
			defender.add(skills[i].getDefender());
			playmaker.add(skills[i].getPlaymaker());
			scorer.add(skills[i].getScorer());
		}
		staminaGraph.fillGraph(stamina, form, date, SokkerDate.THURSDAY, true, 18, false, Messages.getString("player.stamina") + " & " + Messages.getString("player.form"), Messages.getString("player.stamina"), Messages.getString("player.form")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		paceGraph.fillGraph(pace, date, SokkerDate.THURSDAY, true, 18, false, Messages.getString("player.pace")); //$NON-NLS-1$
		techniqueGraph.fillGraph(technique, date, SokkerDate.THURSDAY, true, 18, false, Messages.getString("player.technique")); //$NON-NLS-1$
		passingGraph.fillGraph(passing, date, SokkerDate.THURSDAY, true, 18, false, Messages.getString("player.passing")); //$NON-NLS-1$
		keeperGraph.fillGraph(keeper, date, SokkerDate.THURSDAY, true, 18, false, Messages.getString("player.keeper")); //$NON-NLS-1$
		defenderGraph.fillGraph(defender, date, SokkerDate.THURSDAY, true, 18, false, Messages.getString("player.defender")); //$NON-NLS-1$
		playmakerGraph.fillGraph(playmaker, date, SokkerDate.THURSDAY, true, 18, false, Messages.getString("player.playmaker")); //$NON-NLS-1$
		scorerGraph.fillGraph(scorer, date, SokkerDate.THURSDAY, true, 18, false, Messages.getString("player.scorer")); //$NON-NLS-1$
	}

}
