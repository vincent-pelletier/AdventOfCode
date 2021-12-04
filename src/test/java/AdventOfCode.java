import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@Test
	public void day2() {
		printHeader();
		List<String> steps = FileUtil.readFileAsStrings("day2.txt");

		int position = 0;
		int depth = 0;
		for(String step : steps) {
			if(step.startsWith("forward")) {
				position += Integer.valueOf(step.replace("forward ", ""));
			} else if(step.startsWith("up")) {
				depth -= Integer.valueOf(step.replace("up ", ""));
			} else if(step.startsWith("down")) {
				depth += Integer.valueOf(step.replace("down ", ""));
			}
		}
		print(position + " x " + depth + " = " + position * depth);

		br();

		position = 0;
		depth = 0;
		int aim = 0;
		for(String step : steps) {
			if(step.startsWith("forward")) {
				position += Integer.valueOf(step.replace("forward ", ""));
				depth += aim * Integer.valueOf(step.replace("forward ", ""));
			} else if(step.startsWith("up")) {
				aim -= Integer.valueOf(step.replace("up ", ""));
			} else if(step.startsWith("down")) {
				aim += Integer.valueOf(step.replace("down ", ""));
			}
		}
		print(position + " x " + depth + " = " + position * depth);
	}

	@Test
	public void day3() {
		printHeader();
		List<String> binaries = FileUtil.readFileAsStrings("day3.txt");

		String gamma = "";
		for(int i = 0; i < binaries.get(0).length(); i++) {
			int ones = 0;
			int zeroes = 0;
			for(String binary : binaries) {
				if(binary.charAt(i) == '0') {
					zeroes++;
				} else {
					ones++;
				}
			}
			if(zeroes > ones) {
				gamma += "0";
			} else {
				gamma += "1";
			}
		}
		String epsilon = gamma.replace("0", "2").replace("1", "0").replace("2", "1");
		print(bin2dec(gamma) + " x " + bin2dec(epsilon) + " = " + bin2dec(gamma) * bin2dec(epsilon));

		br();

		gamma = ""; // oxygen generator
		for(int i = 0; i < binaries.get(0).length(); i++) {
			int ones = 0;
			int zeroes = 0;
			String last = "";
			for(String binary : binaries) {
				if(!binary.startsWith(gamma)) {
					continue;
				}
				last = binary;
				if(binary.charAt(i) == '0') {
					zeroes++;
				} else {
					ones++;
				}
			}
			if(ones + zeroes == 1) {
				gamma = last;
				break;
			}
			if(ones == zeroes) {
				gamma += "1";
			} else if(ones > zeroes) {
				gamma += "1";
			} else {
				gamma += "0";
			}
		}
		epsilon = ""; // co2 scrubber
		for(int i = 0; i < binaries.get(0).length(); i++) {
			int ones = 0;
			int zeroes = 0;
			String last = "";
			for(String binary : binaries) {
				if(!binary.startsWith(epsilon)) {
					continue;
				}
				last = binary;
				if(binary.charAt(i) == '0') {
					zeroes++;
				} else {
					ones++;
				}
			}
			if(ones + zeroes == 1) {
				epsilon = last;
				break;
			}
			if(zeroes == ones) {
				epsilon += "0";
			} else if(zeroes > ones) {
				epsilon += "1";
			} else {
				epsilon += "0";
			}
		}
		print(bin2dec(gamma) + " x " + bin2dec(epsilon) + " = " + bin2dec(gamma) * bin2dec(epsilon));
	}

	@Test
	public void day4() {
		printHeader();
		List<String> input = FileUtil.readFileAsStrings("day4.txt");

		List<Integer> nums = Stream.of(input.get(0).split(",")).map(Integer::valueOf).collect(Collectors.toList());

		List<Bingo> grids = new ArrayList<>();
		for(int i = 1; i < input.size(); i += 6) {
			grids.add(new Bingo(input.subList(i+1, i+6)));
		}
		boolean bingo = false;
		for(Integer num : nums) {
			for(Bingo grid : grids) {
				if(grid.addNumber(num)) {
					print(grid.getValue());
					bingo = true;
					break;
				}
			}
			if(bingo) {
				break;
			}
		}

		br();

		List<Bingo> copy = new ArrayList<>();
		for(Bingo grid : grids) {
			grid.clearNumbers();
			copy.add(grid);
		}
		Bingo last = null;
		for(Integer num : nums) {
			for(Bingo grid : grids) {
				if(grid.addNumber(num)) {
					copy.remove(grid);
				}
				if(copy.size() == 1) {
					last = copy.get(0);
					break;
				}
			}
			if(last != null) {
				break;
			}
		}

		last.clearNumbers();
		for(Integer num : nums) {
			if(last.addNumber(num)) {
				print(last.getValue());
				return;
			}
		}
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

	private int bin2dec(String binary) {
		return Integer.parseInt(binary, 2);
	}

	private class Bingo {
		private List<Integer>[] rows = new ArrayList[5];
		private List<Integer>[] cols = new ArrayList[5];
		private List<Integer> numbers = new ArrayList<>();

		public Bingo(List<String> lines) {
			for(int i = 0; i < 5; i++) {
				rows[i] = Stream.of(lines.get(i).replace("  ", " ").trim().split(" "))
						.map(Integer::valueOf).collect(Collectors.toList());
				cols[i] = new ArrayList<>();
			}
			for(int i = 0; i < rows.length; i++) {
				for(int j = 0; j < rows[i].size(); j++) {
					cols[j].add(rows[i].get(j));
				}
			}
		}

		// returns true if bingo
		public boolean addNumber(int num) {
			numbers.add(num);
			for(List<Integer> row : rows) {
				if(numbers.containsAll(row)) {
					return true;
				}
			}
			for(List<Integer> col : cols) {
				if(numbers.containsAll(col)) {
					return true;
				}
			}
			return false;
		}

		public int getValue() {
			int unmarked = 0;
			for(List<Integer> row : rows) {
				for(Integer val : row) {
					if(!numbers.contains(val)) {
						unmarked += val;
					}
				}
			}
			return unmarked * numbers.get(numbers.size() - 1);
		}

		public void clearNumbers() {
			numbers.clear();
		}
	}
}
