import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class Day13 extends AdventOfCode {

	@Test
	public void day13() {
		printHeader();
		List<String> input = readFileAsStrings("day13.txt");

		List<Point> pts = new ArrayList<>();
		boolean fold = false;
		List<String> folds = new ArrayList<>();
		for(String s : input) {
			if(s.isEmpty()) {
				fold = true;
				continue;
			}
			if(fold) {
				folds.add(s.replace("fold along ", ""));
			} else {
				pts.add(new Point(Integer.valueOf(s.split(",")[0]), Integer.valueOf(s.split(",")[1])));
			}
		}
		for(String f : folds) {
			for(Point pt : pts) {
				if(f.startsWith("x")) {
					pt.foldX(Integer.valueOf(f.replace("x=", "")));
				} else {
					pt.foldY(Integer.valueOf(f.replace("y=", "")));
				}
			}
			if(fold) { // reuse bool to print after first fold
				print(new HashSet<>(pts).size());
				br();
				fold = false;
			}
		}
		Set<Point> finalPts = new HashSet<>(pts);
		int maxX = finalPts.stream().mapToInt(p -> p.x).max().getAsInt();
		int maxY = finalPts.stream().mapToInt(p -> p.y).max().getAsInt();
		for(int j = 0; j <= maxY; j++) {
			for(int i = 0; i <= maxX; i++) {
				if(finalPts.contains(new Point(i, j))) {
					print2("#");
				} else {
					print2("_");
				}
			}
			print("");
		}
	}

	private class Point {
		public int x;
		public int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void foldX(int foldX) {
			if(x > foldX) {
				x = x - (x - foldX) * 2;
			}
		}

		public void foldY(int foldY) {
			if(y > foldY) {
				y = y - (y - foldY) * 2;
			}
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
