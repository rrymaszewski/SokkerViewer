package pl.pronux.sokker.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import pl.pronux.sokker.data.properties.SVProperties;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.utils.Log;

public class PropertiesChecker {

	private String baseDir;

	private SVProperties sokkerProperties;

	private static final String SEP = File.separator;

	public PropertiesChecker() {
		baseDir = System.getProperty("user.dir");
		File sokkerPropertiesFile = new File(baseDir + SEP + "settings" + SEP
				+ "sokker.properties");
		if (sokkerPropertiesFile.exists()) {
			sokkerProperties = new SVProperties();
			try {
				sokkerProperties.loadFile(sokkerPropertiesFile);
			} catch (FileNotFoundException e) {
				Log.warning("Properties Checker", e);
			} catch (IOException e) {
				Log.warning("Properties Checker", e);
			}
		}
	}

	public PropertiesChecker(SVProperties sokkerProperties) {
		baseDir = System.getProperty("user.dir");
		this.sokkerProperties = sokkerProperties;
	}

	public boolean checkBackupProperties() {
		File bakFile;
		try {
			bakFile = new File(baseDir + SEP + "settings" + SEP
					+ "backup.properties");
			if (bakFile.exists()) {
				Properties bakProperties = new Properties();
				FileInputStream fis = new FileInputStream(bakFile);
				bakProperties.load(fis);
				String bakString = bakProperties.getProperty("backup.dir");
				if (fis != null) {
					fis.close();
				}
				if (bakString != null && !bakString.isEmpty()) {
					sokkerProperties.setProperty("backup.dir", bakString);
					sokkerProperties.synchronize();
				}
				bakFile.delete();
			}
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public boolean checkSokkerProperties() {

		HashMap<String, String> hmValues = new HashMap<String, String>();
		hmValues.put("db.login", "");
		hmValues.put("sk.dir", "");
		hmValues.put("sk.login", "");
		hmValues.put("db.name", "");
		hmValues.put("sk.password", "");
		hmValues.put("db.server", "");
		hmValues.put("db.password", "");
		hmValues.put("proxy.turn", "0");
		hmValues.put("proxy.host", "");
		hmValues.put("proxy.port", "");
		hmValues.put("proxy.user", "");
		hmValues.put("proxy.password", "");
		hmValues.put("db.list", "HSQLDB");
		hmValues.put("backup.dir", "");
		hmValues.put("db.type", "HSQLDB");
		hmValues.put("conf.savepass", "1");
		hmValues.put("conf.getXML", "0");
		hmValues.put("lang.code", "en_EN");
		hmValues.put("check.properties", "0");
		hmValues.put("sv.startup", "0");
		hmValues.put("info.update", "1");
		hmValues.put("info.close", "1");

		Set<String> keys = hmValues.keySet();
		String value;
		String temp_value;
		for (String key : keys) {
			value = hmValues.get(key);
			temp_value = sokkerProperties.getProperty(key);
			if (temp_value == null) {
				sokkerProperties.setProperty(key, value);
			}
		}

		if (sokkerProperties.getProperty("backup.dir") == null
				|| sokkerProperties.getProperty("backup.dir").isEmpty()) {
			sokkerProperties.setProperty("backup.dir", System
					.getProperty("user.dir")
					+ SEP + "bak" + SEP);
		}

		try {
			sokkerProperties.synchronize();
		} catch (FileNotFoundException e) {
			Log.warning("Properties Checker", e);
		} catch (IOException e) {
			Log.warning("Properties Checker", e);
		}
		return true;
	}

	public boolean checkPluginsProperties() {
		try {
			File pluginsFile = new File(baseDir + SEP + "settings" + SEP
					+ "plugins.properties");
			SVProperties pluginsProperties;
			if (pluginsFile.exists()) {
				pluginsProperties = new SVProperties();
				pluginsProperties.loadFile(pluginsFile.getAbsolutePath());

				String[] plugins = { "pl.pronux.sokker.ui.plugins.ViewClub",
						"pl.pronux.sokker.ui.plugins.ViewPlayers",
						"pl.pronux.sokker.ui.plugins.ViewJuniors",
						"pl.pronux.sokker.ui.plugins.ViewCoaches",
						"pl.pronux.sokker.ui.plugins.ViewTrainings",
						"pl.pronux.sokker.ui.plugins.ViewArena",
						"pl.pronux.sokker.ui.plugins.ViewPlayersHistory",
						"pl.pronux.sokker.ui.plugins.ViewJuniorsTrained",
						"pl.pronux.sokker.ui.plugins.ViewJuniorsFired",
						"pl.pronux.sokker.ui.plugins.ViewCoachesFired",
						"pl.pronux.sokker.ui.plugins.ViewStatistics",
						"pl.pronux.sokker.ui.plugins.ViewExchange",
						"pl.pronux.sokker.ui.plugins.ViewAssistant",
						"pl.pronux.sokker.ui.plugins.ViewNotepad",
						"pl.pronux.sokker.ui.plugins.ViewTranslator",
						"pl.pronux.sokker.ui.plugins.ViewNT",
						"pl.pronux.sokker.ui.plugins.ViewTrash",
						"pl.pronux.sokker.ui.plugins.ViewBBCode",
						"pl.pronux.sokker.ui.plugins.ViewCalendar",
						"pl.pronux.sokker.ui.plugins.ViewTransfers",
						"pl.pronux.sokker.ui.plugins.ViewMatches",
						"pl.pronux.sokker.ui.plugins.ViewLeague",
						"pl.pronux.sokker.ui.plugins.ViewArchive",
						"pl.pronux.sokker.ui.plugins.ViewOffice",
						"pl.pronux.sokker.ui.plugins.ViewSpy", };

				String pluginsValue = pluginsProperties.getProperty("plugins");
				pluginsValue = pluginsValue.replaceAll(
						"pl.pronux.sokker.viewer.plugins",
						"pl.pronux.sokker.ui.plugins");
				Set<Object> keySet = pluginsProperties.keySet();
				Iterator<Object> itr = keySet.iterator();
				Map<String, String> newKeys = new HashMap<String, String>();
				while (itr.hasNext()) {
					String key = (String) itr.next();
					if (key.matches("pl.pronux.sokker.viewer.*")) {
						String value = pluginsProperties.getProperty(key);
						key = key.replaceAll("pl.pronux.sokker.viewer.plugins",
								"pl.pronux.sokker.ui.plugins");
						newKeys.put(key, value);
						itr.remove();
					}
				}

				for (String key : newKeys.keySet()) {
					pluginsProperties.put(key, newKeys.get(key));
				}

				String[] table = pluginsValue.split(";");

				ArrayList<String> alPlugins = new ArrayList<String>();
				for (String plugin : table) {
					alPlugins.add(plugin);
				}

				for (int i = 0; i < plugins.length; i++) {
					if (pluginsProperties.getProperty(plugins[i] + ".turn") == null) {
						pluginsProperties
								.setProperty(plugins[i] + ".turn", "1");
					}
					if (pluginsProperties.getProperty(plugins[i] + ".author") == null) {
						pluginsProperties.setProperty(plugins[i] + ".author",
								"rym3k");
					}
					if (pluginsProperties.getProperty(plugins[i] + ".version") == null) {
						pluginsProperties.setProperty(plugins[i] + ".version",
								SV.SK_VERSION);
					}
					if (!alPlugins.contains(plugins[i])) {
						alPlugins.add(plugins[i]);
					}
				}

				alPlugins.retainAll(Arrays.asList(plugins));
				StringBuilder pluginList = new StringBuilder();
				for (String plugin : alPlugins) {
					pluginList.append(plugin).append(";");
				}
				pluginsProperties.setProperty("plugins", pluginList.toString());
				pluginsProperties.synchronize();

			}
		} catch (FileNotFoundException e) {
			Log.warning("Properties Checker", e);
		} catch (IOException e) {
			Log.warning("Properties Checker", e);
		}

		return true;
	}

	public boolean checkUserProperties() {

		SVProperties userProperties = new SVProperties();
		File file = new File(baseDir + SEP + "settings" + SEP
				+ "user.properties");
		if (file.exists()) {
			try {
				userProperties.loadFile(file);

				HashMap<String, String> hmValues = new HashMap<String, String>();
				hmValues.put("font.table", "1|Arial|8|0|GTK|1|");
				hmValues.put("color.injuryBg", "255,255,255");
				hmValues.put("color.trainedJunior", "10,150,0");
				hmValues.put("color.decreaseTable", "255,210,210");
				hmValues.put("font.description", "1|Courier New|8|0|GTK|1|");
				hmValues.put("color.newTableItem", "220,222,245");
				hmValues.put("color.increaseDescription", "0,147,0");
				hmValues.put("color.injuryFg", "255,0,0");
				hmValues.put("font.main", "1|Arial|8|0|GTK|1|");
				hmValues.put("color.increaseTable", "233,252,224");
				hmValues.put("color.newTreeItem", "0,0,255");
				hmValues.put("color.decreaseDescription", "255,0,0");
				hmValues.put("color.error", "255,0,0");

				Set<String> keys = hmValues.keySet();
				String value;
				String temp_value;
				for (String key : keys) {
					value = hmValues.get(key);
					temp_value = userProperties.getProperty(key);
					if (temp_value == null) {
						userProperties.setProperty(key, value);
					}
				}
				userProperties.synchronize();
			} catch (FileNotFoundException e) {
				Log.warning("Properties Checker", e);
				return false;
			} catch (IOException e) {
				Log.warning("Properties Checker", e);
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean checkAll() {
		checkBackupProperties();
		checkSokkerProperties();
		checkPluginsProperties();
		checkUserProperties();
		return true;
	}
}
