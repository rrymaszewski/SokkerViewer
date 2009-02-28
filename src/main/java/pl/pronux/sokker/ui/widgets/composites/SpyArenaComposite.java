package pl.pronux.sokker.ui.widgets.composites;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Arena;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Stand;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class SpyArenaComposite extends Composite {
	private Map<Integer, CLabel> labels = new HashMap<Integer, CLabel>();

	public SpyArenaComposite(Composite parent, int style) {
		super(parent, style);

		this.setFont(ConfigBean.getFontMain());

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		this.setLayout(gridLayout);

		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;

		CLabel label = new CLabel(this, SWT.BORDER | SWT.CENTER);
		label.setLayoutData(data);
		label.setFont(ConfigBean.getFontMain());
		labels.put(Stand.NW, label);

		label = new CLabel(this, SWT.BORDER | SWT.CENTER);
		label.setLayoutData(data);
		label.setFont(ConfigBean.getFontMain());
		labels.put(Stand.N, label);

		label = new CLabel(this, SWT.BORDER | SWT.CENTER);
		label.setLayoutData(data);
		label.setFont(ConfigBean.getFontMain());
		labels.put(Stand.NE, label);

		label = new CLabel(this, SWT.BORDER | SWT.CENTER);
		label.setLayoutData(data);
		label.setFont(ConfigBean.getFontMain());
		labels.put(Stand.W, label);

		label = new CLabel(this, SWT.BORDER | SWT.CENTER);
		label.setLayoutData(data);
		label.setFont(ConfigBean.getFontMain());
		labels.put(Stand.FIELD, label);

		label = new CLabel(this, SWT.BORDER | SWT.CENTER);
		label.setLayoutData(data);
		label.setFont(ConfigBean.getFontMain());
		labels.put(Stand.E, label);

		label = new CLabel(this, SWT.BORDER | SWT.CENTER);
		label.setLayoutData(data);
		label.setFont(ConfigBean.getFontMain());
		labels.put(Stand.SW, label);

		label = new CLabel(this, SWT.BORDER | SWT.CENTER);
		label.setLayoutData(data);
		label.setFont(ConfigBean.getFontMain());
		labels.put(Stand.S, label);

		label = new CLabel(this, SWT.BORDER | SWT.CENTER);
		label.setLayoutData(data);
		label.setFont(ConfigBean.getFontMain());
		labels.put(Stand.SE, label);

	}

	public void fill(Arena arena) {
		this.setRedraw(false);
		CLabel label;
		List<Stand> stands = arena.getStands();
		
		for (Stand stand : stands) {
			label = labels.get(stand.getLocation());
			if(stand.getIsRoof() == Stand.ROOF_TRUE) {
				label.setText(String.format("[%d]", stand.getCapacity()));
			} else {
				label.setText(String.valueOf(stand.getCapacity()));	
			}
			
			label.setBackground(getColor(stand.getType()));
			label.setToolTipText(getLabel(stand));
		}
		
		label = labels.get(Stand.FIELD);
		label.setBackground(getColor(Stand.FIELD));
		label.setToolTipText(String.format("%s\r\n%s: %s", arena.getArenaNames().get(arena.getArenaNames().size()-1).getArenaName(), Messages.getString("arena.maxEarns"), Money.formatDoubleCurrencySymbol(arena.getIncome())));
		this.setRedraw(true);
	}

	private Color getColor(int type) {
		switch (type) {
			case Stand.TYPE_BENCHES:
				return Colors.getArenaBenches();
			case Stand.TYPE_SEATS:
				return Colors.getArenaSeats();
			case Stand.TYPE_TERRACES:
				return ColorResources.getGray();
			case Stand.FIELD:
				return Colors.getArenaField();
			case Stand.TYPE_STANDING:
				return Colors.getArenaStanding();
			default:
				return ColorResources.getWhite();
		}
	}
	
	private String getLabel(Stand stand) {
		switch (stand.getType()) {
			case Stand.TYPE_STANDING:
				return Messages.getString("arena.standingPlaces");
			case Stand.TYPE_TERRACES:
				if(stand.getIsRoof() == Stand.ROOF_TRUE) {
					return Messages.getString("arena.terracesUnderRoof");
				} else {
					return Messages.getString("arena.terraces");
				}
			case Stand.TYPE_BENCHES:
				if(stand.getIsRoof() == Stand.ROOF_TRUE) {
					return Messages.getString("arena.benchesUnderRoof");
				} else {
					return Messages.getString("arena.benches");
				}
			case Stand.TYPE_SEATS:
				if(stand.getIsRoof() == Stand.ROOF_TRUE) {
					return Messages.getString("arena.seatsUnderRoof");
				} else {
					return Messages.getString("arena.seats");
				}
		}
		return "";
	}

}