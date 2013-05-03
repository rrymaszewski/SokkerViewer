package pl.pronux.sokker.ui.widgets.groups;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.TeamStats;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ImageResources;

public class MatchFieldGroup extends Group {

	private static int walkaround = (int) (Math.random() * 100) % 4;

	private Label attLabel;

	private Label midLabel;

	private Label defLabel;

	private Label gkLabel;

	private Label centerLabel;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public MatchFieldGroup(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());
		this.setSize(154, 231);
		this.setFont(ConfigBean.getFontMain());
		
		FormData formData;

		formData = new FormData(0, 0);
		formData.left = new FormAttachment(50, 0);
		formData.top = new FormAttachment(0, 0);
		centerLabel = new Label(this, SWT.NONE);
		centerLabel.setLayoutData(formData);

		formData = new FormData(154, 81);
		formData.top = new FormAttachment(centerLabel, 0);
		formData.left = new FormAttachment(centerLabel, 0, SWT.CENTER);

		attLabel = new Label(this, SWT.NONE);
		attLabel.setLayoutData(formData);

		formData = new FormData(154, 84);
		formData.top = new FormAttachment(attLabel, 0);
		formData.left = new FormAttachment(centerLabel, 0, SWT.CENTER);

		midLabel = new Label(this, SWT.NONE);
		midLabel.setLayoutData(formData);

		formData = new FormData(154, 43);
		formData.top = new FormAttachment(midLabel, 0);
		formData.left = new FormAttachment(centerLabel, 0, SWT.CENTER);

		defLabel = new Label(this, SWT.NONE);
		defLabel.setLayoutData(formData);

		formData = new FormData(154, 23);
		formData.top = new FormAttachment(defLabel, 0);
		formData.left = new FormAttachment(centerLabel, 0, SWT.CENTER);

		gkLabel = new Label(this, SWT.NONE);
		gkLabel.setLayoutData(formData);
	}

	public void fillHome(TeamStats homeStats) {
		if (!homeStats.isEmpty()) {
			if (homeStats.getTimeOnHalf() > 0 && homeStats.getTimePossession() > 0) {
				this.setText(homeStats.getTacticName());

				int gk = 0;
				int att = 0;
				int def = 0;
				int mid = 0;
				List<PlayerStats> playersStats = homeStats.getPlayersStats();

				for (PlayerStats playerStats : playersStats) {
					if (playerStats.getTimePlayed() > 0 && playerStats.getTimeIn() == 0) {
						switch (playerStats.getFormation()) {
						case PlayerStats.ATT:
							att++;
							break;
						case PlayerStats.DEF:
							def++;
							break;
						case PlayerStats.MID:
							mid++;
							break;
						case PlayerStats.GK:
							gk++;
							break;
						default:
							break;
						}
					}
				}

				gkLabel.setImage(ImageResources.getImageResources("GK_" + gk + ".png"));  

				if (def > 0 && def <= 5) {
					defLabel.setImage(ImageResources.getImageResources("DEF_" + def + "a.png"));  
				} else if (def == 0) {
					defLabel.setImage(ImageResources.getImageResources("DEF_0.png")); 
				} else {
					defLabel.setImage(ImageResources.getImageResources("DEF_over.png")); 
				}

				if (mid > 0 && mid <= 5) {
					midLabel.setImage(ImageResources.getImageResources("MID_" + mid + "a.png"));  
				} else if (mid == 0) {
					midLabel.setImage(ImageResources.getImageResources("MID_0.png")); 
				} else {
					midLabel.setImage(ImageResources.getImageResources("MID_over.png")); 
				}

				if (att > 0 && att <= 3) {
					attLabel.setImage(ImageResources.getImageResources("ATT_" + att + "a.png"));  
				} else if (att == 0) {
					attLabel.setImage(ImageResources.getImageResources("ATT_0.png")); 
				} else {
					attLabel.setImage(ImageResources.getImageResources("ATT_over.png")); 
				}
			} else {
				this.setText(""); 
				gkLabel.setImage(ImageResources.getImageResources("GK_0.png")); 
				defLabel.setImage(ImageResources.getImageResources("DEF_0.png")); 
				midLabel.setImage(ImageResources.getImageResources("MID_0.png")); 
				attLabel.setImage(ImageResources.getImageResources("ATT_0.png")); 
				switch (walkaround) {
				case PlayerStats.GK:
					gkLabel.setImage(ImageResources.getImageResources("GK_w.png")); 
					break;
				case PlayerStats.DEF:
					defLabel.setImage(ImageResources.getImageResources("DEF_w.png")); 
					break;
				case PlayerStats.MID:
					midLabel.setImage(ImageResources.getImageResources("MID_w.png")); 
					break;
				case PlayerStats.ATT:
					attLabel.setImage(ImageResources.getImageResources("ATT_w.png")); 
					break;
				}
			}
		} else {
			gkLabel.setImage(null);
			defLabel.setImage(null);
			midLabel.setImage(null);
			attLabel.setImage(null);
		}
	}

	public void fillAway(TeamStats awayStats) {
		if (!awayStats.isEmpty()) {
			if (awayStats.getTimeOnHalf() > 0 && awayStats.getTimePossession() > 0) {
				this.setText(awayStats.getTacticName());

				int gk = 0;
				int att = 0;
				int def = 0;
				int mid = 0;
				List<PlayerStats> playersStats = awayStats.getPlayersStats();

				for (PlayerStats playerStats : playersStats) {
					if (playerStats.getTimePlayed() > 0 && playerStats.getTimeIn() == 0) {
						switch (playerStats.getFormation()) {
						case PlayerStats.ATT:
							att++;
							break;
						case PlayerStats.DEF:
							def++;
							break;
						case PlayerStats.MID:
							mid++;
							break;
						case PlayerStats.GK:
							gk++;
							break;
						default:
							break;
						}
					}
				}

				gkLabel.setImage(ImageResources.getImageResources("GK_" + gk + ".png"));  

				if (def > 0 && def <= 5) {
					defLabel.setImage(ImageResources.getImageResources("DEF_" + def + "b.png"));  
				} else if (def == 0) {
					defLabel.setImage(ImageResources.getImageResources("DEF_0.png")); 
				} else {
					defLabel.setImage(ImageResources.getImageResources("DEF_over.png")); 
				}

				if (mid > 0 && mid <= 5) {
					midLabel.setImage(ImageResources.getImageResources("MID_" + mid + "b.png"));  
				} else if (mid == 0) {
					midLabel.setImage(ImageResources.getImageResources("MID_0.png")); 
				} else {
					midLabel.setImage(ImageResources.getImageResources("MID_over.png")); 
				}

				if (att > 0 && att <= 3) {
					attLabel.setImage(ImageResources.getImageResources("ATT_" + att + "b.png"));  
				} else if (att == 0) {
					attLabel.setImage(ImageResources.getImageResources("ATT_0.png")); 
				} else {
					attLabel.setImage(ImageResources.getImageResources("ATT_over.png")); 
				}

			} else {
				this.setText(""); 
				gkLabel.setImage(ImageResources.getImageResources("GK_0.png")); 
				defLabel.setImage(ImageResources.getImageResources("DEF_0.png")); 
				midLabel.setImage(ImageResources.getImageResources("MID_0.png")); 
				attLabel.setImage(ImageResources.getImageResources("ATT_0.png")); 
				switch (walkaround) {
				case PlayerStats.GK:
					gkLabel.setImage(ImageResources.getImageResources("GK_w.png")); 
					break;
				case PlayerStats.DEF:
					defLabel.setImage(ImageResources.getImageResources("DEF_w.png")); 
					break;
				case PlayerStats.MID:
					midLabel.setImage(ImageResources.getImageResources("MID_w.png")); 
					break;
				case PlayerStats.ATT:
					attLabel.setImage(ImageResources.getImageResources("ATT_w.png")); 
					break;
				}
			}
		} else {
			gkLabel.setImage(null);
			defLabel.setImage(null);
			midLabel.setImage(null);
			attLabel.setImage(null);

		}
	}

}
