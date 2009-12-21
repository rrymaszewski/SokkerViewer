package pl.pronux.sokker.ui.widgets.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Person;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.NoteTableListener;
import pl.pronux.sokker.ui.listeners.TableLabelsListener;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.shells.NoteShell;

abstract public class SVTable<T> extends Table {
	public SVTable(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public void sort(SVComparator<T> comparator) {
	}

	protected SVComparator<T> getComparator() {
		return null;
	}

	protected void getChanges( int[] columns) {
		for (int i = 1; i < this.getItemCount(); i++) {
			for (int j = 0; j < columns.length; j++) {
				if (Double.valueOf(this.getItem(i).getText(columns[j]).replaceAll("[^0-9-]", "")).intValue() < Double.valueOf((this.getItem(i - 1).getText(columns[j]).replaceAll("[^0-9-]", ""))).intValue()) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					this.getItem(i - 1).setBackground(columns[j], ConfigBean.getColorIncrease());
				} else if (Double.valueOf(this.getItem(i).getText(columns[j]).replaceAll("[^0-9-]", "")).intValue() > Double.valueOf(this.getItem(i - 1).getText(columns[j]).replaceAll("[^0-9-]", "")).intValue()) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					this.getItem(i - 1).setBackground(columns[j], ConfigBean.getColorDecrease());
				}
			}
		}
	}

	public void setLabel(Label label, int column, TableItem item) {
	}
	
	protected void addLabelsListener() {
		Listener tableListener = new TableLabelsListener<T>(this);
		this.addListener(SWT.Dispose, tableListener);
		this.addListener(SWT.KeyDown, tableListener);
		this.addListener(SWT.MouseMove, tableListener);
		this.addListener(SWT.MouseHover, tableListener);
	}
	
	protected void getChanges(int max, int min, TableItem tableItem, int column) {
		if (max > min) {
			tableItem.setBackground(column, ConfigBean.getColorIncrease());
		} else if (max < min) {
			tableItem.setBackground(column, ConfigBean.getColorDecrease());
		}
	}
	
	public void openNote(TableItem item, String identifier, int column) {
		if(item.getData(identifier) != null && item.getData(identifier) instanceof Person) {
			Person person = (Person) item.getData(identifier);
			final NoteShell noteShell = new NoteShell(this.getShell(), SWT.PRIMARY_MODAL | SWT.CLOSE);
			noteShell.setPerson(person);
			noteShell.open();
			if ( person.getNote() != null) {
				if (person.getNote().equals("")) {
					item.setImage(column, null);
				} else {
					item.setImage(column, ImageResources.getImageResources("note.png"));
				}
			}
		}
	}
	
	protected void addNoteListener(String identifier, int column) {
		this.addListener(SWT.MouseDoubleClick, new NoteTableListener<T>(this, identifier, column));
	}
}
