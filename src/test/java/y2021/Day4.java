package y2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import common.AdventOfCode;

public class Day4 extends AdventOfCode {

	@Test
	public void day4() {
		printHeader();
		List<String> input = readFileAsStrings();

		List<Integer> nums = csv2list(input.get(0));

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

	private class Bingo {
		@SuppressWarnings("unchecked")
		private List<Integer>[] rows = new ArrayList[5];
		@SuppressWarnings("unchecked")
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
