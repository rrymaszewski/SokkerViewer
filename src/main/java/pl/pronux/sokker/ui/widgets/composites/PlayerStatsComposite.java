package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.ui.widgets.tables.PlayerStatsTable;

public class PlayerStatsComposite extends Composite {

	private PlayerStatsTable playerStatsTable;

	public PlayerStatsComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		playerStatsTable = new PlayerStatsTable(this, SWT.FULL_SELECTION);
	}
	
	public void fill(Player player) {
		playerStatsTable.fill(player.getPlayerMatchStatistics());
	}
}
