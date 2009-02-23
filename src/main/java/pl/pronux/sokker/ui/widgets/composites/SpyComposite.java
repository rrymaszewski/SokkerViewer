package pl.pronux.sokker.ui.widgets.composites;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.ui.widgets.groups.OpponentTeamInformationGroup;
import pl.pronux.sokker.ui.widgets.groups.SpyLoginGroup;
import pl.pronux.sokker.ui.widgets.tables.SpyPlayersTable;

public class SpyComposite extends Composite {

	private SpyLoginGroup spyLoginGroup;
	private SpyPlayersTable spyPlayersTable;
	private OpponentTeamInformationGroup opponentInformationGroup;

	public SpyComposite(Composite composite, int style) {
		super(composite, style);
		
		this.setLayout(new FormLayout());

		FormData formData = new FormData(200,100);
		formData.left = new FormAttachment(0,5);
		formData.top = new FormAttachment(0,5);
	
		spyLoginGroup = new SpyLoginGroup(this, SWT.NONE);
		spyLoginGroup.setLayoutData(formData);
		spyLoginGroup.setEnabled(false);
		
		formData = new FormData();
		formData.top = new FormAttachment(spyLoginGroup, 5);
		formData.left = new FormAttachment(0,5);
		formData.right = new FormAttachment(100,-5);
		formData.bottom = new FormAttachment(100,-5);
		
		spyPlayersTable = new SpyPlayersTable(this, SWT.FULL_SELECTION);
		spyPlayersTable.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(spyLoginGroup,5);
		formData.right = new FormAttachment(100,-5);
		formData.bottom = new FormAttachment(spyPlayersTable,-5);
		
		opponentInformationGroup = new OpponentTeamInformationGroup(this, SWT.FULL_SELECTION);
		opponentInformationGroup.setLayoutData(formData);
	}
	
	public void fill(List<Match> matches, Club club, Map<Integer, Club> clubs) {
		spyLoginGroup.fill(matches, club, clubs);
	}
	
	public void fill(Club club) {
		spyPlayersTable.fill(club.getPlayers());
		opponentInformationGroup.fill(club);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		spyLoginGroup.setEnabled(enabled);
	}

}
