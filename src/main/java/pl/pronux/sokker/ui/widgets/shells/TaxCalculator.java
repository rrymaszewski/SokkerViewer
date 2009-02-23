package pl.pronux.sokker.ui.widgets.shells;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.listeners.VerifyMoneyListener;

public class TaxCalculator extends Shell {
	final private static String ORIGINAL_CLUB = "5.0"; //$NON-NLS-1$

	final private static String TAX = "4.0"; //$NON-NLS-1$

	final private static int BRUTTO = 1;

	final private static int NETTO = 2;

	private int lastState = BRUTTO;

	private Text bruttoText;

	private Text nettoText;

	private Text taxPaymentText;

	private Text originalClubPaymentText;

	public TaxCalculator(Shell parent, int style) {
		super(parent, style);

		this.setLayout(new FormLayout());
		this.setText(Messages.getString("taxcalculator.title")); //$NON-NLS-1$
		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				int value;
				switch (event.type) {
				case SWT.Traverse:
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
					if(event.widget.equals(bruttoText)) {
						lastState = BRUTTO;
					} else if(event.widget.equals(nettoText)) {
						lastState = NETTO;
					}
					
					break;
				}
			}
		};
		FormData formData;

		formData = new FormData(0, 0);
		formData.left = new FormAttachment(50, 0);
		formData.top = new FormAttachment(0, 10);

		Label centerPoint = new Label(this, SWT.NONE);
		centerPoint.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, -5);
		formData.top = new FormAttachment(centerPoint, 0);

		CLabel bruttoLabel = new CLabel(this, SWT.NONE);
		bruttoLabel.setLayoutData(formData);
		bruttoLabel.setText(Messages.getString("taxcalculator.value.brutto")); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(bruttoLabel, 5);
		formData.right = new FormAttachment(100, -30);
		formData.top = new FormAttachment(centerPoint, 5);

		bruttoText = new Text(this, SWT.BORDER | SWT.RIGHT);
		bruttoText.setLayoutData(formData);
		bruttoText.setText("0"); //$NON-NLS-1$

		bruttoText.addListener(SWT.FocusOut, listener);
		bruttoText.addListener(SWT.Traverse, listener);
		bruttoText.addListener(SWT.Verify, listener);
		bruttoText.addListener(SWT.Modify, listener);

		formData = new FormData();
		formData.left = new FormAttachment(bruttoText, 5);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(centerPoint, 5);

		CLabel bruttoDescLabel = new CLabel(this, SWT.LEFT);
		bruttoDescLabel.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, -5);
		formData.top = new FormAttachment(bruttoText, 5);

		CLabel nettoLabel = new CLabel(this, SWT.NONE);
		nettoLabel.setLayoutData(formData);
		nettoLabel.setText(Messages.getString("taxcalculator.value.netto")); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(nettoLabel, 5);
		formData.right = new FormAttachment(100, -30);
		formData.top = new FormAttachment(bruttoText, 5);

		nettoText = new Text(this, SWT.BORDER | SWT.RIGHT);
		nettoText.setLayoutData(formData);
		nettoText.setText("0"); //$NON-NLS-1$

		nettoText.addListener(SWT.FocusOut, listener);
		nettoText.addListener(SWT.Traverse, listener);
		nettoText.addListener(SWT.Verify, listener);
		nettoText.addListener(SWT.Modify, listener);

		formData = new FormData();
		formData.left = new FormAttachment(nettoText, 5);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(bruttoText, 5);

		CLabel nettoDescLabel = new CLabel(this, SWT.LEFT);
		nettoDescLabel.setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, -5);
		formData.top = new FormAttachment(nettoText, 5);

		CLabel taxPaymentLabel = new CLabel(this, SWT.NONE);
		taxPaymentLabel.setLayoutData(formData);
		taxPaymentLabel.setText(Messages.getString("taxcalculator.tax.payment")); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(taxPaymentLabel, 5);
		formData.right = new FormAttachment(100, -30);
		formData.top = new FormAttachment(nettoText, 5);

		taxPaymentText = new Text(this, SWT.BORDER | SWT.RIGHT | SWT.READ_ONLY);
		taxPaymentText.setLayoutData(formData);
		taxPaymentText.setText("0"); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(taxPaymentText, 5);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(nettoText, 5);

		CLabel taxPaymentDescLabel = new CLabel(this, SWT.LEFT);
		taxPaymentDescLabel.setLayoutData(formData);
		
		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, -5);
		formData.top = new FormAttachment(taxPaymentLabel, 5);

		CLabel originalClubPaymentLabel = new CLabel(this, SWT.NONE);
		originalClubPaymentLabel.setLayoutData(formData);
		originalClubPaymentLabel.setText(Messages.getString("taxcalculator.originalclub.payment")); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(originalClubPaymentLabel, 5);
		formData.right = new FormAttachment(100, -30);
		formData.top = new FormAttachment(taxPaymentLabel, 5);

		originalClubPaymentText = new Text(this, SWT.BORDER | SWT.RIGHT | SWT.READ_ONLY);
		originalClubPaymentText.setLayoutData(formData);
		originalClubPaymentText.setText("0"); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(originalClubPaymentText, 5);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(nettoText, 5);

		CLabel originalClubPaymentDescLabel = new CLabel(this, SWT.LEFT);
		originalClubPaymentDescLabel.setLayoutData(formData);
		originalClubPaymentDescLabel.setText("%"); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, -5);
		formData.top = new FormAttachment(originalClubPaymentText, 5);

		CLabel taxLabel = new CLabel(this, SWT.NONE);
		taxLabel.setLayoutData(formData);
		taxLabel.setText(Messages.getString("taxcalculator.tax.fee")); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(taxLabel, 5);
		formData.right = new FormAttachment(100, -30);
		formData.top = new FormAttachment(originalClubPaymentText, 5);

		final Text taxText = new Text(this, SWT.BORDER | SWT.RIGHT);
		taxText.setLayoutData(formData);
		taxText.addListener(SWT.Verify, new VerifyMoneyListener());
		taxText.setText(TAX);

		formData = new FormData();
		formData.left = new FormAttachment(taxText, 5);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(originalClubPaymentText, 5);

		CLabel taxDescLabel = new CLabel(this, SWT.LEFT);
		taxDescLabel.setLayoutData(formData);
		taxDescLabel.setText("%"); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, -5);
		formData.top = new FormAttachment(taxText, 5);

		CLabel originalClubLabel = new CLabel(this, SWT.NONE);
		originalClubLabel.setLayoutData(formData);
		originalClubLabel.setText(Messages.getString("taxcalculator.originalclub.fee")); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(originalClubLabel, 5);
		formData.right = new FormAttachment(100, -30);
		formData.top = new FormAttachment(taxText, 5);

		final Text originalClubText = new Text(this, SWT.BORDER | SWT.RIGHT);
		originalClubText.setLayoutData(formData);
		originalClubText.addListener(SWT.Verify, new VerifyMoneyListener());
		originalClubText.setText(ORIGINAL_CLUB);

		formData = new FormData();
		formData.left = new FormAttachment(originalClubText, 5);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(taxText, 5);

		CLabel originalClubDescLabel = new CLabel(this, SWT.LEFT);
		originalClubDescLabel.setLayoutData(formData);
		originalClubDescLabel.setText("%"); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(originalClubText, 5);

		final Button originalClubButton = new Button(this, SWT.CHECK);
		originalClubButton.setLayoutData(formData);
		originalClubButton.setText(Messages.getString("taxcalculator.originalclub.check")); //$NON-NLS-1$
		originalClubButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (originalClubButton.getSelection()) {
					originalClubText.setEnabled(false);
					originalClubPaymentText.setEnabled(false);
				} else {
					originalClubText.setEnabled(true);
					originalClubPaymentText.setEnabled(true);
				}
			}
		});

		formData = new FormData();
		formData.right = new FormAttachment(50, -5);
		formData.top = new FormAttachment(originalClubButton, 5);

		Button countButton = new Button(this, SWT.PUSH);
		countButton.setLayoutData(formData);
		countButton.setText(Messages.getString("button.count")); //$NON-NLS-1$
		countButton.pack();
		countButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				int brutto;
				int netto;
				double taxFee;
				double originalClubFee;
				int taxCharge;
				int originalClubCharge;
				
				try {
					String text = bruttoText.getText().replaceAll("[^0-9]", ""); //$NON-NLS-1$ //$NON-NLS-2$
					brutto = Integer.valueOf(text);
				} catch (NumberFormatException ex) {
					brutto = 0;
				}
				try {
					String text = nettoText.getText().replaceAll("[^0-9]", ""); //$NON-NLS-1$ //$NON-NLS-2$
					netto = Integer.valueOf(text);
				} catch (NumberFormatException ex) {
					netto = 0;
				}
				try {
					taxFee = Double.valueOf(taxText.getText())/100;
				} catch (NumberFormatException ex) {
					taxFee = 0.04;
				}
				try {
					originalClubFee = Double.valueOf(originalClubText.getText())/100;
				} catch (NumberFormatException ex) {
					originalClubFee = 0.05;
				}

				if (originalClubButton.getSelection()) {
					if (lastState == BRUTTO) {
						netto = Double.valueOf(Money.getNettoValue(brutto, taxFee)).intValue();
					} else {
						brutto = Double.valueOf(Money.getBruttoValue(netto, taxFee)).intValue();
					}
					originalClubCharge = 0;
					taxCharge = Double.valueOf(Money.getTaxIncome(brutto, taxFee)).intValue();
				} else {
					if (lastState == BRUTTO) {
						netto = Double.valueOf(Money.getNettoValue(brutto, taxFee + originalClubFee)).intValue();
					} else {
						brutto = Double.valueOf(Money.getBruttoValue(netto, taxFee + originalClubFee)).intValue();
					}
					taxCharge = Double.valueOf(Money.getTaxIncome(brutto, taxFee)).intValue();
					originalClubCharge = Double.valueOf(Money.getTaxIncome(brutto, originalClubFee)).intValue();
				}
				
				nettoText.setText(SVNumberFormat.formatInteger(netto));
				bruttoText.setText(SVNumberFormat.formatInteger(brutto));
				taxPaymentText.setText(SVNumberFormat.formatInteger(taxCharge));
				originalClubPaymentText.setText(SVNumberFormat.formatInteger(originalClubCharge));
			}
		});

		formData = new FormData();
		formData.left = new FormAttachment(50, 5);
		formData.top = new FormAttachment(originalClubButton, 5);

		Button resetButton = new Button(this, SWT.PUSH);
		resetButton.setLayoutData(formData);
		resetButton.setText(Messages.getString("button.reset")); //$NON-NLS-1$
		resetButton.pack();
		resetButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				nettoText.setText("0"); //$NON-NLS-1$
				bruttoText.setText("0"); //$NON-NLS-1$
				originalClubPaymentText.setText("0"); //$NON-NLS-1$
				taxPaymentText.setText("0"); //$NON-NLS-1$
				taxText.setText(TAX);
				originalClubText.setText(ORIGINAL_CLUB);

			}
		});

		this.setDefaultButton(countButton);

		// this.pack();
		this.setSize(320, 300);
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}
}
