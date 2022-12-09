package y2021;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day8 extends AdventOfCode {

	@Test
	public void day8() {
		printHeader();
		List<String> input = readFileAsStrings();

		List<Integer> lengths = Arrays.asList(2, 3, 4, 7);
		int count = 0;
		for(String row : input) {
			String[] parts = row.split("\\|")[1].trim().split(" ");
			for(String part : parts) {
				if(lengths.contains(part.length())) {
					count++;
				}
			}
		}
		print(count);

		br();

		int sum = 0;
		for(String row : input) {
			String[] signalPatterns = row.split("\\|")[0].trim().split(" ");
			String[] parts = row.split("\\|")[1].trim().split(" ");
			int value = 0;
			int factor = 1000;
			SevenSegmentDecoder decoder = new SevenSegmentDecoder(signalPatterns);
			// decoder.display();
			for(String part : parts) {
				value += decoder.getValue(part) * factor;
				factor /= 10;
			}
			sum += value;
		}
		print(sum);
	}

	/**
	 *  aaaa
	 * b    c
	 * b    c
	 *  dddd
	 * e    f
	 * e    f
	 *  gggg
	 */
	private class SevenSegmentDecoder {
		public String a,b,c,d,f; // letter value for above position
		private String e,g;

		public SevenSegmentDecoder(String[] signalPatterns) {
			for(String signal : signalPatterns) {
				if(signal.length() == 2) { // 1
					c = "" + signal.charAt(0);
					f = "" + signal.charAt(1);
				}
			}
			for(String signal : signalPatterns) {
				if(signal.length() == 3) { // 7
					a = signal.replace(c, "").replace(f, "");
				}
				if(signal.length() == 4) { // 4
					b = "" + signal.replace(c, "").replace(f, "").charAt(0); // assign randomly for now
					d = "" + signal.replace(c, "").replace(f, "").charAt(1);
				}
			}
			// update b and d based on the "3" value.
			for(String signal : signalPatterns) {
				if(getValue(signal) == 3) {
					if(!signal.contains(d)) {
						String tmp = b;
						b = d;
						d = tmp;
					}
					break;
				}
			}
			e = "?";
			g = "?";
		}

		private int getValue(String abcdefg) {
			if(abcdefg.length() == 2) return 1;
			if(abcdefg.length() == 3) return 7;
			if(abcdefg.length() == 4) return 4;
			if(abcdefg.length() == 7) return 8;
			if(abcdefg.length() == 5) { // 2, 3 or 5
				if(abcdefg.contains(c) && abcdefg.contains(f)) return 3; // from 1,7
				if(abcdefg.contains(b) && abcdefg.contains(d)) return 5; // from 4
				return 2;
			}
			if(abcdefg.length() == 6) { // 0, 6 or 9
				if(!(abcdefg.contains(c) && abcdefg.contains(f))) return 6; // 0 and 9 have c,f
				if(abcdefg.contains(d)) return 9; // from 4
				return 0;
			}
			throw new IllegalArgumentException(abcdefg);
		}

		public void display() {
			print(" " + a + a + a + a);
			print(b + "    " + c);
			print(b + "    " + c);
			print(" " + d + d + d + d);
			print(e + "    " + f);
			print(e + "    " + f);
			print(" " + g + g + g + g);
		}
	}
}
