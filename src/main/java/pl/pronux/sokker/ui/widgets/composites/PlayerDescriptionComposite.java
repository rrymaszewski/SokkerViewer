package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.ui.widgets.styledtexts.PlayerDescriptionAdditionsStyledText;
import pl.pronux.sokker.ui.widgets.styledtexts.PlayerDescriptionStyledText;

public class PlayerDescriptionComposite extends Composite {

	private PlayerDescriptionStyledText playerDescription;
	private PlayerDescriptionAdditionsStyledText playerDescriptionAdditions;

	public PlayerDescriptionComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());
		this.setVisible(false);
		FormData formData;
		
		formData = new FormData();
		formData.left = new FormAttachment(0,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(60,0);
		formData.bottom = new FormAttachment(100,0);
		
		playerDescription = new PlayerDescriptionStyledText(this, SWT.NONE);
		playerDescription.setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(playerDescription,0);
		formData.top = new FormAttachment(0,0);
		formData.right = new FormAttachment(100,0);
		formData.bottom = new FormAttachment(100,0);
		playerDescriptionAdditions = new PlayerDescriptionAdditionsStyledText(this, SWT.NONE);
		playerDescriptionAdditions.setLayoutData(formData);
	}
	
	public void setStatsPlayerInfo(Player player) {
		this.playerDescription.setStatsPlayerInfo(player);
		this.playerDescriptionAdditions.setStatsPlayerInfo(player);
	}

	public void setStatsPlayerInfo(Player player, int index) {
		this.playerDescription.setStatsPlayerInfo(player, index);
		this.playerDescriptionAdditions.setStatsPlayerInfo(player, index);
	}
	
	public String getText() {
		return playerDescription.getText();
	}
}
