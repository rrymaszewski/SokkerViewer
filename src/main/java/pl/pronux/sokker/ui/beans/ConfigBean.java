package pl.pronux.sokker.ui.beans;

import java.util.Properties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;

public class ConfigBean {
	private static Color colorDecrease;

	private static Color colorDecreaseDescription;

	private static Color colorError;

	private static Color colorFont;

	private static Color colorIncrease;

	private static Color colorIncreaseDescription;

	private static Color colorInjuryBg;

	private static Color colorInjuryFg;

	private static Color colorNewTableObject;

	private static Color colorNewTreeObject;

	private static Color colorTrainedJunior;

	private static Color colorTransferList;
	
	private static Font fontCurier;

	private static Font fontCurrent;

	private static Font fontItalic;

	private static Font fontMain;

	private static Font fontTable;

	public static Color getColorFont() {
		return colorFont;
	}

	public static Color getColorIncrease() {
		return colorIncrease;
	}

	public static Color getColorIncreaseDescription() {
		return colorIncreaseDescription;
	}

	public static Color getColorNewTreeObject() {
		return colorNewTreeObject;
	}

	public static Color getColorNewTableObject() {
		return colorNewTableObject;
	}

	public static Color getColorError() {
		return colorError;
	}

	public static Color getColorInjuryBg() {
		return colorInjuryBg;
	}

	public static Color getColorTrainedJunior() {
		return colorTrainedJunior;
	}

	public static Color getColorTransferList() {
		return colorTransferList;
	}

	public static Color getColorInjuryFg() {
		return colorInjuryFg;
	}

	public static Color getColorDecrease() {
		return colorDecrease;
	}

	public static Color getColorDecreaseDescription() {
		return colorDecreaseDescription;
	}

	public static Font getFontDescription() {
		return fontCurier;
	}

	public static Font getFontItalic() {
		return fontItalic;
	}

	public static Font getFontMain() {
		return fontMain;
	}

	public static Font getFontCurrent() {
		return fontCurrent;
	}

	public static Font getFontTable() {
		return fontTable;
	}

	public static void setColorError(Color colorError) {
		ConfigBean.colorError = colorError;
	}

	public static void setColorIncrease(Color colorIncrease) {
		ConfigBean.colorIncrease = colorIncrease;
	}

	public static void setColorIncreaseDescription(Color colorIncreaseDescription) {
		ConfigBean.colorIncreaseDescription = colorIncreaseDescription;
	}

	public static void setColorFont(Color colorFont) {
		ConfigBean.colorFont = colorFont;
	}

	public static void setColorDecrease(Color colorDecrease) {
		ConfigBean.colorDecrease = colorDecrease;
	}

	public static void setColorDecreaseDescription(Color colorDecreaseDescription) {
		ConfigBean.colorDecreaseDescription = colorDecreaseDescription;
	}

	public static void setColorInjuryBg(Color colorInjuryBg) {
		ConfigBean.colorInjuryBg = colorInjuryBg;
	}

	public static void setColorNewTableObject(Color colorNewTableObject) {
		ConfigBean.colorNewTableObject = colorNewTableObject;
	}

	public static void setColorInjuryFg(Color colorInjuryFg) {
		ConfigBean.colorInjuryFg = colorInjuryFg;
	}

	public static void setColorNewTreeObject(Color colorNewTreeObject) {
		ConfigBean.colorNewTreeObject = colorNewTreeObject;
	}

	public static void setColorTrainedJunior(Color colorTrainedJunior) {
		ConfigBean.colorTrainedJunior = colorTrainedJunior;
	}

	public static void setColorTransferList(Color colorTransferList) {
		ConfigBean.colorTransferList = colorTransferList;
	}

	public static void setFontCurrent(Font fontCurrent) {
		ConfigBean.fontCurrent = fontCurrent;
	}

	public static void setFontDescription(Font fontCurier) {
		ConfigBean.fontCurier = fontCurier;
	}

	public static void setFontItalic(Font font) {
		ConfigBean.fontItalic = font;
	}

	public static void setFontMain(Font fontMain) {
		ConfigBean.fontMain = fontMain;
	}

	public static void setFontTable(Font fontTable) {
		ConfigBean.fontTable = fontTable;
	}

	public static void setDefaults(Properties defaultProperties) {
			String string;
			String[] tempTable;
	
			tempTable = defaultProperties.getProperty("color.decreaseTable").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorDecrease(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
			tempTable = defaultProperties.getProperty("color.decreaseDescription").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorDecreaseDescription(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
			tempTable = defaultProperties.getProperty("color.error").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorError(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
	//		tempTable = defaultProperties.getProperty("color.font").split(",");
	//		SokkerBean.setColorFont(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
			tempTable = defaultProperties.getProperty("color.increaseTable").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorIncrease(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
			tempTable = defaultProperties.getProperty("color.increaseDescription").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorIncreaseDescription(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
			tempTable = defaultProperties.getProperty("color.injuryBg").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorInjuryBg(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
			tempTable = defaultProperties.getProperty("color.injuryFg").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorInjuryFg(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
			tempTable = defaultProperties.getProperty("color.newTableItem").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorNewTableObject(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
			tempTable = defaultProperties.getProperty("color.newTreeItem").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorNewTreeObject(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
			tempTable = defaultProperties.getProperty("color.trainedJunior").split(","); //$NON-NLS-1$ //$NON-NLS-2$
			setColorTrainedJunior(ColorResources.getColor(Integer.valueOf(tempTable[0]), Integer.valueOf(tempTable[1]), Integer.valueOf(tempTable[2])));
	
	
			string = defaultProperties.getProperty("font.main"); //$NON-NLS-1$
			setFontMain(Fonts.getFont(DisplayHandler.getDisplay(), new FontData[] {new FontData(string)}));
			
			string = defaultProperties.getProperty("font.table"); //$NON-NLS-1$
			setFontTable(Fonts.getFont(DisplayHandler.getDisplay(), new FontData[] {new FontData(string)}));
			
			string = defaultProperties.getProperty("font.description"); //$NON-NLS-1$
			setFontDescription(Fonts.getFont(DisplayHandler.getDisplay(), new FontData[] {new FontData(string)}));
		}
}
