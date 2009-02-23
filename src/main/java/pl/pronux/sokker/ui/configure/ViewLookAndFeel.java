package pl.pronux.sokker.ui.configure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.properties.SVProperties;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;
import pl.pronux.sokker.ui.widgets.buttons.ColorButton;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ViewLookAndFeel implements IViewConfigure {

	private HashMap<Button, Label> buttonLabelMap;

	private HashMap<Button, String> buttonMap;

	private Group colorGroup;

	private ArrayList<Label> colorLabelMap;

	private HashMap<String, Color> colorMap;

	final String[] colorTable = {
			"color.decreaseTable", //$NON-NLS-1$
			"color.decreaseDescription", //$NON-NLS-1$
			"color.error", //$NON-NLS-1$
			"color.increaseTable", //$NON-NLS-1$
			"color.increaseDescription", //$NON-NLS-1$
			"color.injuryBg", //$NON-NLS-1$
			"color.injuryFg", //$NON-NLS-1$
			"color.newTableItem", //$NON-NLS-1$
			"color.newTreeItem", //$NON-NLS-1$
			"color.trainedJunior" //$NON-NLS-1$
	};

	final String[] fontTable = {
			"font.table", //$NON-NLS-1$
			"font.description", //$NON-NLS-1$
			"font.main" //$NON-NLS-1$
	};

	private Composite composite;

	// private Group proxyGroup;
	// private SVProperties confProperties;

	protected Properties defaultProperties;

	private Group fontGroup;

	private HashMap<String, FontData> fontButtonMap;

	private TreeItem treeItem;

	private HashMap<String, ColorButton> colorButtonMap;

	private Button systemDefaults;

	private SokkerViewerSettings settings;

	public void applyChanges() {
		SettingsHandler.setUserProperties(new SVProperties());

		defaultProperties = SettingsHandler.getUserProperties();

		for (int i = 0; i < colorTable.length; i++) {
			setColor(colorTable[i], colorMap.get(colorTable[i]));
		}

		for (int i = 0; i < fontTable.length; i++) {
			defaultProperties.setProperty(fontTable[i], fontButtonMap.get(fontTable[i]).toString());
		}

		ConfigBean.setDefaults(defaultProperties);
		reloadMaps();

		try {
			defaultProperties.store(new FileOutputStream(new File(settings.getBaseDirectory() + File.separator + "settings" + File.separator + "user.properties")), ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (FileNotFoundException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite LookAndFeel FNF", e);
		} catch (IOException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite LookAndFeel IO", e);
		}
	}

	public void clear() {

	}

	public void dispose() {

	}

	public Composite getComposite() {
		return composite;
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(final Composite composite) {

		this.composite = composite;
		
		colorMap = new HashMap<String, Color>();
		fontButtonMap = new HashMap<String, FontData>();

		buttonLabelMap = new HashMap<Button, Label>();
		buttonMap = new HashMap<Button, String>();
		colorButtonMap = new HashMap<String, ColorButton>();

		if (SettingsHandler.getUserProperties() != null) {
			defaultProperties = SettingsHandler.getUserProperties();
		} else {
			defaultProperties = SettingsHandler.getDefaultProperties();
		}

		reloadMaps();

		composite.setLayout(new FormLayout());
		FormData formData;

		formData = new FormData(350, 350);
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);

		colorGroup = new Group(composite, SWT.NONE);
		colorGroup.setLayoutData(formData);
		colorGroup.setLayout(new FormLayout());

		colorLabelMap = new ArrayList<Label>();

		for (int i = 0; i < colorTable.length; i++) {
			formData = new FormData(50, 25);
			formData.top = new FormAttachment(0, 30 * i + 10);
			formData.left = new FormAttachment(0, 20);

			final ColorButton button = new ColorButton(colorGroup, SWT.NONE);
			button.setLayoutData(formData);

			// button.setImage(new Image(composite.getDisplay(), 40, 12));
			buttonMap.put(button, colorTable[i]);
			colorButtonMap.put(colorTable[i], button);

			// setImageBackgroundColor(button.getImage(),
			// colorMap.get(buttonMap.get(button)));

			button.setColor(colorMap.get(buttonMap.get(button)));
			button.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					Button tempButton = ((Button) event.widget);
					ColorDialog dialog = new ColorDialog(composite.getShell(), SWT.NONE);
					dialog.setRGB(colorMap.get(buttonMap.get(tempButton)).getRGB());
					RGB rgb = dialog.open();
					if (rgb != null) {
						Color newColor = ColorResources.getColor(rgb);
						// setImageBackgroundColor(tempButton.getImage(), newColor);
						button.setColor(newColor);
						if (!colorMap.get(buttonMap.get(tempButton)).equals(newColor)) {
							colorMap.put(buttonMap.get(tempButton), newColor);
						}
					}
				}
			});

			formData = new FormData(250, 25);
			formData.top = new FormAttachment(0, 30 * i + 15);
			formData.left = new FormAttachment(button, 10);

			Label label = new Label(colorGroup, SWT.NONE);
			label.setLayoutData(formData);
			label.setText(Messages.getString(colorTable[i]));
			label.setData("lang", colorTable[i]); //$NON-NLS-1$

			colorLabelMap.add(label);
		}

		// ******************* END COLOR SETTINGS
		// ******************************************

		// ******************* FONT SETTINGS
		// ***********************************************

		formData = new FormData(350, 100);
		formData.top = new FormAttachment(colorGroup, 10);
		formData.left = new FormAttachment(0, 10);

		fontGroup = new Group(composite, SWT.NONE);
		fontGroup.setLayoutData(formData);
		fontGroup.setLayout(new FormLayout());

		for (int i = 0; i < fontTable.length; i++) {
			formData = new FormData(50, 25);
			formData.top = new FormAttachment(0, 30 * i + 10);
			formData.left = new FormAttachment(0, 20);

			final ColorButton button = new ColorButton(fontGroup, SWT.NONE);
			button.setText("Abc"); //$NON-NLS-1$

			button.setFont(Fonts.getFont(DisplayHandler.getDisplay(), new FontData[] {new FontData(defaultProperties.getProperty(fontTable[i]))}));
			button.setLayoutData(formData);
			buttonMap.put(button, fontTable[i]);
			colorButtonMap.put(fontTable[i], button);

			button.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					Button tempButton = ((Button) event.widget);
					FontDialog dialog = new FontDialog(composite.getShell(), SWT.NONE);
					FontData[] fontDataTable = {
						fontButtonMap.get(buttonMap.get(button))
					};
					dialog.setFontList(fontDataTable);
					FontData fontData = dialog.open();
					if (fontData != null) {
						if (!fontButtonMap.get(buttonMap.get(tempButton)).equals(fontData)) {
							tempButton.setFont(Fonts.getFont(DisplayHandler.getDisplay(), new FontData[] {fontData}));

							buttonLabelMap.get(tempButton).setFont(tempButton.getFont());

							fontButtonMap.put(buttonMap.get(tempButton), fontData);
						}
					}
				}
			});

			formData = new FormData(250, 25);
			formData.top = new FormAttachment(0, 30 * i + 15);
			formData.left = new FormAttachment(button, 10);

			Label label = new Label(fontGroup, SWT.NONE);
			label.setLayoutData(formData);
			label.setText(Messages.getString(fontTable[i]));
			label.setFont(button.getFont());
			label.setData("lang", fontTable[i]); //$NON-NLS-1$

			buttonLabelMap.put(button, label);
		}

		formData = new FormData();
		formData.top = new FormAttachment(fontGroup, 10);
		formData.left = new FormAttachment(fontGroup, 0, SWT.CENTER);

		systemDefaults = new Button(composite, SWT.NONE);
		systemDefaults.setLayoutData(formData);
		systemDefaults.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				systemDefaultChanges();
			}

		});

		// ****************** END FONT SETTINGS
		// *******************************************

		setWidgets();
		treeItem.setText(Messages.getString("configure.look")); //$NON-NLS-1$
		colorGroup.setText(Messages.getString("configure.colorSettings")); //$NON-NLS-1$
		Control[] controls = colorGroup.getChildren();

		for (int i = 0; i < controls.length; i++) {
			if (controls[i] instanceof Label) {
				String lang = (String) controls[i].getData("lang"); //$NON-NLS-1$
				if (lang != null) {
					((Label) controls[i]).setText(Messages.getString(lang));
				}
			}
		}

		controls = fontGroup.getChildren();
		for (int i = 0; i < controls.length; i++) {
			if (controls[i] instanceof Label) {
				String lang = (String) controls[i].getData("lang"); //$NON-NLS-1$
				if (lang != null) {
					((Label) controls[i]).setText(Messages.getString(lang));
				}
			}
		}

		fontGroup.setText(Messages.getString("configure.fontSettings")); //$NON-NLS-1$
		systemDefaults.setText(Messages.getString("button.default.system")); //$NON-NLS-1$
		systemDefaults.pack();

		composite.layout(true);
	}

	private void reloadMaps() {
		colorMap.put("color.decreaseTable", ConfigBean.getColorDecrease()); //$NON-NLS-1$
		colorMap.put("color.decreaseDescription", ConfigBean.getColorDecreaseDescription()); //$NON-NLS-1$
		colorMap.put("color.error", ConfigBean.getColorError()); //$NON-NLS-1$
		colorMap.put("color.increaseTable", ConfigBean.getColorIncrease()); //$NON-NLS-1$
		colorMap.put("color.increaseDescription", ConfigBean.getColorIncreaseDescription()); //$NON-NLS-1$
		colorMap.put("color.injuryBg", ConfigBean.getColorInjuryBg()); //$NON-NLS-1$
		colorMap.put("color.injuryFg", ConfigBean.getColorInjuryFg()); //$NON-NLS-1$
		colorMap.put("color.newTableItem", ConfigBean.getColorNewTableObject()); //$NON-NLS-1$
		colorMap.put("color.newTreeItem", ConfigBean.getColorNewTreeObject()); //$NON-NLS-1$
		colorMap.put("color.trainedJunior", ConfigBean.getColorTrainedJunior()); //$NON-NLS-1$

		fontButtonMap.put("font.main", ConfigBean.getFontMain().getFontData()[0]); //$NON-NLS-1$
		fontButtonMap.put("font.description", ConfigBean.getFontDescription().getFontData()[0]); //$NON-NLS-1$
		fontButtonMap.put("font.table", ConfigBean.getFontTable().getFontData()[0]); //$NON-NLS-1$
	}

	private void systemDefaultChanges() {
		MessageBox messageBox = new MessageBox(composite.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		messageBox.setMessage(Messages.getString("message.question.settings")); //$NON-NLS-1$
		messageBox.setText(Messages.getString("message.QUESTION")); //$NON-NLS-1$
		if (messageBox.open() == SWT.NO) {
			return;
		}

		defaultProperties = SettingsHandler.getDefaultProperties();

		ConfigBean.setDefaults(defaultProperties);
		reloadMaps();

		try {
			defaultProperties.store(new FileOutputStream(new File(settings.getBaseDirectory() + File.separator + "settings" + File.separator + "user.properties")), ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (FileNotFoundException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite LookAndFeel FNF", e);
		} catch (IOException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite LookAndFeel IO", e);
		}

		setWidgets();

	}

	public void restoreDefaultChanges() {
		reloadMaps();
		setWidgets();

	}

	private void setColor(String name, Color color) {
		defaultProperties.setProperty(name, String.format("%d,%d,%d", color.getRed(), color.getGreen(), color.getBlue())); //$NON-NLS-1$
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		this.settings = sokkerViewerSettings;
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public void set() {
	}

	private void setWidgets() {
		for (int i = 0; i < colorTable.length; i++) {
			ColorButton button = colorButtonMap.get(colorTable[i]);
			// setImageBackgroundColor(colorButtonMap.get(colorTable[i]).getImage(),
			// colorMap.get(colorTable[i]));
			button.setColor(colorMap.get(colorTable[i]));
			button.redraw();
		}

		for (int i = 0; i < fontTable.length; i++) {
			Button buttonMap = colorButtonMap.get(fontTable[i]);
			buttonMap.setFont(Fonts.getFont(DisplayHandler.getDisplay(), new FontData[] {fontButtonMap.get(fontTable[i])}));
			buttonLabelMap.get(buttonMap).setFont(buttonMap.getFont());
		}
	}


}
