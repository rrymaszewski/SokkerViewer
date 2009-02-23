package pl.pronux.tools.pages;

import java.io.File;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class WidgetsCollection {
	private Text packageName;
	private Combo packageOS;
	private Spinner packageRevision;

	private Combo packageLocalpath;

	private Text packageAuthor;

	private Composite container;

	private Text packageLocalDirectory;

	private Text packageLocalFilename;
	
	private File file;

	public Text getPackageName() {
		return packageName;
	}

	public void setPackageName(Text packageName) {
		this.packageName = packageName;
	}

	public Combo getPackageOS() {
		return packageOS;
	}

	public void setPackageOS(Combo packageOS) {
		this.packageOS = packageOS;
	}

	public Spinner getPackageRevision() {
		return packageRevision;
	}

	public void setPackageRevision(Spinner packageRevision) {
		this.packageRevision = packageRevision;
	}

	public Combo getPackageLocalpath() {
		return packageLocalpath;
	}

	public void setPackageLocalpath(Combo packageLocalpath) {
		this.packageLocalpath = packageLocalpath;
	}

	public Text getPackageAuthor() {
		return packageAuthor;
	}

	public void setPackageAuthor(Text packageAuthor) {
		this.packageAuthor = packageAuthor;
	}

	public Composite getContainer() {
		return container;
	}

	public void setContainer(Composite container) {
		this.container = container;
	}

	public Text getPackageLocalDirectory() {
		return packageLocalDirectory;
	}

	public void setPackageLocalDirectory(Text packageLocalDirectory) {
		this.packageLocalDirectory = packageLocalDirectory;
	}

	public Text getPackageLocalFilename() {
		return packageLocalFilename;
	}

	public void setPackageLocalFilename(Text packageLocalFilename) {
		this.packageLocalFilename = packageLocalFilename;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
