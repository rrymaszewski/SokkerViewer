package pl.pronux.sokker.ui.plugins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Arena;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.Stand;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.ArenaDescriptionComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.groups.ArenaStandGroup;

public class ViewArena implements IPlugin {

	private Button arenaCountButton;

	private Label arenaName;

	private Button arenaResetButton;

	private Composite viewComposite;

	private ArenaDescriptionComposite descriptionComposite;

	private TreeItem _treeItem;

	private Composite composite;

	private FormData descriptionFormData;

	private FormData viewFormData;

	private String cbData;

	private Clipboard cb;

	private Menu menuPopUpParentTree;

	private Listener viewListener;

	private Listener browserListener;

	private Arena arena;

	private Button arenaVisualButton;

	private Shell browserShell;

	private Map<Integer, ArenaStandGroup> groupMap = new HashMap<Integer, ArenaStandGroup>();

	public static String ticketsCostAlongLine;

	public static String ticketsCostBehindGoal;

	private void addViewComposite() {

		viewComposite = new Composite(composite, SWT.BORDER);
		viewComposite.setLayoutData(viewFormData);
		viewComposite.setVisible(true);

		FormData formData;
		ArenaStandGroup arenaStandGroup;
		viewComposite.setLayout(new FormLayout());

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
		arenaStandGroup = new ArenaStandGroup(viewComposite, SWT.NONE);
		arenaStandGroup.setLayoutData(formData);
		groupMap.put(Stand.NW, arenaStandGroup);

		/* ----------- CENTER TOP --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaName, 5);
		formData.left = new FormAttachment(groupMap.get(Stand.NW), 10);
		formData.height = 80;
		formData.width = 150;
		arenaStandGroup = new ArenaStandGroup(viewComposite, SWT.NONE);
		arenaStandGroup.setLayoutData(formData);
		groupMap.put(Stand.N, arenaStandGroup);

		/* ----------- RIGHT TOP --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaName, 5);
		formData.left = new FormAttachment(groupMap.get(Stand.N), 10);
		formData.height = 80;
		formData.width = 150;
		arenaStandGroup = new ArenaStandGroup(viewComposite, SWT.NONE);
		arenaStandGroup.setLayoutData(formData);
		groupMap.put(Stand.NE, arenaStandGroup);

		/* ----------- CENTER MIDDLE --------------------- */
		// arenaGroup[8] = new Group(arenaComposite, SWT.NONE);
		// arenaGroup[8].setSize(150, 90);
		// arenaGroup[8].setLocation(170, 80);
		formData = new FormData();
		formData.top = new FormAttachment(groupMap.get(Stand.N), 15);
		formData.left = new FormAttachment(groupMap.get(Stand.NW), 15);
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
		arenaStandGroup = new ArenaStandGroup(viewComposite, SWT.NONE);
		arenaStandGroup.setLayoutData(formData);
		groupMap.put(Stand.W, arenaStandGroup);

		/* ----------- RIGHT MIDDLE --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaImageLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(groupMap.get(Stand.N), 10);
		formData.height = 80;
		formData.width = 150;
		arenaStandGroup = new ArenaStandGroup(viewComposite, SWT.NONE);
		arenaStandGroup.setLayoutData(formData);
		groupMap.put(Stand.E, arenaStandGroup);

		/* ----------- LEFT BOTTOM --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaImageLabel, 15);
		formData.left = new FormAttachment(0, 10);
		formData.height = 80;
		formData.width = 150;
		arenaStandGroup = new ArenaStandGroup(viewComposite, SWT.NONE);
		arenaStandGroup.setLayoutData(formData);
		groupMap.put(Stand.SW, arenaStandGroup);

		/* ----------- CENTER BOTTOM --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaImageLabel, 15);
		formData.left = new FormAttachment(groupMap.get(Stand.SW), 10);
		formData.height = 80;
		formData.width = 150;
		arenaStandGroup = new ArenaStandGroup(viewComposite, SWT.NONE);
		arenaStandGroup.setLayoutData(formData);
		groupMap.put(Stand.S, arenaStandGroup);

		/* ----------- RIGHT BOTTOM --------------------- */
		formData = new FormData();
		formData.top = new FormAttachment(arenaImageLabel, 15);
		formData.left = new FormAttachment(groupMap.get(Stand.S), 10);
		formData.height = 80;
		formData.width = 150;
		arenaStandGroup = new ArenaStandGroup(viewComposite, SWT.NONE);
		arenaStandGroup.setLayoutData(formData);
		groupMap.put(Stand.SE, arenaStandGroup);

		/* ----------- BUTTONS ------------------ */

		formData = new FormData();
		formData.top = new FormAttachment(groupMap.get(Stand.SW), 10);
		formData.left = new FormAttachment(0, 10);
		formData.height = 30;
		formData.width = 80;

		arenaCountButton = new Button(viewComposite, SWT.NONE);
		arenaCountButton.setText(Messages.getString("button.count")); //$NON-NLS-1$
		arenaCountButton.setLayoutData(formData);
		arenaCountButton.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.top = new FormAttachment(groupMap.get(Stand.SW), 10);
		formData.left = new FormAttachment(arenaCountButton, 10);
		formData.height = 30;
		formData.width = 80;

		arenaResetButton = new Button(viewComposite, SWT.NONE);
		arenaResetButton.setText(Messages.getString("button.reset")); //$NON-NLS-1$
		arenaResetButton.setLayoutData(formData);
		arenaResetButton.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.top = new FormAttachment(groupMap.get(Stand.SW), 10);
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
		descriptionComposite.clear();
		Set<Integer> keys = groupMap.keySet();
		for (Integer key : keys) {
			groupMap.get(key).setDays(""); //$NON-NLS-1$
			groupMap.get(key).setToolTipText(""); //$NON-NLS-1$
		}
	}

	private void fillArenaData(Arena arena) {
		arenaName.setText(arena.getArenaNames().get(arena.getArenaNames().size() - 1).getArenaName());
		Collection<Stand> stands = arena.getStandsMap().values();
		for (Stand stand : stands) {
			groupMap.get(stand.getLocation()).setStand(stand);
		}
	}

	private Map<Integer, Stand> getStands() {
		Map<Integer, Stand> standsMap = new HashMap<Integer, Stand>();
		Stand tempStand;
		tempStand = new Stand();
		tempStand.setLocation(Stand.NW);
		standsMap.put(tempStand.getLocation(), tempStand);
		tempStand = new Stand();
		tempStand.setLocation(Stand.N);
		standsMap.put(tempStand.getLocation(), tempStand);
		tempStand = new Stand();
		tempStand.setLocation(Stand.NE);
		standsMap.put(tempStand.getLocation(), tempStand);
		tempStand = new Stand();
		tempStand.setLocation(Stand.W);
		standsMap.put(tempStand.getLocation(), tempStand);
		tempStand = new Stand();
		tempStand.setLocation(Stand.E);
		standsMap.put(tempStand.getLocation(), tempStand);
		tempStand = new Stand();
		tempStand.setLocation(Stand.SW);
		standsMap.put(tempStand.getLocation(), tempStand);
		tempStand = new Stand();
		tempStand.setLocation(Stand.S);
		standsMap.put(tempStand.getLocation(), tempStand);
		tempStand = new Stand();
		tempStand.setLocation(Stand.SE);
		standsMap.put(tempStand.getLocation(), tempStand);

		Collection<Stand> stands = standsMap.values();
		for (Stand stand : stands) {

			switch (groupMap.get(stand.getLocation()).getSelectionIndex()) {
				case 0:
					stand.setType(Stand.TYPE_STANDING);
					stand.setIsRoof(Stand.ROOF_FALSE);
					break;
				case 1:
					stand.setType(Stand.TYPE_TERRACES);
					stand.setIsRoof(Stand.ROOF_FALSE);
					break;
				case 2:
					stand.setType(Stand.TYPE_TERRACES);
					stand.setIsRoof(Stand.ROOF_TRUE);
					break;
				case 3:
					stand.setType(Stand.TYPE_BENCHES);
					stand.setIsRoof(Stand.ROOF_FALSE);
					break;
				case 4:
					stand.setType(Stand.TYPE_BENCHES);
					stand.setIsRoof(Stand.ROOF_TRUE);
					break;
				case 5:
					stand.setType(Stand.TYPE_SEATS);
					stand.setIsRoof(Stand.ROOF_FALSE);
					break;
				case 6:
					stand.setType(Stand.TYPE_SEATS);
					stand.setIsRoof(Stand.ROOF_TRUE);
					break;
			}

			if (groupMap.get(stand.getLocation()).getCapacity().matches("[0-9]+")) { //$NON-NLS-1$
				stand.setCapacity(Integer.valueOf(groupMap.get(stand.getLocation()).getCapacity()).intValue());
			} else {
				stand.setCapacity(0);
			}

		}
		return standsMap;
	}

	private void getCostOfArenaChanges(Arena arena) {
		// pobieramy dane do rozbudowy stadionu
		Map<Integer, Stand> tempStand = getStands();
		Arena newArena = new Arena();
		Collection<Stand> stands = tempStand.values();
		newArena.setStands(new ArrayList<Stand>(stands));
		for (Stand stand : stands) {
			groupMap.get(stand.getLocation()).setToolTipText(""); //$NON-NLS-1$
			if (groupMap.get(stand.getLocation()).getCapacity().isEmpty()) { //$NON-NLS-1$
				groupMap.get(stand.getLocation()).setCapacity("0"); //$NON-NLS-1$
			}
		}

		setGroupMap(newArena);
		descriptionComposite.setInfo(arena, newArena);
	}

	/**
	 * @param club
	 */
	private void setGroupMap(Arena arena) {
		for (Stand stand : arena.getStands()) {
			groupMap.get(stand.getLocation()).setToolTipText(""); //$NON-NLS-1$
			groupMap.get(stand.getLocation()).setToolTipText(String.format("%s%s = %s\r\n", groupMap.get(stand.getLocation()).getToolTipText(), Messages.getString("arena.maxEarns"), Money.formatDoubleCurrencySymbol(stand.getIncome()))); //$NON-NLS-1$ //$NON-NLS-2$	
			groupMap.get(stand.getLocation()).setToolTipText(String.format("%s%s = %s\r\n", groupMap.get(stand.getLocation()).getToolTipText(), Messages.getString("arena.payments"), Money.formatDoubleCurrencySymbol(stand.getCost()))); //$NON-NLS-1$ //$NON-NLS-2$
			if (stand.getLocation() == Stand.N || stand.getLocation() == Stand.S) {
				groupMap.get(stand.getLocation()).setToolTipText(groupMap.get(stand.getLocation()).getToolTipText() + ticketsCostAlongLine);
			} else {
				groupMap.get(stand.getLocation()).setToolTipText(groupMap.get(stand.getLocation()).getToolTipText() + ticketsCostBehindGoal);
			}
		}
	}

	public void set() {
		arena = Cache.getClub().getArena();

		ticketsCostAlongLine = String.format("\r\n\r\n%s\r\n", Messages.getString("arena.cost.tickets")); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.standingPlaces"), Money.formatDoubleCurrencySymbol(Stand.STANDING_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.terraces"), Money.formatDoubleCurrencySymbol(Stand.TERRACES_WITHOUT_ROOF_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.terracesUnderRoof"), Money.formatDoubleCurrencySymbol(Stand.TERRACES_WITH_ROOF_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.benches"), Money.formatDoubleCurrencySymbol(Stand.BENCHES_WITHOUT_ROOF_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.benchesUnderRoof"), Money.formatDoubleCurrencySymbol(Stand.BENCHES_WITH_ROOF_ALONG_LINE_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s\r\n", Messages.getString("arena.seats"), Money.formatDoubleCurrencySymbol(Stand.SEATS_WITHOUT_ROOF_ALONG_LINE_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostAlongLine += String.format("%s = %s", Messages.getString("arena.seatsUnderRoof"), Money.formatDoubleCurrencySymbol(Stand.SEATS_WITH_ROOF_ALONG_LINE_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal = String.format("\r\n\r\n%s = \r\n", Messages.getString("arena.cost.tickets")); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.standingPlaces"), Money.formatDoubleCurrencySymbol(Stand.STANDING_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.terraces"), Money.formatDoubleCurrencySymbol(Stand.TERRACES_WITHOUT_ROOF_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.terracesUnderRoof"), Money.formatDoubleCurrencySymbol(Stand.TERRACES_WITHOUT_ROOF_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.benches"), Money.formatDoubleCurrencySymbol(Stand.BENCHES_WITHOUT_ROOF_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.benchesUnderRoof"), Money.formatDoubleCurrencySymbol(Stand.BENCHES_WITH_ROOF_BEHIND_GOAL_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s\r\n", Messages.getString("arena.seats"), Money.formatDoubleCurrencySymbol(Stand.SEATS_WITHOUT_ROOF_BEHIND_GOAL_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$
		ticketsCostBehindGoal += String.format("%s = %s", Messages.getString("arena.seatsUnderRoof"), Money.formatDoubleCurrencySymbol(Stand.SEATS_WITH_ROOF_BEHIND_GOAL_EARNS)); //$NON-NLS-1$ //$NON-NLS-2$

		descriptionComposite.setInfo(arena);
		setGroupMap(arena);
		fillArenaData(arena);

		// oblicz nowe wartosci dla stadionu po rozbudowie

		arenaCountButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				getCostOfArenaChanges(arena);
			}
		});

		// przycisk do przywrocenia danych dla stadionu
		arenaResetButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ViewArena.this.clear();
				setGroupMap(arena);
				descriptionComposite.setInfo(arena);
				// przywracamy dane dla miejsc na stadionie
				fillArenaData(arena);
			}
		});

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
		
		descriptionComposite = new ArenaDescriptionComposite(this.composite, SWT.BORDER);
		descriptionComposite.setLayoutData(descriptionFormData);
		descriptionComposite.setVisible(true);
		
		addPopupMenuParentTree();
		addListeners();
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
				browserShell.setText(arena.getArenaNames().get(arena.getArenaNames().size() - 1).getArenaName());
				Browser browser = null;
				try {
					browser = new Browser(browserShell, SWT.NONE);
				} catch (SWTError e) {
					/*
					 * The Browser widget throws an SWTError if it fails to
					 * instantiate properly. Application code should catch this
					 * SWTError and disable any feature requiring the Browser
					 * widget. Platform requirements for the SWT Browser widget
					 * are available from the SWT FAQ website.
					 */
				}
				if (browser != null) {
					/* The Browser widget can be used */
					//
					StringBuilder request = new StringBuilder();
					Map<Integer, Stand> standRequestMap = getStands();
					Collection<Stand> standRequest = standRequestMap.values();
					for (Stand stand : standRequest) {
						request.append("cap"); //$NON-NLS-1$
						request.append(stand.getLocation());
						request.append("="); //$NON-NLS-1$
						request.append(stand.getCapacity());
						request.append("&type"); //$NON-NLS-1$
						request.append(stand.getLocation());
						request.append("="); //$NON-NLS-1$
						request.append(stand.getType());
						request.append("&roof"); //$NON-NLS-1$
						request.append(stand.getLocation());
						request.append("="); //$NON-NLS-1$
						request.append(stand.getIsRoof());
						request.append("&"); //$NON-NLS-1$
					}
					browser.setUrl("http://files.sokker.org/pic/stadioVIII_10.swf?" + request.toString()); //$NON-NLS-1$
					// browser.setUrl("file:///" + SokkerBean.getBaseDir() +
					// File.separator + "resources" + File.separator +
					// "stadioVIII_10.swf?cap4=2000&type4=2&roof4=0&cap3=500&type3=1&roof3=0&cap1=2000&type1=2&roof1=0&cap6=2500&type6=2&roof6=0&cap2=100&type2=0&roof2=0&cap5=100&type5=0&roof5=0&cap7=100&type7=0&roof7=0&cap8=100&type8=0&roof8=0&");
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

						cbData = descriptionComposite.getText();
						cbData += "=============================="; //$NON-NLS-1$

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
	}
}
