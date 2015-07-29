package com.nbezhenar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
	private static String MAIN_ADDRESS = "https://www.youtube.com";

	public static Map<String, Integer> getLinksList(String url)
			throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");

		List<String> chosenLinks = new ArrayList<>();

		for (Element link : links) {
			chosenLinks.add(link.toString());
		}
		List<String> chosenLinks1 = new ArrayList<>();
		for (String s : chosenLinks) {
			if (s.contains("watch?v=") && s.contains("stat view-count"))
				chosenLinks1.add(s);
		}

		List<String> finalLinks = new LinkedList<>();
		List<Integer> intRating = new LinkedList<>();
		for (String str : chosenLinks1) {
			str.replace("<a href=\"", "");
			Matcher m = Pattern.compile("\\/watch\\?v=.*\"?").matcher(str);
			if (m.find()) {
				String matchedText = m.group();
				String[] arr = matchedText.split(" ");
				finalLinks.add(arr[0].replace("\"", "").trim());
				List<String> tempRating = new LinkedList<>();
				for (String arrString : arr) {
					if (arrString.contains("view-count")) {
						tempRating.add(arrString);
					}
					;
				}
				List<String> templist = new LinkedList<>();

				for (String rating : tempRating) {
					templist.add(rating.replaceAll("\\D+", ""));
				}
				for (String rating : templist) {
					intRating.add(Integer.parseInt(rating));
				}
			}
		}
		Map<String, Integer> linksMap = new HashMap<>();
		Iterator<String> links_iter = finalLinks.iterator();
		Iterator<Integer> rating_iter = intRating.iterator();

		while (links_iter.hasNext() && rating_iter.hasNext()) {
			linksMap.put(MAIN_ADDRESS + links_iter.next(), rating_iter.next());

		}

		return linksMap;
	}

	public static Map<String, Integer> getTotalList(String url, int nestingLevel)
			throws IOException {
		Map<String, Integer> totalMap = new HashMap<String, Integer>();
		Map<String, Integer> tempMap = new HashMap<String, Integer>();
		tempMap = getLinksList(url);
		totalMap.putAll(tempMap);
		int counter = 0;
		if (counter < nestingLevel) {
			Map<String, Integer> tempMap1 = new HashMap<String, Integer>();
			for (String address : tempMap.keySet()) {
				tempMap1.putAll(getLinksList(address));
			}
			totalMap.putAll(tempMap1);
			counter++;
			tempMap.clear();
			tempMap.putAll(tempMap1);
		}
		return totalMap;
	}
}
