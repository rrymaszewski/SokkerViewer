package pl.pronux.sokker.model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Money extends SVNumberFormat implements Comparable<Money>, Serializable {

	private static double currency;

	private static DecimalFormat moneyDoubleFormat;

	private static DecimalFormat moneyDoubleFormatWithSign;

	private static DecimalFormat moneyIntegerFormat;

	private static DecimalFormat moneyIntegerFormatWithSign;

	/**
	 *
	 */
	private static final long serialVersionUID = -4186936836672198485L;

	private static String symbol;

	static {
		// initialize money format
		moneyDoubleFormat = new DecimalFormat("###,###,###,##0.00"); //$NON-NLS-1$
		moneyIntegerFormat = new DecimalFormat("###,###,###,##0"); //$NON-NLS-1$
		moneyIntegerFormatWithSign = new DecimalFormat("'+'###,###,###,##0;'-'###,###,###,##0"); //$NON-NLS-1$
		moneyDoubleFormatWithSign = new DecimalFormat("'+'###,###,###,##0.00;'-'###,###,###,##0.00"); //$NON-NLS-1$
	}

	public static double calculatePrices(double value, double currencyFrom, double currencyTo) {
		return Double.valueOf(convertPricesToBase(value, currencyFrom) / currencyTo);
	}
	
	public static double calculatePrices(double value, double currency) {
		return Double.valueOf(value / currency).intValue();
	}

	public static int calculatePrices(int value) {
		return Double.valueOf(value / currency).intValue();
	}

	public static String convertMoneyFormatDoubleToInteger(double money) {
		return moneyIntegerFormat.format(Double.valueOf(money / currency).intValue());
	}

	public static double convertPricesToBase(double value) {
		return value * currency;
	}
	
	public static double convertPricesToBase(double value, double currency) {
		return value * currency;
	}

	public static String formatDouble(double value) {
		return moneyDoubleFormat.format(value);
	}

	public static String formatDoubleCurrency(double value) {
		return moneyDoubleFormat.format(value / currency);
	}

	public static String formatDoubleCurrencySymbol(double value) {
		return moneyDoubleFormat.format(value / currency) + " " + symbol; //$NON-NLS-1$
	}

	public static String formatDoubleSignCurrencySymbol(double value) {
		if(value == 0) {
			return moneyDoubleFormat.format(value / currency) + " " + symbol; //$NON-NLS-1$
		}
		return moneyDoubleFormatWithSign.format(value / currency) + " " + symbol; //$NON-NLS-1$
	}

	public static String formatInteger(double value) {
		return moneyIntegerFormat.format(value);
	}

	public static String formatIntegerCurrency(double value) {
		return moneyIntegerFormat.format(value / currency);
	}

	public static String formatIntegerCurrencySymbol(double value) {
		return moneyIntegerFormat.format(value / currency) + " " + symbol; //$NON-NLS-1$
	}

	public static String formatIntegerSignSymbol(double value) {
		return moneyIntegerFormat.format(value) + " " + symbol; //$NON-NLS-1$
	}

	public static String formatIntegerSingCurrencySymbol(int value) {
		if(value == 0) {
			return moneyIntegerFormat.format(value / currency) + " " + symbol; //$NON-NLS-1$
		}
		return moneyIntegerFormatWithSign.format(value / currency) + " " + symbol; //$NON-NLS-1$
	}

	public static String formatIntegerSingCurrency(int value) {
		if(value == 0) {
			return moneyIntegerFormat.format(value / currency) + " " + symbol; //$NON-NLS-1$
		}
		return moneyIntegerFormatWithSign.format(value / currency) + " " + symbol; //$NON-NLS-1$
	}

	public static void setCurrency(double currency) {
		Money.currency = currency;
	}

	public static void setCurrencySymbol(String symbol) {
		Money.symbol = symbol;
	}

	public static String getCurrencySymbol() {
		return Money.symbol;
	}
	
	private double value;

	public Money(double value) {
		this.value = value;
	}

	public Money(int value) {
		this.value = value;
	}

	public int calculatePrices() {
		return Double.valueOf(value / currency).intValue();
	}
	
	public static double getTaxIncome(double value, double tax) {
		return value * tax;
	}
	
	public static double getNettoValue(double brutto, double tax) {
		return brutto - (getTaxIncome(brutto, tax));
	}
	
	public static double getBruttoValue(double netto, double tax) { 
		if(tax != 1) {
			return netto / (1 - tax);	
		} else {
			return 0;
		}
	}

	public int compareTo(Money o) {
		if (this.value < o.value) {
			return -1;
		} else if (this.value > o.value) {
			return 1;
		}
		return 0;
	}

	public String formatDoubleCurrency() {
		return formatDoubleCurrency(value);
	}

	public String formatDoubleCurrencySymbol() {
		return formatDoubleCurrencySymbol(value);
	}

	public String formatDoubleSignCurrencySymbol() {
		return formatDoubleSignCurrencySymbol(value);
	}

	public String formatIntegerSignCurrencySymbol() {
		return formatIntegerSingCurrencySymbol(Double.valueOf(value).intValue());
	}

	public String formatInteger() {
		return formatInteger(value);
	}

	public String formatDouble() {
		return formatDouble(value);
	}

	public String formatIntegerCurrency() {
		return formatIntegerCurrency(value);
	}

	public String formatIntegerCurrencySymbol() {
		return formatIntegerCurrencySymbol(value);
	}

	public double getDoubleValue() {
		return value;
	}

	public int toInt() {
		return Double.valueOf(value).intValue();
	}

	public void setDoubleValue(double doubleValue) {
		this.value = doubleValue;
	}

	public void setIntValue(int value) {
		this.value = value;
	}

	public static double getCurrency() {
		return currency;
	}
}
