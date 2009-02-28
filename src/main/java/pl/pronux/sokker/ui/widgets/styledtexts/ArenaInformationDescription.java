package pl.pronux.sokker.ui.widgets.styledtexts;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Arena;
import pl.pronux.sokker.model.ArenaPlaceCapacity;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class ArenaInformationDescription extends StyledText {

	public ArenaInformationDescription(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontDescription());
	}
	
	public void setInfo(Arena arena) {
		Object[][] values;
		this.setRedraw(false);
		this.setText("");
		values = new Object[11][2];
		// koszty utrzymania stadionu
		values[0][0] = Messages.getString("arena.payments"); //$NON-NLS-1$
		// przychod otrzymany ze stadionu
		values[1][0] = Messages.getString("arena.maxEarns"); //$NON-NLS-1$
		// pojemnosc stadionu
		values[2][0] = Messages.getString("arena.capacity"); //$NON-NLS-1$

		values[3][0] = ""; //$NON-NLS-1$

		values[4][0] = Messages.getString("arena.standingPlaces"); //$NON-NLS-1$

		values[5][0] = Messages.getString("arena.terraces"); //$NON-NLS-1$

		values[6][0] = Messages.getString("arena.terracesUnderRoof"); //$NON-NLS-1$

		values[7][0] = Messages.getString("arena.benches"); //$NON-NLS-1$

		values[8][0] = Messages.getString("arena.benchesUnderRoof"); //$NON-NLS-1$

		values[9][0] = Messages.getString("arena.seats"); //$NON-NLS-1$

		values[10][0] = Messages.getString("arena.seatsUnderRoof"); //$NON-NLS-1$

		values[0][1] = Money.formatDoubleCurrencySymbol(arena.getCost());

		values[1][1] = Money.formatDoubleCurrencySymbol(arena.getIncome());
		values[2][1] = SVNumberFormat.formatInteger(arena.getCapacity()) + "   "; //$NON-NLS-1$
		ArenaPlaceCapacity arenaPlaceCapacity = arena.getArenaPlaceCapacity();
		values[3][1] = ""; //$NON-NLS-1$
		values[4][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getStanding()) + "   "; //$NON-NLS-1$
		values[5][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getTerraces()) + "   "; //$NON-NLS-1$
		values[6][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getTerracesWithRoof()) + "   "; //$NON-NLS-1$
		values[7][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getBenches()) + "   "; //$NON-NLS-1$
		values[8][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getBenchesWithRoof()) + "   "; //$NON-NLS-1$
		values[9][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getSeats()) + "   "; //$NON-NLS-1$
		values[10][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getSeatsWithRoof()) + "   "; //$NON-NLS-1$

		for(int i = 0 ; i < values.length; i++) {
			this.append(String.format("%-30s %15s\r\n", values[i]));
		}
		this.setRedraw(true);
	}

}
