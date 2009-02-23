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

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.LeagueRound;
import pl.pronux.sokker.model.LeagueSeason;
import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.LeagueComposite;
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
		treeItem.setText(Messages.getString("tree.ViewLeague")); //$NON-NLS-1$
		treeItem.setImage(ImageResources.getImageResources("league.png")); //$NON-NLS-1$

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
					if (item.getData(LeagueRound.IDENTIFIER) != null) { 
						LeagueRound leagueRound = (LeagueRound) item.getData(LeagueRound.IDENTIFIER); 
						leagueComposite.fillLeagueTable(leagueRound);
						show(leagueComposite);
					}

					if (item.getData(Match.IDENTIFIER) != null) { 
						Match match = (Match) item.getData(Match.IDENTIFIER); 
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
					if (item.getData(LeagueRound.IDENTIFIER) != null) { 
						LeagueRound leagueRound = (LeagueRound) item.getData(LeagueRound.IDENTIFIER); 
						leagueComposite.fillLeagueTable(leagueRound);
						show(leagueComposite);
					}

					if (item.getData(Match.IDENTIFIER) != null) { 
						Match match = (Match) item.getData(Match.IDENTIFIER); 
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
					treeItemSeason.setImage(ImageResources.getImageResources("seasons.png")); //$NON-NLS-1$
//					treeItemSeason.setText(langResources.getString("league.season") + " " + leagueSeason.getSeason());
					treeItemSeason.setText(String.format("%s %2d (%s)", Messages.getString("league.season"), leagueSeason.getRawSeason(), leagueSeason.getLeague().getName())); //$NON-NLS-1$ //$NON-NLS-2$
					treeItemSeason.setData(LeagueSeason.IDENTIFIER, leagueSeason);
					for (LeagueRound round : leagueSeason.getAlRounds()) {
						TreeItem treeItemRound = new TreeItem(treeItemSeason, SWT.NONE);
						if(round.getRoundNumber() == 0) {
							treeItemRound.setImage(ImageResources.getImageResources("round0.png")); //$NON-NLS-1$
						} else if(round.getRoundNumber() == 15) {
							treeItemRound.setImage(ImageResources.getImageResources("round15.png")); //$NON-NLS-1$
						}	else {
							treeItemRound.setImage(ImageResources.getImageResources("round.png")); //$NON-NLS-1$
						}

						treeItemRound.setText(Messages.getString("league.round") + " " + round.getRoundNumber()); //$NON-NLS-1$ //$NON-NLS-2$
						treeItemRound.setData(LeagueRound.IDENTIFIER, round); 
						for (Match match : round.getMatches()) {
							TreeItem treeItemMatch = new TreeItem(treeItemRound, SWT.NONE);
							treeItemMatch.setImage(ImageResources.getImageResources("match.png")); //$NON-NLS-1$
							String homeTeam;
							String awayTeam;
							Club club;
							if(match.getHomeTeamName() != null) {
								homeTeam = match.getHomeTeamName();
							} else if (match.getHomeTeamName() == null && (club = clubMap.get(match.getHomeTeamID())) != null && club.getClubName().get(0).getName() != null) {
								homeTeam = club.getClubName().get(0).getName();
							} else {
								homeTeam = String.valueOf(match.getHomeTeamID());
							}
							if(match.getAwayTeamName() != null) {
								awayTeam = match.getAwayTeamName();
							} else if (match.getAwayTeamName() == null && (club = clubMap.get(match.getAwayTeamID())) != null && club.getClubName().get(0).getName() != null) {
								awayTeam = club.getClubName().get(0).getName();
							} else {
								awayTeam = String.valueOf(match.getAwayTeamID());
							}
							treeItemMatch.setText(homeTeam + " - " + awayTeam); //$NON-NLS-1$
							treeItemMatch.setData(Match.IDENTIFIER, match); 
							if(match.getIsFinished() == Match.NOT_FINISHED) {
								treeItemMatch.setForeground(ColorResources.getGray());
								treeItemMatch.getParentItem().setForeground(ColorResources.getGray());
							}
						}
					}
			}
		}

	}

	private Composite current;
	
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
