/**
 * 
 */
package pl.pronux.sokker.ui.widgets.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author rym3k
 * 
 */
public class DescriptionSingleComposite extends Composite {
	private StyledText centerDescription;

	private String descriptionStringFormat;

	private int firstColumnSize;

	private FormData formFill;

	private int secondColumnSize;
	
	private List<StyleRange> styleRanges = new ArrayList<StyleRange>();

	public DescriptionSingleComposite(Composite parent, int style) {
		super(parent, style);
		centerDescription = new StyledText(this, SWT.READ_ONLY | SWT.MULTI
				| SWT.WRAP);
		this.setLayout(new FormLayout());
		formFill = new FormData();
		formFill.top = new FormAttachment(0, 0);
		formFill.left = new FormAttachment(0, 0);
		formFill.right = new FormAttachment(100, 0);
		formFill.bottom = new FormAttachment(100, 0);
		centerDescription.setLayoutData(formFill);
		centerDescription.setBackground(getBackground());
		setEnabled(false);

		// initialize description string format
		descriptionStringFormat = "%s: %s\r\n"; 
		// create object for colorize text in description composite
	}

	public void addText(Object[] object) {
		centerDescription.setText(centerDescription.getText()
				+ String.format(descriptionStringFormat, object));
	}

	public int checkFirstTextSize(String text) {
		if (text.length() > firstColumnSize) {
			return text.length();
		}
		return firstColumnSize;
	}

	public int checkSecondTextSize(String text) {
		if (text.length() > secondColumnSize) {
			return text.length() + 2;
		}
		return secondColumnSize + 2;
	}

	public void colorText(int length, Color foreground, Color background) {
		if (length > secondColumnSize) {
			styleRanges.add(new StyleRange(centerDescription.getText().length()
					- length - 2, length, foreground, background));
		} else {
			styleRanges.add(new StyleRange(centerDescription.getText().length()
					- secondColumnSize - 2, length, foreground, background));
		}
	}

	public void colorText(int start, int length, Color foreground) {
		styleRanges.add(new StyleRange(start, length, foreground, this
				.getBackground()));
	}

	public void colorText(int start, int length, Color foreground,
			Color background) {
		styleRanges.add(new StyleRange(start, length, foreground, background));
	}

	public String getDescriptionStringFormat() {
		return descriptionStringFormat;
	}

	public int getFirstColumnSize() {
		return firstColumnSize;
	}

	public int getSecondColumnSize() {
		return secondColumnSize;
	}

	public String getText() {
		return centerDescription.getText();
	}

	public void setCenterDescriptionStyle(StyleRange[] styleRanges) {
		centerDescription.setStyleRanges(styleRanges);
	}

	public void setColor() {
		centerDescription.setStyleRanges(styleRanges.toArray(new StyleRange[styleRanges.size()]));
	}

	public void setDescriptionStringFormat(int firstColumnSize,
			int secondColumnSize) {
		this.firstColumnSize = firstColumnSize;
		this.secondColumnSize = secondColumnSize;
		descriptionStringFormat = "%-" + firstColumnSize + "s" + "%-"   
				+ secondColumnSize + "s" + "\r\n";  
	}

	public void setDescriptionStringFormat(String descriptionStringFormat) {
		this.descriptionStringFormat = descriptionStringFormat;
	}

	public void setFont(Font font) {
		this.centerDescription.setFont(font);
	}

	public void setText(Object[] object) {
		centerDescription.setText(String.format(descriptionStringFormat, object));
	}

	public void setText(String text) {
		this.centerDescription.setText(text);
	}
	
	public void clearAll() {
		this.centerDescription.setText(""); 
		styleRanges.clear();
	}

	public void setFirstColumnSize(int firstColumnSize) {
		this.firstColumnSize = firstColumnSize;
	}

	public void setSecondColumnSize(int secondColumnSize) {
		this.secondColumnSize = secondColumnSize;
	}

}
