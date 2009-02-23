package pl.pronux.sokker.ui.widgets.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.composites.MatchComposite;
import pl.pronux.sokker.ui.widgets.composites.MatchTeamComposite;

public class MatchTabComposite extends SVTabFolder {

	public static final String MAIN = "main"; //$NON-NLS-1$
	public static final String HOME = "home"; //$NON-NLS-1$
	public static final String AWAY = "away"; //$NON-NLS-1$
	
	public MatchTabComposite(Composite parent, int style) {
		super(parent, style);
		this.addItem(MAIN, new MatchComposite(this, SWT.NONE));
		this.setSelection(this.getItem(MAIN));
		this.setText(MAIN, Messages.getString("match")); //$NON-NLS-1$
		this.addItem(HOME, new MatchTeamComposite(this, SWT.NONE));
		this.addItem(AWAY, new MatchTeamComposite(this, SWT.NONE));
		this.setSimple(false);
	}
	
	public void fill(Match match) {
		if(this.getComposite(MAIN) instanceof MatchComposite) {
			MatchComposite matches = (MatchComposite)this.getComposite(MAIN);
			matches.fill(match);
		}
		if(this.getComposite(HOME) instanceof MatchTeamComposite) {
			MatchTeamComposite matches = (MatchTeamComposite)this.getComposite(HOME);
			matches.fill(match.getHomeTeamStats());
			if(match.getHomeTeamName() == null) {
				this.setText(HOME, String.valueOf(match.getHomeTeamID()));
			} else {
				this.setText(HOME, match.getHomeTeamName());	
			}
			
		}
		if(this.getComposite(AWAY) instanceof MatchTeamComposite) {
			MatchTeamComposite matches = (MatchTeamComposite)this.getComposite(AWAY);
			matches.fill(match.getAwayTeamStats());
			if(match.getAwayTeamName() == null) {
				this.setText(AWAY, String.valueOf(match.getAwayTeamID()));
			} else {
				this.setText(AWAY, match.getAwayTeamName());	
			}
		}
	}
}
