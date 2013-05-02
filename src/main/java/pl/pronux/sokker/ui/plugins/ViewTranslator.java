package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.comparators.StringLengthComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.enums.Language;
import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.LangResources;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.events.TranslateEvent;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.DescriptionDoubleComposite;
import pl.pronux.sokker.ui.widgets.composites.DescriptionSingleComposite;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ViewTranslator implements IPlugin {

	private Button buttonParse;

	private Composite composite;

	private TreeItem treeItem;

	private FormData viewFormData;

	private Button buttonClear;

	private Combo comboLangFrom;

	private LangResources langTranslatePropertiesFrom;

	private SokkerViewerSettings settings;

	private Combo comboCurrencyFrom;

	private Double[] currencyValueTable;

	private DescriptionDoubleComposite universalPlayerComposite;

	private Combo comboLangTo;

	private Combo comboCurrencyTo;

	private LangResources langTranslatePropertiesTo;

	private String[] currencyTable;

	private String country;

	private Button buttonClipboard;

	private Clipboard cb;

	private Text text;

	protected Player player;

	private Combo comboFormat;

	private DescriptionSingleComposite universalComposite;

	private List<Country> countries;

	private void addComposite(final Composite composite) {
		text = new Text(composite, SWT.BORDER | SWT.MULTI);

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(50, 0);

		text.setLayoutData(formData);

		formData = new FormData(100, 25);
		formData.top = new FormAttachment(text, 10);
		formData.left = new FormAttachment(0, 5);

		comboLangFrom = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		comboLangFrom.setLayoutData(formData);
		comboLangFrom.setEnabled(false);
		comboLangFrom.setFont(ConfigBean.getFontMain());

		addItems(comboLangFrom);

		comboLangFrom.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String text = ((Combo) event.widget).getItem(((Combo) event.widget).getSelectionIndex());
				String langCode = "";
				// confProperties.setProperty("lang.type", text);
//				String[] table = settings.getProperty("lang.list").split(";");
//				String[] codeTable = settings.getProperty("lang.codelist").split(";");
//				for (int i = 0; i < settings.getLanguages().size(); i++) {
//					if (text.equals(settings.getLanguages().get(i))) {
//						// confProperties.setProperty("lang.code", codeTable[i]);
//						langCode = codeTable[i];
//						break;
//					}
//				}
				langCode = Language.getLanguageCode(text);
				String[] temp = langCode.split("_");
				if(temp.length == 2) {
					langTranslatePropertiesFrom = Messages.getLangResources(new Locale(temp[0], temp[1]));	
				}
			}
		});

		formData = new FormData(100, 25);
		formData.top = new FormAttachment(text, 10);
		formData.left = new FormAttachment(comboLangFrom, 5);

		comboCurrencyFrom = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		comboCurrencyFrom.setLayoutData(formData);
		comboCurrencyFrom.setEnabled(false);
		comboCurrencyFrom.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.top = new FormAttachment(text, 10);
		formData.left = new FormAttachment(comboCurrencyFrom, 5);

		buttonParse = new Button(composite, SWT.NONE);
		buttonParse.setText(Messages.getString("button.translate"));
		buttonParse.setLayoutData(formData);
		buttonParse.setEnabled(false);
		buttonParse.setFont(ConfigBean.getFontMain());
		buttonParse.pack();

		buttonParse.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {

				try {

					player = parsePlayer(text.getText());

					player.getSkills()[0].setSalary(new Money(Double.valueOf(convertMoney(player.getSkills()[0].getSalary().toInt(), comboCurrencyFrom.getSelectionIndex(), comboCurrencyTo.getSelectionIndex())).intValue()));
					player.getSkills()[0].setValue(new Money(Double.valueOf(convertMoney(player.getSkills()[0].getValue().toInt(), comboCurrencyFrom.getSelectionIndex(), comboCurrencyTo.getSelectionIndex())).intValue()));

					String selection = comboFormat.getItem(comboFormat.getSelectionIndex());
					if (selection.equalsIgnoreCase("SV")) {
						setStatsPlayerInfo(player, universalPlayerComposite, langTranslatePropertiesTo);
					} else {
						setStatsPlayerInfo(player, universalComposite, langTranslatePropertiesTo);
					}

				} catch (Exception e) {
					new BugReporter(composite.getShell()).openErrorMessage(Messages.getString("message.parse.error.text"), e);
				}

			}
		});
		formData = new FormData();
		formData.top = new FormAttachment(text, 10);
		formData.left = new FormAttachment(buttonParse, 5);
		formData.height = 20;

		comboFormat = new Combo(composite, SWT.READ_ONLY);
		comboFormat.add("SV");
		comboFormat.add("SK");
		comboFormat.setText("SK");
		comboFormat.setLayoutData(formData);
		comboFormat.setEnabled(false);
		comboFormat.setFont(ConfigBean.getFontMain());

		comboFormat.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {

				String selection = comboFormat.getItem(comboFormat.getSelectionIndex());

				if (selection.equalsIgnoreCase("SV")) {
					if (player != null) {
						setStatsPlayerInfo(player, universalPlayerComposite, langTranslatePropertiesTo);
					}
					universalPlayerComposite.setVisible(true);
					universalComposite.setVisible(false);
				} else {
					if (player != null) {
						setStatsPlayerInfo(player, universalComposite, langTranslatePropertiesTo);
					}
					universalComposite.setVisible(true);
					universalPlayerComposite.setVisible(false);
				}
			}

		});

		formData = new FormData();
		formData.top = new FormAttachment(text, 10);
		formData.left = new FormAttachment(comboFormat, 5);

		buttonClipboard = new Button(composite, SWT.NONE);
		buttonClipboard.setText(Messages.getString("button.clipboard"));
		buttonClipboard.setLayoutData(formData);
		buttonClipboard.setEnabled(false);
		buttonClipboard.setFont(ConfigBean.getFontMain());

		buttonClipboard.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {

				TextTransfer textTransfer = TextTransfer.getInstance();

				String selection = comboFormat.getItem(comboFormat.getSelectionIndex());

				if (selection.equalsIgnoreCase("SV")) {
					cb.setContents(new Object[] {
						universalPlayerComposite.getText() + "\r\n\r\n" + "translated by SokkerViewer"
					}, new Transfer[] {
						textTransfer
					});
				} else {
					cb.setContents(new Object[] {
						universalComposite.getText() + "\r\n\r\n" + "translated by SokkerViewer"
					}, new Transfer[] {
						textTransfer
					});
				}

			}
		});
		buttonClipboard.pack();

		formData = new FormData();
		formData.top = new FormAttachment(text, 10);
		formData.left = new FormAttachment(buttonClipboard, 5);

		buttonClear = new Button(composite, SWT.NONE);
		buttonClear.setText(Messages.getString("button.reset"));
		buttonClear.setLayoutData(formData);
		buttonClear.setEnabled(false);
		buttonClear.setFont(ConfigBean.getFontMain());
		buttonClear.pack();

		buttonClear.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {

				universalPlayerComposite.clearAll();
				universalComposite.clearAll();

				text.setText("");

			}
		});

		formData = new FormData(100, 25);
		formData.top = new FormAttachment(text, 10);
		formData.left = new FormAttachment(buttonClear, 5);

		comboLangTo = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		comboLangTo.setLayoutData(formData);
		comboLangTo.setEnabled(false);
		comboLangTo.setFont(ConfigBean.getFontMain());

		addItems(comboLangTo);

		comboLangTo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String text = ((Combo) event.widget).getItem(((Combo) event.widget).getSelectionIndex());
				String langCode = "";
				// confProperties.setProperty("lang.type", text);
//				String[] table = settings.getProperty("lang.list").split(";");
//				String[] codeTable = settings.getProperty("lang.codelist").split(";");
//				for (int i = 0; i < table.length; i++) {
//					if (text.equals(table[i])) {
//						// confProperties.setProperty("lang.code", codeTable[i]);
//						langCode = codeTable[i];
//					}
//				}
				
				langCode = Language.getLanguageCode(text);
				
				String[] temp = langCode.split("_");
				if(temp.length > 1) {
					langTranslatePropertiesTo = Messages.getLangResources(new Locale(temp[0], temp[1]));	
				} else {
					langTranslatePropertiesTo = Messages.getLangResources(new Locale("pl", "PL"));
				}
				
				String selection = comboFormat.getItem(comboFormat.getSelectionIndex());

				if (selection.equalsIgnoreCase("SV")) {
					if (player != null) {
						setStatsPlayerInfo(player, universalPlayerComposite, langTranslatePropertiesTo);
					}
					universalPlayerComposite.setVisible(true);
					universalComposite.setVisible(false);
				} else {
					if (player != null) {
						setStatsPlayerInfo(player, universalComposite, langTranslatePropertiesTo);
					}
					universalComposite.setVisible(true);
					universalPlayerComposite.setVisible(false);
				}

			}
		});

		formData = new FormData(100, 25);
		formData.top = new FormAttachment(text, 10);
		formData.left = new FormAttachment(comboLangTo, 5);

		comboCurrencyTo = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		comboCurrencyTo.setLayoutData(formData);
		comboCurrencyTo.setEnabled(false);
		comboCurrencyTo.setFont(ConfigBean.getFontMain());

		comboCurrencyTo.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				String selection = comboFormat.getItem(comboFormat.getSelectionIndex());

				if (selection.equalsIgnoreCase("SV")) {
					if (player != null) {
						player.getSkills()[0].setSalary(new Money(Double.valueOf(convertMoney(player.getSkills()[0].getSalary().toInt(), comboCurrencyFrom.getSelectionIndex(), comboCurrencyTo.getSelectionIndex())).intValue()));
						player.getSkills()[0].setValue(new Money(Double.valueOf(convertMoney(player.getSkills()[0].getValue().toInt(), comboCurrencyFrom.getSelectionIndex(), comboCurrencyTo.getSelectionIndex())).intValue()));

						setStatsPlayerInfo(player, universalPlayerComposite, langTranslatePropertiesTo);
					}
					universalPlayerComposite.setVisible(true);
					universalComposite.setVisible(false);
				} else {
					if (player != null) {
						player.getSkills()[0].setSalary(new Money(Double.valueOf(convertMoney(player.getSkills()[0].getSalary().toInt(), comboCurrencyFrom.getSelectionIndex(), comboCurrencyTo.getSelectionIndex())).intValue()));
						player.getSkills()[0].setValue(new Money(Double.valueOf(convertMoney(player.getSkills()[0].getValue().toInt(), comboCurrencyFrom.getSelectionIndex(), comboCurrencyTo.getSelectionIndex())).intValue()));

						setStatsPlayerInfo(player, universalComposite, langTranslatePropertiesTo);
					}
					universalComposite.setVisible(true);
					universalPlayerComposite.setVisible(false);
				}
			}
		});

		viewFormData = new FormData();
		viewFormData.top = new FormAttachment(buttonParse, 10);
		viewFormData.left = new FormAttachment(0, 0);
		viewFormData.right = new FormAttachment(100, 0);
		viewFormData.bottom = new FormAttachment(100, 0);

	}

	private double convertMoney(double value, int selectionIndexFrom, int selectionIndexTo) {
		Double currencyFrom = currencyValueTable[selectionIndexFrom];

		Double currencyTo = currencyValueTable[selectionIndexTo];

		double tempSummary = 0;

		tempSummary = value * currencyFrom;

		value = new BigDecimal(tempSummary / currencyTo).setScale(0, RoundingMode.HALF_UP).intValue();

		return value;
	}

	private void addCurrencyItems(Combo comboCurrency) {
		// countryId = Country.getCountriesId();

		currencyValueTable = new Double[countries.size()];
		currencyTable = new String[countries.size()];
		int i = 0;
		for (Country country : countries) {
			comboCurrency.add(Messages.getString("country." + country.getCountryID() + ".name") + " (" + country.getCurrencyName() + ")");
			// getProperty("country." + countryId[i] + ".currency")
			comboCurrency.setText(Messages.getString("country." + Cache.getClub().getCountry() + ".name") + " (" + country.getCurrencyName() + ")");
			currencyValueTable[i] = country.getCurrencyRate();
			currencyTable[i] = country.getCurrencyName();
			i++;
		}

	}

	private void addItems(Combo comboLang) {

		comboLang.setItems(Language.languageNames());
		comboLang.setText(Language.getLanguageName(settings.getLangCode()));

	}

	private void addDescriptionComposite() {
		universalPlayerComposite = new DescriptionDoubleComposite(composite, SWT.BORDER);
		universalPlayerComposite.setLayoutData(viewFormData);
		universalPlayerComposite.setVisible(false);
		universalPlayerComposite.setLeftDescriptionStringFormat("%-20s%-15s\r\n");
		universalPlayerComposite.setLeftFirstColumnSize(20);
		universalPlayerComposite.setLeftSecondColumnSize(15);
		universalPlayerComposite.setRightDescriptionStringFormat("%-20s%-15s\r\n");
		universalPlayerComposite.setRightFirstColumnSize(20);
		universalPlayerComposite.setRightSecondColumnSize(15);
		universalPlayerComposite.setFont(ConfigBean.getFontDescription());

		universalComposite = new DescriptionSingleComposite(composite, SWT.BORDER);
		universalComposite.setLayoutData(viewFormData);
		universalComposite.setVisible(true);
		// descriptionComposite.setDescriptionStringFormat(40, 15);
		universalComposite.setDescriptionStringFormat("%-25s%-15s\r\n");
		universalComposite.setFirstColumnSize(25);
		universalComposite.setSecondColumnSize(15);
		universalComposite.setFont(ConfigBean.getFontDescription());

	}

	private void setStatsPlayerInfo(Player player, DescriptionDoubleComposite description, LangResources langTranslatePropertiesTo) {

		description.clearAll();

		int maxSkill = player.getSkills().length;
		PlayerSkills skills = player.getSkills()[maxSkill - 1];

		// int maxSkill = player.getSkills().length;
		int textSize = 0;

		String[][] values;

		values = new String[10][2];
		values[0][0] = langTranslatePropertiesTo.getString("player.id");
		values[1][0] = langTranslatePropertiesTo.getString("player.name");
		values[2][0] = langTranslatePropertiesTo.getString("player.surname");
		values[3][0] = langTranslatePropertiesTo.getString("player.country");
		values[4][0] = langTranslatePropertiesTo.getString("player.value");
		values[5][0] = langTranslatePropertiesTo.getString("player.salary");
		values[6][0] = langTranslatePropertiesTo.getString("player.matches");
		values[7][0] = langTranslatePropertiesTo.getString("player.goals");
		values[8][0] = langTranslatePropertiesTo.getString("player.assists");
		values[9][0] = langTranslatePropertiesTo.getString("player.club");

		textSize = 0;

		textSize = textSize + description.checkLeftFirstTextSize(values[0][0]);

		values[0][1] = String.valueOf(player.getId());

		textSize = textSize + description.checkLeftSecondTextSize(values[0][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[1][0]);

		values[1][1] = player.getName();

		textSize = textSize + description.checkLeftSecondTextSize(values[1][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[2][0]);

		values[2][1] = player.getSurname();

		textSize = textSize + description.checkLeftSecondTextSize(values[2][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[3][0]);

		values[3][1] = country;

		textSize = textSize + description.checkLeftSecondTextSize(values[3][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[4][0]);

		values[4][1] = skills.getValue().formatDouble() + " " + currencyTable[comboCurrencyTo.getSelectionIndex()];

		textSize = textSize + description.checkLeftSecondTextSize(values[4][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[5][0]);

		values[5][1] = skills.getSalary().formatDouble() + " " + currencyTable[comboCurrencyTo.getSelectionIndex()];

		textSize = textSize + description.checkLeftSecondTextSize(values[5][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[6][0]);

		values[6][1] = String.valueOf(skills.getMatches());

		textSize = textSize + description.checkLeftSecondTextSize(values[6][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[7][0]);

		values[7][1] = String.valueOf(skills.getGoals());

		textSize = textSize + description.checkLeftSecondTextSize(values[7][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[8][0]);

		values[8][1] = String.valueOf(skills.getAssists());

		textSize = textSize + description.checkLeftSecondTextSize(values[8][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[9][0]);

		values[9][1] = player.getClubName();

		textSize = textSize + description.checkLeftSecondTextSize(values[9][1]);

		for (int i = 0; i < values.length; i++) {
			description.addLeftText(values[i]);

		}

		description.setLeftColor();

		values = new String[11][2];
		values[0][0] = langTranslatePropertiesTo.getString("player.age");
		values[1][0] = langTranslatePropertiesTo.getString("player.form");
		values[2][0] = langTranslatePropertiesTo.getString("player.stamina");
		values[3][0] = langTranslatePropertiesTo.getString("player.pace");
		values[4][0] = langTranslatePropertiesTo.getString("player.technique");
		values[5][0] = langTranslatePropertiesTo.getString("player.passing");
		values[6][0] = langTranslatePropertiesTo.getString("player.keeper");
		values[7][0] = langTranslatePropertiesTo.getString("player.defender");
		values[8][0] = langTranslatePropertiesTo.getString("player.playmaker");
		values[9][0] = langTranslatePropertiesTo.getString("player.scorer");
		values[10][0] = langTranslatePropertiesTo.getString("player.general");

		if (skills.getAge() == -1) {
			values[0][1] = "-";
		} else {
			values[0][1] = String.valueOf(skills.getAge());
		}

		if (skills.getForm() == -1) {
			values[1][1] = "-";
		} else {
			values[1][1] = langTranslatePropertiesTo.getString("skill.b" + skills.getForm());
		}

		if (skills.getStamina() == -1) {
			values[2][1] = "-";
		} else {
			values[2][1] = langTranslatePropertiesTo.getString("skill.b" + skills.getStamina());
			values[2][1] += " [" + skills.getStamina() + "] ";
		}

		if (skills.getPace() == -1) {
			values[3][1] = "-";
		} else {
			values[3][1] = langTranslatePropertiesTo.getString("skill.b" + skills.getPace());
			values[3][1] += " [" + skills.getPace() + "] ";
		}

		if (skills.getTechnique() == -1) {
			values[4][1] = "-";
		} else {
			values[4][1] = langTranslatePropertiesTo.getString("skill.b" + skills.getTechnique());
			values[4][1] += " [" + skills.getTechnique() + "] ";
		}

		if (skills.getPassing() == -1) {
			values[5][1] = "-";
		} else {
			values[5][1] = langTranslatePropertiesTo.getString("skill.c" + skills.getPassing());
			values[5][1] += " [" + skills.getPassing() + "] ";
		}

		if (skills.getKeeper() == -1) {
			values[6][1] = "-";
		} else {
			values[6][1] = langTranslatePropertiesTo.getString("skill.a" + skills.getKeeper());
			values[6][1] += " [" + skills.getKeeper() + "] ";
		}

		if (skills.getDefender() == -1) {
			values[7][1] = "-";
		} else {
			values[7][1] = langTranslatePropertiesTo.getString("skill.a" + skills.getDefender());
			values[7][1] += " [" + skills.getDefender() + "] ";
		}

		if (skills.getPlaymaker() == -1) {
			values[8][1] = "-";
		} else {
			values[8][1] = langTranslatePropertiesTo.getString("skill.a" + skills.getPlaymaker());
			values[8][1] += " [" + skills.getPlaymaker() + "] ";
		}

		if (skills.getScorer() == -1) {
			values[9][1] = "-";
		} else {
			values[9][1] = langTranslatePropertiesTo.getString("skill.a" + skills.getScorer());
			values[9][1] += " [" + skills.getScorer() + "] ";
		}

		if (skills.getSummarySkill() < 0 || skills.getStamina() == -1 || skills.getPace() == -1 || skills.getTechnique() == -1 || skills.getPassing() == -1 || skills.getKeeper() == -1 || skills.getDefender() == -1 || skills.getPlaymaker() == -1 || skills.getScorer() == -1) {
			values[10][1] = "-";
		} else {
			values[10][1] = "[" + skills.getSummarySkill() + "] ";
		}

		for (int i = 0; i < values.length; i++) {
			description.addRightText(values[i]);
		}
		description.setRightColor();
	}
	
	private String getStatsPlayerSKFormat(Player player, LangResources langTranslatePropertiesTo) {
		String text = "";
		int maxSkill = player.getSkills().length;
		PlayerSkills skills = player.getSkills()[maxSkill - 1];

		// int maxSkill = player.getSkills().length;

		String[] values;

		values = new String[30];
		values[0] = player.getName();
		values[1] = player.getSurname();
		values[2] = langTranslatePropertiesTo.getString("player.age").toLowerCase();
		if (skills.getAge() == -1) {
			values[3] = "-";
		} else {
			values[3] = String.valueOf(skills.getAge());
		}

		text += values[0] + " " + values[1] + ", " + values[2] + ": " + values[3] + "\r\n";

		values[4] = langTranslatePropertiesTo.getString("player.value").toLowerCase();
		values[5] = skills.getValue().formatDouble() + " " + currencyTable[comboCurrencyTo.getSelectionIndex()];
		values[6] = langTranslatePropertiesTo.getString("player.salary").toLowerCase();
		values[7] = skills.getSalary().formatDouble() + " " + currencyTable[comboCurrencyTo.getSelectionIndex()];

		text += values[4] + ": " + values[5] + ", " + values[6] + ": " + values[7] + "\r\n";

		values[8] = langTranslatePropertiesTo.getString("player.club").toLowerCase();
		values[9] = player.getClubName();
		values[10] = langTranslatePropertiesTo.getString("player.country").toLowerCase();
		if(player.getCountryfrom() != 0) {
			values[11] = country;	
		} else {
			values[11] = langTranslatePropertiesTo.getString("country." + player.getCountryfrom() + ".name");
		}
		

		text += values[8] + ": " + values[9] + ", " + values[10] + ": " + values[11] + "\r\n";

		if (skills.getForm() == -1) {
			values[12] = "-";
		} else {
			values[12] = langTranslatePropertiesTo.getString("skill.b" + skills.getForm());
		}

		values[13] = langTranslatePropertiesTo.getString("player.form").toLowerCase();

		text += values[12] + " " + values[13] + "\r\n";

		text += "\r\n";

		if (skills.getStamina() == -1) {
			values[14] = "-";
		} else {
			values[14] = langTranslatePropertiesTo.getString("skill.b" + skills.getStamina());
		}

		values[15] = langTranslatePropertiesTo.getString("player.stamina").toLowerCase();

		if (skills.getKeeper() == -1) {
			values[16] = "-";
		} else {
			values[16] = langTranslatePropertiesTo.getString("skill.a" + skills.getKeeper());
		}

		values[17] = langTranslatePropertiesTo.getString("player.keeper").toLowerCase();

		text += values[14] + " " + values[15] + ", " + values[16] + " " + values[17] + "\r\n";

		if (skills.getPace() == -1) {
			values[18] = "-";
		} else {
			values[18] = langTranslatePropertiesTo.getString("skill.b" + skills.getPace());
		}

		values[19] = langTranslatePropertiesTo.getString("player.pace").toLowerCase();

		if (skills.getDefender() == -1) {
			values[20] = "-";
		} else {
			values[20] = langTranslatePropertiesTo.getString("skill.a" + skills.getDefender());
		}

		values[21] = langTranslatePropertiesTo.getString("player.defender").toLowerCase();

		text += values[18] + " " + values[19] + ", " + values[20] + " " + values[21] + "\r\n";

		if (skills.getTechnique() == -1) {
			values[22] = "-";
		} else {
			values[22] = langTranslatePropertiesTo.getString("skill.b" + skills.getTechnique());
		}

		values[23] = langTranslatePropertiesTo.getString("player.technique").toLowerCase();

		if (skills.getPlaymaker() == -1) {
			values[24] = "-";
		} else {
			values[24] = langTranslatePropertiesTo.getString("skill.a" + skills.getPlaymaker());
		}

		values[25] = langTranslatePropertiesTo.getString("player.playmaker").toLowerCase();

		text += values[22] + " " + values[23] + ", " + values[24] + " " + values[25] + "\r\n";

		if (skills.getPassing() == -1) {
			values[26] = "-";
		} else {
			values[26] = langTranslatePropertiesTo.getString("skill.c" + skills.getPassing());
		}

		values[27] = langTranslatePropertiesTo.getString("player.passing").toLowerCase();

		if (skills.getScorer() == -1) {
			values[28] = "-";
		} else {
			values[28] = langTranslatePropertiesTo.getString("skill.a" + skills.getScorer());
		}

		values[29] = langTranslatePropertiesTo.getString("player.scorer").toLowerCase();

		text += values[26] + " " + values[27] + ", " + values[28] + " " + values[29] + "\r\n";

		
		return text;
	}

	private void setStatsPlayerInfo(Player player, DescriptionSingleComposite description, LangResources langTranslatePropertiesTo) {
		description.clearAll();

		description.setText("test");

		description.setText(getStatsPlayerSKFormat(player, langTranslatePropertiesTo));
	}

	public void clear() {

	}

	public Composite getComposite() {
		return this.composite;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoTranslator");
	}

	public TreeItem getTreeItem() {
		return this.treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		cb = new Clipboard(composite.getDisplay());
		this.composite.setLayout(new FormLayout());
		addComposite(composite);
		addDescriptionComposite();
	}

	private Player parsePlayer(String text) throws Exception {

		country = "";

		Pattern pattern;
		Matcher matcher;

		Pattern pattern1;
		Matcher matcher1;

		StringLengthComparator comparator = new StringLengthComparator();
		comparator.setColumn(StringLengthComparator.LENGTH);
		comparator.setDirection(StringLengthComparator.DESCENDING);

		HashMap<String, Integer> skillsHM = new HashMap<String, Integer>();

		// text = text.replaceAll(":", "");
		text = text.replaceAll("\r", "");
		text = text.replaceAll("\t", " ");
		text = text.replaceAll("[+-] *[0-9]+", "");
		text = text.replaceAll("  +", " ");
		text = text.replaceAll("\\[.*\\]", "");
		text = text.replaceAll("\\(.*\\)", "");
		text = text.replaceAll(",", "\n");

		ArrayList<String> langReplace = new ArrayList<String>();
		for (int i = 17; i >= 0; i--) {
			langReplace.add(langTranslatePropertiesFrom.getString("skill.a" + i));
			langReplace.add(langTranslatePropertiesFrom.getString("skill.b" + i));
			langReplace.add(langTranslatePropertiesFrom.getString("skill.c" + i));
			skillsHM.put(langTranslatePropertiesFrom.getString("skill.a" + i), i);
			skillsHM.put(langTranslatePropertiesFrom.getString("skill.b" + i), i);
			skillsHM.put(langTranslatePropertiesFrom.getString("skill.c" + i), i);
		}

		Collections.sort(langReplace, comparator);

		for (String replace : langReplace) {

			text = text.replaceAll(replace, skillsHM.get(replace).toString());
		}

		String[] test = text.split("\n");
		Player player = new Player();
		player.setCountryfrom(-1);
		PlayerSkills skills = new PlayerSkills();
		boolean nameMutex = true;

		skills.setAge(Integer.valueOf(-1).byteValue());
		skills.setForm(Integer.valueOf(-1).byteValue());
		skills.setValue(new Money(-1));
		skills.setSalary(new Money(-1));
		skills.setStamina(Integer.valueOf(-1).byteValue());
		skills.setPace(Integer.valueOf(-1).byteValue());
		skills.setTechnique(Integer.valueOf(-1).byteValue());
		skills.setPassing(Integer.valueOf(-1).byteValue());
		skills.setKeeper(Integer.valueOf(-1).byteValue());
		skills.setDefender(Integer.valueOf(-1).byteValue());
		skills.setPlaymaker(Integer.valueOf(-1).byteValue());
		skills.setScorer(Integer.valueOf(-1).byteValue());
		skills.setMatches(-1);
		skills.setGoals(-1);
		skills.setAssists(-1);

		for (int i = 0; i < test.length; i++) {

			test[i] = test[i].replaceAll("^ +", "");

			if (test[i].matches(".*\\p{L}+([ \t]+\\p{L}+)+.*") && nameMutex && !test[i].contains(langTranslatePropertiesFrom.getString("player.name")) && !test[i].contains(langTranslatePropertiesFrom.getString("player.surname"))) {

				test[i] = test[i].replaceAll("[^\\p{L} ]", "");
				test[i] = test[i].replaceAll("\t", " ");
				test[i] = test[i].replaceAll("^ +", "");
				test[i] = test[i].replaceAll(" +", " ");

				String[] name = test[i].split(" ");
				String surname = "";
				player.setName(name[0]);
				for (int j = 1; j < name.length; j++) {
					if (j == name.length - 1) {
						surname += name[j];
					} else {
						surname += name[j] + " ";
					}

				}
				player.setSurname(surname);
				nameMutex = false;
			}

			pattern = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.name") + " *:{0,1}.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches()) {
				int index = test[i].toLowerCase().indexOf(langTranslatePropertiesFrom.getString("player.name").toLowerCase());
				test[i] = test[i].substring(0, index) + test[i].substring(index + langTranslatePropertiesFrom.getString("player.name").length());
				// test[i] =
				// test[i].toLowerCase().replace(langProperties.getProperty("club").toLowerCase(),"");
				test[i] = test[i].replaceAll("^ *:* *", "");
				test[i] = test[i].replaceAll(" +", " ");
				test[i] = test[i].replaceAll("\t", "");
				test[i] = test[i].replaceAll(" +$", "");

				player.setName(test[i]);
				nameMutex = false;
			}

			pattern = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.surname") + " *:{0,1}.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches()) {
				int index = test[i].toLowerCase().indexOf(langTranslatePropertiesFrom.getString("player.surname").toLowerCase());
				test[i] = test[i].substring(0, index) + test[i].substring(index + langTranslatePropertiesFrom.getString("player.surname").length());
				// test[i] =
				// test[i].toLowerCase().replace(langProperties.getProperty("club").toLowerCase(),"");
				test[i] = test[i].replaceAll("^ *:* *", "");
				test[i] = test[i].replaceAll(" +", " ");
				test[i] = test[i].replaceAll("\t", "");
				test[i] = test[i].replaceAll(" +$", "");
				player.setSurname(test[i]);
				nameMutex = false;
			}

			pattern = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.club") + " *:{0,1}.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches()) {
				int index = test[i].toLowerCase().indexOf(langTranslatePropertiesFrom.getString("club").toLowerCase());
				test[i] = test[i].substring(0, index) + test[i].substring(index + langTranslatePropertiesFrom.getString("club").length());
				// test[i] =
				// test[i].toLowerCase().replace(langProperties.getProperty("club").toLowerCase(),"");
				test[i] = test[i].replaceAll("^ *:* *", "");
				test[i] = test[i].replaceAll(" +", " ");
				test[i] = test[i].replaceAll("\t", "");

				player.setClubName(test[i]);
			}

			pattern = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.country") + " *:{0,1}.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches()) {

				int index = test[i].toLowerCase().indexOf(langTranslatePropertiesFrom.getString("player.country").toLowerCase());
				test[i] = test[i].substring(0, index) + test[i].substring(index + langTranslatePropertiesFrom.getString("player.country").length());
				// test[i] =
				// test[i].toLowerCase().replace(langProperties.getProperty("club").toLowerCase(),"");
				test[i] = test[i].replaceAll("^ *:* *", "");
				test[i] = test[i].replaceAll(" +", " ");
				test[i] = test[i].replaceAll("\t", "");
				country = test[i];
				// player.setCountryfrom((test[i]);
			}

			pattern = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.age") + " *:{0,1}.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches()) {
				test[i] = test[i].replaceAll("[^0-9 ]", "");

				pattern = Pattern.compile("(?<=[0-9]+) +[0-9]+");
				matcher = pattern.matcher(test[i]);
				test[i] = matcher.replaceAll("");

				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setAge(Byte.valueOf(test[i]));
			}

			pattern = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.value") + " *:{0,1}.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setValue(new Money(Integer.valueOf(test[i])));
			}

			pattern = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.salary") + " *:{0,1}.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setSalary(new Money(Integer.valueOf(test[i])));
			}

			pattern1 = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.form") + " *[0-9]+ *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher1 = pattern1.matcher(test[i]);

			pattern = Pattern.compile("[0-9]+ *" + langTranslatePropertiesFrom.getString("player.form") + " *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches() || matcher1.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setForm(Byte.valueOf(test[i]));
			}

			pattern1 = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.stamina") + " *[0-9]+ *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher1 = pattern1.matcher(test[i]);

			pattern = Pattern.compile("[0-9]+ *" + langTranslatePropertiesFrom.getString("player.stamina") + " *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches() || matcher1.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setStamina(Byte.valueOf(test[i]));
			}

			pattern1 = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.pace") + " *[0-9]+ *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher1 = pattern1.matcher(test[i]);

			pattern = Pattern.compile("[0-9]+ *" + langTranslatePropertiesFrom.getString("player.pace") + " *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches() || matcher1.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setPace(Byte.valueOf(test[i]));
			}

			pattern1 = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.technique") + " *[0-9]+ *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher1 = pattern1.matcher(test[i]);

			pattern = Pattern.compile("[0-9]+ *" + langTranslatePropertiesFrom.getString("player.technique") + " *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches() || matcher1.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setTechnique(Byte.valueOf(test[i]));
			}

			pattern1 = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.passing") + " *[0-9]+ *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher1 = pattern1.matcher(test[i]);

			pattern = Pattern.compile("[0-9]+ *" + langTranslatePropertiesFrom.getString("player.passing") + " *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches() || matcher1.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setPassing(Byte.valueOf(test[i]));
			}

			pattern1 = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.keeper") + " *[0-9]+ *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher1 = pattern1.matcher(test[i]);

			pattern = Pattern.compile("[0-9]+ *" + langTranslatePropertiesFrom.getString("player.keeper") + " *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches() || matcher1.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setKeeper(Byte.valueOf(test[i]));
			}

			pattern1 = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.defender") + " *[0-9]+ *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher1 = pattern1.matcher(test[i]);

			pattern = Pattern.compile("[0-9]+ *" + langTranslatePropertiesFrom.getString("player.defender") + " *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches() || matcher1.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setDefender(Byte.valueOf(test[i]));
			}

			pattern1 = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.playmaker") + " *[0-9]+ *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher1 = pattern1.matcher(test[i]);

			pattern = Pattern.compile("[0-9]+ *" + langTranslatePropertiesFrom.getString("player.playmaker") + " *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches() || matcher1.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setPlaymaker(Byte.valueOf(test[i]));
			}

			pattern1 = Pattern.compile("^" + langTranslatePropertiesFrom.getString("player.scorer") + " *[0-9]+ *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher1 = pattern1.matcher(test[i]);

			pattern = Pattern.compile("[0-9]+ *" + langTranslatePropertiesFrom.getString("player.scorer") + " *$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			matcher = pattern.matcher(test[i]);

			if (matcher.matches() || matcher1.matches()) {
				test[i] = test[i].replaceAll("[^0-9]", "");
				skills.setScorer(Byte.valueOf(test[i]));
			}

		}

		validPlayer(player);

		player.setSkills(new PlayerSkills[] {
			skills
		});

		player.getSkills()[0].setSummarySkill();
		return player;
	}

	private void validPlayer(Player player) {
		if (player.getClubName() == null) {
			player.setClubName("-");
		}

		if (player.getName() == null) {
			player.setName("-");
		}

		if (player.getSurname() == null) {
			player.setSurname("-");
		}

		if (country == null) {
			country = "-";
		}
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		this.settings = sokkerViewerSettings;

	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewTranslator"));
		this.treeItem.setImage(ImageResources.getImageResources("translator.png"));
	}

	public void set() {
		countries = Cache.getCountries();

		buttonParse.setEnabled(true);
		buttonClear.setEnabled(true);
		buttonClipboard.setEnabled(true);

		comboLangFrom.setEnabled(true);
		comboCurrencyFrom.setEnabled(true);

		comboLangTo.setEnabled(true);
		comboCurrencyTo.setEnabled(true);

		comboFormat.setEnabled(true);

		addCurrencyItems(comboCurrencyFrom);
		addCurrencyItems(comboCurrencyTo);
		String[] table = settings.getLangCode().split("_");
		
		langTranslatePropertiesFrom = Messages.getLangResources(new Locale(table[0], table[1]));
		langTranslatePropertiesTo = Messages.getLangResources(new Locale(table[0], table[1]));
		ViewerHandler.getViewer().addListener(IEvents.TRANSLATE_PLAYER, new Listener() {
			public void handleEvent(Event event) {
				if(event instanceof TranslateEvent) {
					TranslateEvent tEvent = (TranslateEvent) event;
					
					
					text.setText(getStatsPlayerSKFormat(tEvent.getPlayer(), langTranslatePropertiesFrom));
					setStatsPlayerInfo(tEvent.getPlayer(), universalComposite, langTranslatePropertiesTo);
				}
//				TextTransfer transfer = TextTransfer.getInstance();
//				String data = (String) cb.getContents(transfer);
//				if (data != null) {
//					text.setText(data);
//				}
			}
		});

	}

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
