package y2021;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day25 extends AdventOfCode {

	int maxX;
	int maxY;

	@Test
	public void day25() {
		List<String> input = readFileAsStrings();
		maxX = input.get(0).length();
		maxY = input.size();

		List<SeaCucumber> eastCucumbers = new ArrayList<>();
		List<SeaCucumber> southCucumbers = new ArrayList<>();

		int[][] grid = new int[input.size()][input.get(0).length()];
		for(int y = 0; y < input.size(); y++) {
			String row = input.get(y);
			for(int x = 0; x < row.length(); x++) {
				String c = row.charAt(x) + "";
				if(".".equals(c)) {
					grid[y][x] = 0;
				} else {
					grid[y][x] = 1;
					if("v".equals(c)) {
						southCucumbers.add(new SeaCucumber(c, x, y));
					} else {
						eastCucumbers.add(new SeaCucumber(c, x, y));
					}
				}
			}
		}

		for(int i = 0; i < 500; i++) {
			int canMove = 0;
			for(SeaCucumber east : eastCucumbers) {
				east.setCanMove(grid);
				if(east.canMove) {
					canMove++;
				}
			}
			for(SeaCucumber east : eastCucumbers) {
				east.move(grid);
			}
			for(SeaCucumber south : southCucumbers) {
				south.setCanMove(grid);
				if(south.canMove) {
					canMove++;
				}
			}
			for(SeaCucumber south : southCucumbers) {
				south.move(grid);
			}
			if(canMove == 0) {
				print("No cucumber can move at step " + (i+1));
				break;
			}
		}
	}

	private class SeaCucumber {
		Point pos;
		Point direction;
		boolean canMove;

		public SeaCucumber(String v, int x, int y) {
			pos = new Point(x, y);
			direction = v.equals("v") ? new Point(0, 1) : new Point(1, 0);
		}

		public void setCanMove(int[][] grid) {
			Point next = getNextPos();
			canMove = grid[next.y][next.x] == 0;
		}

		public void move(int[][] grid) {
			if(canMove) {
				grid[pos.y][pos.x] = 0;
				pos = getNextPos();
				grid[pos.y][pos.x] = 1;
				canMove = false;
			}
		}

		private Point getNextPos() {
			int x = pos.x + direction.x;
			int y = pos.y + direction.y;
			if(x >= maxX) {
				x = 0;
			}
			if(y >= maxY) {
				y = 0;
			}
			return new Point(x, y);
		}
	}
}
