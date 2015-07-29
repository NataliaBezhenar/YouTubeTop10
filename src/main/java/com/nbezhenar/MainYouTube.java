package com.nbezhenar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainYouTube {

	public static Map<String, Integer> getTOPLinks(String url,
			int numberOfLinks, int nestingLevel) throws IOException {

		Map<String, Integer> finalMap = new HashMap<>();
		finalMap = Sorter.getTopNValues(
				Sorter.compareByRating(Parser.getTotalList(url, nestingLevel)),
				numberOfLinks);

		return finalMap;
	}

	public static void main(String[] args) {

		String url = "https://www.youtube.com/watch?v=WPvGqX-TXP0";
		int numberOfTopElements = 10;
		int nestingLevel = 2;

		Map<String, Integer> map = new HashMap<>();
		try {
			map = getTOPLinks(url, numberOfTopElements, nestingLevel);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("TOP Links: " + map.size());
		for (Map.Entry<String, Integer> m : map.entrySet()) {
			System.out.println(m);
		}
	}
}
