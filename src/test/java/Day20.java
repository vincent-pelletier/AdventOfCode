import java.util.List;

import org.junit.Test;

public class Day20 extends AdventOfCode {

	@Test
	public void day20() {
		printHeader();
		List<String> input = readFileAsStrings("day20.txt");

		String algorithm = input.get(0).replace(".", "0").replace("#", "1");
		int first = ctoi(algorithm.charAt(0));

		int maxIterations = 50;
		int border = maxIterations + 2;
		int maxY = input.size() - 2 + border * 2;
		int maxX = input.get(2).length() + border * 2;

		int[][] grid = new int[maxX][maxY];
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				grid[i][j] = 0;
			}
		}
		for(int r = 2; r < input.size(); r++) {
			String row = input.get(r);
			for(int c = 0; c < row.length(); c++) {
				grid[r+border-2][c+border] = ctoi(row.charAt(c));
			}
		}

		for(int iteration = 0; iteration < maxIterations; iteration++) {
			int[][] newGrid = new int[maxX][maxY];
			for(int i = 0; i < maxX; i++) {
				for(int j = 0; j < maxY; j++) {
					// change to first char of algorithm every 2
					newGrid[i][j] = iteration % 2 == 0 ? first : 0;
				}
			}

			for(int j = border-(iteration+1); j < maxY - (border-(iteration+1)); j++) {
				for(int i = border-(iteration+1); i < maxX - (border-(iteration+1)); i++) {
					String bin = "" +
							grid[j-1][i-1] + grid[j-1][i] + grid[j-1][i+1] +
							grid[j][i-1] + grid[j][i] + grid[j][i+1] +
							grid[j+1][i-1] + grid[j+1][i] + grid[j+1][i+1];
					int idx = bin2dec(bin);
					newGrid[j][i] = Integer.valueOf(algorithm.charAt(idx) + "");
				}
			}
			grid = newGrid;
		}

		int count = 0;
		for(int i = border-maxIterations; i < maxX - (border-maxIterations); i++) {
			for(int j = border-maxIterations; j < maxY - (border-maxIterations); j++) {
				if(grid[j][i] == 1) {
					count++;
				}
			}
		}
		print(count);
	}

	private int ctoi(char c) {
		return Integer.valueOf((c + "").replace(".", "0").replace("#", "1"));
	}
}
