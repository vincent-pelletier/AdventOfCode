package y2022;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day10 extends AdventOfCode {

	@Test
	public void test() {
		List<String> programs = readFileAsStrings();

		int x = 1;
		int cycle = 0;
		int strength = 0;
		for(String program : programs) {
			if(program.equals("noop")) {
				cycle++;
				if((cycle - 20) % 40 == 0) {
					strength += cycle * x;
				}
			} else {
				int val = i(program.split(" ")[1]);
				for(int i = 0; i < 2; i++) {
					cycle++;
					if((cycle - 20) % 40 == 0) {
						strength += cycle * x;
					}
				}
				x += val;
			}
		}
		print(strength);

		br();

		String crt = "";
		x = 1;
		cycle = 0;
		for(String program : programs) {
			if(program.equals("noop")) {
				cycle++;
				if((cycle - 1) % 40 == 0 && cycle > 1) {
					crt += "\r\n";
				}
				if(Arrays.asList(x-1, x, x+1).contains((cycle - 1) % 40)) {
					crt += "█";
				} else {
					crt += " ";
				}
			} else {
				int val = i(program.split(" ")[1]);
				for(int i = 0; i < 2; i++) {
					cycle++;
					if((cycle - 1) % 40 == 0 && cycle > 1) {
						crt += "\r\n";
					}
					if(Arrays.asList(x-1, x, x+1).contains((cycle - 1) % 40)) {
						crt += "█";
					} else {
						crt += " ";
					}
				}
				x += val;
			}
		}
		print(crt);
	}
}
