package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.ui.widgets.styledtexts.PlayerHistoryDescription;
import pl.pronux.sokker.ui.widgets.styledtexts.PlayerHistoryDescriptionAdditions;

public class PlayerHistoryDescriptionComposite extends Composite {

	private PlayerHistoryDescription playerHistoryDescription;
	private PlayerHistoryDescriptionAdditions playerHistoryDescriptionAdditions;

	public PlayerHistoryDescriptionComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());
		this.setVisible(false);
		FormData formData;
		
		formData = new FormData();
		formData.left = new FormAttachment(0,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(60,0);
		formData.bottom = new FormAttachment(100,0);
		
		playerHistoryDescription = new PlayerHistoryDescription(this, SWT.NONE);
		playerHistoryDescription.setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(playerHistoryDescription,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(100,0);
		formData.bottom = new FormAttachment(100,0);
		playerHistoryDescriptionAdditions = new PlayerHistoryDescriptionAdditions(this, SWT.NONE);
		playerHistoryDescriptionAdditions.setLayoutData(formData);
	}
	
	public void setStatsPlayerInfo(Player player) {
		this.playerHistoryDescription.setStatsPlayerInfo(player);
		this.playerHistoryDescriptionAdditions.setStatsPlayerInfo(player);
	}

	public void setStatsPlayerInfo(Player player, int index) {
		this.playerHistoryDescription.setStatsPlayerInfo(player, index);
		this.playerHistoryDescriptionAdditions.setStatsPlayerInfo(player, index);
	}
	
	public String getText() {
		return playerHistoryDescription.getText();
	}
}
