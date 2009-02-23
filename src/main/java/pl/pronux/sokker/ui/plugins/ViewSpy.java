package pl.pronux.sokker.ui.plugins;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.comparators.MatchesComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.events.TeamEvent;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.SpyComposite;
import pl.pronux.sokker.ui.widgets.tabs.MatchTabComposite;

public class ViewSpy implements IPlugin {

	private TreeItem treeItem;
	private Composite composite;
	private Composite currentComposite;
	private SpyComposite spyComposite;
	private MatchTabComposite matchComposite;

	public void clear() {
	}

	public void dispose() {
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
		return Messages.getString("progressBar.info.setInfoSpy");
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		
		composite.setLayout(new FormLayout());
		
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		
		spyComposite = new SpyComposite(composite, SWT.NONE);
		spyComposite.setLayoutData(formData);
		spyComposite.setEnabled(false);
		spyComposite.setVisible(true);
		
		matchComposite = new MatchTabComposite(composite, SWT.NONE);
		matchComposite.setLayoutData(formData);
		matchComposite.setVisible(false);
		
		show(spyComposite);
		composite.layout();
	}
	

	
	public void fill(Club club) {
		clearTreeItem();
		
		spyComposite.fill(club);
		
		List<Match> matches = club.getMatches();
		MatchesComparator comp = new MatchesComparator(MatchesComparator.WEEK_DAY, MatchesComparator.DESCENDING);
		Collections.sort(matches, comp);
		for(Match match : matches) {
			if(match.getIsFinished() == Match.FINISHED) {
				addTreeItem(match);
			}
		}
	}
	
	private void clearTreeItem() {
		this.treeItem.removeAll();
	}
	
	private void addTreeItem(Match match) {
		TreeItem treeItem = new TreeItem(this.treeItem, SWT.NONE);
		treeItem.setText(match.getHomeTeamName() + "-" + match.getAwayTeamName());
		treeItem.setData(Match.IDENTIFIER, match);
		treeItem.setImage(ImageResources.getImageResources("match.png")); 
	}

	public void reload() {
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public void setSvBean(SvBean svBean) {
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setImage(ImageResources.getImageResources("spy.png"));
		this.treeItem.setText(Messages.getString("tree.ViewSpy"));
	}

	public void set() {
		ViewerHandler.getViewer().addListener(IEvents.REFRESH_SPY, new Listener() {
			public void handleEvent(Event event) {
				if(event instanceof TeamEvent) {
					Club team = ((TeamEvent) event).getTeam();
					ViewSpy.this.fill(team);
				}
			}
		});
		List<Match> matches = Cache.getMatches();
		Map<Integer, Club> clubs = Cache.getClubMap();
		Club club = Cache.getClub();
		spyComposite.setEnabled(true);
		spyComposite.fill(matches, club, clubs);
		
		Listener viewListener = new Listener() {
			public void handleEvent(Event event) {

				Point point = new Point(event.x, event.y);
				TreeItem item = treeItem.getParent().getItem(point);

				if (item != null) {
					if (checkParent(treeItem, item)) {
						if (item.getData(Match.IDENTIFIER) != null) {
							show(matchComposite);
							Match match = (Match) item.getData(Match.IDENTIFIER);
							if (match != null) {
								matchComposite.fill(match);
							}
						}
					} else {
						show(spyComposite);
					}
				}
			}
		};
		
		Listener viewKeyListener = new Listener() {

			public void handleEvent(Event event) {
				TreeItem item = null;
				for (int i = 0; i < treeItem.getParent().getSelection().length; i++) {
					item = treeItem.getParent().getSelection()[i];
				}

				if (item != null) {
					if (checkParent(treeItem, item)) {
						if (item.getData(Match.IDENTIFIER) != null) {
							show(matchComposite);
							Match match = (Match) item.getData(Match.IDENTIFIER);
							if (match != null) {
								matchComposite.fill(match);
							}
						}
					} else {
						show(spyComposite);
					}
				}
			}
		};
		
		this.treeItem.getParent().addListener(SWT.KeyUp, viewKeyListener);
		this.treeItem.getParent().addListener(SWT.MouseDown, viewListener);
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

}
