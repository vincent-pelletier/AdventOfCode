import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class Day7 extends AdventOfCode {

	@Test
	public void day7() {
		printHeader();
		List<Integer> crabs = readSingleLineAsInts("day7.txt");

		int max = Collections.max(crabs);
		int min = Collections.min(crabs);
		int lowestFuel = Integer.MAX_VALUE;
		for(int i = min; i < max; i++) {
			int fuel = 0;
			for(Integer crab : crabs) {
				fuel += Math.abs(crab - i);
			}
			if(fuel < lowestFuel) {
				lowestFuel = fuel;
			}
		}
		print(lowestFuel);

		br();

		lowestFuel = Integer.MAX_VALUE;
		for(int i = min; i < max; i++) {
			int fuel = 0;
			for(Integer crab : crabs) {
				int diff = Math.abs(crab - i);
				for(int j = 0; j < diff; j++) {
					fuel += (j+1);
				}
			}
			if(fuel < lowestFuel) {
				lowestFuel = fuel;
			}
		}
		print(lowestFuel);
	}
}
