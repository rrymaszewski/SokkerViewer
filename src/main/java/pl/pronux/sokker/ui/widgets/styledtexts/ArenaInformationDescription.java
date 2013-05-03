package pl.pronux.sokker.ui.widgets.styledtexts;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.bean.ArenaPlaceCapacity;
import pl.pronux.sokker.model.Arena;
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
		values[0][0] = Messages.getString("arena.payments"); 
		// przychod otrzymany ze stadionu
		values[1][0] = Messages.getString("arena.maxEarns"); 
		// pojemnosc stadionu
		values[2][0] = Messages.getString("arena.capacity"); 

		values[3][0] = ""; 

		values[4][0] = Messages.getString("arena.standingPlaces"); 

		values[5][0] = Messages.getString("arena.terraces"); 

		values[6][0] = Messages.getString("arena.terracesUnderRoof"); 

		values[7][0] = Messages.getString("arena.benches"); 

		values[8][0] = Messages.getString("arena.benchesUnderRoof"); 

		values[9][0] = Messages.getString("arena.seats"); 

		values[10][0] = Messages.getString("arena.seatsUnderRoof"); 

		values[0][1] = Money.formatDoubleCurrencySymbol(arena.getCost());

		values[1][1] = Money.formatDoubleCurrencySymbol(arena.getIncome());
		values[2][1] = SVNumberFormat.formatInteger(arena.getCapacity()) + "   "; 
		ArenaPlaceCapacity arenaPlaceCapacity = arena.getArenaPlaceCapacity();
		values[3][1] = ""; 
		values[4][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getStanding()) + "   "; 
		values[5][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getTerraces()) + "   "; 
		values[6][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getTerracesWithRoof()) + "   "; 
		values[7][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getBenches()) + "   "; 
		values[8][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getBenchesWithRoof()) + "   "; 
		values[9][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getSeats()) + "   "; 
		values[10][1] = SVNumberFormat.formatInteger(arenaPlaceCapacity.getSeatsWithRoof()) + "   "; 

		for(int i = 0 ; i < values.length; i++) {
			this.append(String.format("%-30s %15s\r\n", values[i]));
		}
		this.setRedraw(true);
	}

}
