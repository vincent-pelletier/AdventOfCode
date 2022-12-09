import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class Day10 extends AdventOfCode {

	@Test
	public void day10() {
		printHeader();
		List<String> lines = readFileAsStrings("day10.txt");

		List<String> openDelimiters = Arrays.asList("(", "[", "{", "<");
		List<String> closeDelimiters = Arrays.asList(")", "]", "}", ">");
		Map<String, String> mapOpenByClosed = new HashMap<>();
		Map<String, String> mapClosedByOpen = new HashMap<>();
		for(int i = 0; i < openDelimiters.size(); i++) {
			mapOpenByClosed.put(closeDelimiters.get(i), openDelimiters.get(i));
			mapClosedByOpen.put(openDelimiters.get(i), closeDelimiters.get(i));
		}
		List<String> corrupted = new ArrayList<>();
		List<String> corruptedLines = new ArrayList<>();
		for(String line : lines) {
			List<String> open = new ArrayList<>();
			for(int i = 0; i < line.length(); i++) {
				String c = line.charAt(i) + "";
				if(openDelimiters.contains(c)) {
					open.add(c);
				} else if(closeDelimiters.contains(c)) {
					if(mapOpenByClosed.get(c).equals(open.get(open.size() - 1))) { // if open for closed is last open, close
						open.remove(open.size() - 1);
					} else {
						corrupted.add(c);
						corruptedLines.add(line);
						break;
					}
				}
			}
		}
		Map<String, Integer> pts = new HashMap<>();
		pts.put(")", 3);
		pts.put("]", 57);
		pts.put("}", 1197);
		pts.put(">", 25137);
		print(corrupted.stream().mapToInt(c -> pts.get(c)).sum());

		br();

		List<String> incomplete = lines.stream().filter(l -> !corruptedLines.contains(l)).collect(Collectors.toList());

		pts.clear();
		pts.put(")", 1);
		pts.put("]", 2);
		pts.put("}", 3);
		pts.put(">", 4);
		List<Long> scores = new ArrayList<>();
		for(String line : incomplete) {
			List<String> open = new ArrayList<>();
			for(int i = 0; i < line.length(); i++) {
				String c = line.charAt(i) + "";
				if(openDelimiters.contains(c)) {
					open.add(c);
				} else if(closeDelimiters.contains(c)) {
					if(mapOpenByClosed.get(c).equals(open.get(open.size() - 1))) { // if open for closed is last open, close
						open.remove(open.size() - 1);
					}
				}
			}
			long score = 0L;
			Collections.reverse(open);
			for(String openChar : open) {
				score *= 5;
				score += pts.get(mapClosedByOpen.get(openChar));
			}
			scores.add(score);
		}
		Collections.sort(scores);
		print(scores.get(scores.size() / 2));
	}
}
