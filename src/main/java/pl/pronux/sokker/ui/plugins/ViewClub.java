package pl.pronux.sokker.ui.plugins;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.TeamManager;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;
import pl.pronux.sokker.ui.widgets.groups.ClubCountriesGroup;
import pl.pronux.sokker.ui.widgets.groups.ClubInfoGroup;
import pl.pronux.sokker.ui.widgets.groups.ImageGroup;
import pl.pronux.sokker.ui.widgets.groups.ReportsGroup;
import pl.pronux.sokker.ui.widgets.groups.WeatherGroup;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ViewClub implements IPlugin {

	private TeamManager teamManager = TeamManager.instance();
	
	private TreeItem _treeItem;

	private Composite composite;

	protected String cbData;

	private Menu menuPopUpParentTree;

	private Clipboard cb;

	private ImageGroup imageGroup;

	private ClubInfoGroup clubInfoGroup;

	private Label centerLabel;

	private Club club;

	private WeatherGroup weatherComposite;

	private ClubCountriesGroup visitedCountriesGroup;

	private ClubCountriesGroup invitedCountriesGroup;

	private ReportsGroup reportsGroup;

	private void setTreeItemData(Club club) {
		club = Cache.getClub();
		_treeItem.setImage(FlagsResources.getFlag(club.getCountry()));
	}

	public void clear() {
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

	public void init(Composite composite) {
		this.composite = composite;
		this.composite.setLayout(new FormLayout());

		cb = ViewerHandler.getClipboard();
		FormData formData = new FormData(250, 250);
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);

		imageGroup = new ImageGroup(composite, SWT.NONE);
		imageGroup.setLayoutData(formData);
		imageGroup.setText(Messages.getString("club.logo")); //$NON-NLS-1$
		
		formData = new FormData(250, 100);
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(imageGroup, 10);

		weatherComposite = new WeatherGroup(composite, SWT.BORDER);
		weatherComposite.setLayoutData(formData);
		
		formData = new FormData(0, 0);
		formData.top = new FormAttachment(weatherComposite, 0);

		centerLabel = new Label(composite, SWT.NONE);
		centerLabel.setLayoutData(formData);
		
		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(imageGroup, 5);
		formData.right = new FormAttachment(100, -5);
		formData.bottom = new FormAttachment(centerLabel, 0);

		clubInfoGroup = new ClubInfoGroup(composite, SWT.FULL_SELECTION);
		clubInfoGroup.setLayoutData(formData);
		
		formData = new FormData(250,180);
		formData.top = new FormAttachment(centerLabel, 10);
		formData.left = new FormAttachment(0, 5);

		invitedCountriesGroup = new ClubCountriesGroup(composite, SWT.NONE);
		invitedCountriesGroup.setLayoutData(formData);
		invitedCountriesGroup.setText(Messages.getString("club.countries.invited")); //$NON-NLS-1$
		
		formData = new FormData(250,180);
		formData.top = new FormAttachment(centerLabel, 10);
		formData.left = new FormAttachment(invitedCountriesGroup, 10);

		visitedCountriesGroup = new ClubCountriesGroup(composite, SWT.NONE);
		visitedCountriesGroup.setLayoutData(formData);
		visitedCountriesGroup.setText(Messages.getString("club.countries.visited")); //$NON-NLS-1$

		formData = new FormData(250,180);
		formData.top = new FormAttachment(centerLabel, 10);
		formData.left = new FormAttachment(visitedCountriesGroup, 10);
		formData.right = new FormAttachment(100, -10);
		formData.height = 180;

		reportsGroup = new ReportsGroup(composite, SWT.NONE);
		reportsGroup.setLayoutData(formData);
		
		addPopupMenuParentTree();
		addListeners();
	}

	public void set() {
		club = Cache.getClub();
		setTreeItemData(club);

		clubInfoGroup.fill(club);

		imageGroup.setImage(ImageResources.getImageFile(club.getImagePath()));
		
		weatherComposite.setWeatherInfo(club.getRegion().getWeather());
		visitedCountriesGroup.fill(club.getVisitedCountries(), Cache.getCountries());
		invitedCountriesGroup.fill(club.getInvitedCountries(), Cache.getCountries());
		
		reportsGroup.fill(Cache.getReports());
//		_treeItem.getParent().addListener(SWT.MouseDown, viewListener);
	}



	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	private void addListeners() {
//		viewListener = new Listener() {
//			public void handleEvent(Event event) {
//
//				Point point = new Point(event.x, event.y);
//				TreeItem item = _treeItem.getParent().getItem(point);
//
//				if (item != null && item.equals(_treeItem)) {
//					if (item.equals(_treeItem) && event.button == 3) {
//
//						cbData = descriptionComposite.getText();
//
//						_treeItem.getParent().setMenu(menuPopUpParentTree);
//						_treeItem.getParent().getMenu().setVisible(true);
//					}
//				}
//			}
//		};
	}

	public void setTreeItem(TreeItem treeItem) {

		this._treeItem = treeItem;
		_treeItem.setText(Messages.getString("tree.ViewClub")); //$NON-NLS-1$
		// _treeItem.setData("view", this);
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoClub"); //$NON-NLS-1$
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	public Composite getComposite() {
		return composite;
	}

	public class Configure implements IViewConfigure {

		private Composite composite;

		private TreeItem treeItem;

		private Text imagePathText;

		private CLabel imageCLabel;

		private boolean init;

		private Button imagePathButton;

		public void applyChanges() {
			if(init) {
				club.setImagePath(imagePathText.getText());
				try {
					teamManager.updateClubImagePath(club);
				} catch (SQLException e) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewClub -> update logo", e);
				}
				imageGroup.setImage(ImageResources.getImageFile(club.getImagePath()));
			}
		}

		public void clear() {

		}

		public void dispose() {

		}

		public Composite getComposite() {
			return this.composite;
		}

		public TreeItem getTreeItem() {
			return this.treeItem;
		}

		public void init(final Composite composite) {
			init = false;
			this.composite = composite;
			this.composite.setLayout(new FormLayout());

			FormData formData;

			formData = new FormData();
			formData.top = new FormAttachment(0, 10);
			formData.left = new FormAttachment(0, 10);
			formData.right = new FormAttachment(100, -10);
			// formData.height = 25;

			imageCLabel = new CLabel(this.composite, SWT.NONE);
			imageCLabel.setLayoutData(formData);
			imageCLabel.setFont(ConfigBean.getFontMain());

			formData = new FormData();
			formData.top = new FormAttachment(imageCLabel, 10);
			formData.right = new FormAttachment(100, -10);

			imagePathButton = new Button(this.composite, SWT.NONE | SWT.FLAT);
			imagePathButton.setEnabled(false);
			imagePathButton.setText("..."); //$NON-NLS-1$
			imagePathButton.setLayoutData(formData);
			imagePathButton.pack();

			imagePathButton.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event arg0) {

					FileDialog fileDialog = new FileDialog(composite.getShell(), SWT.OPEN);
					fileDialog.setText(Messages.getString("confShell.chooser.title")); //$NON-NLS-1$
					fileDialog.setFilterPath(System.getProperty("user.dir")); //$NON-NLS-1$
					fileDialog.setFilterExtensions(new String[] {
							"*.jpg; *.png; *.bmp; *.gif" //$NON-NLS-1$
					});

					String tempPropsFile = fileDialog.open();
					
					if (tempPropsFile != null) {
						ImageData imageData = new ImageData(tempPropsFile);
						if(imageData.height <= 240 && imageData.width <= 240) {
							imagePathText.setText(tempPropsFile);	
						} else {
							MessageDialog.openErrorMessage(composite.getShell(), Messages.getString("error.image.toobig")); //$NON-NLS-1$
						}
					}
				}
			});

			formData = new FormData();
			formData.top = new FormAttachment(imagePathButton, 0, SWT.CENTER);
			formData.left = new FormAttachment(0, 10);
			formData.right = new FormAttachment(imagePathButton, -10);
			// formData.height = 25;

			imagePathText = new Text(this.composite, SWT.BORDER);
			imagePathText.setLayoutData(formData);
			imagePathText.setFont(ConfigBean.getFontMain());
			this.treeItem.setText(Messages.getString("tree.ViewClub")); //$NON-NLS-1$
			this.imageCLabel.setText(Messages.getString("club.configure.image.label")); //$NON-NLS-1$

			this.composite.layout(true);

		}

		public void restoreDefaultChanges() {
		}

		public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		}

		public void setTreeItem(TreeItem treeItem) {
			this.treeItem = treeItem;
		}

		public void set() {
			init = true;
			imagePathButton.setEnabled(true);
			if(club.getImagePath() != null) {
				imagePathText.setText(club.getImagePath());
			}

		}

	}

	public IViewConfigure getConfigureComposite() {
		return new Configure();
	}

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		clubInfoGroup.fill(club);
	}

}
