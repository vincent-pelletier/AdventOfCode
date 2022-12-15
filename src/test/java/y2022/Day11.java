package y2022;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day11 extends AdventOfCode {

	private List<Monkey> monkeys = new ArrayList<>();
	private long ppcm = 1;

	@Test
	public void test() {
		initMonkeys();

		for(int round = 0; round < 20; round++) {
			for(Monkey m : monkeys) {
				m.inspectItems(false);
			}
		}
		print(monkeys.stream().map(m -> -m.inspected).sorted().limit(2).reduce(1L, (a,b) -> a * b));

		br();

		initMonkeys();
		for(Monkey m : monkeys) {
			ppcm *= m.testDivisible;
		}

		for(int round = 0; round < 10000; round++) {
			for(Monkey m : monkeys) {
				m.inspectItems(true);
			}
		}
		print(monkeys.stream().map(m -> -m.inspected).sorted().limit(2).reduce(1L, (a,b) -> a * b));
	}

	private void initMonkeys() {
		List<String> input = readFileAsStrings();
		monkeys.clear();
		for(int i = 0; i < input.size(); i += 7) {
			monkeys.add(new Monkey(input.subList(i, i + 6)));
		}
	}

	class Monkey {
		final int id;
		final List<Long> items;
		int addOperation = 0;
		int multiplyOperation = 1;
		boolean expOperation = false;
		final int testDivisible;
		final int monkeyIfTrue;
		final int monkeyIfFalse;

		long inspected = 0;

		Monkey(List<String> input) {
			id = i(input.get(0).replace("Monkey ", "").replace(":", ""));
			items = csv2longs(input.get(1).replace("Starting items:", "").replace(" ", ""));
			String op = input.get(2);
			if(op.contains("+")) {
				addOperation = i(op.split(" ")[op.split(" ").length - 1]);
			} else if(op.contains("old * old")) {
				expOperation = true;
			} else {
				multiplyOperation = i(op.split(" ")[op.split(" ").length - 1]);
			}
			testDivisible = i(input.get(3).split(" ")[input.get(3).split(" ").length - 1]);
			monkeyIfTrue = i(input.get(4).split(" ")[input.get(4).split(" ").length - 1]);
			monkeyIfFalse = i(input.get(5).split(" ")[input.get(5).split(" ").length - 1]);
		}

		void inspectItems(boolean part2) {
			for(Long item : items) {
				item += addOperation;
				item *= multiplyOperation;
				item = expOperation ? item * item : item;
				if(part2) {
					item = item % ppcm;
				} else {
					item = Math.floorDiv(item, 3);
				}
				monkeys.get(item % testDivisible == 0 ? monkeyIfTrue : monkeyIfFalse).items.add(item);
				inspected++;
			}
			items.clear();
		}
	}
}
