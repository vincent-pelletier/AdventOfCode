package y2022;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day05 extends AdventOfCode {

	@Test
	public void test() {
		List<String> input = readFileAsStrings();

		List<String> stacks = new ArrayList<>();

		List<String> instructions = new ArrayList<>();

		boolean instr = false;
		for(String line : input) {
			if(line.isEmpty()) {
				instr = true;
				continue;
			}
			if(instr) {
				instructions.add(line);
			} else {
				if(line.startsWith(" 1")) continue;
				if(stacks.isEmpty()) {
					for(int i = 0; i < Math.ceil((double)line.length() / 4); i++) {
						stacks.add("");
					}
				}

				int idx = 0;
				for(int i = 1; i < line.length(); i += 4) {
					if(line.charAt(i) != ' ') {
						stacks.set(idx, line.charAt(i) + stacks.get(idx));
					}
					idx++;
				}
			}
		}

		List<String> stacks2 = new ArrayList<>(stacks);

		for(String inst : instructions) {
			int iterations = i(inst.split(" ")[1]);
			int from = i(inst.split(" ")[3]) - 1;
			int to = i(inst.split(" ")[5]) - 1;
			for(int i = 0; i < iterations; i++) {
				String fromStack = stacks.get(from);
				String toStack = stacks.get(to);
				String last = fromStack.substring(fromStack.length() - 1);
				fromStack = fromStack.substring(0, fromStack.length() - 1);
				toStack += last;
				stacks.set(from, fromStack);
				stacks.set(to, toStack);
			}

			String fromStack = stacks2.get(from);
			String toStack = stacks2.get(to);
			String last = fromStack.substring(fromStack.length() - iterations);
			fromStack = fromStack.substring(0, fromStack.length() - iterations);
			toStack += last;
			stacks2.set(from, fromStack);
			stacks2.set(to, toStack);
		}

		for(String stack : stacks) {
			print2(stack.substring(stack.length() - 1));
		}
		print("");

		br();

		for(String stack : stacks2) {
			print2(stack.substring(stack.length() - 1));
		}
		print("");
	}
}
