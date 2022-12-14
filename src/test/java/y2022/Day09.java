package y2022;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import common.AdventOfCode;

public class Day09 extends AdventOfCode {

	@Test
	public void test() {
		List<String> motions = readFileAsStrings();
		Point h = new Point(0, 0);
		Point t = new Point(0, 0);

		Set<Point> tailLocations = new HashSet<>();
		for(String motion : motions) {
			String dir = motion.split(" ")[0];
			int count = i(motion.split(" ")[1]);
			for(int i = 0; i < count; i++) {
				switch(dir) {
				case "R":
					h.x++;
					break;
				case "L":
					h.x--;
					break;
				case "U":
					h.y++;
					break;
				case "D":
					h.y--;
					break;
				}
				moveTail(h, t);
				tailLocations.add(new Point(t.x, t.y));
			}
		}
		print(tailLocations.size());

		br();

		h = new Point(0, 0);
		Point r1 = new Point(0, 0);
		Point r2 = new Point(0, 0);
		Point r3 = new Point(0, 0);
		Point r4 = new Point(0, 0);
		Point r5 = new Point(0, 0);
		Point r6 = new Point(0, 0);
		Point r7 = new Point(0, 0);
		Point r8 = new Point(0, 0);
		t = new Point(0, 0);
		tailLocations.clear();
		for(String motion : motions) {
			String dir = motion.split(" ")[0];
			int count = i(motion.split(" ")[1]);
			for(int i = 0; i < count; i++) {
				switch(dir) {
				case "R":
					h.x++;
					break;
				case "L":
					h.x--;
					break;
				case "U":
					h.y++;
					break;
				case "D":
					h.y--;
					break;
				}
				moveTail(h, r1);
				moveTail(r1, r2);
				moveTail(r2, r3);
				moveTail(r3, r4);
				moveTail(r4, r5);
				moveTail(r5, r6);
				moveTail(r6, r7);
				moveTail(r7, r8);
				moveTail(r8, t);
				tailLocations.add(new Point(t.x, t.y));
			}
		}
		print(tailLocations.size());
	}

	private void moveTail(Point h, Point t) {
		double d = Math.sqrt(Math.pow(t.y - h.y, 2) + Math.pow(t.x - h.x, 2));
		if(d > Math.sqrt(2.0)) {
			// move
			if(h.x == t.x) {
				if(h.y > t.y) {
					t.y++;
				} else {
					t.y--;
				}
			} else if(h.y == t.y) {
				if(h.x > t.x) {
					t.x++;
				} else {
					t.x--;
				}
			} else {
				// diag
				if(h.x > t.x) {
					t.x++;
				} else {
					t.x--;
				}
				if(h.y > t.y) {
					t.y++;
				} else {
					t.y--;
				}
			}
		}
	}
}
