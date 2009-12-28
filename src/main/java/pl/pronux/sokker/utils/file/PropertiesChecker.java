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

	public PropertiesChecker() {
		baseDir = System.getProperty("user.dir"); //$NON-NLS-1$
		File sokkerPropertiesFile = new File(baseDir + File.separator + "settings" + File.separator + "sokker.properties"); //$NON-NLS-1$ //$NON-NLS-2$
		if (sokkerPropertiesFile.exists()) {
			sokkerProperties = new SVProperties();
			try {
				sokkerProperties.loadFile(sokkerPropertiesFile);
			} catch (FileNotFoundException e) {
				Log.warning("Properties Checker", e); //$NON-NLS-1$
			} catch (IOException e) {
				Log.warning("Properties Checker", e); //$NON-NLS-1$
			}
		}
	}

	public PropertiesChecker(SVProperties sokkerProperties) {
		baseDir = System.getProperty("user.dir"); //$NON-NLS-1$
		this.sokkerProperties = sokkerProperties;
	}

	public boolean checkBackupProperties() {
		File bakFile;
		try {
			bakFile = new File(baseDir + File.separator + "settings" + File.separator + "backup.properties"); //$NON-NLS-1$ //$NON-NLS-2$
			if (bakFile.exists()) {
				Properties bakProperties = new Properties();
				FileInputStream fis = new FileInputStream(bakFile);
				bakProperties.load(fis);
				String bakString = bakProperties.getProperty("backup.dir"); //$NON-NLS-1$
				if (fis != null) {
					fis.close();
				}
				if (bakString != null) {
					sokkerProperties.setProperty("backup.dir", bakString); //$NON-NLS-1$
					sokkerProperties.synchronize();
				}
			}
		} catch (IOException e) {
			return false;
		}

		if (bakFile.exists()) {
			bakFile.delete();
		}
		return true;
	}

	public boolean checkSokkerProperties() {

		HashMap<String, String> hmValues = new HashMap<String, String>();
		hmValues.put("db.login", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("sk.dir", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("sk.login", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("db.name", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("sk.password", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("db.server", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("db.password", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("proxy.turn", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("proxy.host", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("proxy.port", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("proxy.user", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("proxy.password", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("db.list", "HSQLDB"); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("backup.dir", ""); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("db.type", "HSQLDB"); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("conf.savepass", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("conf.getXML", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("lang.code", "en_EN"); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("check.properties", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("sv.startup", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("info.update", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		hmValues.put("info.close", "1"); //$NON-NLS-1$ //$NON-NLS-2$

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

		if (sokkerProperties.getProperty("backup.dir") == null || sokkerProperties.getProperty("backup.dir").isEmpty()) { //$NON-NLS-1$ //$NON-NLS-2$ 
			sokkerProperties.setProperty("backup.dir", System.getProperty("user.dir") + File.separator + "bak" + File.separator); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		try {
			sokkerProperties.synchronize();
		} catch (FileNotFoundException e) {
			Log.warning("Properties Checker", e); //$NON-NLS-1$
		} catch (IOException e) {
			Log.warning("Properties Checker", e); //$NON-NLS-1$
		}
		return true;
	}

	public boolean checkPluginsProperties() {
		try {
			File pluginsFile = new File(baseDir + File.separator + "settings" + File.separator + "plugins.properties"); //$NON-NLS-1$ //$NON-NLS-2$
			SVProperties pluginsProperties;
			if (pluginsFile.exists()) {
				pluginsProperties = new SVProperties();
				pluginsProperties.loadFile(pluginsFile.getAbsolutePath());

				String[] plugins = { "pl.pronux.sokker.ui.plugins.ViewClub", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewPlayers", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewJuniors", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewCoaches", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewTrainings", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewArena", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewPlayersHistory", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewJuniorsTrained", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewJuniorsFired", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewCoachesFired", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewStatistics", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewExchange", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewAssistant", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewNotepad", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewTranslator", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewNT", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewTrash", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewBBCode", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewCalendar", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewTransfers", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewMatches", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewLeague", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewArchive", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewOffice", //$NON-NLS-1$
									"pl.pronux.sokker.ui.plugins.ViewSpy", //$NON-NLS-1$
				};

				String pluginsValue = pluginsProperties.getProperty("plugins"); //$NON-NLS-1$
				pluginsValue = pluginsValue.replaceAll("pl.pronux.sokker.viewer.plugins", "pl.pronux.sokker.ui.plugins");//$NON-NLS-1$ //$NON-NLS-2$
				Set<Object> keySet = pluginsProperties.keySet();
				Iterator<Object> itr = keySet.iterator();
				Map<String, String> newKeys = new HashMap<String, String>();
				while (itr.hasNext()) {
					String key = (String) itr.next();
					if (key.matches("pl.pronux.sokker.viewer.*")) { //$NON-NLS-1$
						String value = pluginsProperties.getProperty(key);
						key = key.replaceAll("pl.pronux.sokker.viewer.plugins", "pl.pronux.sokker.ui.plugins");//$NON-NLS-1$ //$NON-NLS-2$
						newKeys.put(key, value);
						itr.remove();
					}
				}

				for (String key : newKeys.keySet()) {
					pluginsProperties.put(key, newKeys.get(key));
				}

				String[] table = pluginsValue.split(";"); //$NON-NLS-1$

				ArrayList<String> alPlugins = new ArrayList<String>();
				for (String plugin : table) {
					alPlugins.add(plugin);
				}

				for (int i = 0; i < plugins.length; i++) {
					if (pluginsProperties.getProperty(plugins[i] + ".turn") == null) { //$NON-NLS-1$
						pluginsProperties.setProperty(plugins[i] + ".turn", "1"); //$NON-NLS-1$ //$NON-NLS-2$
					}

					if (pluginsProperties.getProperty(plugins[i] + ".author") == null) { //$NON-NLS-1$
						pluginsProperties.setProperty(plugins[i] + ".author", "rym3k"); //$NON-NLS-1$ //$NON-NLS-2$
					}

					if (pluginsProperties.getProperty(plugins[i] + ".version") == null) { //$NON-NLS-1$
						pluginsProperties.setProperty(plugins[i] + ".version", SV.SK_VERSION); //$NON-NLS-1$ //$NON-NLS-2$
					}

					if (!alPlugins.contains(plugins[i])) {
						alPlugins.add(plugins[i]);
					}
				}

				alPlugins.retainAll(Arrays.asList(plugins));
				StringBuffer pluginList = new StringBuffer();
				for (String plugin : alPlugins) {
					pluginList.append(plugin).append(";");
				}
				pluginsProperties.setProperty("plugins", pluginList.toString()); //$NON-NLS-1$
				pluginsProperties.synchronize();

			}
		} catch (FileNotFoundException e) {
			Log.warning("Properties Checker", e); //$NON-NLS-1$
		} catch (IOException e) {
			Log.warning("Properties Checker", e); //$NON-NLS-1$
		}

		return true;
	}

	public boolean checkUserProperties() {

		SVProperties userProperties = new SVProperties();

		File file = new File(baseDir + File.separator + "settings" + File.separator + "user.properties"); //$NON-NLS-1$ //$NON-NLS-2$
		if (file.exists()) {
			try {

				userProperties.loadFile(file);

				HashMap<String, String> hmValues = new HashMap<String, String>();
				hmValues.put("font.table", "1|Arial|8|0|GTK|1|"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.injuryBg", "255,255,255"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.trainedJunior", "10,150,0"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.decreaseTable", "255,210,210"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("font.description", "1|Courier New|8|0|GTK|1|"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.newTableItem", "220,222,245"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.increaseDescription", "0,147,0"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.injuryFg", "255,0,0"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("font.main", "1|Arial|8|0|GTK|1|"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.increaseTable", "233,252,224"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.newTreeItem", "0,0,255"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.decreaseDescription", "255,0,0"); //$NON-NLS-1$ //$NON-NLS-2$
				hmValues.put("color.error", "255,0,0"); //$NON-NLS-1$ //$NON-NLS-2$

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
				Log.warning("Properties Checker", e); //$NON-NLS-1$
				return false;
			} catch (IOException e) {
				Log.warning("Properties Checker", e); //$NON-NLS-1$
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

	public static void main(String[] args) {
		PropertiesChecker pchk = new PropertiesChecker();

		// pchk.checkPluginsProperties();
		pchk.checkSokkerProperties();
		pchk.checkUserProperties();
	}

	public SVProperties getSokkerProperties() {
		return sokkerProperties;
	}
}
