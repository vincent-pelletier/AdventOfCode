package y2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import common.AdventOfCode;

public class Day19 extends AdventOfCode {

	@Test
	public void day19() {
		List<String> input = readFileAsStrings();

		List<Scanner> scanners = new ArrayList<>();
		Scanner last = null;
		for(String row : input) {
			if(last == null) {
				last = new Scanner(row);
				continue;
			}
			if(row.isEmpty()) {
				scanners.add(last);
				last = null;
				continue;
			}
			last.addBeacon(row);
		}
		scanners.add(last);

		scanners.get(0).computeBeaconsForScanner0(new Point3d(0, 0, 0), 0); // 0 will not change any beacon value

		// loop over all scanners, except scanner 0
		// if scanner has 12+ beacons in common, compute position based on scanner 0
		// then, use the computed ones as scanner 0 reference to find more
		// until all done

		List<Scanner> newScannersBasedOnScanner0 = new ArrayList<>();
		newScannersBasedOnScanner0.add(scanners.get(0));

		while(!scanners.stream().allMatch(Scanner::isFinal)) {
			Scanner s0Ref = newScannersBasedOnScanner0.remove(0);
			for(int v = 0; v < scanners.size(); v++) {
				Scanner sX = scanners.get(v);
				if(sX.equals(s0Ref)) {
					continue;
				}
				if(sX.isFinal()) {
					continue;
				}

				// get beacon distances for scanner 0 ref and scanner X
				List<BeaconDuo> duos0 = s0Ref.getBeaconDistancesFromScanner0();
				List<BeaconDuo> duosX = sX.getBeaconDistances();

				// keep identical references
				List<BeaconDuo> matchedDuos0 = duos0.stream().filter(duosX::contains).collect(Collectors.toList());
				Collections.sort(matchedDuos0, BeaconDuo::compareTo);

				// count how many distinct beacons
				Set<Point3d> matchedBeacons0 = new HashSet<>();
				for(BeaconDuo duo : matchedDuos0) {
					matchedBeacons0.add(duo.b1);
					matchedBeacons0.add(duo.b2);
				}

				// check if the two scanners have 12+ beacons in common
				if(matchedBeacons0.size() < 12) {
					continue;
				}

				// find all links to beacon 0 for s0
				List<Point3d> matchedBeacons0List = new ArrayList<>(matchedBeacons0);
				List<BeaconDuo> s0b0duos = new ArrayList<>();
				for(int j = 1; j < matchedBeacons0List.size(); j++) {
					s0b0duos.add(new BeaconDuo(matchedBeacons0List.get(0), matchedBeacons0List.get(j)));
				}
				Collections.sort(s0b0duos, BeaconDuo::compareTo);

				// find all links to beacon 1 for s0
				List<BeaconDuo> s0b1duos = new ArrayList<>();
				for(int j = 0; j < matchedBeacons0List.size(); j++) {
					if(j == 1) continue;
					s0b1duos.add(new BeaconDuo(matchedBeacons0List.get(1), matchedBeacons0List.get(j)));
				}
				Collections.sort(s0b1duos, BeaconDuo::compareTo);

				// do the same for scanner X
				// get beacon distances for scanner X, keep identical ones as scanner 0
				List<BeaconDuo> matchedDuosX = duosX.stream().filter(duos0::contains).collect(Collectors.toList());
				Collections.sort(matchedDuosX, BeaconDuo::compareTo);
				Set<Point3d> matchedBeaconsX = new HashSet<>();
				for(BeaconDuo duo : matchedDuosX) {
					matchedBeaconsX.add(duo.b1);
					matchedBeaconsX.add(duo.b2);
				}

				// find all possible locations for beacon 0 and beacon 1 of sX
				// put them in this list. The second one will use what
				// the first one put in the list and identify the one
				// that gets repeated, to identify the orientation of sX.
				List<Point3d> allPts = new ArrayList<>();
				List<Point3d> matchedBeaconsXList = new ArrayList<>(matchedBeaconsX);

				// loop over the 12 beacons from sX
				int found0and1 = 0;
				for(int i = 0; i < matchedBeaconsXList.size(); i++) {
					if(found0and1 == 2) {
						break;
					}
					// create a list of duos with the 12 from s0
					// both lists are then sorted by distance between dots
					List<BeaconDuo> filteredDuosX = new ArrayList<>();
					for(int j = 0; j < matchedBeacons0List.size(); j++) {
						if(i == j) continue;
						filteredDuosX.add(new BeaconDuo(matchedBeaconsXList.get(i), matchedBeaconsXList.get(j)));
					}
					Collections.sort(filteredDuosX, BeaconDuo::compareTo);

					// if both are equal, then we found the beacon in sX that corresponds to beacon 0 in s0
					if(s0b0duos.stream().allMatch(filteredDuosX::contains)) {
						found0and1++;
						// get both points
						Point3d beacon0FromS0 = matchedBeacons0List.get(0);
						Point3d beacon0FromSx = matchedBeaconsXList.get(i);

						// find the correct orientation for sX
						List<Point3d> beacon0FromSxPossibleLocations = beacon0FromSx.getAllOrientations();
						for(Point3d beacon0FromSxPossibleLocation : beacon0FromSxPossibleLocations) {
							int x = beacon0FromS0.x - beacon0FromSxPossibleLocation.x;
							int y = beacon0FromS0.y - beacon0FromSxPossibleLocation.y;
							int z = beacon0FromS0.z - beacon0FromSxPossibleLocation.z;
							Point3d sXPosFromBeacon0 = new Point3d(x, y, z);

							if(allPts.contains(sXPosFromBeacon0)) {
								// Beacon 1 identified this possible location too, match!
								sX.computeBeaconsForScanner0(sXPosFromBeacon0, beacon0FromSxPossibleLocations.indexOf(beacon0FromSxPossibleLocation));
								newScannersBasedOnScanner0.add(sX);
								break;
							}
							// now, add all possible positions for sX based on beacon 1 in s0
							allPts.add(sXPosFromBeacon0);
						}
					}
					// if both are equal, then we found the beacon in sX that corresponds to beacon 1 in s0
					if(s0b1duos.stream().allMatch(filteredDuosX::contains)) {
						found0and1++;
						// get both points
						Point3d beacon1FromS0 = matchedBeacons0List.get(1);
						Point3d beacon1FromSx = matchedBeaconsXList.get(i);

						// find the correct orientation for sX
						List<Point3d> beacon1FromSxPossibleLocations = beacon1FromSx.getAllOrientations();
						for(Point3d beacon1FromSxPossibleLocation : beacon1FromSxPossibleLocations) {
							int x = beacon1FromS0.x - beacon1FromSxPossibleLocation.x;
							int y = beacon1FromS0.y - beacon1FromSxPossibleLocation.y;
							int z = beacon1FromS0.z - beacon1FromSxPossibleLocation.z;
							Point3d sXPosFromBeacon1 = new Point3d(x, y, z);

							if(allPts.contains(sXPosFromBeacon1)) {
								// Beacon 0 identified this possible location too, match!
								sX.computeBeaconsForScanner0(sXPosFromBeacon1, beacon1FromSxPossibleLocations.indexOf(beacon1FromSxPossibleLocation));
								newScannersBasedOnScanner0.add(sX);
								break;
							}
							// now, add all possible positions for sX based on beacon 1 in s0
							allPts.add(sXPosFromBeacon1);
						}
					}
				}
			}
		}

		Set<Point3d> set = new HashSet<>();
		for(Scanner scanner : scanners) {
			set.addAll(scanner.beaconsFromScanner0);
		}
		print(set.size());

		br();

		int highest = 0;
		for(Scanner s1 : scanners) {
			for(Scanner s2 : scanners) {
				if(s1.equals(s2)) continue;
				int x = s1.pointFromScanner0.x - s2.pointFromScanner0.x;
				int y = s1.pointFromScanner0.y - s2.pointFromScanner0.y;
				int z = s1.pointFromScanner0.z - s2.pointFromScanner0.z;
				int manhattanDistance = Math.abs(x) + Math.abs(y) + Math.abs(z);
				if(manhattanDistance > highest) {
					highest = manhattanDistance;
				}
			}
		}
		print(highest);
	}

	private class Scanner {
		int id;
		Point3d pointFromScanner0;
		List<Point3d> beacons = new ArrayList<>();
		List<Point3d> beaconsFromScanner0 = new ArrayList<>();
		List<BeaconDuo> duos = new ArrayList<>();
		List<BeaconDuo> duosFromScanner0 = new ArrayList<>();

		public Scanner(String row) {
			id = Integer.valueOf(row.replace("--- scanner ", "").replace(" ---", ""));
		}

		public void addBeacon(String row) {
			beacons.add(new Point3d(row));
		}

		public List<BeaconDuo> getBeaconDistances() {
			if(duos.isEmpty()) {
				duos = new ArrayList<>();
				for(int i = 0; i < beacons.size(); i++) {
					for(int j = i+1; j < beacons.size(); j++) {
						duos.add(new BeaconDuo(beacons.get(i), beacons.get(j)));
					}
				}
			}
			return duos;
		}

		public List<BeaconDuo> getBeaconDistancesFromScanner0() {
			if(duosFromScanner0.isEmpty()) {
				duosFromScanner0 = new ArrayList<>();
				for(int i = 0; i < beaconsFromScanner0.size(); i++) {
					for(int j = i+1; j < beaconsFromScanner0.size(); j++) {
						duosFromScanner0.add(new BeaconDuo(beaconsFromScanner0.get(i), beaconsFromScanner0.get(j)));
					}
				}
			}
			return duosFromScanner0;
		}

		public void computeBeaconsForScanner0(Point3d scannerPt, int orientationIndex) {
			pointFromScanner0 = scannerPt;
			for(Point3d beacon : beacons) {
				Point3d orientedBeacon = beacon.getAllOrientations().get(orientationIndex);
				int actualX = orientedBeacon.x + scannerPt.x;
				int actualY = orientedBeacon.y + scannerPt.y;
				int actualZ = orientedBeacon.z + scannerPt.z;
				beaconsFromScanner0.add(new Point3d(actualX, actualY, actualZ));
			}
		}

		public boolean isFinal() {
			return pointFromScanner0 != null;
		}

		@Override
		public boolean equals(Object o) {
			Scanner other = (Scanner)o;
			return id == other.id;
		}

		@Override
		public int hashCode() {
			return id;
		}
	}

	private class BeaconDuo {
		Point3d b1;
		Point3d b2;
		double distance;

		public BeaconDuo(Point3d b1, Point3d b2) {
			this.b1 = b1;
			this.b2 = b2;
			distance = b1.distanceFrom(b2);
		}

		@Override
		public boolean equals(Object o) {
			BeaconDuo other = (BeaconDuo)o;
			return distance == other.distance;
		}

		@Override
		public int hashCode() {
			return Double.valueOf(distance).hashCode();
		}

		public int compareTo(BeaconDuo other) {
			return Double.compare(distance, other.distance);
		}

		@Override
		public String toString() {
			return "Distance[" + b1 + ";" + b2 + "] = " + distance;
		}
	}

	private class Point3d {
		int x, y, z;

		public Point3d(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Point3d(String s) {
			String[] parts = s.split(",");
			x = Integer.valueOf(parts[0]);
			y = Integer.valueOf(parts[1]);
			z = Integer.valueOf(parts[2]);
		}

		public double distanceFrom(Point3d o) {
			return Math.sqrt(Math.pow((x - o.x), 2.0) + Math.pow((y - o.y), 2.0) + Math.pow((z - o.z), 2.0));
		}

		public List<Point3d> getAllOrientations() {
			List<Point3d> orientations = new ArrayList<>();

			// rotate z
			//[ ][ ][ ][ ][ ][ ][ ]
			//[ ][ ][o][ ][x][ ][ ] x = 1, y = 2
			//[ ][o][ ][ ][ ][o][ ]
			//[ ][ ][ ][Ø][ ][ ][ ]
			//[ ][o][ ][ ][ ][o][ ]
			//[ ][ ][o][ ][o][ ][ ]
			//[ ][ ][ ][ ][ ][ ][ ]
			orientations.add(new Point3d(x, y, z));
			orientations.add(new Point3d(x, -y, z));
			orientations.add(new Point3d(-x, y, z));
			orientations.add(new Point3d(-x, -y, z));
			orientations.add(new Point3d(y, x, z));
			orientations.add(new Point3d(y, -x, z));
			orientations.add(new Point3d(-y, x, z));
			orientations.add(new Point3d(-y, -x, z));

			// rotate x
			orientations.add(new Point3d(z, y, x));
			orientations.add(new Point3d(z, -y, x));
			orientations.add(new Point3d(-z, y, x));
			orientations.add(new Point3d(-z, -y, x));
			orientations.add(new Point3d(y, z, x));
			orientations.add(new Point3d(y, -z, x));
			orientations.add(new Point3d(-y, z, x));
			orientations.add(new Point3d(-y, -z, x));

			// rotate y
			orientations.add(new Point3d(x, z, y));
			orientations.add(new Point3d(x, -z, y));
			orientations.add(new Point3d(-x, z, y));
			orientations.add(new Point3d(-x, -z, y));
			orientations.add(new Point3d(z, x, y));
			orientations.add(new Point3d(z, -x, y));
			orientations.add(new Point3d(-z, x, y));
			orientations.add(new Point3d(-z, -x, y));

			// all -?
			orientations.add(new Point3d(-x, -y, -z));
			orientations.add(new Point3d(-x, y, -z));
			orientations.add(new Point3d(x, -y, -z));
			orientations.add(new Point3d(x, y, -z));
			orientations.add(new Point3d(-y, -x, -z));
			orientations.add(new Point3d(-y, x, -z));
			orientations.add(new Point3d(y, -x, -z));
			orientations.add(new Point3d(y, x, -z));

			orientations.add(new Point3d(-z, -y, -x));
			orientations.add(new Point3d(-z, y, -x));
			orientations.add(new Point3d(z, -y, -x));
			orientations.add(new Point3d(z, y, -x));
			orientations.add(new Point3d(-y, -z, -x));
			orientations.add(new Point3d(-y, z, -x));
			orientations.add(new Point3d(y, -z, -x));
			orientations.add(new Point3d(y, z, -x));

			orientations.add(new Point3d(-x, -z, -y));
			orientations.add(new Point3d(-x, z, -y));
			orientations.add(new Point3d(x, -z, -y));
			orientations.add(new Point3d(x, z, -y));
			orientations.add(new Point3d(-z, -x, -y));
			orientations.add(new Point3d(-z, x, -y));
			orientations.add(new Point3d(z, -x, -y));
			orientations.add(new Point3d(z, x, -y));

			return orientations;
		}

		@Override
		public boolean equals(Object o) {
			Point3d other = (Point3d)o;
			return x == other.x && y == other.y && z == other.z;
		}

		@Override
		public int hashCode() {
			return (x + "," + y + "," + z).hashCode();
		}

		@Override
		public String toString() {
			return x + "," + y + "," + z;
		}
	}
}
