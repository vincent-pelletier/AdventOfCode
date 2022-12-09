package y2021;

import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day1 extends AdventOfCode {

	@Test
	public void day1() {
		List<Integer> values = readFileAsInts();

		int increment = 0;
		for(int i = 1; i < values.size(); i++) {
			if(values.get(i) > values.get(i-1)) {
				increment++;
			}
		}
		print(increment);

		br();

		increment = 0;
		for(int i = 1; i < values.size() - 2; i++) {
			int value = values.get(i) + values.get(i+1) + values.get(i+2);
			int prevValue = values.get(i-1) + values.get(i) + values.get(i+1);
			if(value > prevValue) {
				increment++;
			}
		}
		print(increment);
	}
}
