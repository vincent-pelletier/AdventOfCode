package y2021;

import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day2 extends AdventOfCode {

	@Test
	public void day2() {
		printHeader();
		List<String> steps = readFileAsStrings();

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
}
