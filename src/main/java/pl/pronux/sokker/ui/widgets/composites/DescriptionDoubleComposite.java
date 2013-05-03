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
public class DescriptionDoubleComposite extends Composite {
	private FormData formFill;

	private StyledText leftDescription;

	private String leftDescriptionStringFormat;

	private int leftFirstColumnSize;

	private int leftSecondColumnSize;

	private List<StyleRange> leftStyleRanges;

	private StyledText rightDescription;

	private String rightDescriptionStringFormat;

	private int rightFirstColumnSize;

	private int rightSecondColumnSize;

	private List<StyleRange> rightStyleRanges;

	public DescriptionDoubleComposite(Composite parent, int style) {
		super(parent, style);
		leftDescription = new StyledText(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
		rightDescription = new StyledText(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
		setLayout(new FormLayout());
		formFill = new FormData();
		formFill.top = new FormAttachment(0, 0);
		formFill.left = new FormAttachment(0, 0);
		formFill.right = new FormAttachment(50, 0);
		formFill.bottom = new FormAttachment(100, 0);
		leftDescription.setLayoutData(formFill);
		leftDescription.setBackground(getBackground());
		formFill = new FormData();
		formFill.top = new FormAttachment(0, 0);
		formFill.left = new FormAttachment(leftDescription, 0);
		formFill.right = new FormAttachment(100, 0);
		formFill.bottom = new FormAttachment(100, 0);
		rightDescription.setLayoutData(formFill);
		rightDescription.setBackground(getBackground());
		setEnabled(false);

		// initialize description string format
		leftDescriptionStringFormat = "%s: %s\r\n"; 

		// initialize description string format
		rightDescriptionStringFormat = "%s: %s\r\n"; 

		// create object for colorize text in description composite
		leftStyleRanges = new ArrayList<StyleRange>();
		rightStyleRanges = new ArrayList<StyleRange>();
	}

	public String getText() {
		return leftDescription.getText() + "\n" + rightDescription.getText(); 
	}
	public void addLeftText(Object[] object) {
		leftDescription.setText(leftDescription.getText()
				+ String.format(leftDescriptionStringFormat, object));
	}

	public void addRightText(Object[] object) {
		rightDescription.setText(rightDescription.getText()
				+ String.format(rightDescriptionStringFormat, object));
	}

	public int checkLeftFirstTextSize(String text) {
		if (text.length() > leftFirstColumnSize) {
			return text.length();
		}
		return leftFirstColumnSize;
	}

	public int checkLeftSecondTextSize(String text) {
		if (text.length() > leftSecondColumnSize) {
			return text.length() + 2;
		}
		return leftSecondColumnSize + 2;
	}

	public int checkRightFirstTextSize(String text) {
		if (text.length() > rightFirstColumnSize) {
			return text.length();
		}
		return rightFirstColumnSize;
	}

	public int checkRightSecondTextSize(String text) {
		if (text.length() > rightSecondColumnSize) {
			return text.length() + 2;
		}
		return rightSecondColumnSize + 2;
	}

	public String getLeftDescriptionStringFormat() {
		return leftDescriptionStringFormat;
	}

	public int getLeftFirstColumnSize() {
		return leftFirstColumnSize;
	}

	public int getLeftSecondColumnSize() {
		return leftSecondColumnSize;
	}

	public String getLeftText() {
		return leftDescription.getText();
	}

	public String getRightDescriptionStringFormat() {
		return rightDescriptionStringFormat;
	}

	public int getRightFirstColumnSize() {
		return leftFirstColumnSize;
	}

	public int getRightSecondColumnSize() {
		return leftSecondColumnSize;
	}

	public String getRightText() {
		return rightDescription.getText();
	}

	public void leftColorText(int length, Color foreground, Color background) {
		if (length > leftFirstColumnSize) {
			leftStyleRanges.add(new StyleRange(leftDescription.getText().length()
					- length - 2, length, foreground, background));
		} else {
			leftStyleRanges.add(new StyleRange(leftDescription.getText().length()
					- leftFirstColumnSize - 2, length, foreground, background));
		}
	}

	public void leftColorText(int start, int length, Color foreground) {
		leftStyleRanges.add(new StyleRange(start, length, foreground, this
				.getBackground()));
	}
	
	public void leftStyleText(int start, int length, Color foreground, Color background, int style) {
		leftStyleRanges.add(new StyleRange(start, length, foreground, background, style));
	}

	public void leftColorText(int start, int length, Color foreground,
			Color background) {
		leftStyleRanges.add(new StyleRange(start, length, foreground, background));
	}

	public void rightColorText(int length, Color foreground, Color background) {
		if (length > rightFirstColumnSize) {
			rightStyleRanges.add(new StyleRange(rightDescription.getText().length()
					- length - 2, length, foreground, background));
		} else {
			rightStyleRanges.add(new StyleRange(rightDescription.getText().length()
					- rightFirstColumnSize - 2, length, foreground, background));
		}
	}

	public void rightColorText(int start, int length, Color foreground) {
		rightStyleRanges.add(new StyleRange(start, length, foreground, this
				.getBackground()));
	}

	public void rightColorText(int start, int length, Color foreground,
			Color background) {
		rightStyleRanges.add(new StyleRange(start, length, foreground, background));
	}

	public void setFont(Font font) {
		this.leftDescription.setFont(font);
		this.rightDescription.setFont(font);
	}

	public void setLeftColor() {
		leftDescription.setStyleRanges(leftStyleRanges.toArray(new StyleRange[leftStyleRanges.size()]));
	}

	public void setLeftDescriptionStringFormat(int leftFirstColumnSize,
			int leftSecondColumnSize) {
		this.leftFirstColumnSize = leftFirstColumnSize;
		this.leftSecondColumnSize = leftSecondColumnSize;
		leftDescriptionStringFormat = "%-" + leftFirstColumnSize + "s" + "%-"   
				+ leftSecondColumnSize + "s" + "\r\n";  
	}

	public void setLeftDescriptionStringFormat(String descriptionStringFormat) {
		this.leftDescriptionStringFormat = descriptionStringFormat;
	}

	public void setLeftDescriptionStyle(StyleRange[] styleRanges) {
		leftDescription.setStyleRanges(styleRanges);
	}

	public void setLeftText(Object[] object) {
		leftDescription.setText(String.format(leftDescriptionStringFormat, object));
	}

	public void setLeftText(String text) {
		this.leftDescription.setText(text);
	}

	public void setRightColor() {
		rightDescription.setStyleRanges(rightStyleRanges.toArray(new StyleRange[rightStyleRanges.size()]));
	}

	public void setRightDescriptionStringFormat(int rightFirstColumnSize,
			int rightSecondColumnSize) {
		this.rightFirstColumnSize = rightFirstColumnSize;
		this.rightSecondColumnSize = rightSecondColumnSize;
		rightDescriptionStringFormat = "%-" + rightFirstColumnSize + "s" + "%-"   
				+ rightSecondColumnSize + "s" + "\r\n";  
	}

	public void setRightDescriptionStringFormat(String descriptionStringFormat) {
		this.rightDescriptionStringFormat = descriptionStringFormat;
	}

	public void setRightDescriptionStyle(StyleRange[] styleRanges) {
		rightDescription.setStyleRanges(styleRanges);
	}

	public void setRightText(Object[] object) {
		rightDescription.setText(String
				.format(rightDescriptionStringFormat, object));
	}

	public void setRightText(String text) {
		this.rightDescription.setText(text);
	}

	public void setLeftFirstColumnSize(int leftFirstColumnSize) {
		this.leftFirstColumnSize = leftFirstColumnSize;
	}

	public void setLeftSecondColumnSize(int leftSecondColumnSize) {
		this.leftSecondColumnSize = leftSecondColumnSize;
	}

	public void setRightFirstColumnSize(int rightFirstColumnSize) {
		this.rightFirstColumnSize = rightFirstColumnSize;
	}

	public void setRightSecondColumnSize(int rightSecondColumnSize) {
		this.rightSecondColumnSize = rightSecondColumnSize;
	}

	public StyledText getLeftDescription() {
		return leftDescription;
	}

	public StyledText getRightDescription() {
		return rightDescription;
	}

	public void clearAll() {
		this.leftDescription.setText(""); 
		this.rightDescription.setText(""); 
		leftStyleRanges.clear();
		rightStyleRanges.clear();
	}
}
