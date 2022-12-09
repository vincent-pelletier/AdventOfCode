package y2022;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import common.AdventOfCode;

public class Day02 extends AdventOfCode {

	@Test
	public void test() {
		List<String> matches = readFileAsStrings();

		Map<String, Integer> scores = new HashMap<>();
		scores.put("A X", 1 + 3);
		scores.put("A Y", 2 + 6);
		scores.put("A Z", 3 + 0);
		scores.put("B X", 1 + 0);
		scores.put("B Y", 2 + 3);
		scores.put("B Z", 3 + 6);
		scores.put("C X", 1 + 6);
		scores.put("C Y", 2 + 0);
		scores.put("C Z", 3 + 3);

		int score = 0;
		for(String match : matches) {
			score += scores.get(match);
		}
		print(score);

		br();

		scores.clear();
		scores.put("A X", 3 + 0);
		scores.put("A Y", 1 + 3);
		scores.put("A Z", 2 + 6);
		scores.put("B X", 1 + 0);
		scores.put("B Y", 2 + 3);
		scores.put("B Z", 3 + 6);
		scores.put("C X", 2 + 0);
		scores.put("C Y", 3 + 3);
		scores.put("C Z", 1 + 6);

		score = 0;
		for(String match : matches) {
			score += scores.get(match);
		}
		print(score);
	}
}
