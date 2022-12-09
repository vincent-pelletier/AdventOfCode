package y2021;

import org.junit.Test;

import common.AdventOfCode;

public class Day17 extends AdventOfCode {

	private int minX, maxX, minY, maxY;

	@Test
	public void day17() {
		printHeader();
		String input = readFileAsStrings().get(0);
		// target area: x=20..30, y=-10..-5
		input = input.replace("target area:", "").replace(" ", "").replace("x=", "").replace("y=", "").replace("..", ",");
		minX = Integer.valueOf(input.split(",")[0]);
		maxX = Integer.valueOf(input.split(",")[1]);
		minY = Integer.valueOf(input.split(",")[2]);
		maxY = Integer.valueOf(input.split(",")[3]);
		print("x [" + minX + "," + maxX + "], y [" + minY + "," + maxY + "]");

		int threshold = 300;
		int maxSteps = 1000;
		int validHighestY = 0;
		int distinct = 0;
		for(int x = -threshold; x < threshold; x++) {
			for(int y = -threshold; y < threshold; y++) {
				int velocityX = x;
				int velocityY = y;
				int highestY = 0;
				Point17 p = new Point17(0, 0);
				for(int step = 0; step < maxSteps; step++) {
					p.move(velocityX, velocityY);
					if(p.y > highestY) {
						highestY = p.y;
					}
					if(velocityX > 0) {
						velocityX--;
					} else if(velocityX < 0) {
						velocityX++;
					}
					velocityY--;
					if(isInZone(p)) {
						distinct++;
						if(highestY > validHighestY) {
							validHighestY = highestY;
							//print("HighestY=" + highestY + " from " + x + "," + y);
						}
						break;
					}
				}
			}
		}
		print(validHighestY);
		print(distinct);
	}

	private boolean isInZone(Point p) {
		return minX <= p.x && p.x <= maxX && minY <= p.y && p.y <= maxY;
	}

	private class Point17 extends Point {
		public Point17(int x, int y) {
			super(x, y);
		}

		public void move(int byX, int byY) {
			x += byX;
			y += byY;
		}
	}
}
