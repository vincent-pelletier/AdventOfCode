package common;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;

public class AdventOfCode {

	private final static String prefix = "src/test/resources/";
	private long start;

	protected String getDefaultFilename() {
		return prefix + getClass().getName().substring(1).replace(".", "/").toLowerCase() + ".txt";
	}

	@Before
	public void printHeader() {
		String day = getClass().getSimpleName();
		print("[" + String.valueOf(day.charAt(0)).toUpperCase() +
				day.substring(1, 3) + " " + i(day.substring(3, day.length())) + "]");
		start = System.currentTimeMillis();
	}

	@After
	public void computeDuration() {
		print("(" + (System.currentTimeMillis() - start) + " ms)");
	}

	protected List<String> readFileAsStrings() {
		List<String> strings = new ArrayList<>();
		try {
			Scanner reader = new Scanner(new File(getDefaultFilename()));
			while (reader.hasNextLine()) {
				strings.add(reader.nextLine());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			print(getDefaultFilename() + " not found");
			throw new IllegalArgumentException();
		}
		return strings;
	}

	protected List<Integer> readFileAsInts() {
		return readFileAsStrings().stream().map(Integer::valueOf).collect(Collectors.toList());
	}

	protected List<Integer> readSingleLineAsInts() {
		return csv2list(readFileAsStrings().get(0));
	}

	protected List<Integer> csv2list(String csv) {
		return Stream.of(csv.split(",")).map(Integer::valueOf).collect(Collectors.toList());
	}

	protected void print(Object o) {
		System.out.println(o);
	}

	protected static void prints(Object o) {
		System.out.println(o);
	}

	protected void print2(Object o) {
		System.out.print(o);
	}

	protected void br() {
		print("--------");
	}

	protected int i(String s) {
		return Integer.valueOf(s);
	}

	protected int bin2dec(String binary) {
		return Integer.parseInt(binary, 2);
	}

	protected long bin2dec2(String binary) {
		return Long.parseLong(binary, 2);
	}

	protected String hex2bin(String hex) {
		hex = hex.replaceAll("0", "0000");
		hex = hex.replaceAll("1", "0001");
		hex = hex.replaceAll("2", "0010");
		hex = hex.replaceAll("3", "0011");
		hex = hex.replaceAll("4", "0100");
		hex = hex.replaceAll("5", "0101");
		hex = hex.replaceAll("6", "0110");
		hex = hex.replaceAll("7", "0111");
		hex = hex.replaceAll("8", "1000");
		hex = hex.replaceAll("9", "1001");
		hex = hex.replaceAll("A", "1010");
		hex = hex.replaceAll("B", "1011");
		hex = hex.replaceAll("C", "1100");
		hex = hex.replaceAll("D", "1101");
		hex = hex.replaceAll("E", "1110");
		hex = hex.replaceAll("F", "1111");
		return hex;
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

	protected void printGrid(int[][] grid, List<Point> nodesToHighlight) {
		int maxX = grid.length;
		int maxY = grid[0].length;
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				if(nodesToHighlight.contains(new Point(i, j))) {
					print2("[" + grid[i][j] + "]");
				} else {
					print2(" " + grid[i][j] + " ");
				}
			}
			print("");
		}
	}

	protected void printGridToFile(int[][] grid, List<Point> nodesToHighlight, String filename) {
		int maxX = grid.length;
		int maxY = grid[0].length;
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(prefix + filename))) {
			for(int i = 0; i < maxX; i++) {
				String s = "";
				for(int j = 0; j < maxY; j++) {
					if(nodesToHighlight.contains(new Point(i, j))) {
						s += ("[" + grid[i][j] + "]");

					} else {
						s += (" " + grid[i][j] + " ");
					}
				}
				writer.write(s + "\n");
			}
		} catch (IOException e) {
			print("Failed to print to file " + filename);
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

		@Override
		public String toString() {
			return x + "," + y;
		}
	}
}
