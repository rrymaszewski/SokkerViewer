package pl.pronux.sokker.ui.widgets.shells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.widgets.buttons.ColorButton;

public class AddTemplateShell extends Shell {
	private static Map<String, String> values = new HashMap<String, String>();
	private static List<String> valuesList = new ArrayList<String>();
	static {
		values.put(Messages.getString("player.name"), "${player_name_desc}");
		values.put(Messages.getString("player.surname"), "${player_surname_desc}");
		values.put(Messages.getString("player.stamina"), "${stamina_desc}");
		values.put(Messages.getString("player.pace"), "${pace_desc}");
		values.put(Messages.getString("player.technique"), "${technique_desc}");
		values.put(Messages.getString("player.passing"), "${passing_desc}");
		values.put(Messages.getString("player.keeper"), "${keeper_desc}");
		values.put(Messages.getString("player.defender"), "${defender_desc}");
		values.put(Messages.getString("player.playmaker"), "${playmaker_desc}");
		values.put(Messages.getString("player.scorer"), "${scorer_desc}");
		values.put(Messages.getString("player.age"), "${age_desc}");
		values.put(Messages.getString("player.form"), "${form_desc}");
		values.put(Messages.getString("player.teamwork"), "${teamwork_desc}");
		values.put(Messages.getString("player.discipline"), "${discipline_desc}");
		values.put(Messages.getString("player.experience"), "${experience_desc}");
		values.put(Messages.getString("player.club"), "${club_desc}");
		values.put(Messages.getString("player.country"), "${country_desc}");
		values.put(Messages.getString("player.value"), "${value_desc}");
		values.put(Messages.getString("player.wage"), "${wage_desc}");
		values.put(Messages.getString("player.cards"), "${cards_desc}");
		values.put(Messages.getString("player.matches"), "${matches_desc}");
		values.put(Messages.getString("player.assists"), "${assists_desc}");
		values.put(Messages.getString("player.goals"), "${goals_desc}");
		
		values.put("<" + Messages.getString("player.name") + ">", "${player_name_value}");
		values.put("<" + Messages.getString("player.surname") + ">", "${player_surname_value}");
		values.put("<" + Messages.getString("player.stamina") + ">", "${stamina_value}");
		values.put("<" + Messages.getString("player.pace") + ">", "${pace_value}");
		values.put("<" + Messages.getString("player.technique") + ">", "${technique_value}");
		values.put("<" + Messages.getString("player.passing") + ">", "${passing_value}");
		values.put("<" + Messages.getString("player.keeper") + ">", "${keeper_value}");
		values.put("<" + Messages.getString("player.defender") + ">", "${defender_value}");
		values.put("<" + Messages.getString("player.playmaker") + ">", "${playmaker_value}");
		values.put("<" + Messages.getString("player.scorer") + ">", "${scorer_value}");
		values.put("<" + Messages.getString("player.age") + ">", "${age_value}");
		values.put("<" + Messages.getString("player.form") + ">", "${form_value}");
		values.put("<" + Messages.getString("player.teamwork") + ">", "${teamwork_value}");
		values.put("<" + Messages.getString("player.discipline") + ">", "${discipline_value}");
		values.put("<" + Messages.getString("player.experience") + ">", "${experience_value}");
		values.put("<" + Messages.getString("player.club") + ">", "${club_value}");
		values.put("<" + Messages.getString("player.country") + ">", "${country_value}");
		values.put("<" + Messages.getString("player.value") + ">", "${value_value}");
		values.put("<" + Messages.getString("player.wage") + ">", "${wage_value}");
		values.put("<" + Messages.getString("player.cards") + ">", "${cards_value}");
		values.put("<" + Messages.getString("player.matches") + ">", "${matches_value}");
		values.put("<" + Messages.getString("player.assists") + ">", "${assists_value}");
		values.put("<" + Messages.getString("player.goals") + ">", "${goals_value}");
		
		values.put("[" + Messages.getString("player.stamina") + "]", "${stamina_raw}");
		values.put("[" + Messages.getString("player.pace") + "]", "${pace_raw}");
		values.put("[" + Messages.getString("player.technique") + "]", "${technique_raw}");
		values.put("[" + Messages.getString("player.passing") + "]", "${passing_raw}");
		values.put("[" + Messages.getString("player.keeper") + "]", "${keeper_raw}");
		values.put("[" + Messages.getString("player.defender") + "]", "${defender_raw}");
		values.put("[" + Messages.getString("player.playmaker") + "]", "${playmaker_raw}");
		values.put("[" + Messages.getString("player.scorer") + "]", "${scorer_raw}");
		values.put("[" + Messages.getString("player.form") + "]", "${form_raw}");	
		values.put("[" + Messages.getString("player.teamwork") + "]", "${teamwork_raw}");
		values.put("[" + Messages.getString("player.discipline") + "]", "${discipline_raw}");
		values.put("[" + Messages.getString("player.experience") + "]", "${experience_raw}");
		
		valuesList.add(Messages.getString("player.name"));
		valuesList.add("<" + Messages.getString("player.name")+ ">");
		
		valuesList.add(Messages.getString("player.surname"));
		valuesList.add("<" + Messages.getString("player.surname")+ ">");
		
		valuesList.add(Messages.getString("player.stamina"));
		valuesList.add("<" + Messages.getString("player.stamina")+ ">");
		valuesList.add("[" + Messages.getString("player.stamina")+ "]");
		
		valuesList.add(Messages.getString("player.pace"));
		valuesList.add("<" + Messages.getString("player.pace")+ ">");
		valuesList.add("[" + Messages.getString("player.pace")+ "]");
		
		valuesList.add(Messages.getString("player.technique"));
		valuesList.add("<" + Messages.getString("player.technique")+ ">");
		valuesList.add("[" + Messages.getString("player.technique")+ "]");
		
		valuesList.add(Messages.getString("player.passing"));
		valuesList.add("<" + Messages.getString("player.passing")+ ">");
		valuesList.add("[" + Messages.getString("player.passing")+ "]");
		
		valuesList.add(Messages.getString("player.keeper"));
		valuesList.add("<" + Messages.getString("player.keeper")+ ">");
		valuesList.add("[" + Messages.getString("player.keeper")+ "]");
		
		valuesList.add(Messages.getString("player.defender"));
		valuesList.add("<" + Messages.getString("player.defender")+ ">");
		valuesList.add("[" + Messages.getString("player.defender")+ "]");
		
		valuesList.add(Messages.getString("player.playmaker"));
		valuesList.add("<" + Messages.getString("player.playmaker")+ ">");
		valuesList.add("[" + Messages.getString("player.playmaker")+ "]");
		
		valuesList.add(Messages.getString("player.scorer"));
		valuesList.add("<" + Messages.getString("player.scorer")+ ">");
		valuesList.add("[" + Messages.getString("player.scorer")+ "]");
		
		valuesList.add(Messages.getString("player.age"));
		valuesList.add("<" + Messages.getString("player.age")+ ">");
		
		valuesList.add(Messages.getString("player.form"));
		valuesList.add("<" + Messages.getString("player.form")+ ">");
		valuesList.add("[" + Messages.getString("player.form")+ "]");
		
		valuesList.add(Messages.getString("player.teamwork"));
		valuesList.add("<" + Messages.getString("player.teamwork")+ ">");
		valuesList.add("[" + Messages.getString("player.teamwork")+ "]");
		
		valuesList.add(Messages.getString("player.discipline"));
		valuesList.add("<" + Messages.getString("player.discipline")+ ">");
		valuesList.add("[" + Messages.getString("player.discipline")+ "]");
		
		valuesList.add(Messages.getString("player.experience"));
		valuesList.add("<" + Messages.getString("player.experience")+ ">");
		valuesList.add("[" + Messages.getString("player.experience")+ "]");
		
		valuesList.add(Messages.getString("player.club"));
		valuesList.add("<" + Messages.getString("player.club")+ ">");
		valuesList.add(Messages.getString("player.country"));
		valuesList.add("<" + Messages.getString("player.country")+ ">");
		valuesList.add(Messages.getString("player.value"));
		valuesList.add("<" + Messages.getString("player.value")+ ">");
		valuesList.add(Messages.getString("player.salary"));
		valuesList.add("<" + Messages.getString("player.salary")+ ">");
		valuesList.add(Messages.getString("player.cards"));
		valuesList.add("<" + Messages.getString("player.cards")+ ">");
		valuesList.add(Messages.getString("player.matches"));
		valuesList.add("<" + Messages.getString("player.matches")+ ">");
		valuesList.add(Messages.getString("player.assists"));
		valuesList.add("<" + Messages.getString("player.assists")+ ">");
		valuesList.add(Messages.getString("player.goals"));
		valuesList.add("<" + Messages.getString("player.goals")+ ">");
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public AddTemplateShell(final Shell parent, int style) {
		super(parent, style);
		this.setSize(new Point(800, 650));

		this.setLayout(new FormLayout());

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 10);

		CLabel label = new CLabel(this, SWT.NONE);
		label.setText(Messages.getString("template.editor"));
		label.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(label, 5);
		formData.bottom = new FormAttachment(40, 0);

		final StyledText text = new StyledText(this, SWT.BORDER);
		text.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(text, 5);

		final CCombo combo = new CCombo(this, SWT.BORDER | SWT.READ_ONLY);
		combo.setLayoutData(formData);
		for (String string : valuesList) {
			combo.add(string);
		}

//		String[] table = combo.getItems();
//		Arrays.sort(table, new StringComparator(StringComparator.LENGTH,
//				StringComparator.ASCENDING));
//		combo.setItems(table);
		combo.setVisibleItemCount(20);
		combo.pack();

		formData = new FormData();
		formData.left = new FormAttachment(combo, 5);
		formData.top = new FormAttachment(text, 5);

		Button insert = new Button(this, SWT.NONE);
		insert.setText(Messages.getString("b.INSERT"));
		insert.setLayoutData(formData);
		insert.pack();
		insert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (values.get(combo.getText()) != null) {
					text.append(values.get(combo.getText()) + " ");
				}
			}
		});

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(combo, 5);
		formData.right = new FormAttachment(100, -5);
		formData.bottom = new FormAttachment(100, -5);

		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(formData);

		GridLayout layout = new GridLayout();
		layout.numColumns = 12;
		group.setLayout(layout);
		Label description;

		for (int i = 0; i < 2; i++) {
			description = new Label(group, SWT.NONE);
			description.setText(Messages.getString("skill"));
			description = new Label(group, SWT.NONE);
			description.setText(Messages.getString("color"));
			description = new Label(group, SWT.NONE);
			description.setText(Messages.getString("font"));
			description = new Label(group, SWT.NONE);
			description.setText(Messages.getString("b"));
			description = new Label(group, SWT.NONE);
			description.setText(Messages.getString("i"));
			description = new Label(group, SWT.NONE);
			description.setText(Messages.getString("u"));
		}

		int j = 0;
		int k = 9;
		for (int i = 0; i < 18; i++) {
			Label settingsLabel = new Label(group, SWT.NONE);
			if (i % 2 == 0) {
				settingsLabel.setText(String.format("[%d] %s", j, Messages
						.getString("skill.a" + j++)));
			} else {
				settingsLabel.setText(String.format("[%d] %s ", k, Messages
						.getString("skill.a" + k++)));
			}
			final ColorButton changer = new ColorButton(group, SWT.PUSH);
			changer.setSize(50, 25);
			changer.setColor(ColorResources.getBlack());
			changer.setData("color", changer.getColor());
			changer.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					ColorDialog dialog = new ColorDialog(parent, SWT.NONE);
					dialog.setRGB(((Color) changer.getData("color")).getRGB());
					RGB color = dialog.open();
					if (color != null) {
						Color newColor = ColorResources.getColor(color);
						changer.setColor(newColor);
						changer.setData("color", newColor);
					}
				}
			});

			Combo font = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
			font.setItems(new String[] { "1", "2", "3", "4", "5", "6", "7" });
			font.setText("4");
			font.setTextLimit(1);
			font.setVisibleItemCount(7);

			Button bold = new Button(group, SWT.CHECK);
			bold.setSelection(false);
			Button underline = new Button(group, SWT.CHECK);
			underline.setSelection(false);
			Button italic = new Button(group, SWT.CHECK);
			italic.setSelection(false);
		}

		Rectangle splashRect = this.getBounds();
		Rectangle displayRect = DisplayHandler.getDisplay().getPrimaryMonitor()
				.getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		this.setLocation(x, y);

	}

}
