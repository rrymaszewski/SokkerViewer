package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.model.Stand;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class ArenaStandGroup extends Group {

	private Text capacity;
	private Combo type;
	private Label days;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public ArenaStandGroup(Composite parent, int style) {
		super(parent, style);
		type = new Combo(this, SWT.READ_ONLY);
		type.setLocation(5, 10);
		type.setSize(140, 30);
		type.setItems(new String[] {
				Messages.getString("arena.standingPlaces"), 
				Messages.getString("arena.terraces"), 
				Messages.getString("arena.terracesUnderRoof"), 
				Messages.getString("arena.benches"), 
				Messages.getString("arena.benchesUnderRoof"), 
				Messages.getString("arena.seats"), 
				Messages.getString("arena.seatsUnderRoof") 
				});
		type.setFont(ConfigBean.getFontMain());

		Listener resetStandingPlacesList = new Listener() {
			public void handleEvent(Event event) {
				if (((Combo) (event.widget)).getSelectionIndex() == 0) {
					((Text) ((Combo) (event.widget)).getParent().getChildren()[1]).setText("100"); 
				}
			}
		};

		type.addListener(SWT.Selection, resetStandingPlacesList);

		capacity = new Text(this, SWT.CENTER | SWT.SINGLE);
		capacity.setLocation(35, 40);
		capacity.setSize(80, 15);
		capacity.setFont(ConfigBean.getFontMain());
		capacity.setTextLimit(5);

		capacity.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int j = 0; j < chars.length; j++) {
					if (!('0' <= chars[j] && chars[j] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});

		Listener readonlyList = new Listener() {
			public void handleEvent(Event event) {
				if (((Combo) ((Text) (event.widget)).getParent().getChildren()[0]).getSelectionIndex() == 0) {
					((Text) (event.widget)).setEditable(false);
				} else {
					((Text) (event.widget)).setEditable(true);
					((Text) event.widget).selectAll();
				}
			}
		};

		capacity.addListener(SWT.MouseDown, readonlyList);

		days = new Label(this, SWT.CENTER | SWT.SINGLE);
		days.setLocation(15, 55);
		days.setSize(120, 15);
		days.setFont(ConfigBean.getFontMain());

		this.setToolTipText(""); 
	}

	public void setDays(String days) {
		this.days.setText(days);
	}

	public void setCapacity(String capacity) {
		this.capacity.setText(capacity);
	}

	public void setType(String type) {
		this.type.setText(type);
	}

	public void setStand(Stand stand) {
		this.setType(getNameOfSeatArena(stand));
		this.setCapacity(String.valueOf(stand.getCapacity()));
		if (stand.getConstructionDays() > 0) {
			this.setDays(Messages.getString("arena.days") + stand.getConstructionDays()); 
		}
	}

	private String getNameOfSeatArena(Stand stand) {
		String arenaNameOfSeat = ""; 

		if (stand.getIsRoof() == Stand.ROOF_TRUE) {
			switch (stand.getType()) {
				case Stand.TYPE_TERRACES:
					arenaNameOfSeat = Messages.getString("arena.terracesUnderRoof"); 
					break;
				case Stand.TYPE_BENCHES:
					arenaNameOfSeat = Messages.getString("arena.benchesUnderRoof"); 
					break;
				case Stand.TYPE_SEATS:
					arenaNameOfSeat = Messages.getString("arena.seatsUnderRoof"); 
					break;
			}
		} else {
			switch (stand.getType()) {
				case Stand.TYPE_STANDING:
					arenaNameOfSeat = Messages.getString("arena.standingPlaces"); 
					break;
				case Stand.TYPE_TERRACES:
					arenaNameOfSeat = Messages.getString("arena.terraces"); 
					break;
				case Stand.TYPE_BENCHES:
					arenaNameOfSeat = Messages.getString("arena.benches"); 
					break;
				case Stand.TYPE_SEATS:
					arenaNameOfSeat = Messages.getString("arena.seats"); 
					break;
			}
		}
		return arenaNameOfSeat;
	}

	public int getSelectionIndex() {
		return type.getSelectionIndex();
	}

	public String getCapacity() {
		return capacity.getText();
	}

	@Override
	public void setToolTipText(String text) {
		this.type.setToolTipText(text);
		this.capacity.setToolTipText(text);
		super.setToolTipText(text);
	}
}
