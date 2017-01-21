package de.studware;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class OnlineUtils {
	
	public static String removeUnusedParamsInUrl(String url) {
		System.out.println("I: Check URL for parameters and remove them");
		if (url.contains("&day=")) {
			url = url.substring(0, url.indexOf("&day="));
		}
		return changeConnetionToHTTPS(url);
	}
	
	public static String changeConnetionToHTTPS(String url) {
		return url.replaceFirst("http:", "https:");
	}

	public static boolean isRaplaAvailable(String url) {
		if (pointsURLtoRapla(url)) {
			try {
				URL raplaURL = new URL(url);
				URLConnection conn = raplaURL.openConnection();
				Map<String, List<String>> map = conn.getHeaderFields();

				System.out.println("I: Server Response for URL: " + raplaURL.toString());
				for (Map.Entry<String, List<String>> entry : map.entrySet()) {
					System.out.println("I: -> " + entry.getKey() + ": " + entry.getValue());
				}
				if (map.containsKey(null)) {
					List<String> contentLength = map.get(null);
					if (contentLength != null) {
						if (contentLength.get(0).contains("200 OK")) {
							return true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("E: URL is not from an DHBW Rapla calendar!");
		}
		return false;
	}

	private static boolean pointsURLtoRapla(String url) {
		if (url.contains("rapla.dhbw-")) {
			return true;
		} else {
			return false;
		}
	}
	
}
