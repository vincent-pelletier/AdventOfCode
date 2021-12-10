import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class Day9 extends AdventOfCode {

	private int maxX;
	private int maxY;

	@Test
	public void day9() {
		printHeader();
		List<String> input = readFileAsStrings("day9.txt");
		maxX = input.size();
		maxY = input.get(0).length();
		int[][] grid = new int[maxX][maxY];
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				grid[i][j] = Integer.valueOf(input.get(i).charAt(j) + "");
			}
		}
		int sum = 0;
		List<Point> lowPts = new ArrayList<>();
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				boolean isLow = true;
				if(i > 0) {
					isLow &= grid[i][j] < grid[i-1][j];
				}
				if(j > 0) {
					isLow &= grid[i][j] < grid[i][j-1];
				}
				if(i < maxX - 1) {
					isLow &= grid[i][j] < grid[i+1][j];
				}
				if(j < maxY - 1) {
					isLow &= grid[i][j] < grid[i][j+1];
				}
				if(isLow) {
					sum += grid[i][j] + 1;
					lowPts.add(new Point(i, j));
					// print2("[" + grid[i][j] + "]");
				} else {
					// print2(" " + grid[i][j] + " ");
				}
			}
			// print("");
		}
		print(sum);

		br();

		List<Integer> bassins = new ArrayList<>();
		for(Point low : lowPts) {
			List<Point> neighbors = getAllNeighbors(low).stream().filter(pt -> grid[pt.x][pt.y] < 9).collect(Collectors.toList());
			Set<Point> set = new HashSet<>();
			set.addAll(neighbors);
			while(true) {
				Set<Point> setCopy = new HashSet<>(set);
				for(Point p : set) {
					setCopy.addAll(getAllNeighbors(p).stream().filter(pt -> grid[pt.x][pt.y] < 9).collect(Collectors.toList()));
				}
				if(setCopy.size() == set.size()) {
					// no new neighbors
					break;
				}
				set.addAll(setCopy);
			}
			bassins.add(set.size());
		}
		Collections.sort(bassins);
		Collections.reverse(bassins);
		print(bassins.get(0) * bassins.get(1) * bassins.get(2));
	}

	private List<Point> getAllNeighbors(Point p) {
		List<Point> neighbors = new ArrayList<>();
		if(isInGrid(p.x-1, p.y)) {
			neighbors.add(new Point(p.x-1, p.y));
		}
		if(isInGrid(p.x+1, p.y)) {
			neighbors.add(new Point(p.x+1, p.y));
		}
		if(isInGrid(p.x, p.y-1)) {
			neighbors.add(new Point(p.x, p.y-1));
		}
		if(isInGrid(p.x, p.y+1)) {
			neighbors.add(new Point(p.x, p.y+1));
		}
		return neighbors;
	}

	private boolean isInGrid(int x, int y) {
		return x >= 0 && x < maxX && y >= 0 && y < maxY;
	}

	private class Point {
		public final int x, y;

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
			return x * 100 + y;
		}
	}
}
