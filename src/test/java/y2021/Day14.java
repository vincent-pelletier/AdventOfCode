package y2021;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import common.AdventOfCode;

public class Day14 extends AdventOfCode {

	@Test
	public void day14() {
		printHeader();
		List<String> input = readFileAsStrings();

		String polymer = input.get(0);

		List<String> rules = input.subList(2, input.size());
		Map<String, String> map = new HashMap<>();
		for(String rule : rules) {
			map.put(rule.split(" -> ")[0], rule.split(" -> ")[1]);
		}

		String newValue = "";
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < polymer.length() - 1; j++) {
				String pair = polymer.substring(j, j+2);
				String value = map.get(pair);
				newValue += pair.charAt(0) + value;
				if(j == polymer.length() - 2) {
					newValue += pair.charAt(1);
				}
			}
			polymer = newValue;
			newValue = "";
		}

		Map<String, Long> letters = getCharCounts(polymer);
		print(letters);
		print(Collections.max(letters.values()) - Collections.min(letters.values()));

		br();

		polymer = input.get(0);
		Map<String, Map<String, Long>> letterIncrementsAfterHalf = new HashMap<>();
		for(Entry<String, String> rule : map.entrySet()) {
			letterIncrementsAfterHalf.put(rule.getKey(), new HashMap<>());
			for(String r : map.values()) {
				letterIncrementsAfterHalf.get(rule.getKey()).put(r, 0L);
			}
		}
		// for each pair, compute growth of letters after half
		for(Entry<String, String> rule : map.entrySet()) {
			addPairInitial(rule.getKey(), 1, 20, map, letterIncrementsAfterHalf.get(rule.getKey()));
		}

		letters.clear();
		for(String r : map.values()) {
			letters.put(r, 0L);
		}
		addCharCounts(letters, polymer);
		for(int i = 0; i < polymer.length() - 1; i++) {
			String pair = polymer.substring(i, i+2);
			addPair(pair, 1, 20, map, letters, letterIncrementsAfterHalf);
		}
		print(letters);
		print(Collections.max(letters.values()) - Collections.min(letters.values()));
	}

	private void addPairInitial(String pair, int step, int max, Map<String, String> map, Map<String, Long> letters) {
		String newChar = map.get(pair);
		letters.put(newChar, letters.get(newChar) + 1L);
		if(step < max) {
			addPairInitial(pair.charAt(0) + newChar, step + 1, max, map, letters);
			addPairInitial(newChar + pair.charAt(1), step + 1, max, map, letters);
		}
	}

	private void addPair(String pair, int step, int max, Map<String, String> map, Map<String, Long> letters, Map<String, Map<String, Long>> letterIncrementsAfterHalf) {
		String newChar = map.get(pair);
		letters.put(newChar, letters.get(newChar) + 1L);
		if(step < max) {
			addPair(pair.charAt(0) + newChar, step + 1, max, map, letters, letterIncrementsAfterHalf);
			addPair(newChar + pair.charAt(1), step + 1, max, map, letters, letterIncrementsAfterHalf);
		} else {
			// add letters of first half for each child pair
			for(Entry<String, Long> mapAfterHalf : letterIncrementsAfterHalf.get(pair.charAt(0) + newChar).entrySet()) {
				letters.put(mapAfterHalf.getKey(), letters.get(mapAfterHalf.getKey()) + mapAfterHalf.getValue());
			}
			for(Entry<String, Long> mapAfterHalf : letterIncrementsAfterHalf.get(newChar + pair.charAt(1)).entrySet()) {
				letters.put(mapAfterHalf.getKey(), letters.get(mapAfterHalf.getKey()) + mapAfterHalf.getValue());
			}
		}
	}
}
