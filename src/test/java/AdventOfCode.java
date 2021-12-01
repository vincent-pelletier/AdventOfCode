import java.util.List;

import org.junit.Test;

public class AdventOfCode {

	@Test
	public void day1() {
		printHeader();
		List<Integer> values = FileUtil.readFileAsInts("day1.txt");

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

	private void print(Object o) {
		System.out.println(o);
	}

	private void printHeader() {
		String day = Thread.currentThread().getStackTrace()[2].getMethodName();
		print("[" + String.valueOf(day.charAt(0)).toUpperCase() +
				day.substring(1, 3) + " " + day.charAt(day.length() - 1) + "]");
	}

	private void br() {
		print("--------");
	}
}
