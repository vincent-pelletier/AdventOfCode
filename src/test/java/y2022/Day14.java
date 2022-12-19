package y2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day14 extends AdventOfCode {

	@Test
	public void test() {
		List<String> input = readFileAsStrings();

		List<Point> rocks = new ArrayList<>();

		for(String line : input) {
			String[] pts = line.split(" -> ");
			Point prev = null;
			for(int i = 0; i < pts.length; i++) {
				Point p = new Point(i(pts[i].split(",")[0]), i(pts[i].split(",")[1]));
				rocks.add(p);
				if(prev != null) {
					for(int x = Math.min(p.x, prev.x) + 1; x < Math.max(p.x, prev.x); x++) {
						rocks.add(new Point(x, prev.y));
					}
					for(int y = Math.min(p.y, prev.y) + 1; y < Math.max(p.y, prev.y); y++) {
						rocks.add(new Point(prev.x, y));
					}
				}
				prev = p;
			}
		}

		int maxY = rocks.stream().mapToInt(r -> r.y).max().getAsInt();
		int minX = rocks.stream().mapToInt(r -> r.x).min().getAsInt();
		int maxX = rocks.stream().mapToInt(r -> r.x).max().getAsInt();

		int sandCount = 0;
		boolean exit = false;
		while(!exit) {
			Point sand = new Point(500, 0);
			while(!exit) {
				if(sand.y >= maxY) exit = true;
				if(sand.x <= minX) exit = true;
				if(sand.x >= maxX) exit = true;
				Point bottom = new Point(sand.x, sand.y + 1);
				Point bottomLeft = new Point(sand.x - 1, sand.y + 1);
				Point bottomRight = new Point(sand.x + 1, sand.y + 1);
				if(!rocks.containsAll(Arrays.asList(bottom, bottomLeft, bottomRight))) {
					if(!rocks.contains(bottom)) {
						sand = bottom;
					} else if(!rocks.contains(bottomLeft)) {
						sand = bottomLeft;
					} else if(!rocks.contains(bottomRight)) {
						sand = bottomRight;
					}
				} else {
					rocks.add(sand);
					sandCount++;
					break;
				}
			}
		}
		print(sandCount);

		br();

		int floorY = maxY + 2;
		exit = false;
		while(!exit) {
			Point sand = new Point(500, 0);
			if(rocks.contains(sand)) {
				break;
			}
			boolean added = false;
			while(sand.y < floorY) {
				Point bottom = new Point(sand.x, sand.y + 1);
				Point bottomLeft = new Point(sand.x - 1, sand.y + 1);
				Point bottomRight = new Point(sand.x + 1, sand.y + 1);
				if(!rocks.containsAll(Arrays.asList(bottom, bottomLeft, bottomRight))) {
					if(!rocks.contains(bottom)) {
						sand = bottom;
					} else if(!rocks.contains(bottomLeft)) {
						sand = bottomLeft;
					} else if(!rocks.contains(bottomRight)) {
						sand = bottomRight;
					}
				} else {
					rocks.add(sand);
					sandCount++;
					added = true;
					break;
				}
			}
			if(!added) {
				rocks.add(sand);
				// print(sandCount + ": " + sand + " (floor)"); // uncomment for progress... < 160*160
			}
		}
		print(sandCount);
	}
}
