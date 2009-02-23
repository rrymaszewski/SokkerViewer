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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


/**
 * A color registry with ability to cache requested colors. The colors acquired
 * from this class are disposed on release of the default {@link Display} instance.
 * 
 * @author Alexey Afanasyev
 * @author Alexey Kharlamov
 */
public final class ColorResources {

	private static Display display = Display.getDefault();

	private static Map<Integer, Color> mapIdToColor = new HashMap<Integer, Color>();

	private static Map<RGB, Color> mapRgbToColor = new HashMap<RGB, Color>();

	static {
		display.addListener(SWT.Dispose, new Listener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
			 */
			public void handleEvent(Event event) {
				disposeColors(mapIdToColor.values());
				disposeColors(mapRgbToColor.values());
			}
		});
	}

	/**
	 * 
	 * @param source
	 * @param percent
	 * @return
	 */
	public static Color darker(Color source, double percent) {
		double factor = 1.0 - percent / 100.0;
		Color result = new Color(Display.getDefault(),
				(int) (source.getRed() * factor),
				(int) (source.getGreen() * factor),
				(int) (source.getBlue() * factor));
		return result;
	}

	/**
	 * Returns the default color black.
	 * 
	 * @return the default color black
	 */
	public static Color getBlack() {
		return getSystemColor(SWT.COLOR_BLACK);
	}
	
	/**
	 * Returns the default color blue.
	 * 
	 * @return the default color blue
	 */
	public static Color getBlue() {
		return getSystemColor(SWT.COLOR_BLUE);
	}

	/**
	 * Returns a cached color for the given <code>red</code>,
	 * <code>green</code> and <code>blue</code> values.
	 * 
	 * @param red
	 *            the red component
	 * @param green
	 *            the green component
	 * @param blue
	 *            the blue component
	 * @return the desired color
	 */
	public static Color getColor(int red, int green, int blue) {
		return getColor(new RGB(red, green, blue));
	}

	/**
	 * Returns a cached color for the given <code>RGB</code> describing the
	 * desired red, green and blue values.
	 * 
	 * @param rgb
	 *            the RGB values of the desired color
	 * @return the desired color
	 */
	public static Color getColor(RGB rgb) {
		Color color = (Color) mapRgbToColor.get(rgb);
		if (color == null || color.isDisposed()) {
			color = new Color(display, rgb);
			mapRgbToColor.put(rgb, color);
		}
		return color;
	}

	/**
	 * Returns the default color cyan.
	 * 
	 * @return the default color cyan
	 */
	public static Color getCyan() {
		return getSystemColor(SWT.COLOR_CYAN);
	}

	/**
	 * Returns the default color dark blue.
	 * 
	 * @return the default color dark blue
	 */
	public static Color getDarkBlue() {
		return getSystemColor(SWT.COLOR_DARK_BLUE);
	}

	/**
	 * Returns the default color dark cyan.
	 * 
	 * @return the default color dark cyan
	 */
	public static Color getDarkCyan() {
		return getSystemColor(SWT.COLOR_DARK_CYAN);
	}

	/**
	 * Returns the default color dark gray.
	 * 
	 * @return the default color dark gray
	 */
	public static Color getDarkGray() {
		return getSystemColor(SWT.COLOR_DARK_GRAY);
	}

	/**
	 * Returns the default color dark green.
	 * 
	 * @return the default color dark green
	 */
	public static Color getDarkGreen() {
		return getSystemColor(SWT.COLOR_DARK_GREEN);
	}

	/**
	 * Returns the default color dark magenta.
	 * 
	 * @return the default color dark magenta
	 */
	public static Color getDarkMagenta() {
		return getSystemColor(SWT.COLOR_DARK_MAGENTA);
	}

	/**
	 * Returns the default color dark red.
	 * 
	 * @return the default color dark red
	 */
	public static Color getDarkRed() {
		return getSystemColor(SWT.COLOR_DARK_RED);
	}

	/**
	 * Returns the default color dark yellow.
	 * 
	 * @return the default color dark yellow
	 */
	public static Color getDarkYellow() {
		return getSystemColor(SWT.COLOR_DARK_YELLOW);
	}

	/**
	 * Returns the default color gray.
	 * 
	 * @return the default color gray
	 */
	public static Color getGray() {
		return getSystemColor(SWT.COLOR_GRAY);
	}

	/**
	 * Returns the default color green.
	 * 
	 * @return the default color green
	 */
	public static Color getGreen() {
		return getSystemColor(SWT.COLOR_GREEN);
	}

	/**
	 * Returns the system color used to paint tooltip background areas.
	 * 
	 * @return the desired color
	 */
	public static Color getInfoBackground() {
		return getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	}

	/**
	 * Returns the system color used to paint tooltip text.
	 * 
	 * @return the desired color
	 */
	public static Color getInfoForeground() {
		return getSystemColor(SWT.COLOR_INFO_FOREGROUND);
	}

	/**
	 * Returns the system color used to paint list background areas.
	 * 
	 * @return the desired color
	 */
	public static Color getListBackground() {
		return getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}

	/**
	 * Returns the system color used to paint list foreground areas.
	 * 
	 * @return the desired color
	 */
	public static Color getListForeground() {
		return getSystemColor(SWT.COLOR_LIST_FOREGROUND);
	}

	/**
	 * Returns the system color used to paint list selection background areas.
	 * 
	 * @return the desired color
	 */
	public static Color getListSelection() {
		return getSystemColor(SWT.COLOR_LIST_SELECTION);
	}

	/**
	 * Returns the system color used to paint list selected text.
	 * 
	 * @return the desired color
	 */
	public static Color getListSelectionText() {
		return getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
	}

	/**
	 * Returns the default color magenta.
	 * 
	 * @return the default color magenta
	 */
	public static Color getMagenta() {
		return getSystemColor(SWT.COLOR_MAGENTA);
	}

	/**
	 * Returns the default color red.
	 * 
	 * @return the default color red
	 */
	public static Color getRed() {
		return getSystemColor(SWT.COLOR_RED);
	}

	/**
	 * Returns a cached system color for the given <code>id</code>.
	 * 
	 * @param colorId
	 *            the color id
	 * @return the desired color
	 */
	public static Color getSystemColor(int colorId) {
		Integer id = colorId;
		Color color = (Color) mapIdToColor.get(id);
		if (color == null || color.isDisposed()) {
			color = display.getSystemColor(colorId);
			mapIdToColor.put(id, color);
		}
		return color;
	}

	/**
	 * Returns the system color used to paint title background areas.
	 * 
	 * @return the desired color
	 */
	public static Color getTitleBackground() {
		return getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
	}

	/**
	 * Returns the system color used to paint title background gradient.
	 * 
	 * @return the desired color
	 */
	public static Color getTitleBackgroundGradient() {
		return getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	}

	/**
	 * Returns the system color used to paint title text.
	 * 
	 * @return the desired color
	 */
	public static Color getTitleForeground() {
		return getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
	}

	/**
	 * Returns the system color used to paint inactive title background areas.
	 * 
	 * @return the desired color
	 */
	public static Color getTitleInactiveBackground() {
		return getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
	}

	/**
	 * Returns the system color used to paint inactive title background
	 * gradient.
	 * 
	 * @return the desired color
	 */
	public static Color getTitleInactiveBackgroundGradient() {
		return getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT);
	}

	/**
	 * Returns the system color used to paint inactive title text.
	 * 
	 * @return the desired color
	 */
	public static Color getTitleInactiveForeground() {
		return getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
	}

	/**
	 * Returns the default color white.
	 * 
	 * @return the default color white
	 */
	public static Color getWhite() {
		return getSystemColor(SWT.COLOR_WHITE);
	}

	/**
	 * Returns the system color used to paint background areas.
	 * 
	 * @return the desired color
	 */
	public static Color getWidgetBackground() {
		return getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	}

	/**
	 * Returns the system color used to paint border areas.
	 * 
	 * @return the desired color
	 */
	public static Color getWidgetBorder() {
		return getSystemColor(SWT.COLOR_WIDGET_BORDER);
	}

	/**
	 * Returns the system color used to paint dark shadow areas.
	 * 
	 * @return the desired color
	 */
	public static Color getWidgetDarkShadow() {
		return getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
	}

	/**
	 * Returns the system color used to paint foreground areas.
	 * 
	 * @return the desired color
	 */
	public static Color getWidgetForeground() {
		return getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
	}

	/**
	 * Returns the system color used to paint highlight shadow areas.
	 * 
	 * @return the desired color
	 */
	public static Color getWidgetHighlightShadow() {
		return getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	}

	/**
	 * Returns the system color used to paint light shadow areas.
	 * 
	 * @return the desired color
	 */
	public static Color getWidgetLightShadow() {
		return getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
	}

	/**
	 * Returns the system color used to paint normal shadow areas.
	 * 
	 * @return the desired color
	 */
	public static Color getWidgetNormalShadow() {
		return getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	}

	/**
	 * Returns the default color yellow.
	 * 
	 * @return the default color yellow
	 */
	public static Color getYellow() {
		return getSystemColor(SWT.COLOR_YELLOW);
	}

	private static void disposeColors(Collection<Color> colors) {
		for (Color color : colors) {
			if (color != null && !color.isDisposed()) {
				color.dispose();
			}
		}
	}
	
	public static Color getBlueDescription() {
		return getColor(0, 70, 213);
	}

	

	/**
	 * Private constructor to block instantiation.
	 */
	private ColorResources() {
	}

	public static String rgb2hex(Color color) {
		return ColorResources.rgb2hex(color.getRGB());
	}

	public static String rgb2hex(RGB rgb) {
		String hex = "#"; //$NON-NLS-1$
	
		if(Integer.toHexString(rgb.red).length() == 1) {
			hex+= 0;
		}
		hex += Integer.toHexString(rgb.red);
	
		if(Integer.toHexString(rgb.green).length() == 1) {
			hex+= 0;
		}
		hex += Integer.toHexString(rgb.green);
	
		if(Integer.toHexString(rgb.blue).length() == 1) {
			hex+= 0;
		}
		hex += Integer.toHexString(rgb.blue);
	
		return hex.toUpperCase();
	}
}
