package pl.pronux.sokker.ui.widgets.groups;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.SpyManager;
import pl.pronux.sokker.comparators.MatchesComparator;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.events.TeamEvent;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class SpyLoginGroup extends Group {

	private Button buttonGet;
	private Label idLabel;
	private Combo idCombo;
	private HashMap<String, Integer> clubMap;
	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}
	public SpyLoginGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		
		this.setFont(ConfigBean.getFontMain());
		this.setLayout(new FormLayout());
		this.setText(Messages.getString("spy.group.data")); //$NON-NLS-1$
		this.setForeground(ColorResources.getBlueDescription());
		
		FormData formData;
		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 5);

		idLabel = new Label(this, SWT.NONE);
		idLabel.setLayoutData(formData);
		idLabel.setText(Messages.getString("spy.id.opponent")); //$NON-NLS-1$
		idLabel.setFont(ConfigBean.getFontMain());
		idLabel.pack();

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.top = new FormAttachment(idLabel, 5);
		formData.right = new FormAttachment(100, -10);

		idCombo = new Combo(this, SWT.BORDER);
		idCombo.setLayoutData(formData);
		idCombo.setFont(ConfigBean.getFontMain());
		idCombo.setTextLimit(50);
//		idCombo.addVerifyListener(new VerifyDigitsAction());
		
		formData = new FormData();
		formData.left = new FormAttachment(idCombo, 0, SWT.CENTER);
		formData.top = new FormAttachment(idCombo, 5);

		buttonGet = new Button(this, SWT.NONE);
		buttonGet.setText(Messages.getString("button.download")); //$NON-NLS-1$
		buttonGet.setLayoutData(formData);
		buttonGet.setFont(ConfigBean.getFontMain());
		buttonGet.pack();
		buttonGet.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				String text = idCombo.getText();
				int teamID = 0;
				if(clubMap.get(text) != null) {
					teamID = clubMap.get(text);
				} else if (text != null && text.length() < 7 && text.matches("[0-9]+")) { //$NON-NLS-1$
					teamID = Integer.valueOf(text);
				}
				if(teamID > 0) {
					try {
						
						Club team = new SpyManager().getTeam(teamID);
						if(team != null) {
							ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_SPY, new TeamEvent(team));
						}
					} catch (SVException e) {
						new BugReporter(SpyLoginGroup.this.getShell()).openErrorMessage( e.getMessage(), e);
					} catch (IOException e) {
						new BugReporter(SpyLoginGroup.this.getShell()).openErrorMessage( e.getMessage(), e);
					} catch (SQLException e) {
						new BugReporter(SpyLoginGroup.this.getShell()).openErrorMessage(e.getMessage(), e);					
					} catch (SAXException e) {
						new BugReporter(SpyLoginGroup.this.getShell()).openErrorMessage(e.getMessage(), e);					
					}	
				}
			}
		});
	}
	public void fill(List<Match> matches, Club club, Map<Integer, Club> clubs) {
		clubMap = new HashMap<String, Integer>();
		idCombo.removeAll();
		String key;
		MatchesComparator comp = new MatchesComparator(MatchesComparator.WEEK_DAY, MatchesComparator.ASCENDING);
		Collections.sort(matches, comp);

		for(Match match : matches) {
			if(match.getIsFinished() == Match.NOT_FINISHED) {
				if(match.getHomeTeamID() != club.getId() && !clubMap.containsValue(match.getHomeTeamID())) {
					if(clubs.get(match.getHomeTeamID()) != null) {
						key = clubs.get(match.getHomeTeamID()).getClubName().get(0).getName();
						idCombo.add(key);
					} else {
						key = String.valueOf(match.getHomeTeamID());
						idCombo.add(key);	
					}
					clubMap.put(key, match.getHomeTeamID());
				} else if (match.getAwayTeamID() != club.getId() && !clubMap.containsValue(match.getAwayTeamID())){
					if(clubs.get(match.getAwayTeamID()) != null) {
						key = clubs.get(match.getAwayTeamID()).getClubName().get(0).getName();
						idCombo.add(key);
					} else {
						key = String.valueOf(match.getAwayTeamID());
						idCombo.add(key);	
					}
					clubMap.put(key, match.getAwayTeamID());
				}
			}
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.buttonGet.setEnabled(enabled);
		//		super.setEnabled(arg0);
	}

}
