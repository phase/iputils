package eagle.nest.iputils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 * 
 * @author Originally by Skyost, modified by Phase
 *
 */
public class IPInfo {
	
	private static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s,%s";
	private static final String IP_INFO_DB_API_KEY = "NONE"; // If you do not setup, IP_INFO_DB will not work !
	
	public static final String SKIP_VERSION = "0.1";
	
	private static IPMode mode = IPMode.IP_API;
	
	public static final IPMode getIPMode() {
		return mode;
	}
	
	public static final void setIPMode(final IPMode mode) {
		IPInfo.mode = mode;
	}
	
	public static final String getPlayerIP(final Player player) {
		return player.getAddress().getAddress().getHostAddress();
	}
	
	public static final IPData getIPData(String ip) {
		try {
			final JSONObject json = (JSONObject)new JSONParser().parse(httpGet(mode.getQueryURL(ip)));
			final String[] args = mode.getArguments();
			System.out.println(args.length);
			System.out.println(json);
			return new IPData(json.get(args[0]).toString(), json.get(args[1]).toString(), json.get(args[2]).toString(), json.get(args[3]).toString(), json.get(args[4]).toString(), Double.valueOf(json.get(args[5]).toString()), Double.valueOf(json.get(args[6]).toString()), json.get(args[7]).toString());
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static final IPDataWeather getWeatherData(final String city, final String countryCode) {
		try {
			final JSONObject weather = (JSONObject)((JSONArray)((JSONObject)new JSONParser().parse(httpGet(String.format(OPEN_WEATHER_MAP_URL, city, countryCode)))).get("weather")).get(0);
			return new IPDataWeather(weather.get("main").toString(), weather.get("description").toString());
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static final String httpGet(final String url) throws IOException {
		final HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "SkIP");
		final String response = connection.getResponseCode() + " " + connection.getResponseMessage();
		if(!response.startsWith("200")) {
			return null;
		}
		final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		final StringBuilder builder = new StringBuilder();
		while((line = in.readLine()) != null) {
			builder.append(line);
		}
		in.close();
		return builder.toString();
	}
	
	public static class IPData {
		
		private final String countryName;
		private final String countryCode;
		private final String region;
		private final String city;
		private final String zip;
		private final double latitude;
		private final double longitude;
		private final String timezone;
		
		public IPData(final String countryName, final String countryCode, final String region, final String city, final String zip, final double latitude, final double longitude, final String timezone) {
			this.countryName = countryName;
			this.countryCode = countryCode;
			this.region = region;
			this.zip = zip;
			this.city = city;
			this.latitude = latitude;
			this.longitude = longitude;
			this.timezone = timezone.replaceAll("_", " ");
		}
		
		public final String getCountryName() {
			return countryName;
		}
		
		public final String getCountryCode() {
			return countryCode;
		}
		
		public final String getRegion() {
			return region;
		}
		
		public final String getZip() {
			return zip;
		}
		
		public final String getCity() {
			return city;
		}
		
		public final double getLatitude() {
			return latitude;
		}
		
		public final double getLongitude() {
			return longitude;
		}
		
		public final String getTimezone() {
			return timezone;
		}
		
		public final IPDataWeather getWeatherData(){
			return IPInfo.getWeatherData(city, countryCode);
		}
		
	}
	
	public static class IPDataWeather {
		
		private final String weatherName;
		private final String weatherDesc;
		
		public IPDataWeather(final String weatherName, final String weatherDesc) {
			this.weatherName = weatherName;
			this.weatherDesc = weatherDesc;
		}
		
		public final String getWeatherName() {
			return weatherName;
		}
		
		public final String getWeatherDescription() {
			return weatherDesc;
		}
		
	}
	
	public enum IPMode {
		
		IP_API("http://ip-api.com/json/%s", new String[]{"country", "countryCode", "regionName", "city", "zip", "lat", "lon", "timezone"}),
		IP_INFO_DB("http://api.ipinfodb.com/v3/ip-city/?key=" + IP_INFO_DB_API_KEY + "&ip=%s", new String[]{"countryName", "countryCode", "regionName", "cityName", "zipCode", "latitude", "longitude", "timeZone"});
		
		private final String queryURL;
		private final String[] args;
		
		IPMode(final String queryURL, final String[] args) {
			this.queryURL = queryURL;
			this.args = args;
		}
		
		public final String getQueryURL(final String ip) {
			return String.format(queryURL, ip);
		}
		
		public final String[] getArguments() {
			return args;
		}
		
	}

}
