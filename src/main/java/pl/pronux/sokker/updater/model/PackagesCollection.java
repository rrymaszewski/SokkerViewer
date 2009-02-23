package pl.pronux.sokker.updater.model;

import java.util.ArrayList;
import java.util.List;


public class PackagesCollection {
	private List<Package> packages;
	private String info;

	public List<Package> getPackages() {
		return packages;
	}

	public PackagesCollection() {
		this.info = ""; //$NON-NLS-1$
		this.packages = new ArrayList<Package>();
	}

	public void setPackages(List<Package> packages) {
		this.packages = packages;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
