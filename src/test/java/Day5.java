import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Day5 extends AdventOfCode {

	@Test
	public void day5() {
		printHeader();
		List<String> input = readFileAsStrings("day5.txt");
		List<Line> lines = new ArrayList<>();
		for(String line : input) {
			lines.add(new Line(line));
		}
		int maxX = lines.stream().map(l -> Math.max(l.x1, l.x2)).max(Integer::compareTo).get();
		int maxY = lines.stream().map(l -> Math.max(l.y1, l.y2)).max(Integer::compareTo).get();
		int[][] grid = new int[maxX + 1][maxY + 1];
		for(int[] row : grid) {
			Arrays.fill(row, 0);
		}
		for(Line line : lines) {
			for(Coordinates c : line.getCoords()) {
				grid[c.x][c.y] += 1;
			}
		}

		int over2 = 0;
		for(int i = 0; i <= maxX; i++) {
			for(int j = 0; j <= maxY; j++) {
				// System.out.print(String.valueOf(grid[j][i]).replace("0", "."));
				if(grid[i][j] >= 2) {
					over2++;
				}
			}
			// System.out.println("");
		}
		print(over2);
	}

	private class Line {
		private int x1, y1, x2, y2;

		public Line(String input) {
			String[] values = input.replace(" ", "").replace("->", ",").split(",");
			x1 = Integer.valueOf(values[0]);
			y1 = Integer.valueOf(values[1]);
			x2 = Integer.valueOf(values[2]);
			y2 = Integer.valueOf(values[3]);
		}

		public List<Coordinates> getCoords() {
			List<Coordinates> coords = new ArrayList<>();

			if(x1 == x2) {
				int maxY = Math.max(y1, y2);
				int minY = Math.min(y1, y2);
				for(int y = minY; y <= maxY; y++) {
					coords.add(new Coordinates(x1, y));
				}
			} else if(y1 == y2) {
				int maxX = Math.max(x1, x2);
				int minX = Math.min(x1, x2);
				for(int x = minX; x <= maxX; x++) {
					coords.add(new Coordinates(x, y1));
				}
			} else {
				// part 2
				if(x1 < x2 && y1 < y2) {
					int idx = 0;
					for(int x = x1; x <= x2; x++) {
						coords.add(new Coordinates(x, y1 + idx));
						idx++;
					}
				} else if(x1 > x2 && y1 > y2) {
					int idx = 0;
					for(int x = x1; x >= x2; x--) {
						coords.add(new Coordinates(x, y1 - idx));
						idx++;
					}
				} else if(x1 < x2 && y1 > y2) {
					int idx = 0;
					for(int x = x1; x <= x2; x++) {
						coords.add(new Coordinates(x, y1 - idx));
						idx++;
					}
				} else if(x1 > x2 && y1 < y2) {
					int idx = 0;
					for(int x = x1; x >= x2; x--) {
						coords.add(new Coordinates(x, y1 + idx));
						idx++;
					}
				}
			}

			return coords;
		}
	}

	private class Coordinates {
		public int x, y;
		public Coordinates(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
