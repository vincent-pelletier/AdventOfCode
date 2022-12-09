package y2022;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day01 extends AdventOfCode {

	@Test
	public void test() {
		List<String> calories = readFileAsStrings();

		int sum = 0;
		int max = 0;
		for(String c : calories) {
			if(c.isEmpty()) {
				sum = 0;
			} else {
				sum += i(c);
				if(sum > max) {
					max = sum;
				}
			}
		}
		print(max);

		br();

		List<Integer> sums = new ArrayList<>();
		sum = 0;
		for(String c : calories) {
			if(c.isEmpty()) {
				sums.add(sum);
				sum = 0;
			} else {
				sum += i(c);
			}
		}
		sums.add(sum);

		sums.sort(Integer::compareTo);
		print(sums.subList(sums.size() - 3, sums.size()).stream().mapToInt(Integer::intValue).sum());
	}
}
