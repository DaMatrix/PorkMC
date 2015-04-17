package redstonelamp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import redstonelamp.cmd.CommandSender;
import redstonelamp.cmd.ConsoleCommandSender;
import redstonelamp.cmd.SimpleCommandMap;
import redstonelamp.plugin.Plugin;
import redstonelamp.plugin.PluginBase;
import redstonelamp.plugin.PluginLoader;
import redstonelamp.plugin.PluginManager;

public class RedstoneLamp {

	public static String SOFTWARE = "RedstoneLamp";
	public static String VERSION = "1.0.0";
	public static String CODENAME = "Baby Villager";
	public static String STAGE = "DEVELOPMENT";
	public static int API_VERSION = 1;

	public static boolean DEGUG = true; // Debug mode for developers

	public static Server server;

	/*
	 * RedstoneLamp properties
	 */
	public static Properties props;
	private final static String REDSTONELAMP_PROPERTIES = "redstonelamp.properties";
	private final static String PLUGIN_FOLDER = "PLUGIN_FOLDER";
	private final static String PLUGIN_CLASS_FILE_FOLDER = "PLUGIN_CLASS_FILE_FOLDER";
	private final static String JAVA_SDK = "JAVA_SDK";

	public static void main(String[] args) {
		server = new Server("RedstoneLamp Server", "Welcome to this server!",
				19132, false, true, 16, 20, false, true, true, 0, false, false,
				true, 1, null, "world", null, "DEFAULT", true, false, null,
				true);

		// load Redstone property file
		loadProperties();
		/*
		 * Load each plug-in in the Plug-ins directory and create the directory
		 * if it doesnt exist
		 */
		File folder = new File(props.getProperty(PLUGIN_FOLDER));
		if (!folder.exists())
			folder.mkdirs();

		File inuse = new File(props.getProperty(PLUGIN_CLASS_FILE_FOLDER).trim()); // class files are generated in this folder
		if (!inuse.exists())
			inuse.mkdirs();
		
	    SimpleCommandMap  simpleCommandMap = new SimpleCommandMap();
		PluginManager pluginManager        = new PluginManager(server, simpleCommandMap);
		PluginLoader pluginLoader          = new PluginLoader();
		
		// sets java SDK Location and PLUGIN_FOLDER
		pluginLoader.setPluginOption(props.getProperty(PLUGIN_FOLDER).trim(),
				props.getProperty(PLUGIN_CLASS_FILE_FOLDER).trim(), props
						.getProperty(JAVA_SDK).trim());
		
		pluginManager.registerPluginLoader(pluginLoader);
		pluginManager.loadPlugins(folder);
		
		CommandSender sender   = new ConsoleCommandSender();
		////// test sample command: Player issues a '/List' command
		// gets the plug-in for which this command is associated with
		// call PluginBase.onCommand() with commandSender and other arguments
		String cmd  = "List";
		PluginBase base = (PluginBase) pluginLoader.getPluginCommand(cmd);
		if( base != null )
			base.onCommand(sender, null, cmd, null);
		////// End test command
		
		/*
		 * Tell the console the server has loaded (Dummy location)
		 */
		server.getLogger().info("Done! For help, type \"help\" or \"?\"");
	}

	/*
	 * loads property file
	 */
	private static void loadProperties() {
		props = new Properties();
		InputStream is = RedstoneLamp.class.getClassLoader()
				.getResourceAsStream(REDSTONELAMP_PROPERTIES);
		try {
			props.load(is);
		} catch (IOException ioe) {
			throw new IllegalStateException(
					" redstonelamp property file is missing....");
		}
	}

}
