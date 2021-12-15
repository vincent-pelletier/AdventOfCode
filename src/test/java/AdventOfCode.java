import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdventOfCode {

	private final static String prefix = "src/test/resources/";

	protected List<String> readFileAsStrings(String filename) {
		List<String> strings = new ArrayList<>();
		try {
			Scanner reader = new Scanner(new File(prefix + filename));
			while (reader.hasNextLine()) {
				strings.add(reader.nextLine());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			print(filename + " not found");
			throw new IllegalArgumentException();
		}
		return strings;
	}

	protected List<Integer> readFileAsInts(String filename) {
		return readFileAsStrings(filename).stream().map(Integer::valueOf).collect(Collectors.toList());
	}

	protected List<Integer> readSingleLineAsInts(String filename) {
		return csv2list(readFileAsStrings(filename).get(0));
	}

	protected List<Integer> csv2list(String csv) {
		return Stream.of(csv.split(",")).map(Integer::valueOf).collect(Collectors.toList());
	}

	protected void print(Object o) {
		System.out.println(o);
	}

	protected void print2(Object o) {
		System.out.print(o);
	}

	protected void printHeader() {
		String day = Thread.currentThread().getStackTrace()[2].getMethodName();
		print("[" + String.valueOf(day.charAt(0)).toUpperCase() +
				day.substring(1, 3) + " " + day.substring(3, day.length()) + "]");
	}

	protected void br() {
		print("--------");
	}

	protected int bin2dec(String binary) {
		return Integer.parseInt(binary, 2);
	}

	protected Map<String, Long> getCharCounts(String s) {
		Map<String, Long> map = new HashMap<>();
		for(int i = 0; i < s.length(); i++) {
			String c = s.charAt(i) + "";
			if(!map.containsKey(c)) {
				map.put(c, 0L);
			}
			map.put(c, map.get(c) + 1L);
		}
		return map;
	}

	protected void addCharCounts(Map<String, Long> initialMap, String s) {
		Map<String, Long> charCounts = getCharCounts(s);
		for(Entry<String, Long> entry : charCounts.entrySet()) {
			if(!initialMap.containsKey(entry.getKey())) {
				initialMap.put(entry.getKey(), 0L);
			}
			initialMap.put(entry.getKey(), initialMap.get(entry.getKey()) + entry.getValue());
		}
	}

	protected Map<String, Long> getWordCounts(String s, String separator) {
		String[] parts = s.split(separator);
		Map<String, Long> map = new HashMap<>();
		for(String part : parts) {
			if(map.containsKey(part)) {
				map.put(part, 0L);
			}
			map.put(part, map.get(part) + 1L);
		}
		return map;
	}

	protected void printGrid(int[][] grid) {
		int maxX = grid.length;
		int maxY = grid[0].length;
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				print2(grid[i][j]);
			}
			print("");
		}
	}

	protected class Point {
		public int x;
		public int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object o) {
			Point other = (Point)o;
			return x == other.x && y == other.y;
		}

		@Override
		public int hashCode() {
			return x * 1000 + y;
		}
	}
}
