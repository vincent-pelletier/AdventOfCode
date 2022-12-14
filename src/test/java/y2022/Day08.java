package y2022;

import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day08 extends AdventOfCode {

	@Test
	public void test() {
		boolean debug = false;

		List<String> rows = readFileAsStrings();
		Tree[][] grid = new Tree[rows.size()][rows.get(0).length()];
		for(int i = 0; i < rows.size(); i++) {
			for(int j = 0; j < rows.size(); j++) {
				grid[i][j] = new Tree(i, j, i(rows.get(i).charAt(j) + ""));
			}
		}

		for(int i = 0; i < rows.size(); i++) {
			int min = -1;
			for(int j = 0; j < rows.get(0).length(); j++) {
				if(grid[i][j].value > min) {
					grid[i][j].visible = true;
					min = grid[i][j].value;
				}
			}
		}

		for(int i = 0; i < rows.size(); i++) {
			int min = -1;
			for(int j = rows.get(0).length() - 1; j >= 0 ; j--) {
				if(grid[i][j].value > min) {
					grid[i][j].visible = true;
					min = grid[i][j].value;
				}
			}
		}

		for(int j = 0; j < rows.get(0).length(); j++) {
			int min = -1;
			for(int i = 0; i < rows.size(); i++) {
				if(grid[i][j].value > min) {
					grid[i][j].visible = true;
					min = grid[i][j].value;
				}
			}
		}

		for(int j = 0; j < rows.get(0).length(); j++) {
			int min = -1;
			for(int i = rows.size() - 1; i >= 0; i--) {
				if(grid[i][j].value > min) {
					grid[i][j].visible = true;
					min = grid[i][j].value;
				}
			}
		}

		int visible = 0;
		for(int i = 0; i < rows.size(); i++) {
			for(int j = 0; j < rows.get(0).length(); j++) {
				if(grid[i][j].visible) {
					if(debug) print2("[" + grid[i][j].value + "]");
					visible++;
				} else {
					if(debug) print2(" " + grid[i][j].value + " ");
				}
			}
			if(debug) print("");
		}
		print(visible);

		br();

		for(int i = 0; i < rows.size(); i++) {
			for(int j = 0; j < rows.size(); j++) {
				// up (-j)
				int count = 0;
				for(int k = j - 1; k >= 0; k--) {
					if(grid[i][k].value < grid[i][j].value) {
						count++;
					} else {
						count++;
						break;
					}
				}
				grid[i][j].score *= count;

				// right (+i)
				count = 0;
				for(int k = i + 1; k < rows.size(); k++) {
					if(grid[k][j].value < grid[i][j].value) {
						count++;
					} else {
						count++;
						break;
					}
				}
				grid[i][j].score *= count;

				// down (+j)
				count = 0;
				for(int k = j + 1; k < rows.get(0).length(); k++) {
					if(grid[i][k].value < grid[i][j].value) {
						count++;
					} else {
						count++;
						break;
					}
				}
				grid[i][j].score *= count;

				// left (-i)
				count = 0;
				for(int k = i - 1; k >= 0; k--) {
					if(grid[k][j].value < grid[i][j].value) {
						count++;
					} else {
						count++;
						break;
					}
				}
				grid[i][j].score *= count;
			}
		}

		int max = 0;
		for(int i = 0; i < rows.size(); i++) {
			for(int j = 0; j < rows.size(); j++) {
				if(debug) print2(grid[i][j].score + ".");
				if(grid[i][j].score > max) {
					max = grid[i][j].score;
				}
			}
			if(debug) print("");
		}
		print(max);
	}

	class Tree extends Point {
		public final int value;
		public boolean visible = false;

		public int score = 1;

		public Tree(int x, int y, int value) {
			super(x, y);
			this.value = value;
		}
	}
}
