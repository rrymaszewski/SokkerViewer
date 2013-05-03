package pl.pronux.sokker.ui.widgets.shells;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class ChangeLoginShell extends Shell {

	private Combo combo;

	private Text text;

	private Button button;

	private CLabel labelOldLogin;

	private CLabel labelNewLogin;

	private String login;
	
	private SokkerViewerSettings settings;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public ChangeLoginShell(final Shell parent, int style) {
		super(parent, style);
		settings = SettingsHandler.getSokkerViewerSettings();
		this.setSize(200, 170);
		this.setLayout(new FormLayout());
		this.setText(Messages.getString("shell.login.change.title")); 

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		formData.height = 20;

		labelOldLogin = new CLabel(this, SWT.NONE);
		labelOldLogin.setLayoutData(formData);
		labelOldLogin.setText(Messages.getString("shell.login.change.old.text")); 
		labelOldLogin.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(labelOldLogin, 0);
		formData.height = 20;

		combo = new Combo(this, SWT.READ_ONLY);
		combo.setLayoutData(formData);
		combo.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(combo, 10);

		labelNewLogin = new CLabel(this, SWT.NONE);
		labelNewLogin.setLayoutData(formData);
		labelNewLogin.setText(Messages.getString("shell.login.change.new.text")); 
		labelNewLogin.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(labelNewLogin, 0);

		text = new Text(this, SWT.BORDER | SWT.SINGLE);
		text.setLayoutData(formData);
		text.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.left = new FormAttachment(text, 0, SWT.CENTER);
		formData.top = new FormAttachment(text, 10);
		formData.height = 20;
		formData.width = 80;

		button = new Button(this, SWT.NONE);
		button.setLayoutData(formData);
		button.setText(Messages.getString("button.change")); 
		button.setFont(ConfigBean.getFontMain());

		button.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				if (!text.getText().isEmpty() && !text.getText().equals(combo.getText())) { 
					if (new File(settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + text.getText() + ".script").exists()) {   
						MessageBox msg = new MessageBox(ChangeLoginShell.this, SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
						msg.setText(Messages.getString("message.file.exists.warning.title")); 
						msg.setMessage(Messages.getString("message.file.exists.warning")); 
						if (msg.open() != SWT.YES) {
							return;
						}
					}
					new File(settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + combo.getText() + ".script").renameTo(new File(settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + text.getText() + ".script"));      
					new File(settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + combo.getText() + ".properties").renameTo(new File(settings.getBaseDirectory() + File.separator + "db" + File.separator + "db_file_" + text.getText() + ".properties"));      
					if (new File(settings.getBaseDirectory() + File.separator + "bak" + File.separator + combo.getText()).exists()) { 
						new File(settings.getBaseDirectory() + File.separator + "bak" + File.separator + combo.getText()).renameTo(new File(settings.getBaseDirectory() + File.separator + "bak" + File.separator + text.getText()));  
					}
					login = text.getText();
					ChangeLoginShell.this.close();
				}

			}
		});

		getLogins();

		Rectangle splashRect = this.getBounds();
		Rectangle displayRect = this.getDisplay().getPrimaryMonitor().getClientArea();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		this.setLocation(x, y);

		this.setDefaultButton(button);

	}

	private String[] getLogins() {
		String[] logins = new String[0];
		File dbDir = new File(settings.getBaseDirectory() + File.separator + "db" + File.separator); 
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory() || file.getName().endsWith(".script");
			}
		};
		List<File> files = OperationOnFile.visitAllDirs(dbDir, fileFilter, new ArrayList<File>());

		for (File file : files) {
			String[] filename = file.getName().split("\\."); 
			combo.add(filename[0].replaceAll("db_file_", ""));  
		}
		if (combo.getItemCount() == 0) {
			button.setEnabled(false);
		} else {
			combo.select(0);
		}

		return logins;
	}

	public String getLogin() {
		return login;
	}
	
	@Override
	public void open() {
		super.open();
		while (!this.isDisposed()) {
			if (!this.getDisplay().readAndDispatch()) {
				this.getDisplay().sleep();
			}
		}
	}

}
