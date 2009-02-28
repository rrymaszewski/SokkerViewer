package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Arena;
import pl.pronux.sokker.ui.widgets.styledtexts.ArenaInformationDescription;
import pl.pronux.sokker.ui.widgets.styledtexts.ArenaRebuildDescription;

public class ArenaDescriptionComposite extends Composite {

	private ArenaInformationDescription arenaInformationDescription;
	private ArenaRebuildDescription arenaRebuildDescription;

	public ArenaDescriptionComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());

		FormData formData;

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.top = new FormAttachment(0, 0);
		formData.right = new FormAttachment(50, 0);
		formData.bottom = new FormAttachment(100, 0);

		arenaInformationDescription = new ArenaInformationDescription(this, SWT.NONE);
		arenaInformationDescription.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(arenaInformationDescription, 0);
		formData.top = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);
		arenaRebuildDescription = new ArenaRebuildDescription(this, SWT.NONE);
		arenaRebuildDescription.setLayoutData(formData);
	}

	public void setInfo(Arena arena) {
		arenaInformationDescription.setInfo(arena);
		arenaRebuildDescription.renew();
	}
	
	public void setInfo(Arena arena, Arena newArena) {
		arenaInformationDescription.setInfo(newArena);
		arenaRebuildDescription.setInfo(arena, newArena);
	}

	public String getText() {
		return arenaInformationDescription.getText();
	}
	
	public void clear() {
		arenaInformationDescription.setText("");
		arenaRebuildDescription.setText("");
	}
}
