package pl.pronux.sokker.ui.widgets.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.ui.widgets.tables.MatchesTable;

public class MatchesComposite extends Composite {

	private MatchesTable leftTable;
	private MatchesTable rightTable;

	public MatchesComposite(Composite parent, int style) {
		super(parent, style);
		
		this.setLayout(new FormLayout());
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.top = new FormAttachment(0, 10);
		formData.bottom = new FormAttachment(100, -10);
		formData.right = new FormAttachment(50, -5);

		leftTable = new MatchesTable(this, SWT.FULL_SELECTION | SWT.BORDER);
		leftTable.setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(50, 5);
		formData.top = new FormAttachment(0, 10);
		formData.bottom = new FormAttachment(100, -10);
		formData.right = new FormAttachment(100, -10);

		rightTable = new MatchesTable(this, SWT.FULL_SELECTION | SWT.BORDER);
		rightTable.setLayoutData(formData);
		
	}
	
	public void fill(int teamID, List<Match> matches) {
		this.setRedraw(false);
		int counter = 0;
		int week = 0;
		Match previousMatch = new Match();
		List<Match> sundayMatches = new ArrayList<Match>();
		List<Match> wednesdayMatches = new ArrayList<Match>();
		for(Match match : matches) {
			if(match.getIsFinished() == Match.FINISHED) {
				if(week != match.getWeek()) {
					if(counter == 1) {
						Match emptyMatch = new Match();
						emptyMatch.setWeek(week);
						emptyMatch.setDay(SokkerDate.WEDNESDAY);
						emptyMatch.setAwayTeamName(""); 
						emptyMatch.setHomeTeamName(""); 
						emptyMatch.setAwayTeamScore(-1);
						emptyMatch.setHomeTeamScore(-1);
						emptyMatch.setDateStarted(new Date(previousMatch.getDateStarted().getCalendar().getTimeInMillis() + 3 * Date.DAY));
						emptyMatch.setIsFinished(Match.FINISHED);
						wednesdayMatches.add(emptyMatch);
					} else if(counter == 2) {
						Match emptyMatch = new Match();
						emptyMatch.setWeek(week);
						emptyMatch.setAwayTeamName(""); 
						emptyMatch.setHomeTeamName(""); 
						emptyMatch.setDateStarted(new Date(previousMatch.getDateStarted().getCalendar().getTimeInMillis() - 3 * Date.DAY));
						emptyMatch.setAwayTeamScore(-1);
						emptyMatch.setHomeTeamScore(-1);
						emptyMatch.setDay(SokkerDate.SUNDAY);
						emptyMatch.setIsFinished(Match.FINISHED);
						sundayMatches.add(emptyMatch);
					}
					week = match.getWeek();
					counter = 0;
				} 
				
				if(match.getDay() < SokkerDate.WEDNESDAY) {
					sundayMatches.add(match);
					counter += 1;
				} else {
					wednesdayMatches.add(match);
					counter += 2;
				}
				previousMatch = match;
			}
		}
		
		leftTable.fill(teamID, sundayMatches);
		rightTable.fill(teamID, wednesdayMatches);
		
		this.setRedraw(true);
	}
	
	public MatchesTable getLeftTable() {
		return leftTable;
	}
	
	public MatchesTable getRightTable() {
		return rightTable;
	}

}
