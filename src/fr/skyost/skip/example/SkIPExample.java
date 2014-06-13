package fr.skyost.skip.example;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.skyost.skip.SkIP;

public class SkIPExample extends JavaPlugin implements Listener {
	
	@Override
	public final void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	private final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		event.setJoinMessage(player.getName() + " is coming from " + SkIP.getIPData("213.111.35.163").getCountryName() + " !");
	}

}
