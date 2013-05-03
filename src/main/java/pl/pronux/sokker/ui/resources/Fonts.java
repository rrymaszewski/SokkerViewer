/*******************************************************************************
 * Copyright (c) 2004, 2006 Plum Canary Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Plum Canary Corporation - initial API and implementation
 * 
 * $Id$
 * $HeadURL$
 *******************************************************************************/
package pl.pronux.sokker.ui.resources;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

/**
 * The utility class that provides miscellaneous {@link Font}operations.
 * 
 * @author Alexey Afanasyev
 * @author Alexey Kharlamov
 */
public final class Fonts {
	private static class FDArray {
		private FontData[] fontData;

		public FDArray(FontData[] fontData) {
			super();
			this.fontData = fontData;
		}

		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof FDArray) {
				FDArray other = (FDArray) obj;
				return Arrays.equals(this.fontData, other.fontData);
			}
			return false;
		}

		public int hashCode() {
			int hash = fontData[0].hashCode();
			for (int i = 1; i < fontData.length; i++) {
				hash ^= fontData[i].hashCode();
			}
			return hash;
		}
	}

	private static Shell hiddenShell;

	private static Font defaultFont;

	private static Font appliedFont;

	private static GC gc;

	private static Map<Device, Map<FDArray, Font>> mapDeviceToMapFontDataToFont = new HashMap<Device, Map<FDArray, Font>>();

	private static FontMetrics metrics;

	/**
	 * Returns the bold fond based on the given font.
	 * 
	 * @param device
	 *            the device to create the font on
	 * @param fontData
	 *            the original font data array
	 */
	public static Font getBoldFont(Device device, FontData[] fontData) {
		FontData[] boldData = new FontData[fontData.length];

		for (int i = 0; i < boldData.length; i++) {
			FontData data = fontData[i];
			boldData[i] = new FontData(data.getName(), data.getHeight(), data
					.getStyle()
					| SWT.BOLD);
		}

		return getFont(device, boldData);
	}
	
	public static Font getFont(Device device, String name, double height, int style) {
		return getFont(device, new FontData[] { new FontData(name, (int)height, style) } );
	}

	/**

	 * returns default system font.
	 * 
	 * @return the default system font.
	 */
	public static Font getDefault() {
		if (defaultFont == null) {
			defaultFont = getShell().getFont();
		}
		return defaultFont;
	}

	/**
	 * returns font object for the given device/description pair. The font is
	 * also cached for later use.
	 * 
	 * @param device
	 *            the device to allocate font for.
	 * @param fontData
	 *            the font description object.
	 * @return the font.
	 */
	public static Font getFont(Device device, FontData[] fontData) {
		Map<FDArray, Font> mapFontDataToFont = mapDeviceToMapFontDataToFont.get(device);
		if (mapFontDataToFont == null) {
			mapFontDataToFont = new HashMap<FDArray, Font>();
			mapDeviceToMapFontDataToFont.put(device, mapFontDataToFont);
		}

		FDArray wrapper = new FDArray(fontData);
		Font font = (Font) mapFontDataToFont.get(wrapper);
		if (font == null || font.isDisposed()) {
			font = new Font(device, fontData);
			mapFontDataToFont.put(wrapper, font);
		}

		return font;
	}

	/**
	 * Returns the FontMetrics associated with the passed Font.
	 * 
	 * @param font
	 *            the font
	 * @return the FontMetrics for the given font
	 * @see GC#getFontMetrics()
	 */
	public static FontMetrics getFontMetrics(Font font) {
		setFont(font);
		if (metrics == null) {
			metrics = getGC().getFontMetrics();
		}
		return metrics;
	}

	/**
	 * Returns the extent of the String <code>text</code> using the font
	 * <code>font</code>. Tab expansion and carriage return processing are
	 * performed.
	 * 
	 * @param text
	 *            the text string
	 * @param font
	 *            the font
	 * @return the text's extent
	 * @see GC#textExtent(String)
	 */
	public static Point getTextExtent(String text, Font font) {
		setFont(font);
		return getGC().textExtent(text);
	}

	/**
	 * Returns the width of <code>text</code> in Font <code>font</code>.
	 * 
	 * @param text
	 *            the text string
	 * @param font
	 *            the font
	 * @return the width
	 */
	public static int getTextWidth(String text, Font font) {
		return getTextExtent(text, font).x;
	}

	private static GC getGC() {
		if (gc == null) {
			gc = new GC(new Shell());
			appliedFont = gc.getFont();
		}
		return gc;
	}

	private static Shell getShell() {
		if (hiddenShell == null) {
			hiddenShell = new Shell();
		}
		return hiddenShell;
	}

	private static void setFont(Font font) {
		if (font == appliedFont || font.equals(appliedFont)) {
			return;
		}

		getGC().setFont(font);
		appliedFont = font;
		metrics = null;
	}

	/**
	 * Private constructor to block instantiation.
	 */
	private Fonts() {
	}
}
