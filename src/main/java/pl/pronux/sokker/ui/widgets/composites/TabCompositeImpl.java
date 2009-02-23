package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.widgets.Composite;

abstract public class TabCompositeImpl<T> extends Composite implements ITabComposite<T> {

	public TabCompositeImpl(Composite parent, int style) {
		super(parent, style);
	}

}
