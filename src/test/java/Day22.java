import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class Day22 extends AdventOfCode {

	Set<Cuboid> remove = new HashSet<>();

	@Test
	public void day22() {
		printHeader();
		List<String> input = readFileAsStrings("day22.txt");

		Set<Cuboid> positiveCuboids = new HashSet<>();
		for(String row : input) {
			String[] parts = row.replace("on ", "").replace("off ", "").replace("x=", "").replace("y=", "").replace("z=", "").replace("..", ",").split(",");
			int val = row.startsWith("on") ? 1 : 0;
			int x1 = i(parts[0]);
			int x2 = i(parts[1]);
			int y1 = i(parts[2]);
			int y2 = i(parts[3]);
			int z1 = i(parts[4]);
			int z2 = i(parts[5]);
			Cuboid cube = new Cuboid(x1, x2, y1, y2, z1, z2, val);
			if(positiveCuboids.isEmpty()) {
				positiveCuboids.add(cube);
			} else {
				Set<Cuboid> newList = new HashSet<>();
				for(Cuboid c : positiveCuboids) {
					newList.addAll(c.getSplitPositives(cube));
				}
				for(Cuboid c : remove) {
					newList.remove(c);
				}
				remove.clear();
				positiveCuboids.clear();
				positiveCuboids.addAll(newList);
			}
		}
		print(positiveCuboids.stream().map(c -> c.getValue()).mapToLong(l -> l).sum());
	}

	private int i(String s) {
		return Integer.valueOf(s);
	}

	private class Cuboid {
		int x1, x2, y1, y2, z1, z2, val;

		public Cuboid(int x1, int x2, int y1, int y2, int z1, int z2, int val) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			this.z1 = z1;
			this.z2 = z2;
			this.val = val;
		}

		public long getValue() {
			if(z1 > z2 || y1 > y2 || x1 > x2) return -1; // invalid, exclude
			return Math.abs((long)(z2 - z1) + 1) * Math.abs((long)(y2 - y1) + 1) * Math.abs((long)(x2 - x1) + 1);
		}

		public List<Cuboid> getSplitPositives(Cuboid other) {
			List<Cuboid> list = new ArrayList<>();
			if(overlaps(other)) {
				if(completeOverlap(other) && other.val == 1) {
					list.add(this);
					remove.add(other);
				} else if(other.completeOverlap(this) && other.val == 1) {
					list.add(other);
					remove.add(this);
				} else if(completeOverlap(other) && other.val == 0) {
					list.addAll(removeOverlap(other));
				} else if(other.completeOverlap(this) && other.val == 0) {
					// nothing to add, cuboid disappears.
				} else {
					// partial
					Cuboid intersect = new Cuboid(Math.max(x1, other.x1), Math.min(x2, other.x2),
							Math.max(y1, other.y1), Math.min(y2, other.y2),
							Math.max(z1, other.z1), Math.min(z2, other.z2), 0);

					list.addAll(removeOverlap(intersect));
					if(other.val == 1) {
						list.add(other);
					}
				}
			} else {
				list.add(this);
				if(other.val == 1) {
					list.add(other);
				}
			}
			return list;
		}

		// other should be completely included in this
		private List<Cuboid> removeOverlap(Cuboid other) {
			List<Cuboid> list = new ArrayList<>();
			// split 1 into 6 (top, bottom, left, right, front, back)
			// top: x1-x2, y1-y2, other.z2-z2
			list.add(new Cuboid(x1, x2, y1, y2, other.z2 + 1, z2, 1));
			// bottom: x1-x2, y1-y2: z1-other.z1;
			list.add(new Cuboid(x1, x2, y1, y2, z1, other.z1 - 1, 1));
			// front: x1-x2, y1-other.y1, other.z1-other.z2
			list.add(new Cuboid(x1, x2, y1, other.y1 - 1, other.z1, other.z2, 1));
			// back: x1-x2, other.y2-y2, other.z1-other.z2
			list.add(new Cuboid(x1, x2, other.y2 + 1, y2, other.z1, other.z2, 1));
			// left: x1-other.x1, other.y1-other.y2, other.z1-other.z2
			list.add(new Cuboid(x1, other.x1 - 1, other.y1, other.y2, other.z1, other.z2, 1));
			// right: other.x2-x2, other.y1-other.y2, other.z1-other.z2
			list.add(new Cuboid(other.x2 + 1, x2, other.y1, other.y2, other.z1, other.z2, 1));

			// in case the full overlap has 1 side that's equal, remove empty ones
			list.removeIf(c -> c.getValue() <= 0);

			return list;
		}

		private boolean overlaps(Cuboid other) {
			if(noOverlap(other)) {
				//print("No overlap between " + this + " and " + other);
				return false;
			}
			if(completeOverlap(other)) {
				//print("Complete overlap of " + other + " by " + this);
				return true;
			}
			if(other.completeOverlap(this)) {
				//print("Complete overlap of " + this + " by " + other);
				return true;
			}
			if(partialOverlap(other)) {
				//print("Partial overlap");
				return true;
			}
			print("Missed overlap condition between " + this + " and " + other);
			return false;
		}

		private boolean noOverlap(Cuboid other) {
			return x1 > other.x2 || x2 < other.x1 || y1 > other.y2 || y2 < other.y1 || z1 > other.z2 || z2 < other.z1;
		}

		private boolean completeOverlap(Cuboid other) {
			return x1 <= other.x1 && other.x2 <= x2 && y1 <= other.y1 && other.y2 <= y2 && z1 <= other.z1 && other.z2 <= z2;
		}

		private boolean partialOverlap(Cuboid other) {
			return (isBetween(x1, other.x1, x2) || isBetween(x1, other.x2, x2) || areBetween(x1, other.x1, other.x2, x2) || areBetween(other.x1, x1, x2, other.x2))
					&& (isBetween(y1, other.y1, y2) || isBetween(y1, other.y2, y2) || areBetween(y1, other.y1, other.y2, y2) || areBetween(other.y1, y1, y2, other.y2))
					&& (isBetween(z1, other.z1, z2) || isBetween(z1, other.z2, z2) || areBetween(z1, other.z1, other.z2, z2) || areBetween(other.z1, z1, z2, other.z2));
		}

		private boolean isBetween(int a, int b, int c) {
			return a <= b && b <= c;
		}

		private boolean areBetween(int a, int b, int c, int d) {
			return a <= b && c <= d;
		}

		@Override
		public boolean equals(Object o) {
			Cuboid other = (Cuboid)o;
			return x1 == other.x1 && x2 == other.x2 && y1 == other.y1 && y2 == other.y2 && z1 == other.z1 && z2 == other.z2;
		}

		@Override
		public int hashCode() {
			return Arrays.asList(x1, x2, y1, y2, z1, z2).hashCode();
		}

		@Override
		public String toString() {
			return (val == 1 ? "[on x=" : "[off x=") + x1 + ".." + x2 + ", y=" + y1 + ".." + y2 + ", z=" + z1 + ".." + z2 + "]";
		}
	}
}
