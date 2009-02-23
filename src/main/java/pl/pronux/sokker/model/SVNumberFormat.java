package pl.pronux.sokker.model;

import java.text.DecimalFormat;

public class SVNumberFormat {
	private static DecimalFormat integerFormatWithSign;

	private static DecimalFormat integerFormat;

	private static DecimalFormat doubleFormatWithSign;

	private static DecimalFormat doubleFormat;

	static {
		integerFormatWithSign = new DecimalFormat("'+'###,###,###,##0;'-'###,###,###,##0"); //$NON-NLS-1$
		integerFormat = new DecimalFormat("###,###,###,##0"); //$NON-NLS-1$
		doubleFormatWithSign = new DecimalFormat("'+'###,###,###,##0.00;'-'###,###,###,##0.00"); //$NON-NLS-1$
		doubleFormat = new DecimalFormat("###,###,###,##0.00"); //$NON-NLS-1$
	}

	public static DecimalFormat getIntegerFormatWithSign() {
		return integerFormatWithSign;
	}

	public static void setIntegerFormatWithSign(DecimalFormat integerFormatWithSign) {
		SVNumberFormat.integerFormatWithSign = integerFormatWithSign;
	}

	public static String formatIntegerWithSignZero(int value) {
		if (value == 0) {
			return "0"; //$NON-NLS-1$
		} else {
			return integerFormatWithSign.format(value);
		}
	}

	public static String formatIntegerWithSign(int value) {
		return integerFormatWithSign.format(value);
	}

	public static String formatDouble(double value) {
		return doubleFormat.format(value);
	}

	public static String formatDoubleWithSign(double value) {
		if (value == 0) {
			return "0"; //$NON-NLS-1$
		} else {
			return doubleFormatWithSign.format(value);
		}
	}

	public static String formatInteger(int value) {
		return integerFormat.format(value);
	}

	public static DecimalFormat getIntegerFormat() {
		return integerFormat;
	}

	public static void setIntegerFormat(DecimalFormat integerFormat) {
		SVNumberFormat.integerFormat = integerFormat;
	}

	public static DecimalFormat getDoubleFormat() {
		return doubleFormat;
	}

	public static void setDoubleFormat(DecimalFormat doubleFormat) {
		SVNumberFormat.doubleFormat = doubleFormat;
	}

	public static DecimalFormat getDoubleFormatWithSign() {
		return doubleFormatWithSign;
	}

	public static void setDoubleFormatWithSign(DecimalFormat doubleFormatWithSign) {
		SVNumberFormat.doubleFormatWithSign = doubleFormatWithSign;
	}
}
