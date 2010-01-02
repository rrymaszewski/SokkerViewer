package pl.pronux.sokker.ui.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.comparators.MatchesComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.managers.MatchUIManager;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.MatchesComposite;
import pl.pronux.sokker.ui.widgets.menus.MatchesMenu;
import pl.pronux.sokker.ui.widgets.tabs.MatchTabComposite;

public class ViewMatches implements IPlugin {

	final private static String MATCHES_IDENTIFIER = "matches"; //$NON-NLS-1$
	private Composite composite;

	private TreeItem treeItem;

	private List<Match> matches;

	private Listener viewListener;

	private MatchTabComposite matchComposite;

	private Menu matchesAddMenu;

	private Menu menuClear;

	private MatchesComparator comparator;

	private Composite currentComposite;

	private MatchesComposite matchesComposite;

	private MatchesComposite seasonComposite;

	private HashMap<Match, TreeItem> hmTreeItemMatch;
	
	private MatchUIManager matchesManager = MatchUIManager.instance(); 

	public void clear() {

	}

	public void dispose() {

	}

	public Composite getComposite() {
		return this.composite;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoMatches");
	}

	public TreeItem getTreeItem() {
		return this.treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		composite.setLayout(new FormLayout());
		addView(composite);
		composite.layout();

		comparator = new MatchesComparator();
		comparator.setColumn(MatchesComparator.WEEK_DAY);
		comparator.setDirection(MatchesComparator.DESCENDING);
	}

	private void addView(final Composite composite) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);

		matchComposite = new MatchTabComposite(composite, SWT.NONE);
		matchComposite.setLayoutData(formData);
		matchComposite.setVisible(false);
		matchesComposite = new MatchesComposite(composite, SWT.NONE);
		matchesComposite.setLayoutData(formData);
		seasonComposite = new MatchesComposite(composite, SWT.NONE);
		seasonComposite.setLayoutData(formData);
		seasonComposite.setVisible(false);

		show(matchesComposite);
		matchesAddMenu = new MatchesMenu(composite.getShell(), SWT.POP_UP);
		menuClear = new Menu(composite.getShell(), SWT.POP_UP);
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public void setSvBean(SvBean svBean) {
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewMatches")); //$NON-NLS-1$
		this.treeItem.setImage(ImageResources.getImageResources("match.png")); //$NON-NLS-1$
	}

	public void set() {
		final Club team = Cache.getClub();
		matches = Cache.getMatches();
		fillTree(this.treeItem, matches);
		// for (int i = alMatches.size() - 1; i >= 0; i--) {
		// if (alMatches.get(i).getIsFinished() == Match.FINISHED) {
		// matchComposite.fill(alMatches.get(i));
		// break;
		// }
		// }

		matchesComposite.fill(team.getId(), matches);

		Listener matchesListener = new Listener() {
			@SuppressWarnings("unchecked")//$NON-NLS-1$
			public void handleEvent(Event event) {
				if (event.widget instanceof Table) {
					Point point = new Point(event.x, event.y);
					TableItem item = ((Table) event.widget).getItem(point);

					if (item != null) {
						switch (event.type) {
						case SWT.MouseDoubleClick:

							if (item.getData(Match.class.getName()) != null && item.getData(Match.class.getName()) instanceof Match) {
								Match match = (Match) item.getData(Match.class.getName());
								if (match.getAwayTeamScore() >= 0 || match.getHomeTeamScore() >= 0) {
									matchComposite.fill(match);
									show(matchComposite);

									if (hmTreeItemMatch.get(match) != null) {
										treeItem.getParent().setRedraw(false);
										collapseChildrens(treeItem.getItems());
										expandParent(hmTreeItemMatch.get(match));
										hmTreeItemMatch.get(match).getParent().setSelection(hmTreeItemMatch.get(match));
										treeItem.getParent().setRedraw(true);
									}
								}
							} else if (item.getData(MATCHES_IDENTIFIER) != null && item.getData(MATCHES_IDENTIFIER) instanceof ArrayList) {
								List<Match> matches = (ArrayList<Match>) item.getData(MATCHES_IDENTIFIER);
								seasonComposite.fill(team.getId(), matches);
								show(seasonComposite);
							}

							break;
						case SWT.MouseDown:
							break;
						}
					}
				}
			}

		};
		matchesComposite.getLeftTable().addListener(SWT.MouseDoubleClick, matchesListener);
		matchesComposite.getRightTable().addListener(SWT.MouseDoubleClick, matchesListener);
		seasonComposite.getLeftTable().addListener(SWT.MouseDoubleClick, matchesListener);
		seasonComposite.getRightTable().addListener(SWT.MouseDoubleClick, matchesListener);
		matchesComposite.getLeftTable().addListener(SWT.MouseDown, matchesListener);
		matchesComposite.getRightTable().addListener(SWT.MouseDown, matchesListener);
		seasonComposite.getLeftTable().addListener(SWT.MouseDown, matchesListener);
		seasonComposite.getRightTable().addListener(SWT.MouseDown, matchesListener);

		viewListener = new Listener() {
			@SuppressWarnings("unchecked")//$NON-NLS-1$
			public void handleEvent(Event event) {

				Point point = new Point(event.x, event.y);
				TreeItem item = treeItem.getParent().getItem(point);

				if (item != null) {
					if (checkParent(treeItem, item)) {
						if (item.getData(Match.class.getName()) != null) {
							show(matchComposite);
							Match match = (Match) item.getData(Match.class.getName());
							if (match != null) {
								matchComposite.fill(match);
							}

						} else if (item.getData(MATCHES_IDENTIFIER) != null && item.getData(MATCHES_IDENTIFIER) instanceof ArrayList) {
							ArrayList<Match> alMatches = (ArrayList<Match>) item.getData(MATCHES_IDENTIFIER);
							seasonComposite.fill(team.getId(), alMatches);
							show(seasonComposite);
						}

					} else if (item.equals(treeItem)) {
						show(matchesComposite);
						if (event.button == 3) {
							treeItem.getParent().setMenu(matchesAddMenu);
							treeItem.getParent().getMenu().setVisible(true);
						} else {
							treeItem.getParent().setMenu(menuClear);
							treeItem.getParent().getMenu().setVisible(true);
						}
					}

				}
			}
		};

		Listener viewKeyListener = new Listener() {

			@SuppressWarnings("unchecked")//$NON-NLS-1$
			public void handleEvent(Event event) {
				TreeItem item = null;
				for (int i = 0; i < treeItem.getParent().getSelection().length; i++) {
					item = treeItem.getParent().getSelection()[i];
				}

				if (item != null) {
					if (checkParent(treeItem, item)) {
						if (item.getData(Match.class.getName()) != null && item.getData(Match.class.getName()) instanceof Match) {
							show(matchComposite);
							Match match = (Match) item.getData(Match.class.getName());
							matchComposite.fill(match);
						} else if (item.getData(MATCHES_IDENTIFIER) != null && item.getData(MATCHES_IDENTIFIER) instanceof ArrayList) {
							ArrayList<Match> alMatches = (ArrayList<Match>) item.getData(MATCHES_IDENTIFIER);
							seasonComposite.fill(team.getId(), alMatches);
							show(seasonComposite);
						}
					} else if (item.equals(treeItem)) {
						show(matchesComposite);
						// showDescription(playersDescription);
						// showView(playersTable);

					}
				}

			}
		};

		ViewerHandler.getViewer().addListener(IEvents.REFRESH_MATCHES, new Listener() {

			public void handleEvent(Event arg0) {
				fillTree(treeItem, matches);
				matchesComposite.fill(team.getId(), matches);
			}
		});

		this.treeItem.getParent().addListener(SWT.MouseDown, viewListener);
		this.treeItem.getParent().addListener(SWT.KeyUp, viewKeyListener);

	}

	private boolean checkParent(TreeItem parent, TreeItem child) {
		if (child.getParentItem() != null) {
			if (parent.equals(child.getParentItem())) {
				return true;
			} else {
				return checkParent(parent, child.getParentItem());
			}
		} else {
			return false;
		}
	}

	private void expandParent(TreeItem child) {
		if (child != null) {
			child.setExpanded(true);
			expandParent(child.getParentItem());
		} else {
			return;
		}
	}

	private void collapseChildrens(TreeItem[] childs) {
		for (int i = 0; i < childs.length; i++) {
			childs[i].setExpanded(false);
			collapseChildrens(childs[i].getItems());
		}
	}

	private void fillTree(TreeItem treeItem, List<Match> alMatches) {
		hmTreeItemMatch = new HashMap<Match, TreeItem>();
		treeItem.removeAll();
		TreeItem treeItemMatch;
		TreeItem treeItemSeason = null;
		int season = -1;
		ArrayList<ArrayList<Match>> alSeasonsMatches = new ArrayList<ArrayList<Match>>();
		Collections.sort(alMatches, comparator);
		for (Match match : alMatches) {
			if(match.getIsFinished() == Match.FINISHED) {
				if (season != (match.getWeek() / 16)) {
					alSeasonsMatches.add(new ArrayList<Match>());
					season = match.getWeek() / 16;
					treeItemSeason = new TreeItem(treeItem, SWT.NONE);
					treeItemSeason.setImage(ImageResources.getImageResources("matches_season.png")); //$NON-NLS-1$
					treeItemSeason.setData(MATCHES_IDENTIFIER, alSeasonsMatches.get(alSeasonsMatches.size() - 1));
					treeItemSeason.setText(Messages.getString("league.season") + " " + season); //$NON-NLS-1$ //$NON-NLS-2$
				}

				alSeasonsMatches.get(alSeasonsMatches.size() - 1).add(match);

				if (treeItemSeason != null) {
					treeItemMatch = new TreeItem(treeItemSeason, SWT.NONE);
					treeItemMatch.setData(Match.class.getName(), match);

					hmTreeItemMatch.put(match, treeItemMatch);

					if (match.getLeague() != null) {
						treeItemMatch.setImage(matchesManager.getMatchImage(match.getLeague()));	
					}
					
					if (match.getIsFinished() == Match.FINISHED) {
						treeItemMatch.setText(match.getHomeTeamName() + " - " + match.getAwayTeamName()); //$NON-NLS-1$
					} else {
						treeItemMatch.setText(match.getHomeTeamID() + " - " + match.getAwayTeamID()); //$NON-NLS-1$
						treeItemMatch.setForeground(ColorResources.getGray());
					}
				}
			}
		}

		// for (int i = treeItem.getItems().length - 1; i >= 0; i--) {
		// treeItem.getItem(i).setText(Messages.getString("league.season") + " "
		// + (treeItem.getItems().length - i));
		// }

	}

	public void show(Composite composite) {
		if (currentComposite == null) {
			currentComposite = composite;
			currentComposite.setVisible(true);
		} else {
			currentComposite.setVisible(false);
			currentComposite = composite;
			currentComposite.setVisible(true);
		}
	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
