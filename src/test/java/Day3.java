import java.util.List;

import org.junit.Test;

public class Day3 extends AdventOfCode {

	@Test
	public void day3() {
		printHeader();
		List<String> binaries = readFileAsStrings("day3.txt");

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

		String oxygen = "";
		for(int i = 0; i < binaries.get(0).length(); i++) {
			int ones = 0;
			int zeroes = 0;
			String last = "";
			for(String binary : binaries) {
				if(!binary.startsWith(oxygen)) {
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
				oxygen = last;
				break;
			}
			if(ones == zeroes) {
				oxygen += "1";
			} else if(ones > zeroes) {
				oxygen += "1";
			} else {
				oxygen += "0";
			}
		}
		String co2 = "";
		for(int i = 0; i < binaries.get(0).length(); i++) {
			int ones = 0;
			int zeroes = 0;
			String last = "";
			for(String binary : binaries) {
				if(!binary.startsWith(co2)) {
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
				co2 = last;
				break;
			}
			if(zeroes == ones) {
				co2 += "0";
			} else if(zeroes > ones) {
				co2 += "1";
			} else {
				co2 += "0";
			}
		}
		print(bin2dec(oxygen) + " x " + bin2dec(co2) + " = " + bin2dec(oxygen) * bin2dec(co2));
	}
}
