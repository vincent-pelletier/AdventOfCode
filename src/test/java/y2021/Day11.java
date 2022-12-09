package y2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import common.AdventOfCode;

public class Day11 extends AdventOfCode {

	private int flashes = 0;
	private int maxX;
	private int maxY;

	@Test
	public void day11() {
		printHeader();
		List<String> input = readFileAsStrings();
		List<Dumbo> dumbos = new ArrayList<>();
		Map<Point, Dumbo> map = new HashMap<>();
		maxX = input.size();
		maxY = input.get(0).length();
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				Point p = new Point(i, j);
				Dumbo dumbo = new Dumbo(Integer.valueOf(input.get(i).charAt(j) + ""), p);
				dumbos.add(dumbo);
				map.put(p, dumbo);
			}
		}

		for(int step = 1; step < 1000; step++) {
			for(Dumbo dumbo : dumbos) {
				if(dumbo.inc()) {
					dumbo.flashed = true;
					incNeighbors(dumbo, map);
				}
			}
			if(dumbos.stream().allMatch(d -> d.flashed)) {
				print("All flashed at step " + step);
				break;
			}
			for(Dumbo dumbo : dumbos) {
				dumbo.endStep();
			}
			if(step == 100) {
				print("Flashes after step 100: " + flashes);
			}
		}
	}

	private void incNeighbors(Dumbo d, Map<Point, Dumbo> map) {
		for(Point p : getAllNeighbors(d.pos)) {
			if(map.get(p).inc()) {
				map.get(p).flashed = true;
				incNeighbors(map.get(p), map);
			}
		}
	}

	private List<Point> getAllNeighbors(Point p) {
		List<Point> neighbors = new ArrayList<>();
		for(int i = p.x-1; i <= p.x+1; i++) {
			for(int j = p.y-1; j <= p.y+1; j++) {
				if(isInGrid(i, j) && !(p.x == i && p.y == j)) {
					neighbors.add(new Point(i, j));
				}
			}
		}
		return neighbors;
	}

	private boolean isInGrid(int x, int y) {
		return x >= 0 && x < maxX && y >= 0 && y < maxY;
	}

	private class Dumbo {
		public int value;
		public Point pos;
		public boolean flashed;

		public Dumbo(int v, Point p) {
			value = v;
			pos = p;
			flashed = false;
		}

		// return true to flash
		public boolean inc() {
			return ++value > 9 && !flashed;
		}

		public void endStep() {
			flashed = false;
			if(value > 9) {
				flashes++;
				value = 0;
			}
		}
	}
}
