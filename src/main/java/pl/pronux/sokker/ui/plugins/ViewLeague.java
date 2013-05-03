package pl.pronux.sokker.ui.plugins;

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

import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.LeagueRound;
import pl.pronux.sokker.model.LeagueSeason;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.views.LeagueComposite;
import pl.pronux.sokker.ui.widgets.tabs.MatchTabComposite;

public class ViewLeague implements IPlugin {

	private Composite composite;

	private TreeItem treeItem;

	private List<LeagueSeason> leagueSeasons;

	private Listener viewListener;

	private LeagueComposite leagueComposite;

	private MatchTabComposite matchComposite;

	private FormData viewFormData;

	private Map<Integer, Club> clubMap;

	private Listener keyListener;
	
	private Composite current;

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
		return Messages.getString("progressBar.info.setInfoLeague");
	}

	public TreeItem getTreeItem() {
		return this.treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		
		viewFormData = new FormData();
		viewFormData.top = new FormAttachment(0,0);
		viewFormData.left = new FormAttachment(0,0);
		viewFormData.right = new FormAttachment(100,0);
		viewFormData.bottom = new FormAttachment(100,0);
		
		composite.setLayout(new FormLayout());
		addView(composite);
		composite.layout();
	}

	private void addView(Composite composite) {
		leagueComposite = new LeagueComposite(composite, SWT.NONE);
		leagueComposite.setLayoutData(viewFormData);
		leagueComposite.setVisible(false);
		show(leagueComposite);
		
		matchComposite = new MatchTabComposite(composite, SWT.NONE);
		matchComposite.setLayoutData(viewFormData);
		matchComposite.setVisible(false);
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		treeItem.setText(Messages.getString("tree.ViewLeague")); 
		treeItem.setImage(ImageResources.getImageResources("league.png")); 

	}

	public void set() {
		leagueSeasons = Cache.getLeagueSeasons();
		clubMap = Cache.getClubMap();
		fillTree(this.treeItem, leagueSeasons);
		viewListener = new Listener() {
			public void handleEvent(Event event) {

				Point point = new Point(event.x, event.y);
				TreeItem item = treeItem.getParent().getItem(point);

				if (item != null) {
					if (item.getData(LeagueRound.class.getName()) != null) { 
						LeagueRound leagueRound = (LeagueRound) item.getData(LeagueRound.class.getName()); 
						leagueComposite.fillLeague(leagueRound);
						show(leagueComposite);
					}

					if (item.getData(Match.class.getName()) != null) { 
						Match match = (Match) item.getData(Match.class.getName()); 
						matchComposite.fill(match);
						show(matchComposite);
					}
				}

			}
		};
		
		keyListener = new Listener() {
			public void handleEvent(Event event) {

				TreeItem item = null;
				for (int i = 0; i < treeItem.getParent().getSelection().length; i++) {
					item = treeItem.getParent().getSelection()[i];
				}

				if (item != null) {
					if (item.getData(LeagueRound.class.getName()) != null) { 
						LeagueRound leagueRound = (LeagueRound) item.getData(LeagueRound.class.getName()); 
						leagueComposite.fillLeague(leagueRound);
						show(leagueComposite);
					}

					if (item.getData(Match.class.getName()) != null) { 
						Match match = (Match) item.getData(Match.class.getName()); 
						matchComposite.fill(match);
						show(matchComposite);
					}
				}

			}
		};

		this.treeItem.getParent().addListener(SWT.MouseDown, viewListener);
		this.treeItem.getParent().addListener(SWT.KeyUp, keyListener);
	}

	private void fillTree(TreeItem treeItem, List<LeagueSeason> leagueSeasons) {
		treeItem.removeAll();
		for (LeagueSeason leagueSeason : leagueSeasons) {
			if (leagueSeason.getLeague() != null && leagueSeason.getLeague().getType() == League.TYPE_LEAGUE) {
//				TreeItem treeItemLeague = new TreeItem(treeItem, SWT.NONE);
//				if(league.getName() == null) { 
//					treeItemLeague.setText(Messages.getString("match.type.0-0-0"));
//				} else {
//					treeItemLeague.setText(league.getName());	
//				}
				
					TreeItem treeItemSeason = new TreeItem(treeItem, SWT.NONE);
					treeItemSeason.setImage(ImageResources.getImageResources("seasons.png")); 
//					treeItemSeason.setText(langResources.getString("league.season") + " " + leagueSeason.getSeason());
					treeItemSeason.setText(String.format("%s %2d (%s)", Messages.getString("league.season"), leagueSeason.getRawSeason(), leagueSeason.getLeague().getName()));  
					treeItemSeason.setData(LeagueSeason.class.getName(), leagueSeason);
					for (LeagueRound round : leagueSeason.getRounds()) {
						TreeItem treeItemRound = new TreeItem(treeItemSeason, SWT.NONE);
						if(round.getRoundNumber() == 0) {
							treeItemRound.setImage(ImageResources.getImageResources("round0.png")); 
						} else if(round.getRoundNumber() == 15) {
							treeItemRound.setImage(ImageResources.getImageResources("round15.png")); 
						}	else {
							treeItemRound.setImage(ImageResources.getImageResources("round.png")); 
						}

						treeItemRound.setText(Messages.getString("league.round") + " " + round.getRoundNumber());  
						treeItemRound.setData(LeagueRound.class.getName(), round); 
						for (Match match : round.getMatches()) {
							TreeItem treeItemMatch = new TreeItem(treeItemRound, SWT.NONE);
							treeItemMatch.setImage(ImageResources.getImageResources("match.png")); 
							String homeTeamName;
							String awayTeamName;
							Club homeClub = clubMap.get(match.getHomeTeamId());
							if(match.getHomeTeamName() != null) {
								homeTeamName = match.getHomeTeamName();
							} else if (match.getHomeTeamName() == null && homeClub != null && homeClub.getClubName().get(0).getName() != null) {
								homeTeamName = homeClub.getClubName().get(0).getName();
							} else {
								homeTeamName = String.valueOf(match.getHomeTeamId());
							}
							Club awayClub = clubMap.get(match.getAwayTeamId());
							if(match.getAwayTeamName() != null) {
								awayTeamName = match.getAwayTeamName();
							} else if (match.getAwayTeamName() == null && awayClub != null && awayClub.getClubName().get(0).getName() != null) {
								awayTeamName = awayClub.getClubName().get(0).getName();
							} else {
								awayTeamName = String.valueOf(match.getAwayTeamId());
							}
							treeItemMatch.setText(homeTeamName + " - " + awayTeamName); 
							treeItemMatch.setData(Match.class.getName(), match); 
							if(match.getIsFinished() == Match.NOT_FINISHED) {
								treeItemMatch.setForeground(ColorResources.getGray());
								treeItemMatch.getParentItem().setForeground(ColorResources.getGray());
							}
						}
					}
			}
		}

	}
	
	private void show(Composite composite) {
		if(current == null) {
			current = composite;
			current.setVisible(true);
		} else {
			if(!current.equals(composite)) {
				current.setVisible(false);
				current = composite;
				current.setVisible(true);
			}
		}
	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
