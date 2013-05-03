package pl.pronux.sokker.ui.widgets.composites;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.FlagsResources;

public class CountryChooser extends Shell {

	private Listener listener;

	private int id = FlagsResources.EMPTY_FLAG;

	private List<Label> labels = new ArrayList<Label>();

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CountryChooser(Shell parent, int style) {
		super(parent, style | SWT.PRIMARY_MODAL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 10;
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 5;
		layout.marginWidth = 5;
		this.setLayout(layout);
		this.setFont(ConfigBean.getFontMain());
		this.setForeground(Colors.getBlueDescription());

		listener = new Listener() {

			public void handleEvent(Event event) {
				switch (event.type) {

				case SWT.MouseExit:
				case SWT.Dispose:
					if (event.widget != null && event.widget instanceof Label && event.widget.getData("id") != null) { 
						Label label = (Label) event.widget;
						Integer id = (Integer) label.getData("id"); 
						label.setImage(FlagsResources.getFlagVeryLight(id));
					}
					break;
				case SWT.MouseMove:
				case SWT.KeyDown:
				case SWT.MouseHover:
					if (event.widget != null && event.widget instanceof Label && event.widget.getData("id") != null) { 
						Label label = (Label) event.widget;
						Integer id = (Integer) label.getData("id"); 
						label.setImage(FlagsResources.getFlag(id));
					}
					break;
				case SWT.MouseDown:
					if (event.widget != null && event.widget instanceof Label && event.widget.getData("id") != null) { 
						Label label = (Label) event.widget;
						Integer id = (Integer) label.getData("id"); 
						CountryChooser.this.setId(id);
						CountryChooser.this.close();
					}
					break;
				}

			}

		};
	}


	public void fill(List<Country> countries) {
		fill(countries, false);
	}

	public void fill(List<Country> countries, boolean emptyFlag) {
		Iterator<Label> itrLabel = labels.iterator();
		while (itrLabel.hasNext()) {
			Label label = itrLabel.next();
			label.dispose();
			labels.remove(itrLabel);
			itrLabel.remove();
		}
		Label countryLabel;
		if (emptyFlag) {
			countryLabel = new Label(this, SWT.NONE);
			this.labels.add(countryLabel);
			countryLabel.setToolTipText(""); 
			countryLabel.setData("id", FlagsResources.QUESTION_FLAG); 
			countryLabel.setImage(FlagsResources.getFlagVeryLight(FlagsResources.QUESTION_FLAG));
			countryLabel.addListener(SWT.Dispose, listener);
			countryLabel.addListener(SWT.KeyDown, listener);
			countryLabel.addListener(SWT.MouseMove, listener);
			countryLabel.addListener(SWT.MouseHover, listener);
			countryLabel.addListener(SWT.MouseExit, listener);
			countryLabel.addListener(SWT.MouseDown, listener);
		}

		countryLabel = new Label(this, SWT.NONE);
		this.labels.add(countryLabel);
		countryLabel.setToolTipText(""); 
		countryLabel.setData("id", FlagsResources.EMPTY_FLAG); 
		countryLabel.setImage(FlagsResources.getFlagVeryLight(FlagsResources.EMPTY_FLAG));
		countryLabel.addListener(SWT.Dispose, listener);
		countryLabel.addListener(SWT.KeyDown, listener);
		countryLabel.addListener(SWT.MouseMove, listener);
		countryLabel.addListener(SWT.MouseHover, listener);
		countryLabel.addListener(SWT.MouseExit, listener);
		countryLabel.addListener(SWT.MouseDown, listener);

		for (Country country : countries) {
			countryLabel = new Label(this, SWT.NONE);
			this.labels.add(countryLabel);
			countryLabel.setToolTipText(Messages.getString("country." + country.getCountryId() + ".name"));  
			countryLabel.setData("id", country.getCountryId()); 
			countryLabel.setImage(FlagsResources.getFlagVeryLight(country.getCountryId()));
			countryLabel.addListener(SWT.Dispose, listener);
			countryLabel.addListener(SWT.KeyDown, listener);
			countryLabel.addListener(SWT.MouseMove, listener);
			countryLabel.addListener(SWT.MouseHover, listener);
			countryLabel.addListener(SWT.MouseExit, listener);
			countryLabel.addListener(SWT.MouseDown, listener);
		}

		this.layout();

	}

	public void open(List<Country> countries, boolean emptyFlag) {
		fill(countries, emptyFlag);
		this.pack();
		Point cursor = this.getDisplay().getCursorLocation();
		this.setLocation(cursor.x + 10, cursor.y);
		super.open();

		while (!this.isDisposed()) {
			if (!this.getDisplay().readAndDispatch()) {
				this.getDisplay().sleep();
			}
		}
	}

	public void open(List<Country> countries) {
		open(countries, false);
	}

}
