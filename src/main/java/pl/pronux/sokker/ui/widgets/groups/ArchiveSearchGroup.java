package pl.pronux.sokker.ui.widgets.groups;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.widgets.composites.CountryChooser;

public class ArchiveSearchGroup extends Group {

	private Label idLabel;
	private Label nameLabel;
	private Label surnameLabel;
	private Label youthTeamIdLabel;
	private Label countryLabel;
	private Button searchButton;
	private Text idText;
	private Text nameText;
	private Text surnameText;
	private Text youthTeamIdText;
	private Button clearButton;
	private Label countryImageLabel;
	private Listener countryListener;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}
	
	public String getPlayerName() {
		return nameText.getText();
	}
	
	public String getPlayerSurname() {
		return surnameText.getText();
	}
	
	public Integer getPlayerID() {
		if(idText == null || idText.getText().isEmpty() || !idText.getText().matches("[0-9]+")) { //$NON-NLS-1$ //$NON-NLS-2$
			return -1;
		}
		return Integer.valueOf(idText.getText());
	}
	
	public Integer getPlayerYouthTeamId() {
		if(youthTeamIdText == null || youthTeamIdText.getText().isEmpty() || !youthTeamIdText.getText().matches("[0-9]+")) { //$NON-NLS-1$ //$NON-NLS-2$
			return -1;
		}
		return Integer.valueOf(youthTeamIdText.getText());
	}
	
	public Button getSearchButton() {
		return searchButton;
	}

	public ArchiveSearchGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;

		this.setLayout(layout);
		this.setFont(ConfigBean.getFontMain());
		this.setText(Messages.getString("ArchiveSearchGroup.search")); //$NON-NLS-1$
		this.setForeground(Colors.getBlueDescription());

		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = GridData.FILL;

		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.horizontalSpan = 3;
		
		idLabel = new Label(this, SWT.LEFT);
		idLabel.setLayoutData(gridData1);
		idLabel.setText(Messages.getString("player.id")); //$NON-NLS-1$
		idLabel.setFont(this.getFont());
		
		idText = new Text(this, SWT.BORDER);
		idText.setLayoutData(gridData2);
		idText.setFont(this.getFont());
		
		nameLabel = new Label(this, SWT.LEFT);
		nameLabel.setLayoutData(gridData1);
		nameLabel.setText(Messages.getString("player.name")); //$NON-NLS-1$
		nameLabel.setFont(this.getFont());
		
		nameText = new Text(this, SWT.BORDER);
		nameText.setLayoutData(gridData2);
		nameText.setFont(this.getFont());
		
		surnameLabel = new Label(this, SWT.LEFT);
		surnameLabel.setLayoutData(gridData1);
		surnameLabel.setText(Messages.getString("player.surname")); //$NON-NLS-1$
		surnameLabel.setFont(this.getFont());
		
		surnameText = new Text(this, SWT.BORDER);
		surnameText.setLayoutData(gridData2);
		surnameText.setFont(this.getFont());
		
		youthTeamIdLabel = new Label(this, SWT.LEFT);
		youthTeamIdLabel.setLayoutData(gridData1);
		youthTeamIdLabel.setText(Messages.getString("player.youth.team.id")); //$NON-NLS-1$
		youthTeamIdLabel.setFont(this.getFont());
		
		youthTeamIdText = new Text(this, SWT.BORDER);
		youthTeamIdText.setLayoutData(gridData2);
		youthTeamIdText.setFont(this.getFont());
		
		countryLabel = new Label(this, SWT.LEFT);
		countryLabel.setLayoutData(gridData1);
		countryLabel.setText(Messages.getString("player.country")); //$NON-NLS-1$
		countryLabel.setFont(this.getFont());
		
		countryImageLabel = new Label(this, SWT.NONE);
		countryImageLabel.setLayoutData(gridData2);
		countryImageLabel.setImage(FlagsResources.getFlag(FlagsResources.EMPTY_FLAG));
		countryImageLabel.setData("id", FlagsResources.EMPTY_FLAG); //$NON-NLS-1$

		GridData gridData = new GridData();
		gridData.widthHint = 80;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.CENTER;
		
		searchButton = new Button(this, SWT.PUSH);
		searchButton.setLayoutData(gridData);
		searchButton.setText(Messages.getString("button.search")); //$NON-NLS-1$
		searchButton.setFont(this.getFont());
		
		clearButton = new Button(this, SWT.PUSH);
		clearButton.setLayoutData(gridData);
		clearButton.setText(Messages.getString("button.clear")); //$NON-NLS-1$
		clearButton.setFont(this.getFont());
		
		clearButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				idText.setText(""); //$NON-NLS-1$
				nameText.setText(""); //$NON-NLS-1$
				surnameText.setText(""); //$NON-NLS-1$
				youthTeamIdText.setText(""); //$NON-NLS-1$
				countryImageLabel.setImage(FlagsResources.getFlag(FlagsResources.EMPTY_FLAG));
				countryImageLabel.setData("id", FlagsResources.EMPTY_FLAG); //$NON-NLS-1$
			}
			
		});
		
		countryListener = new Listener() {

			public void handleEvent(Event event) {
				switch(event.type) {
				case SWT.MouseDown:
					CountryChooser countryChooser = new CountryChooser(ArchiveSearchGroup.this.getShell(), SWT.TOOL | SWT.PRIMARY_MODAL);
					if(Cache.getCountries() != null) {
						countryChooser.open(Cache.getCountries(), true);
						int id = countryChooser.getId();
//						if( id >= 0 ) {
							countryImageLabel.setImage(FlagsResources.getFlag(id));
							countryImageLabel.setData("id", id); //$NON-NLS-1$
//						}
					}
					break;
					default:
						break;
				}

			}
		};
		
		countryImageLabel.addListener(SWT.MouseDown, countryListener);

		
	}
	
	public int getPlayerCountryID() {
		if(countryImageLabel.getData("id") == null) { //$NON-NLS-1$
			return FlagsResources.EMPTY_FLAG;
		}
		return (Integer)countryImageLabel.getData("id"); //$NON-NLS-1$
		
	}
}
