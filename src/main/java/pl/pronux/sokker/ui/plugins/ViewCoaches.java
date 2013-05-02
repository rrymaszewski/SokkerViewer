package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.comparators.CoachComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.DescriptionDoubleComposite;
import pl.pronux.sokker.ui.widgets.composites.DescriptionSingleComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.shells.NoteShell;
import pl.pronux.sokker.ui.widgets.tables.CoachesTable;

public class ViewCoaches implements IPlugin, ISort {

	private TreeItem _treeItem;

	private CoachesTable coachesTable;

	private Clipboard cb;

	private String cbData;

	private List<Coach> coaches;

	private DescriptionDoubleComposite coachView;

	private Composite composite;

	private Composite currentView;

	private DescriptionSingleComposite descriptionComposite;

	private FormData descriptionFormData;

	// public ViewCoach(Composite parent, int style) {
	// super(parent, style);
	// }

	private Menu menuClear;

	private Menu menuPopUp;

	private Menu menuPopUpParentTree;

	private Listener showMain;

	private FormData viewFormData;

	private Listener viewListener;

	private Map<Integer, Composite> viewMap;

	public void clear() {
		descriptionComposite.clearAll();
		coachesTable.removeAll();

		coaches.clear();
		// List the entries using entrySet()
		viewMap.clear();
	}

	public Composite getComposite() {
		return composite;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoCoaches"); //$NON-NLS-1$
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	public void init(Composite composite) {
		
		this.composite = new ViewComposite(composite.getParent(), composite.getStyle());
		viewFormData = ((ViewComposite) this.composite).getViewFormData();
		descriptionFormData = ((ViewComposite) this.composite).getDescriptionFormData();
		composite.dispose();
		
		cb = ViewerHandler.getClipboard();

		coaches = new ArrayList<Coach>();

		viewMap = new HashMap<Integer, Composite>();

		addListener();

		addViewComposite();
		addPopupMenuParentTree();
		addPopupMenu();
		addDescriptionComposite();

		currentView = descriptionComposite;
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		// this.confProperties = confProperties;

	}

	public void setTreeItem(TreeItem treeItem) {

		viewListener = new Listener() {
			public void handleEvent(Event event) {

				Point point = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(point);

				if (item != null && item.equals(_treeItem)) {
					showDescriptionView(descriptionComposite);
					if (item.equals(_treeItem) && event.button == 3) {

						cbData = descriptionComposite.getText();

						_treeItem.getParent().setMenu(menuPopUpParentTree);
						_treeItem.getParent().getMenu().setVisible(true);
					}
				}
			}
		};

		this._treeItem = treeItem;

		_treeItem.setText(Messages.getString("tree.ViewCoaches")); //$NON-NLS-1$

		_treeItem.setImage(ImageResources.getImageResources("whistle.png")); //$NON-NLS-1$

	}

	public void set() {
		coaches = Cache.getCoaches();
		coachesTable.fill(coaches);
		setDescriptionComposite(coaches);

		viewMap = new HashMap<Integer, Composite>();

		for (Coach coach : coaches) {
			coachView = new DescriptionDoubleComposite(composite, SWT.BORDER);
			coachView.setLayoutData(descriptionFormData);
			coachView.setVisible(false);

			coachView.setLeftDescriptionStringFormat("%-20s%-15s\r\n"); //$NON-NLS-1$
			coachView.setLeftFirstColumnSize(20);
			coachView.setLeftSecondColumnSize(15);

			coachView.setRightDescriptionStringFormat("%-20s%-15s\r\n"); //$NON-NLS-1$
			coachView.setRightFirstColumnSize(20);
			coachView.setRightSecondColumnSize(15);

			coachView.setFont(ConfigBean.getFontDescription());

			coachView.getLeftDescription().addListener(SWT.MouseDown, showMain);
			coachView.getRightDescription().addListener(SWT.MouseDown, showMain);
			// coachView.addListener(SWT.MouseDown, showMain);

			setStatsCoachInfo(coach, coachView);
			viewMap.put(coach.getId(), coachView);
		}

		_treeItem.getParent().addListener(SWT.MouseDown, viewListener);
		composite.layout(true);
	}

	private void addDescriptionComposite() {
		descriptionComposite = new DescriptionSingleComposite(composite, SWT.BORDER);
		descriptionComposite.setLayoutData(descriptionFormData);
		descriptionComposite.setVisible(true);

		// descriptionComposite.setDescriptionStringFormat(40, 15);
		descriptionComposite.setDescriptionStringFormat("%-40s%15s\r\n"); //$NON-NLS-1$
		descriptionComposite.setFirstColumnSize(40);
		descriptionComposite.setSecondColumnSize(15);

		descriptionComposite.setFont(ConfigBean.getFontDescription());
	}

	private void addListener() {
		showMain = new Listener() {
			public void handleEvent(Event event) {
				showDescriptionView(descriptionComposite);
			}
		};
	}

	private void openNote(TableItem item) {
		Coach coach = (Coach) item.getData(Coach.class.getName()); 
		final NoteShell noteShell = new NoteShell(composite.getShell(), SWT.PRIMARY_MODAL | SWT.CLOSE);
		noteShell.setPerson(coach);
		noteShell.open();

		if (coach.getNote() != null) {
			if (coach.getNote().isEmpty()) {
				item.setImage(CoachComparator.NOTE, null);
			} else {
				item.setImage(CoachComparator.NOTE, ImageResources.getImageResources("note.png")); //$NON-NLS-1$
			}
		}
	}

	private void addPopupMenu() {
		// added popup menu
		menuPopUp = new Menu(composite.getShell(), SWT.POP_UP);

		MenuItem menuItem;

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.note.open")); //$NON-NLS-1$
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (menuPopUp.getData("item") != null) { //$NON-NLS-1$
					TableItem item = (TableItem) menuPopUp.getData("item"); //$NON-NLS-1$
					if (item.getData(Coach.class.getName()) != null) { 
						openNote(item);
					}
				}
			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.SEPARATOR);

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
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

		menuClear = new Menu(composite.getShell(), SWT.POP_UP);

	}

	private void addPopupMenuParentTree() {
		// added popup menu
		menuPopUpParentTree = new Menu(composite.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menuPopUpParentTree, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.clipboard")); //$NON-NLS-1$
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				TextTransfer textTransfer = TextTransfer.getInstance();
				if (cbData != null) {
					cb.setContents(new Object[] {
						cbData
					}, new Transfer[] {
						textTransfer
					});
				}
			}
		});
	}

	private void addViewComposite() {
		coachesTable = new CoachesTable(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		coachesTable.setLayoutData(viewFormData);
		coachesTable.setVisible(true);
		
		coachesTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event != null) {
					showDescriptionView(((Coach) event.item.getData(Coach.class.getName())).getId());
				}
			}
		});

		coachesTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {

					Point pt = new Point(event.x, event.y);
					TableItem item = coachesTable.getItem(pt);
					if (item != null) {
						Coach coach = (Coach) item.getData(Coach.class.getName()); 

						setCbData(coach);
						menuPopUp.setData("item", item); //$NON-NLS-1$
						coachesTable.setMenu(menuPopUp);
						coachesTable.getMenu().setVisible(true);
					} else {
						coachesTable.setMenu(menuClear);
						showDescriptionView(descriptionComposite);
					}

				}

			}
		});

	}

	private void setCbData(Coach coach) {
		cbData = ((DescriptionDoubleComposite) viewMap.get(coach.getId())).getLeftText();
		cbData += ((DescriptionDoubleComposite) viewMap.get(coach.getId())).getRightText();
	}
	
	private void setDescriptionComposite(List<Coach> coaches) {
		String[][] values;
		int assistants = 0;
		int juniorsCoach = 0;
		int headCoach = 0;
		double allSalary = 0;
		double allAge = 0;
		int textSize = 0;

		for (Coach coach : coaches) {
			allSalary += coach.getSalary().toInt();
			allAge += coach.getAge();
			if (coach.getJob()==Coach.JOB_HEAD) {
				headCoach++;
			} else if (coach.getJob()==Coach.JOB_JUNIORS) {
				juniorsCoach++;
			} else if (coach.getJob()==Coach.JOB_ASSISTANT) {
				assistants++;
			}
		}

		values = new String[7][2];
		values[0][0] = Messages.getString("coach.allSalary"); //$NON-NLS-1$
		values[1][0] = Messages.getString("coach.averageSalary"); //$NON-NLS-1$
		values[2][0] = Messages.getString("coach.averageAge"); //$NON-NLS-1$
		values[3][0] = Messages.getString("coach.allCoaches"); //$NON-NLS-1$
		values[4][0] = Messages.getString("coach.head"); //$NON-NLS-1$
		values[5][0] = Messages.getString("coach.juniors"); //$NON-NLS-1$
		values[6][0] = Messages.getString("coach.assistants"); //$NON-NLS-1$

		values[0][1] = Money.formatDoubleCurrencySymbol(allSalary);
		textSize = descriptionComposite.checkFirstTextSize(values[0][0]) + descriptionComposite.checkSecondTextSize(values[0][1]);

		if (coaches.size() > 0) {
			values[1][1] = Money.formatDoubleCurrencySymbol(BigDecimal.valueOf(allSalary / coaches.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			textSize += descriptionComposite.checkFirstTextSize(values[1][0]) + descriptionComposite.checkSecondTextSize(values[1][1]);

			values[2][1] = BigDecimal.valueOf(allAge / coaches.size()).setScale(2, BigDecimal.ROUND_HALF_UP) + "   "; //$NON-NLS-1$
			textSize += descriptionComposite.checkFirstTextSize(values[2][0]) + descriptionComposite.checkSecondTextSize(values[2][1]);
		} else {
			values[1][1] = Money.formatDoubleCurrencySymbol(0);
			textSize += descriptionComposite.checkFirstTextSize(values[1][0]) + descriptionComposite.checkSecondTextSize(values[1][1]);

			values[2][1] = 0 + "   "; //$NON-NLS-1$
			textSize += descriptionComposite.checkFirstTextSize(values[2][0]) + descriptionComposite.checkSecondTextSize(values[2][1]);

		}

		values[3][1] = coaches.size() + "   "; //$NON-NLS-1$
		textSize += descriptionComposite.checkFirstTextSize(values[3][0]) + descriptionComposite.checkSecondTextSize(values[3][1]);

		textSize += descriptionComposite.checkFirstTextSize(values[4][0]);

		if (headCoach > 0) {
			values[4][1] = Messages.getString("coach.yes") + "   "; //$NON-NLS-1$ //$NON-NLS-2$
			textSize += descriptionComposite.checkSecondTextSize(values[4][1]);
		} else {
			values[4][1] = Messages.getString("coach.no") + "   "; //$NON-NLS-1$ //$NON-NLS-2$
			textSize += descriptionComposite.checkSecondTextSize(values[4][1]);
			descriptionComposite.colorText(textSize - values[4][1].length() - 2, values[4][1].length(), ConfigBean.getColorError());
		}

		textSize += descriptionComposite.checkFirstTextSize(values[5][0]);

		if (juniorsCoach > 0) {
			values[5][1] = Messages.getString("coach.yes") + "   "; //$NON-NLS-1$ //$NON-NLS-2$
			textSize += descriptionComposite.checkSecondTextSize(values[5][1]);
		} else {
			values[5][1] = Messages.getString("coach.no") + "   "; //$NON-NLS-1$ //$NON-NLS-2$
			textSize += descriptionComposite.checkSecondTextSize(values[5][1]);
			if (Cache.getJuniors().size() > 0) {
				descriptionComposite.colorText(textSize - values[5][1].length() - 2, values[5][1].length(), ConfigBean.getColorError());
			}
		}

		textSize += descriptionComposite.checkFirstTextSize(values[6][0]);

		if (assistants > 0 && assistants < 4) {
			values[6][1] = assistants + "   "; //$NON-NLS-1$
			textSize += descriptionComposite.checkSecondTextSize(values[6][1]);
		} else if (assistants > 3) {
			values[6][1] = assistants + "   "; //$NON-NLS-1$
			textSize += descriptionComposite.checkSecondTextSize(values[6][1]);
			descriptionComposite.colorText(textSize - values[6][1].length() - 2, values[6][1].length(), ConfigBean.getColorError());
		} else {
			values[6][1] = Messages.getString("coach.job.none") + "!   "; //$NON-NLS-1$ //$NON-NLS-2$
		}

		for (int i = 0; i < values.length; i++) {
			descriptionComposite.addText(values[i]);
		}
		descriptionComposite.setColor();
	}

	private void setStatsCoachInfo(Coach coach, DescriptionDoubleComposite description) {

		String[][] values;

		values = new String[9][2];
		values[0][0] = Messages.getString("coach.id"); //$NON-NLS-1$
		values[1][0] = Messages.getString("coach.name"); //$NON-NLS-1$
		values[2][0] = Messages.getString("coach.surname"); //$NON-NLS-1$
		values[3][0] = Messages.getString("coach.general"); //$NON-NLS-1$
		values[4][0] = Messages.getString("coach.age"); //$NON-NLS-1$
		values[5][0] = Messages.getString("coach.country"); //$NON-NLS-1$
		values[6][0] = Messages.getString("coach.salary"); //$NON-NLS-1$
		values[7][0] = Messages.getString("coach.job"); //$NON-NLS-1$
		values[8][0] = Messages.getString("coach.signed"); //$NON-NLS-1$

		values[0][1] = String.valueOf(coach.getId());
		values[1][1] = coach.getName();
		values[2][1] = coach.getSurname();
		values[3][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getGeneralskill()), coach.getGeneralskill()); //$NON-NLS-1$ //$NON-NLS-2$
		values[4][1] = String.valueOf(coach.getAge());
		values[5][1] = Messages.getString("country." + coach.getCountryfrom() + ".name"); //$NON-NLS-1$ //$NON-NLS-2$
		values[6][1] = coach.getSalary().formatDoubleCurrencySymbol();
		values[7][1] = Messages.getString("coach.job." + coach.getJob()); //$NON-NLS-1$
		if (coach.getSigned() > 0) {
			values[8][1] = Messages.getString("coach.signed.yes"); //$NON-NLS-1$
		} else {
			values[8][1] = Messages.getString("coach.signed.no"); //$NON-NLS-1$
		}

		for (int i = 0; i < values.length; i++) {
			description.addLeftText(values[i]);
		}

		// statsLeft.setStyleRanges(leftStyle);

		values = new String[10][2];
		values[0][0] = Messages.getString("coach.training"); //$NON-NLS-1$
		values[1][0] = Messages.getString("coach.stamina"); //$NON-NLS-1$
		values[2][0] = Messages.getString("coach.pace"); //$NON-NLS-1$
		values[3][0] = Messages.getString("coach.technique"); //$NON-NLS-1$
		values[4][0] = Messages.getString("coach.passing"); //$NON-NLS-1$
		values[5][0] = Messages.getString("coach.keeper"); //$NON-NLS-1$
		values[6][0] = Messages.getString("coach.defender"); //$NON-NLS-1$
		values[7][0] = Messages.getString("coach.playmaker"); //$NON-NLS-1$
		values[8][0] = Messages.getString("coach.scorer"); //$NON-NLS-1$
		values[9][0] = Messages.getString("coach.general"); //$NON-NLS-1$
		values[0][1] = ""; //$NON-NLS-1$
		values[1][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getStamina()),  coach.getStamina()); //$NON-NLS-1$ //$NON-NLS-2$
		values[2][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getPace()),  coach.getPace()); //$NON-NLS-1$ //$NON-NLS-2$
		values[3][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getTechnique()),  coach.getTechnique()); //$NON-NLS-1$ //$NON-NLS-2$
		values[4][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getPassing()),  coach.getPassing()); //$NON-NLS-1$ //$NON-NLS-2$
		values[5][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getKeepers()),  coach.getKeepers()); //$NON-NLS-1$ //$NON-NLS-2$
		values[6][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getDefenders()),  coach.getDefenders()); //$NON-NLS-1$ //$NON-NLS-2$
		values[7][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getPlaymakers()),  coach.getPlaymakers()); //$NON-NLS-1$ //$NON-NLS-2$
		values[8][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getScorers()), coach.getScorers()); //$NON-NLS-1$ //$NON-NLS-2$
		values[9][1] = String.format("[%d]", coach.getSummarySkill()); //$NON-NLS-1$

		for (int i = 0; i < values.length; i++) {
			description.addRightText(values[i]);
		}
	}

	private void showDescriptionView(Composite composite) {
		currentView.setVisible(false);
		currentView = composite;
		currentView.setVisible(true);
	}

	private void showDescriptionView(int id) {
		currentView.setVisible(false);
		currentView = (Composite) viewMap.get(id);
		currentView.setVisible(true);
	}

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
