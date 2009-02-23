package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Arena;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.Stand;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.DescriptionDoubleComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;

public class ViewArena implements IPlugin {

	private Stand[] stand;

	private Combo[] arenaCombo;

	private Button arenaCountButton;

	private Label[] arenaDays;

	private Group[] arenaGroup;

	private Label arenaName;

	private Button arenaResetButton;

	private Text[] arenaTextTable;

	private Composite viewComposite;

	private DescriptionDoubleComposite descriptionComposite;

	private TreeItem _treeItem;

	private Composite composite;

	private FormData descriptionFormData;

	private FormData viewFormData;

	protected String cbData;

	private Clipboard cb;

	private Menu menuPopUpParentTree;

	private Listener viewListener;

	private Listener browserListener;

	private String ticketsCostAlongLine;

	private String ticketsCostBehindGoal;

	private Arena arena;

	private Button arenaVisualButton;

	protected Shell browserShell;

	private Stand[] tempStand;
	
	private HashMap<Integer, Group> groupLocationMap = new HashMap<Integer, Group>();

	// public ViewArena(Composite parent, int style) {
	// super(parent, style);
	// }

	private void addViewComposite() {

		viewComposite = new Composite(composite, SWT.BORDER);
		viewComposite.setLayoutData(viewFormData);
		viewComposite.setVisible(true);

		arenaTextTable = new Text[8];
		arenaDays = new Label[8];
		arenaCombo = new Combo[8];
		arenaGroup = new Group[8];

		FormData formData;

		viewComposite.setLayout(new FormLayout());

		for (int i = 0; i < arenaGroup.length; i++) {

			arenaGroup[i] = new Group(viewComposite, SWT.NONE);
			arenaGroup[i].setToolTipText(""); //$NON-NLS-1$
			arenaCombo[i] = new Combo(arenaGroup[i], SWT.READ_ONLY);
			arenaCombo[i].setLocation(5, 10);
			arenaCombo[i].setSize(140, 30);
			arenaCombo[i].setItems(new String[] {
					Messages.getString("arena.standingPlaces"), //$NON-NLS-1$
					Messages.getString("arena.terraces"), //$NON-NLS-1$
					Messages.getString("arena.terracesUnderRoof"), //$NON-NLS-1$
					Messages.getString("arena.benches"), //$NON-NLS-1$
					Messages.getString("arena.benchesUnderRoof"), //$NON-NLS-1$
					Messages.getString("arena.seats"), //$NON-NLS-1$
					Messages.getString("arena.seatsUnderRoof") //$NON-NLS-1$
			});
			arenaCombo[i].setFont(ConfigBean.getFontMain());

			Listener resetStandingPlacesList = new Listener() {
				public void handleEvent(Event event) {
					if (((Combo) (event.widget)).getSelectionIndex() == 0) {
						((Text) ((Combo) (event.widget)).getParent().getChildren()[1]).setText("100"); //$NON-NLS-1$
					}
				}
			};

			arenaCombo[i].addListener(SWT.Selection, resetStandingPlacesList);

			arenaTextTable[i] = new Text(arenaGroup[i], SWT.CENTER | SWT.SINGLE);
			arenaTextTable[i].setLocation(35, 40);
			arenaTextTable[i].setSize(80, 15);
			arenaTextTable[i].setFont(ConfigBean.getFontMain());
			arenaTextTable[i].setTextLimit(5);

			arenaTextTable[i].addListener(SWT.Verify, new Listener() {
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

			arenaTextTable[i].addListener(SWT.MouseDown, readonlyList);

			arenaDays[i] = new Label(arenaGroup[i], SWT.CENTER | SWT.SINGLE);
			arenaDays[i].setLocation(15, 55);
			arenaDays[i].setSize(120, 15);
			arenaDays[i].setFont(ConfigBean.getFontMain());
		}

		/* ----------- ARENA NAME --------------------- */
		arenaName = new Label(viewComposite, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, 0);
		formData.height = 20;
		arenaName.setLayoutData(formData);

		/* ----------- LEFT TOP --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaName, 5);
		formData.left = new FormAttachment(0, 10);
		formData.height = 80;
		formData.width = 150;
		arenaGroup[0].setLayoutData(formData);

		/* ----------- CENTER TOP --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaName, 5);
		formData.left = new FormAttachment(arenaGroup[0], 10);
		formData.height = 80;
		formData.width = 150;
		arenaGroup[1].setLayoutData(formData);

		/* ----------- RIGHT TOP --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaName, 5);
		formData.left = new FormAttachment(arenaGroup[1], 10);
		formData.height = 80;
		formData.width = 150;
		arenaGroup[2].setLayoutData(formData);

		/* ----------- CENTER MIDDLE --------------------- */
		// arenaGroup[8] = new Group(arenaComposite, SWT.NONE);
		// arenaGroup[8].setSize(150, 90);
		// arenaGroup[8].setLocation(170, 80);
		formData = new FormData();
		formData.top = new FormAttachment(arenaGroup[1], 15);
		formData.left = new FormAttachment(arenaGroup[0], 15);
		formData.height = 100;
		formData.width = 140;

		Label arenaImageLabel = new Label(viewComposite, SWT.NONE);
		arenaImageLabel.setImage(ImageResources.getImageResources("poolview.jpg")); //$NON-NLS-1$
		arenaImageLabel.setLayoutData(formData);

		/* ----------- LEFT MIDDLE --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaImageLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(0, 10);
		formData.height = 80;
		formData.width = 150;
		arenaGroup[3].setLayoutData(formData);

		/* ----------- RIGHT MIDDLE --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaImageLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(arenaGroup[1], 10);
		formData.height = 80;
		formData.width = 150;
		arenaGroup[4].setLayoutData(formData);

		/* ----------- LEFT BOTTOM --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaImageLabel, 15);
		formData.left = new FormAttachment(0, 10);
		formData.height = 80;
		formData.width = 150;
		arenaGroup[5].setLayoutData(formData);

		/* ----------- CENTER BOTTOM --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaImageLabel, 15);
		formData.left = new FormAttachment(arenaGroup[3], 10);
		formData.height = 80;
		formData.width = 150;
		arenaGroup[6].setLayoutData(formData);

		/* ----------- RIGHT BOTTOM --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaImageLabel, 15);
		formData.left = new FormAttachment(arenaGroup[6], 10);
		formData.height = 80;
		formData.width = 150;
		arenaGroup[7].setLayoutData(formData);

		/* ----------- BUTTONS ------------------ */

		formData = new FormData();
		formData.top = new FormAttachment(arenaGroup[5], 10);
		formData.left = new FormAttachment(0, 10);
		formData.height = 30;
		formData.width = 80;

		arenaCountButton = new Button(viewComposite, SWT.NONE);
		arenaCountButton.setText(Messages.getString("button.count")); //$NON-NLS-1$
		arenaCountButton.setLayoutData(formData);
		arenaCountButton.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.top = new FormAttachment(arenaGroup[5], 10);
		formData.left = new FormAttachment(arenaCountButton, 10);
		formData.height = 30;
		formData.width = 80;

		arenaResetButton = new Button(viewComposite, SWT.NONE);
		arenaResetButton.setText(Messages.getString("button.reset")); //$NON-NLS-1$
		arenaResetButton.setLayoutData(formData);
		arenaResetButton.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.top = new FormAttachment(arenaGroup[5], 10);
		formData.left = new FormAttachment(arenaResetButton, 10);
		formData.height = 30;
		// formData.width = 80;

		arenaVisualButton = new Button(viewComposite, SWT.NONE);
		arenaVisualButton.setLayoutData(formData);
		arenaVisualButton.setFont(ConfigBean.getFontMain());
		arenaVisualButton.setText(Messages.getString("button.show")); //$NON-NLS-1$
		arenaVisualButton.pack();

		if (arenaVisualButton.getBounds().width < 80) {
			((FormData) arenaVisualButton.getLayoutData()).width = 80;
			// viewComposite.layout();
			// arenaVisualButton.setSize(80,30);
		}

		viewComposite.setVisible(true);
	}

	public void clear() {
		descriptionComposite.clearAll();
		for (int i = 0; i < arenaDays.length; i++) {
			arenaDays[i].setText(""); //$NON-NLS-1$
		}
	}

	private void fillArenaData(Stand[] tempStand) {
		arenaName.setText(arena.getAlArenaName().get(arena.getAlArenaName().size()-1).getArenaName());

		stand = new Stand[tempStand.length];

		for (int i = 0; i < stand.length; i++) {
			stand[i] = new Stand();
		}

		for (int i = 0; i < tempStand.length; i++) {
			switch (tempStand[i].getLocation()) {
			case Stand.NW:
				arenaCombo[0].setText(getNameOfSeatArena(tempStand[i]));
				arenaTextTable[0].setText(String.valueOf(tempStand[i].getSize()));
				if (tempStand[i].getConstructionDays() > 0) {
					arenaDays[0].setText(Messages.getString("arena.days") + tempStand[i].getConstructionDays()); //$NON-NLS-1$
				}
				stand[0] = tempStand[i];
				break;
			case Stand.N:
				arenaCombo[1].setText(getNameOfSeatArena(tempStand[i]));
				arenaTextTable[1].setText(String.valueOf(tempStand[i].getSize()));
				if (tempStand[i].getConstructionDays() > 0) {
					arenaDays[1].setText(Messages.getString("arena.days") + tempStand[i].getConstructionDays()); //$NON-NLS-1$
				}
				stand[1] = tempStand[i];
				break;
			case Stand.NE:
				arenaCombo[2].setText(getNameOfSeatArena(tempStand[i]));
				arenaTextTable[2].setText(String.valueOf(tempStand[i].getSize()));
				if (tempStand[i].getConstructionDays() > 0) {
					arenaDays[2].setText(Messages.getString("arena.days") + tempStand[i].getConstructionDays()); //$NON-NLS-1$
				}
				stand[2] = tempStand[i];
				break;
			case Stand.E:
				arenaCombo[4].setText(getNameOfSeatArena(tempStand[i]));
				arenaTextTable[4].setText(String.valueOf(tempStand[i].getSize()));
				if (tempStand[i].getConstructionDays() > 0) {
					arenaDays[4].setText(Messages.getString("arena.days") + tempStand[i].getConstructionDays()); //$NON-NLS-1$
				}
				stand[4] = tempStand[i];
				break;
			case Stand.W:
				arenaCombo[3].setText(getNameOfSeatArena(tempStand[i]));
				arenaTextTable[3].setText(String.valueOf(tempStand[i].getSize()));
				if (tempStand[i].getConstructionDays() > 0) {
					arenaDays[3].setText(Messages.getString("arena.days") + tempStand[i].getConstructionDays()); //$NON-NLS-1$
				}
				stand[3] = tempStand[i];
				break;
			case Stand.SW:
				arenaCombo[5].setText(getNameOfSeatArena(tempStand[i]));
				arenaTextTable[5].setText(String.valueOf(tempStand[i].getSize()));
				if (tempStand[i].getConstructionDays() > 0) {
					arenaDays[5].setText(Messages.getString("arena.days") + tempStand[i].getConstructionDays()); //$NON-NLS-1$
				}
				stand[5] = tempStand[i];
				break;
			case Stand.S:
				arenaCombo[6].setText(getNameOfSeatArena(tempStand[i]));
				arenaTextTable[6].setText(String.valueOf(tempStand[i].getSize()));
				if (tempStand[i].getConstructionDays() > 0) {
					arenaDays[6].setText(Messages.getString("arena.days") + tempStand[i].getConstructionDays()); //$NON-NLS-1$
				}
				stand[6] = tempStand[i];
				break;
			case Stand.SE:
				arenaCombo[7].setText(getNameOfSeatArena(tempStand[i]));
				arenaTextTable[7].setText(String.valueOf(tempStand[i].getSize()));
				if (tempStand[i].getConstructionDays() > 0) {
					arenaDays[7].setText(Messages.getString("arena.days") + tempStand[i].getConstructionDays()); //$NON-NLS-1$
				}
				stand[7] = tempStand[i];
				break;
			default:
			}
		}
	}

	private String getArenaCapacity(Stand[] tempStand) {
		int arenaCapacity = 0;
		for (int i = 0; i < tempStand.length; i++) {
			arenaCapacity += tempStand[i].getSize();
		}
		return SVNumberFormat.formatInteger(arenaCapacity) + "   "; //$NON-NLS-1$
	}

	private int[] getArenaPlacesCapacity(Stand[] tempStand) {
		int[] arenaPlacesCapacity = new int[7];

		for (int i = 0; i < arenaPlacesCapacity.length; i++) {
			arenaPlacesCapacity[i] = 0;
		}

		for (int i = 0; i < tempStand.length; i++) {
			switch (tempStand[i].getType()) {
			case 0:
				arenaPlacesCapacity[0] += tempStand[i].getSize();
				break;
			case 1:
				if (tempStand[i].getIsRoof() == Stand.ROOF_FALSE) {
					arenaPlacesCapacity[1] += tempStand[i].getSize();
				} else {
					arenaPlacesCapacity[2] += tempStand[i].getSize();
				}
				break;
			case 2:
				if (tempStand[i].getIsRoof() == Stand.ROOF_FALSE) {
					arenaPlacesCapacity[3] += tempStand[i].getSize();
				} else {
					arenaPlacesCapacity[4] += tempStand[i].getSize();
				}
				break;
			case 3:
				if (tempStand[i].getIsRoof() == Stand.ROOF_FALSE) {
					arenaPlacesCapacity[5] += tempStand[i].getSize();
				} else {
					arenaPlacesCapacity[6] += tempStand[i].getSize();
				}
				break;
			}
		}
		return arenaPlacesCapacity;
	}

	private String getArenaCost(Stand[] tempStand) {
		double arenaCost = 0;
		double arenaCostOfPlace = 3;

		for (int i = 0; i < tempStand.length; i++) {
			if (tempStand[i].getType() > 0) {
				arenaCost += (tempStand[i].getSize() * arenaCostOfPlace);
				groupLocationMap.get(tempStand[i].getLocation()).setToolTipText(String.format("%s%s = %s\r\n", groupLocationMap.get(tempStand[i].getLocation()).getToolTipText(), Messages.getString("arena.payments"), Money.formatDoubleCurrencySymbol(tempStand[i].getSize() * arenaCostOfPlace))); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				groupLocationMap.get(tempStand[i].getLocation()).setToolTipText(String.format("%s%s = %s\r\n", groupLocationMap.get(tempStand[i].getLocation()).getToolTipText(), Messages.getString("arena.payments"), Money.formatDoubleCurrencySymbol(0.0))); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		return Money.formatDoubleCurrencySymbol(arenaCost);
	}

	private String getArenaCostOfRebuild(Stand[] mainStand, Stand[] tempStand) {
		StringBuffer all = new StringBuffer(""); //$NON-NLS-1$
		// int terraces = 100;
		int fastening = 100;
		int dissasemblyFastening = 10;
		int dissasemblyBenches = 10;
		int dissasemblySeats = 10;
		int dissasemblyRoof = 20;
		int benches = 50;
		int seats = 180;
		int roof = 400;
		int fasteningCost = 0;
		int dissasemblyFasteningCost = 0;
		int dissasemblyBenchesCost = 0;
		int dissasemblySeatsCost = 0;
		int dissasemblyRoofCost = 0;
		int dissasemblyTerracesCost = 0;
		int benchesCost = 0;
		int seatsCost = 0;
		int roofCost = 0;
		int allCosts = 0;
		int projectCost = 0; // 100000
		int incdec = 0;
		int terracesCost = 0;

		for (int i = 0; i < mainStand.length; i++) {
			if (mainStand[i].getSize() != tempStand[i].getSize() || mainStand[i].getType() != tempStand[i].getType() || mainStand[i].getIsRoof() != tempStand[i].getIsRoof()) {

				// koszt projektu
				projectCost += 100000;

				if (mainStand[i].getType() == Stand.TYPE_STANDING) {
					incdec = tempStand[i].getSize();
					terracesCost += getCostOfTerracesRebuild(0, tempStand[i].getSize());
				} else if (tempStand[i].getType() == Stand.TYPE_STANDING) {
					incdec = tempStand[i].getSize();

					dissasemblyTerracesCost += -1 * new BigDecimal(getCostOfTerracesRebuild(mainStand[i].getSize(), 0) / 5.0).setScale(0, BigDecimal.ROUND_UP).intValue();
				} else {
					incdec = tempStand[i].getSize() - mainStand[i].getSize();
					// rebuild terraces
					if (incdec > 0) {
						terracesCost += getCostOfTerracesRebuild(mainStand[i].getSize(), tempStand[i].getSize());
					} else if (incdec < 0) {
						dissasemblyTerracesCost += new BigDecimal(getCostOfTerracesRebuild(tempStand[i].getSize(), mainStand[i].getSize()) / 5.0).setScale(0, BigDecimal.ROUND_UP).intValue();
					}
				}

				// rebuild roof
				if (tempStand[i].getIsRoof() == Stand.ROOF_TRUE && mainStand[i].getIsRoof() == Stand.ROOF_FALSE) {
					roofCost += tempStand[i].getSize() * roof;
				} else if (tempStand[i].getIsRoof() == Stand.ROOF_FALSE && mainStand[i].getIsRoof() == Stand.ROOF_TRUE) {
					dissasemblyRoofCost += mainStand[i].getSize() * dissasemblyRoof;
				} else if (tempStand[i].getIsRoof() == Stand.ROOF_TRUE && mainStand[i].getIsRoof() == Stand.ROOF_TRUE) {
					if (incdec > 0) {
						roofCost += incdec * roof;
					} else if (incdec < 0) {
						roofCost += incdec * dissasemblyRoof;
					}
				}

				// rebuild stadion if the type is same
				if (mainStand[i].getType() == tempStand[i].getType()) {

					// if (mainArena[i].getType() == 2 && mainArena[i].getIsRoof() == 0) {
					if (mainStand[i].getType() == Stand.TYPE_BENCHES) {
						if (incdec > 0) {
							fasteningCost += incdec * fastening;
							benchesCost += incdec * benches;
						} else if (incdec < 0) {
							dissasemblyBenchesCost += -1 * incdec * dissasemblyBenches;
							dissasemblyFasteningCost += -1 * incdec * dissasemblyFastening;
						}
					}

					// if (mainArena[i].getType() == 3 && mainArena[i].getIsRoof() == 0) {
					if (mainStand[i].getType() == Stand.TYPE_SEATS) {
						if (incdec > 0) {
							fasteningCost += incdec * fastening;
							seatsCost += incdec * seats;
						} else if (incdec < 0) {
							dissasemblyFasteningCost += -1 * incdec * dissasemblyFastening;
							dissasemblySeatsCost += -1 * incdec * dissasemblySeats;
						}
					}
				}

				// rebuild stadion if the type is diffrent
				if (mainStand[i].getType() != tempStand[i].getType()) {

					if (mainStand[i].getType() == Stand.TYPE_BENCHES && tempStand[i].getType() == Stand.TYPE_SEATS) {
						dissasemblyBenchesCost += mainStand[i].getSize() * dissasemblyBenches;
						seatsCost += seats * tempStand[i].getSize();
						if (incdec > 0) {
							fasteningCost += incdec * fastening;
						} else if (incdec < 0) {
							dissasemblyFasteningCost += -1 * incdec * dissasemblyFastening;
						}
					}

					if (mainStand[i].getType() == Stand.TYPE_SEATS && tempStand[i].getType() == Stand.TYPE_BENCHES) {
						dissasemblySeatsCost += mainStand[i].getSize() * dissasemblySeats;
						benchesCost += benches * tempStand[i].getSize();
						if (incdec > 0) {
							fasteningCost += incdec * fastening;
						} else if (incdec < 0) {
							dissasemblyFasteningCost += -1 * incdec * dissasemblyFastening;
						}
					}

					if ((mainStand[i].getType() == Stand.TYPE_TERRACES || mainStand[i].getType() == Stand.TYPE_STANDING) && tempStand[i].getType() == Stand.TYPE_BENCHES) {
						benchesCost += benches * tempStand[i].getSize();
						fasteningCost += tempStand[i].getSize() * fastening;
					}

					if ((mainStand[i].getType() == Stand.TYPE_TERRACES || mainStand[i].getType() == Stand.TYPE_STANDING) && tempStand[i].getType() == Stand.TYPE_SEATS) {
						seatsCost += seats * tempStand[i].getSize();
						fasteningCost += tempStand[i].getSize() * fastening;
					}

					if (mainStand[i].getType() == Stand.TYPE_SEATS && (tempStand[i].getType() == Stand.TYPE_TERRACES || tempStand[i].getType() == Stand.TYPE_STANDING)) {
						dissasemblySeatsCost += dissasemblySeats * tempStand[i].getSize();
						dissasemblyFasteningCost += tempStand[i].getSize() * dissasemblyFastening;
					}

					if (mainStand[i].getType() == Stand.TYPE_BENCHES && (tempStand[i].getType() == Stand.TYPE_TERRACES || tempStand[i].getType() == Stand.TYPE_STANDING)) {
						dissasemblyBenchesCost += dissasemblyBenches * tempStand[i].getSize();
						dissasemblyFasteningCost += tempStand[i].getSize() * dissasemblyFastening;
					}

				}

			}
		}

		allCosts = terracesCost + dissasemblyTerracesCost + projectCost + roofCost + dissasemblyRoofCost + fasteningCost + dissasemblyFasteningCost + benchesCost + dissasemblyBenchesCost + seatsCost + dissasemblySeatsCost;

		String stringFormat = "%-30s %15s\r\n"; //$NON-NLS-1$
		String[][] values;
		values = new String[1][2];

		if (projectCost > 0) {
			values[0][0] = Messages.getString("arena.projectCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(projectCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (terracesCost > 0) {
			values[0][0] = Messages.getString("arena.terracesCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(terracesCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (dissasemblyTerracesCost > 0) {
			values[0][0] = Messages.getString("arena.dissasemblyTerracesCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(dissasemblyTerracesCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (fasteningCost > 0) {
			values[0][0] = Messages.getString("arena.fasteningCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(fasteningCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (dissasemblyFasteningCost > 0) {
			values[0][0] = Messages.getString("arena.dissasemblyFasteningCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(dissasemblyFasteningCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (benchesCost > 0) {
			values[0][0] = Messages.getString("arena.benchesCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(benchesCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (dissasemblyBenchesCost > 0) {
			values[0][0] = Messages.getString("arena.dissasemblyBenchesCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(dissasemblyBenchesCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (seatsCost > 0) {
			values[0][0] = Messages.getString("arena.seatsCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(seatsCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (dissasemblySeatsCost > 0) {
			values[0][0] = Messages.getString("arena.dissasemblySeatsCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(dissasemblySeatsCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (roofCost > 0) {
			values[0][0] = Messages.getString("arena.roofCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(roofCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (dissasemblyRoofCost > 0) {
			values[0][0] = Messages.getString("arena.dissasemblyRoofCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(dissasemblyRoofCost);
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		values[0][0] = Messages.getString("arena.all"); //$NON-NLS-1$
		values[0][1] = Money.formatDoubleCurrencySymbol(allCosts);
		all.append(String.format(stringFormat, (Object[]) values[0]));
		return all.toString();
	}

	private String getArenaEarns(Stand[] stand) {

		double arenaEarns = 0;
		double arenaEarn = 0;
		for (int i = 0; i < stand.length; i++) {
			arenaEarn = 0;
			if (stand[i].getIsRoof() == Stand.ROOF_TRUE) {
				switch (stand[i].getType()) {
				case Stand.TYPE_TERRACES:
					arenaEarn += stand[i].getSize() * 20;
					break;
				case Stand.TYPE_BENCHES:
					if (stand[i].getLocation() == Stand.N || stand[i].getLocation() == Stand.S) {
						arenaEarn += stand[i].getSize() * 30;
					} else {
						arenaEarn += stand[i].getSize() * 25;
					}
					break;
				case Stand.TYPE_SEATS:
					if (stand[i].getLocation() == Stand.N || stand[i].getLocation() == Stand.S) {
						arenaEarn += stand[i].getSize() * 35;
					} else {
						arenaEarn += stand[i].getSize() * 30;
					}
					break;
				}
			} else {
				switch (stand[i].getType()) {
				case Stand.TYPE_STANDING:
					arenaEarn += stand[i].getSize() * 8;
					break;
				case Stand.TYPE_TERRACES:
					arenaEarn += stand[i].getSize() * 16;
					break;
				case Stand.TYPE_BENCHES:
					arenaEarn += stand[i].getSize() * 25;
					break;
				case Stand.TYPE_SEATS:
					if (stand[i].getLocation() == Stand.N || stand[i].getLocation() == Stand.S) {
						arenaEarn += stand[i].getSize() * 30;
					} else {
						arenaEarn += stand[i].getSize() * 25;
					}
					break;
				}
			}
			groupLocationMap.get(stand[i].getLocation()).setToolTipText(String.format("%s%s = %s\r\n", groupLocationMap.get(stand[i].getLocation()).getToolTipText(), Messages.getString("arena.maxEarns"), Money.formatDoubleCurrencySymbol(arenaEarn))); //$NON-NLS-1$ //$NON-NLS-2$
			arenaEarns += arenaEarn;
		}
		return Money.formatDoubleCurrencySymbol(arenaEarns);
	}

	private Stand[] getStands() {
		tempStand = new Stand[8];
		for (int i = 0; i < tempStand.length; i++) {
			tempStand[i] = new Stand();
		}

		tempStand[0].setLocation(Stand.NW);
		tempStand[1].setLocation(Stand.N);
		tempStand[2].setLocation(Stand.NE);
		tempStand[3].setLocation(Stand.W);
		tempStand[4].setLocation(Stand.E);
		tempStand[5].setLocation(Stand.SW);
		tempStand[6].setLocation(Stand.S);
		tempStand[7].setLocation(Stand.SE);

		for (int i = 0; i < tempStand.length; i++) {

			switch (arenaCombo[i].getSelectionIndex()) {
			case 0:
				tempStand[i].setType(Stand.TYPE_STANDING);
				tempStand[i].setIsRoof(Stand.ROOF_FALSE);
				break;
			case 1:
				tempStand[i].setType(Stand.TYPE_TERRACES);
				tempStand[i].setIsRoof(Stand.ROOF_FALSE);
				break;
			case 2:
				tempStand[i].setType(Stand.TYPE_TERRACES);
				tempStand[i].setIsRoof(Stand.ROOF_TRUE);
				break;
			case 3:
				tempStand[i].setType(Stand.TYPE_BENCHES);
				tempStand[i].setIsRoof(Stand.ROOF_FALSE);
				break;
			case 4:
				tempStand[i].setType(Stand.TYPE_BENCHES);
				tempStand[i].setIsRoof(Stand.ROOF_TRUE);
				break;
			case 5:
				tempStand[i].setType(Stand.TYPE_SEATS);
				tempStand[i].setIsRoof(Stand.ROOF_FALSE);
				break;
			case 6:
				tempStand[i].setType(Stand.TYPE_SEATS);
				tempStand[i].setIsRoof(Stand.ROOF_TRUE);
				break;

			}

			if(arenaTextTable[i].getText().matches("[0-9]+")) { //$NON-NLS-1$
				tempStand[i].setSize(Integer.valueOf(arenaTextTable[i].getText()).intValue());
			} else {
				tempStand[i].setSize(0);
			}

		}
			return tempStand;
	}

	private void getCostOfArenaChanges() {
		// pobieramy dane do rozbudowy stadionu
		tempStand = getStands();

		for (int i = 0; i < tempStand.length; i++) {
			arenaGroup[i].setToolTipText(""); //$NON-NLS-1$
			if (arenaTextTable[i].getText().equals("")) { //$NON-NLS-1$
				arenaTextTable[i].setText("0"); //$NON-NLS-1$
			}
		}

		setDescription(tempStand);

		String[][] values;

		values = new String[1][2];
		// koszty rozbudowy stadionu
		values[0][0] = Messages.getString("arena.buildCost"); //$NON-NLS-1$

		values[0][1] = "\r\n" + getArenaCostOfRebuild(stand, tempStand); //$NON-NLS-1$

		for (int i = 0; i < values.length; i++) {
			if (i == 0) {
				descriptionComposite.setRightText(values[i]);
			} else {
				descriptionComposite.addRightText(values[i]);
			}
		}
	}

	private int getCostOfTerracesRebuild(int before, int after) {
		int terraces = 100;
		return (int) ((terraces * after + 0.005 * after * after) - (terraces * before + 0.005 * before * before));
	}

	private String getNameOfSeatArena(Stand tempStand) {
		String arenaNameOfSeat = ""; //$NON-NLS-1$

		if (tempStand.getIsRoof() == Stand.ROOF_TRUE) {
			switch (tempStand.getType()) {
			case Stand.TYPE_TERRACES:
				arenaNameOfSeat = Messages.getString("arena.terracesUnderRoof"); //$NON-NLS-1$
				break;
			case Stand.TYPE_BENCHES:
				arenaNameOfSeat = Messages.getString("arena.benchesUnderRoof"); //$NON-NLS-1$
				break;
			case Stand.TYPE_SEATS:
				arenaNameOfSeat = Messages.getString("arena.seatsUnderRoof"); //$NON-NLS-1$
				break;
			}
		} else {
			switch (tempStand.getType()) {
			case Stand.TYPE_STANDING:
				arenaNameOfSeat = Messages.getString("arena.standingPlaces"); //$NON-NLS-1$
				break;
			case Stand.TYPE_TERRACES:
				arenaNameOfSeat = Messages.getString("arena.terraces"); //$NON-NLS-1$
				break;
			case Stand.TYPE_BENCHES:
				arenaNameOfSeat = Messages.getString("arena.benches"); //$NON-NLS-1$
				break;
			case Stand.TYPE_SEATS:
				arenaNameOfSeat = Messages.getString("arena.seats"); //$NON-NLS-1$
				break;
			}
		}
		return arenaNameOfSeat;
	}

	private void setDataComposite(Arena arena, final Stand[] stand) {
		arenaName.setText(arena.getAlArenaName().get(arena.getAlArenaName().size() - 1).getArenaName());
		fillArenaData(stand);

		// oblicz nowe wartosci dla stadionu po rozbudowie
		Listener countEarns = new Listener() {
			public void handleEvent(Event event) {

				getCostOfArenaChanges();
			}
		};

		arenaCountButton.addListener(SWT.Selection, countEarns);

		// przycisk do przywrocenia danych dla stadionu
		Listener resetEarns = new Listener() {
			public void handleEvent(Event event) {

				for (int i = 0; i < arenaGroup.length; i++) {
					arenaGroup[i].setToolTipText(""); //$NON-NLS-1$
				}

				setDescription(stand);

				// przywracamy dane dla miejsc na stadionie
				fillArenaData(stand);

				tempStand = null;
			}
		};
		arenaResetButton.addListener(SWT.Selection, resetEarns);

	}

	/**
	 * @param club
	 */
	private void setDescription(Stand[] stand) {

		String[][] values;

		int[] placesCapacity;

		for (int i = 0; i < arenaGroup.length; i++) {
			arenaGroup[i].setToolTipText(""); //$NON-NLS-1$
		}

		descriptionComposite.setRightText(""); //$NON-NLS-1$
		descriptionComposite.setLeftText(""); //$NON-NLS-1$

		values = new String[11][2];
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

		values[0][1] = getArenaCost(stand);
		values[1][1] = getArenaEarns(stand);
		values[2][1] = getArenaCapacity(stand);
		placesCapacity = getArenaPlacesCapacity(stand);
		values[3][1] = ""; //$NON-NLS-1$
		values[4][1] = SVNumberFormat.formatInteger(placesCapacity[0]) + "   "; //$NON-NLS-1$
		values[5][1] = SVNumberFormat.formatInteger(placesCapacity[1]) + "   "; //$NON-NLS-1$
		values[6][1] = SVNumberFormat.formatInteger(placesCapacity[2]) + "   "; //$NON-NLS-1$
		values[7][1] = SVNumberFormat.formatInteger(placesCapacity[3]) + "   "; //$NON-NLS-1$
		values[8][1] = SVNumberFormat.formatInteger(placesCapacity[4]) + "   "; //$NON-NLS-1$
		values[9][1] = SVNumberFormat.formatInteger(placesCapacity[5]) + "   "; //$NON-NLS-1$
		values[10][1] = SVNumberFormat.formatInteger(placesCapacity[6]) + "   "; //$NON-NLS-1$

		for (int i = 0; i < values.length; i++) {
			if (i == 0) {
				descriptionComposite.setLeftText(values[i]);
			} else {
				descriptionComposite.addLeftText(values[i]);
			}
		}

		values = new String[1][2];
		// koszty rozbudowy stadionu
		values[0][0] = Messages.getString("arena.buildCost"); //$NON-NLS-1$

		values[0][1] = "".toString(); //$NON-NLS-1$

		for (int i = 0; i < values.length; i++) {
			if (i == 0) {
				descriptionComposite.setRightText(values[i]);
			} else {
				descriptionComposite.addRightText(values[i]);
			}
		}

		for (int i = 0; i < arenaGroup.length; i++) {
			if (i == 1 || i == 6) {
				arenaCombo[i].setToolTipText(arenaGroup[i].getToolTipText() + ticketsCostAlongLine);
				arenaTextTable[i].setToolTipText(arenaGroup[i].getToolTipText() + ticketsCostAlongLine);
				arenaGroup[i].setToolTipText(arenaGroup[i].getToolTipText() + ticketsCostAlongLine);
			} else {
				arenaCombo[i].setToolTipText(arenaGroup[i].getToolTipText() + ticketsCostBehindGoal);
				arenaTextTable[i].setToolTipText(arenaGroup[i].getToolTipText() + ticketsCostBehindGoal);
				arenaGroup[i].setToolTipText(arenaGroup[i].getToolTipText() + ticketsCostBehindGoal);
			}
		}
	}

	public void set() {
		arena = Cache.getClub().getArena();
		
		stand = arena.getStands().toArray(new Stand[arena.getStands().size()]);
		
		setTicketsCost();

		setDescription(stand);
		setDataComposite(arena, stand);
		_treeItem.getParent().addListener(SWT.MouseDown, viewListener);

		arenaVisualButton.addListener(SWT.Selection, browserListener);

	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public void init(Composite composite) {
		
		this.composite = new ViewComposite(composite.getParent(), composite.getStyle());
		composite.dispose();
		
		viewFormData = ((ViewComposite) this.composite).getViewFormData();
		descriptionFormData = ((ViewComposite) this.composite).getDescriptionFormData();

		cb = ViewerHandler.getClipboard();
		addViewComposite();
		addDescriptionComposite();
		addPopupMenuParentTree();
		addListeners();
		setGroupLocationMap();
	}
	
	private void setGroupLocationMap() {
		groupLocationMap.put(Stand.NW, arenaGroup[0]);
		groupLocationMap.put(Stand.N, arenaGroup[1]);
		groupLocationMap.put(Stand.NE, arenaGroup[2]);
		groupLocationMap.put(Stand.W, arenaGroup[3]);
		groupLocationMap.put(Stand.E, arenaGroup[4]);
		groupLocationMap.put(Stand.SW, arenaGroup[5]);
		groupLocationMap.put(Stand.S, arenaGroup[6]);
		groupLocationMap.put(Stand.SE, arenaGroup[7]);
	}

	private void setTicketsCost() {
		ticketsCostAlongLine = String.format("\r\n\r\n%s\r\n",Messages.getString("arena.cost.tickets")); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.standingPlaces"), Money.formatDoubleCurrencySymbol(8.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.terraces"), Money.formatDoubleCurrencySymbol(16.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.terracesUnderRoof"), Money.formatDoubleCurrencySymbol(20.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.benches"), Money.formatDoubleCurrencySymbol(25.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.benchesUnderRoof"), Money.formatDoubleCurrencySymbol(30.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.seats"), Money.formatDoubleCurrencySymbol(30.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s", Messages.getString("arena.seatsUnderRoof"), Money.formatDoubleCurrencySymbol(35.0)); //$NON-NLS-1$ //$NON-NLS-2$

		ticketsCostBehindGoal = String.format("\r\n\r\n%s = \r\n", Messages.getString("arena.cost.tickets")); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.standingPlaces"), Money.formatDoubleCurrencySymbol(8.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.terraces"), Money.formatDoubleCurrencySymbol(16.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.terracesUnderRoof"), Money.formatDoubleCurrencySymbol(20.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.benches"), Money.formatDoubleCurrencySymbol(25.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.benchesUnderRoof") ,Money.formatDoubleCurrencySymbol(25.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.seats"), Money.formatDoubleCurrencySymbol(25.0)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s", Messages.getString("arena.seatsUnderRoof"),Money.formatDoubleCurrencySymbol(30.0)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void addDescriptionComposite() {
		descriptionComposite = new DescriptionDoubleComposite(composite, SWT.BORDER);
		descriptionComposite.setLayoutData(descriptionFormData);
		descriptionComposite.setVisible(true);

		descriptionComposite.setLeftDescriptionStringFormat("%-30s %15s\r\n"); //$NON-NLS-1$
		descriptionComposite.setLeftFirstColumnSize(30);
		descriptionComposite.setLeftSecondColumnSize(15);

		descriptionComposite.setRightDescriptionStringFormat("%-30s %15s\r\n"); //$NON-NLS-1$
		descriptionComposite.setRightFirstColumnSize(30);
		descriptionComposite.setRightSecondColumnSize(15);

		descriptionComposite.setFont(ConfigBean.getFontDescription());
	}

	public void setTreeItem(TreeItem treeItem) {

		this._treeItem = treeItem;

		_treeItem.setText(Messages.getString("tree.ViewArena")); //$NON-NLS-1$

		_treeItem.setImage(ImageResources.getImageResources("poolview_ico.png")); //$NON-NLS-1$

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);
				if (item != null) {
					if (item.equals(_treeItem)) {
						ViewerHandler.getViewer().setDefaultButton(arenaCountButton);
					}
				}
			}
		});
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoArena"); //$NON-NLS-1$
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		// this.confProperties = confProperties;
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	public Composite getComposite() {
		return composite;
	}

	private void addListeners() {

		browserListener = new Listener() {

			public void handleEvent(Event arg0) {
				if (browserShell == null || browserShell.isDisposed()) {
					browserShell = new Shell(composite.getShell(), SWT.CLOSE | SWT.RESIZE);
					browserShell.setLayout(new FillLayout());
				}
				browserShell.setText(arena.getAlArenaName().get(arena.getAlArenaName().size()-1).getArenaName());
				Browser browser = null;
				try {
					browser = new Browser(browserShell, SWT.NONE);
				} catch (SWTError e) {
					/*
					 * The Browser widget throws an SWTError if it fails to instantiate
					 * properly. Application code should catch this SWTError and disable
					 * any feature requiring the Browser widget. Platform requirements for
					 * the SWT Browser widget are available from the SWT FAQ website.
					 */
				}
				if (browser != null) {
					/* The Browser widget can be used */
					//
					StringBuffer request = new StringBuffer(""); //$NON-NLS-1$
					Stand[] standRequest = getStands();

					for(int i = 0 ; i < standRequest.length; i++) {
						request.append("cap"); //$NON-NLS-1$
						request.append(standRequest[i].getLocation());
						request.append("="); //$NON-NLS-1$
						request.append(standRequest[i].getSize());
						request.append("&type"); //$NON-NLS-1$
						request.append(standRequest[i].getLocation());
						request.append("="); //$NON-NLS-1$
						request.append(standRequest[i].getType());
						request.append("&roof"); //$NON-NLS-1$
						request.append(standRequest[i].getLocation());
						request.append("="); //$NON-NLS-1$
						request.append(standRequest[i].getIsRoof());
						request.append("&"); //$NON-NLS-1$
					}
					browser.setUrl("http://files.sokker.org/pic/stadioVIII_10.swf?" + request.toString()); //$NON-NLS-1$
//					browser.setUrl("file:///" + SokkerBean.getBaseDir() + File.separator + "resources" + File.separator +  "stadioVIII_10.swf?cap4=2000&type4=2&roof4=0&cap3=500&type3=1&roof3=0&cap1=2000&type1=2&roof1=0&cap6=2500&type6=2&roof6=0&cap2=100&type2=0&roof2=0&cap5=100&type5=0&roof5=0&cap7=100&type7=0&roof7=0&cap8=100&type8=0&roof8=0&");
				}
				browserShell.open();

			}

		};

		viewListener = new Listener() {
			public void handleEvent(Event event) {

				Point point = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(point);

				if (item != null && item.equals(_treeItem)) {
					if (item.equals(_treeItem) && event.button == 3) {

						cbData = descriptionComposite.getLeftText();
						cbData += "=============================="; //$NON-NLS-1$
						cbData += descriptionComposite.getRightText();

						_treeItem.getParent().setMenu(menuPopUpParentTree);
						_treeItem.getParent().getMenu().setVisible(true);
					}
				}
			}
		};
	}

	private void addPopupMenuParentTree() {
		// added popup menu
		menuPopUpParentTree = new Menu(composite.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menuPopUpParentTree, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.clipboard")); //$NON-NLS-1$
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[] {
					cbData
				}, new Transfer[] {
					textTransfer
				});
			}
		});
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
