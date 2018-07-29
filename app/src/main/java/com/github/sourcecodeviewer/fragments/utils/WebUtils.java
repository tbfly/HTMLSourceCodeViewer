package com.github.sourcecodeviewer.fragments.utils;

import android.util.Log;

import com.github.sourcecodeviewer.MyApp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUtils {

	public WebUtils() {
	}

	public static HashMap<String, ArrayList<String>> getHtmlResources(String html, String enteredUrl) {

		Document doc;
		doc = Jsoup.parse(html);

		ArrayList<String> cssLinks = new ArrayList<String>();
		Elements allCss = doc.select("link[href$=.css]"); // CSS
		for (Element css : allCss) {
			cssLinks.add(returnValidUrl(css.attr("href"), enteredUrl));
		}

		ArrayList<String> jsLinks = new ArrayList<String>();
		Elements allJs = doc.select("script[src]"); // JS
		for (Element js : allJs) {
			jsLinks.add(returnValidUrl(js.attr("src"), enteredUrl));
		}

		ArrayList<String> bigimgLinks = new ArrayList<String>();
		Elements allbigImg = doc.select("a[bigimgsrc]"); // BigImgSrc
		for (Element bigimg : allbigImg) {
			bigimgLinks.add(removeParams(returnValidUrl(bigimg.attr("bigimgsrc"), enteredUrl)));
		}

		if (allbigImg.size() == 0) {
			Elements allImg = doc.select("img"); // BigImgSrc
			for (Element img : allImg) {
				String url = returnValidUrl(img.attr("src"), enteredUrl);
				//for TuChong fix
				if (!url.contains("logo_small"))
					bigimgLinks.add(removeParams(url));
			}
		}

		//instagram
		if (allbigImg.size() == 0) {
			Elements allJsEle = doc.select("script");
			for (Element ele : allJsEle) {
				String ele_js = ele.toString();
				//window._sharedData
				if (ele_js.contains("display_url")) {
					//Log.d("MAP RESULTS JS/CSS display_url element = ", ele.toString());
					Pattern p = Pattern.compile("(?is)display_url\":\"(.+?)\""); // Regex for the value of the key
					Matcher m = p.matcher(ele.html()); // you have to use html here and NOT text! Text will drop the 'key' part

					while( m.find() ) {
						//Log.d("MAP RESULTS JS/CSS display_url element all = ", m.group()); // the whole key ('key = value')
						//Log.d("MAP RESULTS JS/CSS display_url element url = ", m.group(1)); // value only
						bigimgLinks.add(removeParams(returnValidUrl(m.group(1), enteredUrl)));
					}
				}
			}
		}

		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		result.put(MyApp.CSS, cssLinks);
		result.put(MyApp.JS, jsLinks);
		result.put(MyApp.BIGIMG, bigimgLinks);
		
		Log.d("MAP RESULTS JS/CSS", result.toString());
		
		return result;
	}
	
	public static String returnValidUrl(String partialUrl, String enteredUrl) {
			
		if (partialUrl.startsWith("http") || partialUrl.startsWith("https") || partialUrl.startsWith("www")) {
			return partialUrl;
		} else {
			String prefix = "http://";
			if(enteredUrl.startsWith("https://")) {
				prefix = "https://";
			}
			String host = getBaseDomain(enteredUrl);
			if(partialUrl.startsWith("/")) {
				return prefix + host + partialUrl;
			} else {
				return prefix + host + "/" + partialUrl;
			}
		}
	}
	
	public static String getHost(String url){
	    if(url == null || url.length() == 0)
	        return "";

	    int doubleslash = url.indexOf("//");
	    if(doubleslash == -1)
	        doubleslash = 0;
	    else
	        doubleslash += 2;

	    int end = url.indexOf('/', doubleslash);
	    end = end >= 0 ? end : url.length();

	    return url.substring(doubleslash, end);
	}

	public static String removeParams(String url){
		if(url == null || url.length() == 0)
			return "";

		int questionmark = url.indexOf("?");
		if(questionmark != -1)
			return url.substring(0, questionmark);
		else
			return url;
	}

	public static String getBaseDomain(String url) {
	    String host = getHost(url);

	    int startIndex = 0;
	    int nextIndex = host.indexOf('.');
	    int lastIndex = host.lastIndexOf('.');
	    while (nextIndex < lastIndex) {
	        startIndex = nextIndex + 1;
	        nextIndex = host.indexOf('.', startIndex);
	    }
	    if (startIndex > 0) {
	        return host.substring(startIndex);
	    } else {
	        return host;
	    }
	}
	
	public static String checkHttpPrefix(String url) {
		if (!url.startsWith("http") && !url.startsWith("https")) {
			url = "http://" + url;
		}
		return url;
	}

	public static String escapeForJs(String origin) {
		origin = origin.replaceAll("&", "&amp;");
		origin = origin.replaceAll("\"", "&quot;");
		origin = origin.replaceAll("'", "&#39;");
		origin = origin.replaceAll("<", "&lt;");
		origin = origin.replaceAll(">", "&gt;");
		origin = origin.replaceAll("\n", "&#13;");
		origin = origin.replaceAll("/", "&#47;");
		return origin;
	}
	
	public static String getResourceName(String resourceUrl) {
		String[] splitted = resourceUrl.split("/");
		return splitted[splitted.length-1];
	}
}
