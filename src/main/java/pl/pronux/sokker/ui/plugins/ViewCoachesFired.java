package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.PersonsManager;
import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.CursorResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.DescriptionDoubleComposite;
import pl.pronux.sokker.ui.widgets.composites.DescriptionSingleComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.tables.CoachFiredTable;

public class ViewCoachesFired implements IPlugin, ISort {

	private PersonsManager personsManager = PersonsManager.instance();
	
	private TreeItem _treeItem;

	private CoachFiredTable coachesTable;

	private Clipboard cb;

	private String cbData;

	private List<Coach> coaches;

	private DescriptionDoubleComposite coachView;

	private Composite composite;

	private Coach currentCoach;

	private Composite currentView;

	private DescriptionSingleComposite descriptionComposite;
	
	// public ViewCoach(Composite parent, int style) {
	// super(parent, style);
	// }

	private FormData descriptionFormData;

	private Menu menuClear;

	private Menu menuPopUp;

	private Menu menuPopUpParentTree;

	private Listener showMain;

	private FormData viewFormData;

	private Listener viewListener;

	public void clear() {
		descriptionComposite.clearAll();
		coachesTable.removeAll();

		coaches.clear();
		// List the entries using entrySet()
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
		return Messages.getString("progressBar.info.setInfoCoachesFired"); //$NON-NLS-1$
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

		addListener();

		addViewComposite();

		addDescriptionComposite();

		addPopupMenu();
		addPopupMenuParentTree();

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
				}
			}
		};

		this._treeItem = treeItem;

		_treeItem.setText(Messages.getString("tree.ViewCoachesFired")); //$NON-NLS-1$

		_treeItem.setImage(ImageResources.getImageResources("sacked_trainer.png")); //$NON-NLS-1$

		_treeItem.getParent().addListener(SWT.MouseDown, viewListener);

	}

	public void set() {

		ViewerHandler.getViewer().addListener(IEvents.REFRESH_COACHES_FIRED, new Listener() {

			public void handleEvent(Event arg0) {
				setDescriptionComposite(coaches);
				coachesTable.fill(coaches);
			}
		});

		coaches = Cache.getCoachesFired();

		coachesTable.fill(coaches);

		setDescriptionComposite(coaches);

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

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);

				if (item != null) {
					if (item.equals(_treeItem) && event.button == 3) {

						cbData = descriptionComposite.getText();

						_treeItem.getParent().setMenu(menuPopUpParentTree);
						_treeItem.getParent().getMenu().setVisible(true);
					}
				}
			}

		});

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

	private void addPopupMenu() {
		// added popup menu
		menuPopUp = new Menu(composite.getShell(), SWT.POP_UP);

		MenuItem menuItem = new MenuItem(menuPopUp, SWT.PUSH);
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

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.move")); //$NON-NLS-1$
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
				messageBox.setMessage(Messages.getString("message.playerMove.text")); //$NON-NLS-1$
				messageBox.setText(Messages.getString("message.playerMove.title")); //$NON-NLS-1$

				if (messageBox.open() == SWT.YES) {
					Coach coach = currentCoach;

					try {
						personsManager.movePersonToTrash(coach);
						coachesTable.fill(coaches);

						setDescriptionComposite(coaches);
						showDescriptionView(descriptionComposite);

						ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_TRASH_COACHES, new Event());
					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewCoaches -> move to trash", e1);
					}
				}

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
				cb.setContents(new Object[] {
					cbData
				}, new Transfer[] {
					textTransfer
				});
			}
		});

		menuItem = new MenuItem(menuPopUpParentTree, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.moveAll")); //$NON-NLS-1$
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try {
					MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
					messageBox.setMessage(Messages.getString("message.coachMoveAll.text")); //$NON-NLS-1$
					messageBox.setText(Messages.getString("message.coachMoveAll.title")); //$NON-NLS-1$

					if (messageBox.open() == SWT.YES) {

						ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_WAIT));

						showDescriptionView(descriptionComposite);
						setDescriptionComposite(coaches);

						// allJuniorsTable.setRedraw(false);

						SQLSession.connect();

						for (Iterator<Coach> itr = coaches.iterator(); itr.hasNext();) {

							Coach coach = itr.next();
							itr.remove();
							personsManager.movePersonToTrash(coach);

						}

						coachesTable.fill(coaches);

						// allJuniorsTable.setRedraw(true);

						_treeItem.getParent().setSelection(new TreeItem[] {
							_treeItem
						});

						setDescriptionComposite(coaches);
						showDescriptionView(descriptionComposite);

						ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_ARROW));

						ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_TRASH_COACHES, new Event());
					}

				} catch (SQLException e1) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewCoachesFired", e1);
				} finally {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
				}
			}
		});

	}

	private void addViewComposite() {
		coachesTable = new CoachFiredTable(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		coachesTable.setLayoutData(viewFormData);

		// tworzymy kolumny dla trenerow

		coachesTable.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event != null) {
					Coach coach = (Coach) event.item.getData(Coach.class.getName()); 
					setStatsCoachInfo(coach, coachView);
					showDescriptionView(coachView);
					currentCoach = coach;
					setCbData(coachView);
				}
			}

		});

		coachesTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {

					Point pt = new Point(event.x, event.y);
					TableItem item = coachesTable.getItem(pt);
					if (item != null) {
						// Coach coach = (Coach) item.getData(Coach.IDENTIFIER);

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

	private void setCbData(DescriptionDoubleComposite coachView) {
		cbData = coachView.getLeftText();
		cbData += coachView.getRightText();
	}

	private void setDescriptionComposite(List<Coach> coach) {
		double allSalary = 0;
		double allAge = 0;
		int textSize = 0;

		descriptionComposite.clearAll();

		for (int i = 0; i < coach.size(); i++) {
			allSalary += coach.get(i).getSalary().toInt();
			allAge += coach.get(i).getAge();
		}

		String[][] values = new String[4][2];
		values[0][0] = Messages.getString("coach.allSalary"); //$NON-NLS-1$
		values[1][0] = Messages.getString("coach.averageSalary"); //$NON-NLS-1$
		values[2][0] = Messages.getString("coach.averageAge"); //$NON-NLS-1$
		values[3][0] = Messages.getString("coach.allCoaches"); //$NON-NLS-1$

		values[0][1] = Money.formatDoubleCurrencySymbol(allSalary);
		textSize = descriptionComposite.checkFirstTextSize(values[0][0]) + descriptionComposite.checkSecondTextSize(values[0][1]);

		if (coach.size() > 0) {

			values[1][1] = Money.formatDoubleCurrencySymbol(BigDecimal.valueOf(allSalary / coach.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			textSize += descriptionComposite.checkFirstTextSize(values[1][0]) + descriptionComposite.checkSecondTextSize(values[1][1]);

			values[2][1] = BigDecimal.valueOf(allAge / coach.size()).setScale(2, BigDecimal.ROUND_HALF_UP) + "   "; //$NON-NLS-1$
			textSize += descriptionComposite.checkFirstTextSize(values[2][0]) + descriptionComposite.checkSecondTextSize(values[2][1]);

		} else {
			values[1][1] = Money.formatDoubleCurrencySymbol(0);
			textSize += descriptionComposite.checkFirstTextSize(values[1][0]) + descriptionComposite.checkSecondTextSize(values[1][1]);

			values[2][1] = BigDecimal.valueOf(0).toString() + "   "; //$NON-NLS-1$
			textSize += descriptionComposite.checkFirstTextSize(values[2][0]) + descriptionComposite.checkSecondTextSize(values[2][1]);

		}

		values[3][1] = String.valueOf(coach.size()).toString() + "   "; //$NON-NLS-1$
		textSize += descriptionComposite.checkFirstTextSize(values[3][0]) + descriptionComposite.checkSecondTextSize(values[3][1]);

		for (int i = 0; i < values.length; i++) {
			descriptionComposite.addText(values[i]);
		}
		descriptionComposite.setColor();
	}

	private void setStatsCoachInfo(Coach coach, DescriptionDoubleComposite description) {
		description.clearAll();

		String[][] values = new String[7][2];
		values[0][0] = Messages.getString("coach.id"); //$NON-NLS-1$
		values[1][0] = Messages.getString("coach.name"); //$NON-NLS-1$
		values[2][0] = Messages.getString("coach.surname"); //$NON-NLS-1$
		values[3][0] = Messages.getString("coach.general"); //$NON-NLS-1$
		values[4][0] = Messages.getString("coach.age"); //$NON-NLS-1$
		values[5][0] = Messages.getString("coach.country"); //$NON-NLS-1$
		values[6][0] = Messages.getString("coach.salary"); //$NON-NLS-1$

		values[0][1] = String.valueOf(coach.getId());
		values[1][1] = coach.getName();
		values[2][1] = coach.getSurname();
		values[3][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getGeneralskill()), coach.getGeneralskill()); //$NON-NLS-1$ //$NON-NLS-2$
		values[4][1] = String.valueOf(coach.getAge());
		values[5][1] = Messages.getString("country." + coach.getCountryfrom() + ".name"); //$NON-NLS-1$ //$NON-NLS-2$
		values[6][1] = coach.getSalary().formatDoubleCurrencySymbol();

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
		values[1][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getStamina()), coach.getStamina()); //$NON-NLS-1$ //$NON-NLS-2$
		values[2][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getPace()), coach.getPace()); //$NON-NLS-1$ //$NON-NLS-2$
		values[3][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getTechnique()), coach.getTechnique()); //$NON-NLS-1$ //$NON-NLS-2$
		values[4][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getPassing()), coach.getPassing()); //$NON-NLS-1$ //$NON-NLS-2$
		values[5][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getKeepers()), coach.getKeepers()); //$NON-NLS-1$ //$NON-NLS-2$
		values[6][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getDefenders()), coach.getDefenders()); //$NON-NLS-1$ //$NON-NLS-2$
		values[7][1] = String.format("%s [%d]", Messages.getString("skill.a" + coach.getPlaymakers()), coach.getPlaymakers()); //$NON-NLS-1$ //$NON-NLS-2$
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

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
