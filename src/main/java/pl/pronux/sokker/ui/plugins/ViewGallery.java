package pl.pronux.sokker.ui.plugins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.GalleryManager;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.xml.dao.GalleryImagesDownloader;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.GalleryImage;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;
import pl.pronux.sokker.ui.widgets.dialogs.ProgressBarDialog;
import pl.pronux.sokker.ui.widgets.groups.ImageGroup;
import pl.pronux.sokker.ui.widgets.groups.ImageInformationGroup;
import pl.pronux.sokker.ui.widgets.menus.GalleryMenu;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.tables.GalleryTable;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class ViewGallery implements IPlugin {

	private Composite composite;
	private TreeItem treeItem;
	private ImageGroup imageGroup;
	private GalleryTable galleryTable;
	private ImageInformationGroup informationGroup;
	private Listener viewListener;
	private Menu menuClear;
	private GalleryMenu galleryMenu;
	private List<GalleryImage> images;
	private ToolBar toolBar;

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
		return Messages.getString("progressBar.info.setInfoGallery"); //$NON-NLS-1$
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		this.composite.setLayout(new FormLayout());

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);

		toolBar = new ToolBar(composite, SWT.HORIZONTAL | SWT.FLAT);
		toolBar.setLayoutData(formData);

		ToolItem item;

		item = new ToolItem(toolBar, SWT.NONE);
		item.setImage(ImageResources.getImageResources("gallery_load.png"));

		item = new ToolItem(toolBar, SWT.NONE);
		item.setImage(ImageResources.getImageResources("gallery_download.png"));

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(toolBar, 0);

		Label separator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(formData);

		formData = new FormData(250, 250);
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(separator, 5);

		imageGroup = new ImageGroup(composite, SWT.NONE);
		imageGroup.setLayoutData(formData);
		imageGroup.setText(Messages.getString("club.logo")); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(imageGroup, 5);
		formData.top = new FormAttachment(toolBar, 5);
		formData.right = new FormAttachment(100, -10);
		formData.height = 250;

		informationGroup = new ImageInformationGroup(composite, SWT.NONE);
		informationGroup.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(imageGroup, 5);
		formData.right = new FormAttachment(100, -10);
		formData.bottom = new FormAttachment(100, -10);

		galleryTable = new GalleryTable(composite, SWT.BORDER | SWT.FULL_SELECTION);
		galleryTable.setLayoutData(formData);

		galleryMenu = new GalleryMenu(composite.getShell(), SWT.POP_UP);
		menuClear = new Menu(composite.getShell(), SWT.POP_UP);
	}

	public void reload() {
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public void setSvBean(SvBean svBean) {
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewGallery")); //$NON-NLS-1$
		this.treeItem.setImage(ImageResources.getImageResources("gallery.png")); //$NON-NLS-1$
	}

	public void set() {
		viewListener = new Listener() {
			public void handleEvent(Event event) {

				Point point = new Point(event.x, event.y);
				TreeItem item = treeItem.getParent().getItem(point);

				if (item != null) {
					if (item.equals(treeItem)) {
						if (event.button == 3) {
							treeItem.getParent().setMenu(galleryMenu);
							treeItem.getParent().getMenu().setVisible(true);
						} else {
							treeItem.getParent().setMenu(menuClear);
							treeItem.getParent().getMenu().setVisible(true);
						}
					}

				}
			}
		};
		this.treeItem.getParent().addListener(SWT.MouseDown, viewListener);

		toolBar.getItem(1).addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				ProgressBarDialog dialog = new ProgressBarDialog(composite.getShell(), SWT.PRIMARY_MODAL | SWT.CLOSE);
				try {
					dialog.run(false, true, false, new GalleryImagesDownloader(new ArrayList<GalleryImage>()));
				} catch (InterruptedException e1) {
				} catch (InvocationTargetException e1) {
				}
			}
		});

		toolBar.getItem(0).addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				FileDialog fileDialog = new FileDialog(composite.getShell(), SWT.OPEN | SWT.SINGLE);
				fileDialog.setFilterExtensions(new String[] { "*.png;*.jpg;*.jpeg;*.gif;*.bmp" });
				String filename = fileDialog.open();
				if (filename != null && new File(filename).canWrite()) {
					File srcfile = new File(filename);
					ImageData imageData = new ImageData(filename);
					if(imageData.height <= 240 && imageData.width <= 240) {
						File dstFile = new File(GalleryImage.LOGOS_PATH + srcfile.getName());
						if(dstFile.exists()) {
							MessageDialog.openErrorMessage(composite.getShell(), Messages.getString("error.file.exists"));
						} else {
							try {
								OperationOnFile.copyFile(srcfile, dstFile);
								GalleryImage galleryImage = new GalleryImage();
								galleryImage.setFile(srcfile.getName());
								galleryImage.setPublicationDate(new Date(Calendar.getInstance().getTimeInMillis()));
								new GalleryManager().addGalleryImage(galleryImage);
								MessageDialog.openInformationMessage(composite.getShell(), Messages.getString("message.information.reload"));
							} catch (IOException e) {
								MessageDialog.openErrorMessage(composite.getShell(), Messages.getString("error.write"));
							} catch (SQLException e) {
								dstFile.delete();
								new BugReporter(composite.getShell()).openErrorMessage("Gallery-> adding images", e);
							}
						}
					} else {
						MessageDialog.openErrorMessage(composite.getShell(), Messages.getString("error.image.toolarge"));
					}
				}
			}
		});

		images = Cache.getGalleryImages();
		galleryTable.fill(images);

		galleryTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 1) {
					// Rectangle clientArea = allCoachesTable.getClientArea();
					Point pt = new Point(event.x, event.y);
					TableItem item = galleryTable.getItem(pt);
					if (item != null) {
						GalleryImage galleryImage = (GalleryImage) item.getData(GalleryImage.IDENTIFIER);
						Image img = null;
						try {
							img = new Image(galleryTable.getDisplay(), GalleryImage.LOGOS_PATH + galleryImage.getFile());
							imageGroup.setImage(img);
							informationGroup.fill(galleryImage, img);
						} catch (Exception e) {
						}

					}
				}
			}
		});

		galleryTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event != null && event.item != null && event.item.getData(GalleryImage.IDENTIFIER) != null) {
					GalleryImage galleryImage = (GalleryImage) event.item.getData(GalleryImage.IDENTIFIER);
					Image img = null;
					try {
						img = new Image(galleryTable.getDisplay(), GalleryImage.LOGOS_PATH + galleryImage.getFile());
						imageGroup.setImage(img);
						informationGroup.fill(galleryImage, img);
					} catch (Exception e) {
					}
				}
			}
		});
	}

}
