package pl.pronux.sokker.ui.widgets.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.composites.MatchTeamComposite;
import pl.pronux.sokker.ui.widgets.composites.views.MatchComposite;

public class MatchTabComposite extends SVTabFolder {

	public static final String MAIN = "main"; 
	public static final String HOME = "home"; 
	public static final String AWAY = "away"; 
	
	public MatchTabComposite(Composite parent, int style) {
		super(parent, style);
		this.addItem(MAIN, new MatchComposite(this, SWT.NONE));
		this.setSelection(this.getItem(MAIN));
		this.setText(MAIN, Messages.getString("match")); 
		this.addItem(HOME, new MatchTeamComposite(this, SWT.NONE));
		this.addItem(AWAY, new MatchTeamComposite(this, SWT.NONE));
		this.setSimple(false);
	}
	
	public void fill(Match match) {
		this.setSelection(0);
		if(this.getComposite(MAIN) instanceof MatchComposite) {
			MatchComposite matches = (MatchComposite)this.getComposite(MAIN);
			matches.fill(match);
		}
		if(this.getComposite(HOME) instanceof MatchTeamComposite) {
			MatchTeamComposite matches = (MatchTeamComposite)this.getComposite(HOME);
			matches.fill(match.getHomeTeamStats());
			if(match.getHomeTeamName() == null) {
				this.setText(HOME, String.valueOf(match.getHomeTeamId()));
			} else {
				this.setText(HOME, match.getHomeTeamName());	
			}
			
		}
		if(this.getComposite(AWAY) instanceof MatchTeamComposite) {
			MatchTeamComposite matches = (MatchTeamComposite)this.getComposite(AWAY);
			matches.fill(match.getAwayTeamStats());
			if(match.getAwayTeamName() == null) {
				this.setText(AWAY, String.valueOf(match.getAwayTeamId()));
			} else {
				this.setText(AWAY, match.getAwayTeamName());	
			}
		}
	}
}
