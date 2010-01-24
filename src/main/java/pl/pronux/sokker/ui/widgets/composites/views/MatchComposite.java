package pl.pronux.sokker.ui.widgets.composites.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.ui.widgets.composites.TabCompositeImpl;
import pl.pronux.sokker.ui.widgets.groups.MatchFieldGroup;
import pl.pronux.sokker.ui.widgets.groups.MatchInfoGroup;
import pl.pronux.sokker.ui.widgets.groups.MatchPlayersGroup;
import pl.pronux.sokker.ui.widgets.groups.MatchResultGroup;
import pl.pronux.sokker.ui.widgets.groups.MatchSummaryGroup;
import pl.pronux.sokker.ui.widgets.groups.WeatherGroup;

public class MatchComposite extends TabCompositeImpl<Match> {

	private MatchResultGroup matchResultComposite;

	private MatchSummaryGroup matchSummaryComposite;

	private WeatherGroup weatherComposite;

	private MatchInfoGroup matchInfoComposite;

	private MatchPlayersGroup matchHomePlayersGroup;

	private MatchPlayersGroup matchAwayPlayersGroup;

	private MatchFieldGroup matchHomeTeamTactic;

	private MatchFieldGroup matchAwayTeamTactic;

	public MatchComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());
		FormData formData = new FormData(0, 0);
		formData.left = new FormAttachment(50, 0);
		formData.top = new FormAttachment(0, 0);
		Label point = new Label(this, SWT.NONE);
		point.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(point, 0, SWT.CENTER);
		formData.top = new FormAttachment(point, 5);
		formData.width = 370;
		formData.height = 150;

		matchResultComposite = new MatchResultGroup(this, SWT.BORDER);
		matchResultComposite.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(matchResultComposite, 0, SWT.CENTER);
		formData.top = new FormAttachment(matchResultComposite, 5);
		formData.width = 370;
		formData.height = 200;

		matchSummaryComposite = new MatchSummaryGroup(this, SWT.BORDER);
		matchSummaryComposite.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.top = new FormAttachment(point, 5);
		formData.right = new FormAttachment(matchResultComposite, -10);
		formData.height = 120;
		matchInfoComposite = new MatchInfoGroup(this, SWT.BORDER);
		matchInfoComposite.setLayoutData(formData);
		
		formData = new FormData();
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(point, 5);
		formData.left = new FormAttachment(matchResultComposite, 10);
		formData.height = 120;

		weatherComposite = new WeatherGroup(this, SWT.BORDER);
		weatherComposite.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.top = new FormAttachment(matchSummaryComposite, 5);
		formData.right = new FormAttachment(50, -10);
		formData.bottom = new FormAttachment(100, -10);

		matchHomePlayersGroup = new MatchPlayersGroup(this, SWT.BORDER);
		matchHomePlayersGroup.setLayoutData(formData);

		formData = new FormData();
		// formData.left = new FormAttachment(matchSummaryComposite, 0, SWT.CENTER);
		formData.left = new FormAttachment(50, 10);
		formData.top = new FormAttachment(matchSummaryComposite, 5);
		formData.right = new FormAttachment(100, -10);
		formData.bottom = new FormAttachment(100, -10);
		// formData.width = 300;
		// formData.height = 200;

		matchAwayPlayersGroup = new MatchPlayersGroup(this, SWT.BORDER);
		matchAwayPlayersGroup.setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(0 ,10);
		formData.top = new FormAttachment(matchInfoComposite, 5);
		formData.right = new FormAttachment(matchResultComposite, -10);
		formData.bottom = new FormAttachment(matchHomePlayersGroup, -5);
		
		matchHomeTeamTactic = new MatchFieldGroup(this, SWT.NONE);
		matchHomeTeamTactic.setLayoutData(formData);
		
		formData = new FormData();
		formData.right = new FormAttachment(100 ,-10);
		formData.top = new FormAttachment(matchInfoComposite, 5);
		formData.left = new FormAttachment(matchResultComposite, 10);
		formData.bottom = new FormAttachment(matchAwayPlayersGroup, -5);
		matchAwayTeamTactic = new MatchFieldGroup(this, SWT.NONE);
		matchAwayTeamTactic.setLayoutData(formData);
	}

	public void fill(Match match) {

		this.setRedraw(false);

		weatherComposite.setWeatherInfo(match);
		matchInfoComposite.fill(match);
		matchSummaryComposite.fill(match);
		matchResultComposite.fill(match);
		matchHomePlayersGroup.fill(match.getHomeTeamStats(), match.getHomeTeamName());
		matchAwayPlayersGroup.fill(match.getAwayTeamStats(), match.getAwayTeamName());

		matchAwayTeamTactic.fillAway(match.getAwayTeamStats());
		matchHomeTeamTactic.fillHome(match.getHomeTeamStats());
		this.setRedraw(true);

	}

}
