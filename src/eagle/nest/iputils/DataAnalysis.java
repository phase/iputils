package eagle.nest.iputils;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Used to see what country most people join from.
 * @author Phase
 *
 */
public class DataAnalysis implements Listener {
	
	static HashMap<String/*CountryCode*/, Integer /*Joins from that location*/> countryRanks = new HashMap<String, Integer>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		IPInfo.IPData data = IPInfo.getIPData(IPInfo.getPlayerIP(p));
		String cc = data.getCountryCode();
		if(!countryRanks.containsKey(cc))
			countryRanks.put(cc, 1);
		else
			countryRanks.put(cc, countryRanks.get(cc));
	}
	
	public static String getHighestCountryCount(){
		String cc = "US";
		int current = 0;
		for(String s : countryRanks.keySet())
			if(countryRanks.get(s) > current){
				cc = s;
				current = countryRanks.get(s);
			}
		return cc;
	}
}
