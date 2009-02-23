package pl.pronux.sokker.ui.widgets.shells;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.ui.handlers.DisplayHandler;

public class CurrencyCalculator extends Shell {
	private CLabel toCurrenclyLabel;

	private Combo fromCountryCombo;

	private CLabel fromCurrenclyLabel;

	private Text fromValueText;

	private Text toValueText;

	private Combo toCountryCombo;

	private List<Country> countries;
	
	final private static int FROM = 0;
	final private static int TO = 1;
	private static int state = FROM;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}
	
	private String count(String from, int indexFrom, int indexTo) {
		int value;
		try {
			value = Integer.valueOf(from.replaceAll("[^0-9]", ""));	 //$NON-NLS-1$ //$NON-NLS-2$
		} catch(NumberFormatException ex) {
			value = 0;
		}
		
		if (indexFrom > -1 && indexTo > -1) {
			double converted = Money.calculatePrices(value, countries.get(indexFrom).getCurrencyRate(), countries.get(indexTo).getCurrencyRate());
			return SVNumberFormat.formatInteger(Double.valueOf(converted).intValue());
		} else {
			return "0"; //$NON-NLS-1$
		}
	}

	public CurrencyCalculator(Shell parent, int style) {
		super(parent, style);

		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				int value;
				switch (event.type) {
				case SWT.Traverse:
				case SWT.FocusIn:
					if(event.widget.equals(fromValueText)) {
						state = FROM;
					} else {
						state = TO;
					}
					break;
				case SWT.FocusOut:
					if (event.widget instanceof Text) {
						Text text = (Text) event.widget;
						if (text != null) {
							try {
								String string = text.getText().replaceAll("[^0-9]", ""); //$NON-NLS-1$ //$NON-NLS-2$
								value = Integer.valueOf(string);
							} catch (NumberFormatException ex) {
								value = 0;
							}
							text.setText(SVNumberFormat.formatInteger(value));
						}
					}
					break;
				case SWT.Verify:
					String string = event.text;
					char[] chars = new char[string.length()];
					string.getChars(0, chars.length, chars, 0);
					for (int j = 0; j < chars.length; j++) {
						if (!('0' <= chars[j] && chars[j] <= '9') && !(Character.getNumericValue(chars[j]) == -1)) {
							event.doit = false;
							return;
						}
					}
					break;
				case SWT.Modify:
					if (event.widget.equals(fromValueText) && state == FROM) {
						
							Text combo = (Text) event.widget;
							toValueText.setText(count(combo.getText(), fromCountryCombo.getSelectionIndex(), toCountryCombo.getSelectionIndex()));
							
					} else if(event.widget.equals(toValueText) && state == TO) {
							Text combo = (Text) event.widget;
							fromValueText.setText(count(combo.getText(), toCountryCombo.getSelectionIndex(), fromCountryCombo.getSelectionIndex()));							
					}

					break;
				}
			}
		};

		Listener currencyListener = new Listener() {

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection:
					if (event.widget instanceof Combo) {
						Combo combo = (Combo) event.widget;
						int index = combo.getSelectionIndex();
						if (index > -1) {
							if(combo.equals(fromCountryCombo)) {
								state = TO;
								fromCurrenclyLabel.setText(countries.get(index).getCurrencyName());	
								fromValueText.setText(count(toValueText.getText(), toCountryCombo.getSelectionIndex(), fromCountryCombo.getSelectionIndex()));
							} else if (combo.equals(toCountryCombo)) {
								state = FROM;
								toCurrenclyLabel.setText(countries.get(index).getCurrencyName());
								toValueText.setText(count(fromValueText.getText(), fromCountryCombo.getSelectionIndex(), toCountryCombo.getSelectionIndex()));
							}
						}
					}
					break;

				}

			}

		};

		this.setLayout(new FormLayout());
		this.setSize(new Point(400, 130));

		FormData formData = new FormData(0, 0);
		formData.left = new FormAttachment(50, 0);
		formData.top = new FormAttachment(0, 10);
		Label point = new Label(this, SWT.NONE);
		point.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(point, 0, SWT.CENTER);
		formData.top = new FormAttachment(point, 0);
		formData.width = 15;

		CLabel label = new CLabel(this, SWT.CENTER);
		label.setText("="); //$NON-NLS-1$
		label.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(point, 0);
		formData.right = new FormAttachment(label, -35);

		fromValueText = new Text(this, SWT.BORDER);
		fromValueText.setLayoutData(formData);
		fromValueText.addListener(SWT.Verify, listener);
		fromValueText.addListener(SWT.Modify, listener);
		fromValueText.addListener(SWT.FocusOut, listener);
		fromValueText.addListener(SWT.FocusIn, listener);

		formData = new FormData();
		formData.left = new FormAttachment(fromValueText, 0);
		formData.top = new FormAttachment(point, 0);
		formData.right = new FormAttachment(label, -5);

		fromCurrenclyLabel = new CLabel(this, SWT.CENTER);
		fromCurrenclyLabel.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(fromValueText, 10);
		formData.right = new FormAttachment(label, -10);

		fromCountryCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		fromCountryCombo.setLayoutData(formData);
		fromCountryCombo.addListener(SWT.Selection, currencyListener);

		formData = new FormData();
		formData.left = new FormAttachment(label, 10);
		formData.top = new FormAttachment(point, 0);
		formData.right = new FormAttachment(100, -30);

		toValueText = new Text(this, SWT.BORDER);
		toValueText.setLayoutData(formData);
		toValueText.addListener(SWT.Verify, listener);
		toValueText.addListener(SWT.Modify, listener);
		toValueText.addListener(SWT.FocusOut, listener);
		toValueText.addListener(SWT.FocusIn, listener);

		formData = new FormData();
		formData.left = new FormAttachment(toValueText, 0);
		formData.top = new FormAttachment(point, 0);
		formData.right = new FormAttachment(100, -5);

		toCurrenclyLabel = new CLabel(this, SWT.CENTER);
		toCurrenclyLabel.setLayoutData(formData);
		toCurrenclyLabel.addListener(SWT.Selection, currencyListener);

		formData = new FormData();
		formData.left = new FormAttachment(label, 10);
		formData.top = new FormAttachment(toValueText, 10);
		formData.right = new FormAttachment(100, -5);

		toCountryCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		toCountryCombo.setLayoutData(formData);
		toCountryCombo.addListener(SWT.Selection, currencyListener);

		Rectangle splashRect = this.getBounds();
		Rectangle displayRect = DisplayHandler.getDisplay().getPrimaryMonitor().getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		this.setLocation(x, y);

		countries = Cache.getCountries();
		
		if (countries != null) {
			for (int i = 0; i < countries.size(); i++) {
				fromCountryCombo.add(countries.get(i).getName());
				toCountryCombo.add(countries.get(i).getName());
			}
			
			if(countries.size() > 0 ) {
				fromCountryCombo.select(0);
				toCountryCombo.select(0);
				fromCurrenclyLabel.setText(countries.get(0).getCurrencyName());
				toCurrenclyLabel.setText(countries.get(0).getCurrencyName());
			}
		}

	}
}
