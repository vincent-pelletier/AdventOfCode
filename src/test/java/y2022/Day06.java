package y2022;

import org.junit.Test;

import common.AdventOfCode;

public class Day06 extends AdventOfCode {

	@Test
	public void test() {
		String subroutine = readFileAsStrings().get(0);

		for(int i = 3; i < subroutine.length(); i++) {
			if(subroutine.charAt(i) != subroutine.charAt(i - 1)
					&& subroutine.charAt(i) != subroutine.charAt(i - 2)
					&& subroutine.charAt(i) != subroutine.charAt(i - 3)
					&& subroutine.charAt(i - 1) != subroutine.charAt(i - 2)
					&& subroutine.charAt(i - 1) != subroutine.charAt(i - 3)
					&& subroutine.charAt(i - 2) != subroutine.charAt(i - 3)) {
				print(i + 1);
				break;
			}
		}

		br();

		for(int i = 14; i < subroutine.length(); i++) {
			if(getCharCounts(subroutine.substring(i - 14, i)).size() == 14) {
				print(i);
				break;
			}
		}
	}
}
