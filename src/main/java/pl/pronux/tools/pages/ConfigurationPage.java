package pl.pronux.tools.pages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.data.properties.SVProperties;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;
import pl.pronux.sokker.updater.model.Package;
import pl.pronux.sokker.updater.xml.UpdateXMLParser;

public class ConfigurationPage extends Page {

	public static final String PAGE_NAME = "CONFIGURATION_PAGE";

	private Set<String> files = new HashSet<String>();
	private Map<String, WidgetsCollection> widgetsMap = new HashMap<String, WidgetsCollection>();

	private Text packageName;
	private Combo packageOS;
	private Spinner packageRevision;

	private Combo packageLocalpath;

	private Text packageAuthor;

	private Composite container;

	private Text packageLocalDirectory;

	private Text packageLocalFilename;

	private Text versionText;

	private Text remotepathText;

	private Spinner revision;

	private SVProperties properties;

	public Set<String> getFiles() {
		return files;
	}

	public void setFiles(Set<String> files) {
		this.files = files;
	}

	public ConfigurationPage(Wizard parent) {
		super(parent, "Configuration page", PAGE_NAME);
	}

	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		container.setLayout(layout);

		properties = new SVProperties();
		try {
			properties.loadFile(System.getProperty("user.dir") + "/settings/tools.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;

		versionText = new Text(container, SWT.BORDER);
		versionText.setLayoutData(data);
		versionText.setText(properties.getProperty("version"));

		revision = new Spinner(container, SWT.BORDER);
		revision.setMinimum(0);
		revision.setSelection(Integer.valueOf(properties.getProperty("revision")));

		remotepathText = new Text(container, SWT.BORDER);
		remotepathText.setLayoutData(data);
		remotepathText.setText(properties.getProperty("path"));

		setContainer(container);
	}

	@Override
	public void onEnterPage() {
		
		UpdateXMLParser oldParser = new UpdateXMLParser();
		try {
			InputSource inputOld = new InputSource(new FileReader(((OldXMLPage) getWizard().getPage(OldXMLPage.PAGE_NAME)).getWindowsXMLFile()));
			oldParser.parseXmlSax(inputOld, null);
		} catch (FileNotFoundException e) {
			new BugReporter(getWizard().getShell()).openErrorMessage("Configuration Page" ,e);
		} catch (SAXException e) {
			new BugReporter(getWizard().getShell()).openErrorMessage("Configuration Page" ,e);
		} catch (IOException e) {
			new BugReporter(getWizard().getShell()).openErrorMessage("Configuration Page" ,e);
		}
		
		Map<String, Package> packages = oldParser.alPackages;
		Set<String> files = ((FilesPage) getWizard().getPage(FilesPage.PAGE_NAME)).getFiles();
		for (String filename : files) {
			addWidgetCollection(filename, packages);
		}
		
		
		
		container.layout();
		super.onEnterPage();
	}

	public void addWidgetCollection(String filename, Map<String, Package> packages) {
		File file = new File(filename);

		WidgetsCollection collection = new WidgetsCollection();
		collection.setFile(file);
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;

		packageName = new Text(container, SWT.BORDER);
		packageName.setLayoutData(data);
		packageName.setText(file.getName().replaceAll("(\\.|-|[0-9]).*", "").toLowerCase());
		collection.setPackageName(packageName);
		
		Package pkg = packages.get(packageName.getText());
		
		packageAuthor = new Text(container, SWT.BORDER);

		packageAuthor.setLayoutData(data);
		collection.setPackageAuthor(packageAuthor);
		if (pkg != null) {
			packageAuthor.setText(pkg.getAuthor());
		} else {
			packageAuthor.setText("rym3k");
		}

		packageLocalpath = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
		packageLocalpath.setItems(new String[] { "/", "/resources/", "/lib/", "/fonts/", "/sql/" });
		if (pkg != null) {
			packageLocalpath.setText(pkg.getLocalpath());
		} else {
			if (properties.getProperty(packageName.getText() + ".path") != null && !properties.getProperty(packageName.getText() + ".path").equals("")) {
				packageLocalpath.setText(properties.getProperty(packageName.getText() + ".path"));
			} else {
				packageLocalpath.setText("/lib/");
			}
		}

		collection.setPackageLocalpath(packageLocalpath);

		packageOS = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
		packageOS.setItems(new String[] { "", "windows/", "linux/", "mac/" });

//		if (pkg != null) {
//			if (pkg.getPath().contains("linux")) {
//				packageOS.setText("linux/");
//			} else if (pkg.getPath().contains("mac")) {
//				packageOS.setText("mac/");
//			} else if (pkg.getPath().contains("windows")){
//				packageOS.setText("windows/");
//			} else {
//				packageOS.setText("");
//			}
//		} else {
			if (file.getPath().contains("linux")) {
				packageOS.setText("linux/");
			} else if (file.getPath().contains("mac")) {
				packageOS.setText("mac/");
			} else if (file.getPath().contains("windows")){
				packageOS.setText("windows/");
			} else {
				packageOS.setText("");
			}
//		}

		collection.setPackageOS(packageOS);

		packageRevision = new Spinner(container, SWT.BORDER);
		packageRevision.setMinimum(0);
		if (pkg != null) {
			packageRevision.setSelection(pkg.getRevision());
		} else {
			if (properties.getProperty(packageName.getText() + ".revision") != null && !properties.getProperty(packageName.getText() + ".revision").equals("")) {
				packageRevision.setSelection(Integer.valueOf(properties.getProperty(packageName.getText() + ".revision")));
			} else {
				packageRevision.setSelection(0);
			}
		}
		collection.setPackageRevision(packageRevision);

		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 3;

		packageLocalDirectory = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		packageLocalDirectory.setLayoutData(data);
		packageLocalDirectory.setText(file.getParent());
		collection.setPackageLocalDirectory(packageLocalDirectory);

		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;

		packageLocalFilename = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		packageLocalFilename.setEnabled(false);
		packageLocalFilename.setLayoutData(data);
		packageLocalFilename.setText(file.getName());
		collection.setPackageLocalFilename(packageLocalFilename);

		widgetsMap.put(packageName.getText()+packageOS.getText(), collection);
	}
	
	public String getBasePath() {
		return remotepathText.getText();
	}

	public String getRevision() {
		return String.valueOf(revision.getSelection());
	}

	public String getVersion() {
		return versionText.getText();
	}

	public Map<String, WidgetsCollection> getWidgetsMap() {
		return widgetsMap;
	}

}
