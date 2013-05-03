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
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.events.TeamEvent;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class SpyLoginGroup extends Group {

	private SpyManager spyManager = SpyManager.getInstance();
	
	private Button buttonGet;
	private Label labelId;
	private Combo comboId;
	private Map<String, Integer> clubMap;
	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}
	public SpyLoginGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		
		this.setFont(ConfigBean.getFontMain());
		this.setLayout(new FormLayout());
		this.setText(Messages.getString("spy.group.data")); 
		this.setForeground(Colors.getBlueDescription());
		
		FormData formData;
		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 5);

		labelId = new Label(this, SWT.NONE);
		labelId.setLayoutData(formData);
		labelId.setText(Messages.getString("spy.id.opponent")); 
		labelId.setFont(ConfigBean.getFontMain());
		labelId.pack();

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.top = new FormAttachment(labelId, 5);
		formData.right = new FormAttachment(100, -10);

		comboId = new Combo(this, SWT.BORDER);
		comboId.setLayoutData(formData);
		comboId.setFont(ConfigBean.getFontMain());
		comboId.setTextLimit(50);
//		idCombo.addVerifyListener(new VerifyDigitsAction());
		
		formData = new FormData();
		formData.left = new FormAttachment(comboId, 0, SWT.CENTER);
		formData.top = new FormAttachment(comboId, 5);

		buttonGet = new Button(this, SWT.NONE);
		buttonGet.setText(Messages.getString("button.download")); 
		buttonGet.setLayoutData(formData);
		buttonGet.setFont(ConfigBean.getFontMain());
		buttonGet.pack();
		buttonGet.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				String text = comboId.getText();
				int teamId = 0;
				if(clubMap.get(text) != null) {
					teamId = clubMap.get(text);
				} else if (text != null && text.length() < 7 && text.matches("[0-9]+")) { 
					teamId = Integer.valueOf(text);
				}
				if(teamId > 0) {
					try {
						
						Club team = spyManager.getTeam(teamId);
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
		this.clubMap = new HashMap<String, Integer>();
		comboId.removeAll();
		String key;
		MatchesComparator comp = new MatchesComparator(MatchesComparator.WEEK_DAY, MatchesComparator.ASCENDING);
		Collections.sort(matches, comp);

		for(Match match : matches) {
			if(match.getIsFinished() == Match.NOT_FINISHED) {
				if(match.getHomeTeamId() != club.getId() && !clubMap.containsValue(match.getHomeTeamId())) {
					if(clubs.get(match.getHomeTeamId()) != null) {
						key = clubs.get(match.getHomeTeamId()).getClubName().get(0).getName();
						comboId.add(key);
					} else {
						key = String.valueOf(match.getHomeTeamId());
						comboId.add(key);	
					}
					clubMap.put(key, match.getHomeTeamId());
				} else if (match.getAwayTeamId() != club.getId() && !clubMap.containsValue(match.getAwayTeamId())){
					if(clubs.get(match.getAwayTeamId()) != null) {
						key = clubs.get(match.getAwayTeamId()).getClubName().get(0).getName();
						comboId.add(key);
					} else {
						key = String.valueOf(match.getAwayTeamId());
						comboId.add(key);	
					}
					clubMap.put(key, match.getAwayTeamId());
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
