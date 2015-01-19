package eagle.nest.iputils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class IPUtils extends JavaPlugin {

	public void onEnable(){
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new DataAnalysis(), this);
	}
	
}
