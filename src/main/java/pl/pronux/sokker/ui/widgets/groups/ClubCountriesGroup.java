package pl.pronux.sokker.ui.widgets.groups;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.FlagsResources;

public class ClubCountriesGroup extends Group {

	private List<Label> labels = new ArrayList<Label>();

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public ClubCountriesGroup(Composite parent, int style) {
		super(parent, style);
		RowLayout layout = new RowLayout();
		layout.justify = false;
		layout.pack = true;
		layout.wrap = true;
		this.setLayout(layout);
		this.setFont(ConfigBean.getFontMain());
		this.setForeground(Colors.getBlueDescription());
	}

	public void fill(Set<Integer> visitedCountries, List<Country> countries) {
		Iterator<Label> itrLabel = labels.iterator();
		while(itrLabel.hasNext()) {
			Label label = itrLabel.next(); 
			label.dispose();
			labels.remove(itrLabel);
			itrLabel.remove();
		}
		
		Label countryLabel;
		for(Country country : countries) {
			countryLabel = new Label(this, SWT.NONE);
			this.labels.add(countryLabel);
			countryLabel.setToolTipText(country.getName());
			if(visitedCountries.contains(country.getCountryId())) {
				countryLabel.setImage(FlagsResources.getFlag(country.getCountryId()));
			} else {
				countryLabel.setImage(FlagsResources.getFlagVeryLight(country.getCountryId()));
			}
		}
		this.layout();
	}
}
