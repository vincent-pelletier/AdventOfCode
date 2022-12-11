package y2022;

import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day04 extends AdventOfCode {

	@Test
	public void test() {
		List<String> pairs = readFileAsStrings();

		int part1count = 0;
		int part2count = 0;
		for(String pair : pairs) {
			String elf1 = pair.split(",")[0];
			String elf2 = pair.split(",")[1];

			int e1s1 = i(elf1.split("-")[0]);
			int e1s2 = i(elf1.split("-")[1]);
			int e2s1 = i(elf2.split("-")[0]);
			int e2s2 = i(elf2.split("-")[1]);

			if((e1s1 >= e2s1 && e1s2 <= e2s2) || (e1s1 <= e2s1 && e1s2 >= e2s2)) {
				part1count++;
			}

			if((e1s1 <= e2s1 && e2s1 <= e1s2) || (e1s1 <= e2s2 && e2s2 <= e1s2) || (e2s1 <= e1s1 && e1s1 <= e2s2) || (e2s1 <= e1s2 && e1s2 <= e2s2)) {
				part2count++;
			}
		}
		print(part1count);
		br();
		print(part2count);
	}
}
