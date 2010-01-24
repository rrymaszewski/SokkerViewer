package pl.pronux.sokker.ui.widgets.groups;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.actions.PlayersManager;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.widgets.composites.CountryChooser;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ArchiveInformationGroup extends Group {

	private PlayersManager playersManager = PlayersManager.instance();

	private FormData formData;
	private Label idLabel;
	private Label nameLabel;
	private Label surnameLabel;
	private Label youthTeamIdLabel;
	private Label countryLabel;
	private Text idText;
	private Text nameText;
	private Text surnameText;
	private Text youthTeamIdText;
	private Button saveButton;
	private PlayerArchive playerArchive;
	private Label countryImageLabel;
	private Listener countryListener;
	private Label idWarningLabel;
	private Label nameWarningLabel;
	private Label surnameWarningLabel;
	private Label youthTeamIdWarningLabel;
	private Label countryWarningLabel;

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
		if (idText == null || idText.getText().isEmpty() || !idText.getText().matches("[0-9]+")) { //$NON-NLS-1$ 
			return -1;
		}
		return Integer.valueOf(idText.getText());
	}

	public Integer getPlayerYouthTeamId() {
		if (youthTeamIdText == null || youthTeamIdText.getText().isEmpty() || !youthTeamIdText.getText().matches("[0-9]+")) { //$NON-NLS-1$ 
			return -1;
		}
		return Integer.valueOf(youthTeamIdText.getText());
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public ArchiveInformationGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;

		this.setLayout(layout);
		this.setFont(ConfigBean.getFontMain());
		this.setText(Messages.getString("archive.group.information.text")); //$NON-NLS-1$
		this.setForeground(Colors.getBlueDescription());

		GridData warningGD = new GridData();
		warningGD.widthHint = 5;

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;

		idLabel = new Label(this, SWT.LEFT);
		idLabel.setLayoutData(formData);
		idLabel.setText(Messages.getString("player.id")); //$NON-NLS-1$
		idLabel.setFont(this.getFont());

		idText = new Text(this, SWT.BORDER);
		idText.setEditable(false);
		idText.setBackground(ColorResources.getColor(240, 240, 240));
		idText.setFont(this.getFont());
		idText.setLayoutData(gridData);

		idWarningLabel = new Label(this, SWT.NONE);
		idWarningLabel.setForeground(ColorResources.getRed());
		idWarningLabel.setText("*"); //$NON-NLS-1$
		idWarningLabel.setVisible(false);
		idWarningLabel.setLayoutData(warningGD);

		nameLabel = new Label(this, SWT.LEFT);
		nameLabel.setLayoutData(formData);
		nameLabel.setText(Messages.getString("player.name")); //$NON-NLS-1$
		nameLabel.setFont(this.getFont());

		nameText = new Text(this, SWT.BORDER);
		nameText.setLayoutData(gridData);
		nameText.setEditable(false);
		nameText.setBackground(ColorResources.getColor(240, 240, 240));
		nameText.setFont(this.getFont());

		nameWarningLabel = new Label(this, SWT.NONE);
		nameWarningLabel.setForeground(ColorResources.getRed());
		nameWarningLabel.setText("*"); //$NON-NLS-1$
		nameWarningLabel.setVisible(false);
		nameWarningLabel.setLayoutData(warningGD);

		surnameLabel = new Label(this, SWT.LEFT);
		surnameLabel.setLayoutData(formData);
		surnameLabel.setText(Messages.getString("player.surname")); //$NON-NLS-1$
		surnameLabel.setFont(this.getFont());

		surnameText = new Text(this, SWT.BORDER);
		surnameText.setLayoutData(gridData);
		surnameText.setEditable(false);
		surnameText.setBackground(ColorResources.getColor(240, 240, 240));
		surnameText.setFont(this.getFont());

		surnameWarningLabel = new Label(this, SWT.NONE);
		surnameWarningLabel.setForeground(ColorResources.getRed());
		surnameWarningLabel.setText("*"); //$NON-NLS-1$
		surnameWarningLabel.setVisible(false);
		surnameWarningLabel.setLayoutData(warningGD);

		youthTeamIdLabel = new Label(this, SWT.LEFT);
		youthTeamIdLabel.setLayoutData(formData);
		youthTeamIdLabel.setText(Messages.getString("player.youth.team.id")); //$NON-NLS-1$
		youthTeamIdLabel.setFont(this.getFont());

		youthTeamIdText = new Text(this, SWT.BORDER);
		youthTeamIdText.setLayoutData(gridData);
		youthTeamIdText.setEditable(false);
		youthTeamIdText.setBackground(ColorResources.getColor(240, 240, 240));
		youthTeamIdText.setFont(this.getFont());

		youthTeamIdWarningLabel = new Label(this, SWT.NONE);
		youthTeamIdWarningLabel.setForeground(ColorResources.getRed());
		youthTeamIdWarningLabel.setText("*"); //$NON-NLS-1$
		youthTeamIdWarningLabel.setVisible(false);
		youthTeamIdWarningLabel.setLayoutData(warningGD);

		countryLabel = new Label(this, SWT.LEFT);
		countryLabel.setLayoutData(formData);
		countryLabel.setText(Messages.getString("player.country")); //$NON-NLS-1$
		countryLabel.setFont(this.getFont());

		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.widthHint = 20;

		countryImageLabel = new Label(this, SWT.NONE);
		countryImageLabel.setLayoutData(gridData);

		countryWarningLabel = new Label(this, SWT.NONE);
		countryWarningLabel.setForeground(ColorResources.getRed());
		countryWarningLabel.setText("*"); //$NON-NLS-1$
		countryWarningLabel.setVisible(false);
		countryWarningLabel.setLayoutData(warningGD);

		gridData = new GridData();
		gridData.widthHint = 80;
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = GridData.CENTER;

		saveButton = new Button(this, SWT.PUSH);
		saveButton.setLayoutData(gridData);
		saveButton.setText(Messages.getString("button.save")); //$NON-NLS-1$
		saveButton.setEnabled(false);
		saveButton.setFont(this.getFont());

		saveButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				if (playerArchive != null) {
					int countryID = (Integer) countryImageLabel.getData("id");//$NON-NLS-1$
					if (nameText.getText().length() == 0 || surnameText.getText().length() == 0 || countryID == FlagsResources.EMPTY_FLAG) {
						if (nameText.getText().length() == 0) {
							nameWarningLabel.setVisible(true);
						} else {
							nameWarningLabel.setVisible(false);
						}

						if (surnameText.getText().length() == 0) {
							surnameWarningLabel.setVisible(true);
						} else {
							surnameWarningLabel.setVisible(false);
						}

						if (countryID == FlagsResources.EMPTY_FLAG) {
							countryWarningLabel.setVisible(true);
						} else {
							countryWarningLabel.setVisible(false);
						}
					} else {
						countryWarningLabel.setVisible(false);
						surnameWarningLabel.setVisible(false);
						nameWarningLabel.setVisible(false);
						playerArchive.setName(nameText.getText());
						playerArchive.setSurname(surnameText.getText());
						playerArchive.setCountryID(countryID);
						if (youthTeamIdText.getText().matches("[0-9]+")) { //$NON-NLS-1$
							playerArchive.setYouthTeamID(Integer.valueOf(youthTeamIdText.getText()));
						}
						playerArchive.setExistsInSokker(PlayerArchive.EXISTS_IN_SOKKER_COMPLETED);
						try {
							playersManager.updatePlayerArchive(playerArchive);
							MessageDialog.openInformationMessage(ArchiveInformationGroup.this.getShell(), Messages.getString("saved"));//$NON-NLS-1$
						} catch (SQLException e) {
							new BugReporter(ArchiveInformationGroup.this.getDisplay()).openErrorMessage("Update Player", e);
						}
					}
				}
			}

		});

		countryListener = new Listener() {

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.MouseDown:
					CountryChooser countryChooser = new CountryChooser(ArchiveInformationGroup.this.getShell(), SWT.TOOL | SWT.PRIMARY_MODAL);
					countryChooser.open(Cache.getCountries());
					int id = countryChooser.getId();
					// if (id >= 0 && id < FlagsResources.EMPTY_FLAG) {
					countryImageLabel.setImage(FlagsResources.getFlag(id));
					countryImageLabel.setData("id", id); //$NON-NLS-1$
					// }
					break;
				default:
					break;
				}

			}
		};

	}

	public void fill(PlayerArchive playerArchive) {
		countryImageLabel.removeListener(SWT.MouseDown, countryListener);
		this.playerArchive = playerArchive;
		idText.setText(String.valueOf(playerArchive.getId()));
		nameText.setText(playerArchive.getName());
		surnameText.setText(playerArchive.getSurname());
		youthTeamIdText.setText(String.valueOf(playerArchive.getYouthTeamID()));
		if (playerArchive.getCountryID() == 0) {
			countryImageLabel.setImage(FlagsResources.getFlag(FlagsResources.EMPTY_FLAG));
			countryImageLabel.setData("id", FlagsResources.EMPTY_FLAG); //$NON-NLS-1$
		} else {
			countryImageLabel.setImage(FlagsResources.getFlag(playerArchive.getCountryID()));
			countryImageLabel.setData("id", playerArchive.getCountryID()); //$NON-NLS-1$
		}

		if (playerArchive.getExistsInSokker() == PlayerArchive.EXISTS_IN_SOKKER_FALSE
			|| playerArchive.getExistsInSokker() == PlayerArchive.EXISTS_IN_SOKKER_COMPLETED) {
			saveButton.setEnabled(true);
			surnameText.setEditable(true);
			nameText.setEditable(true);
			youthTeamIdText.setEditable(true);
			surnameText.setBackground(ColorResources.getWhite());
			nameText.setBackground(ColorResources.getWhite());
			youthTeamIdText.setBackground(ColorResources.getWhite());
			countryImageLabel.addListener(SWT.MouseDown, countryListener);
			saveButton.setEnabled(true);
		} else {
			saveButton.setEnabled(true);
			surnameText.setEditable(false);
			surnameText.setBackground(ColorResources.getColor(240, 240, 240));
			nameText.setEditable(false);
			youthTeamIdText.setEditable(false);
			nameText.setBackground(ColorResources.getColor(240, 240, 240));
			youthTeamIdText.setBackground(ColorResources.getColor(240, 240, 240));
			countryImageLabel.removeListener(SWT.MouseDown, countryListener);
			saveButton.setEnabled(false);
		}
	};
}
