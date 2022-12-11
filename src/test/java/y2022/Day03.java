package y2022;

import java.util.List;

import org.junit.Test;

import common.AdventOfCode;

public class Day03 extends AdventOfCode {

	@Test
	public void test() {
		List<String> rucksacks = readFileAsStrings();

		String priority = "+abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		int sum = 0;
		for(String rucksack : rucksacks) {
			String comp1 = rucksack.substring(0, rucksack.length() / 2);
			String comp2 = rucksack.substring(comp1.length());

			for(int i = 0; i < comp1.length(); i++) {
				if(comp2.indexOf(comp1.charAt(i)) != -1) {
					sum += priority.indexOf(comp1.charAt(i));
					break;
				}
			}
		}
		print(sum);

		br();

		sum = 0;
		for(int i = 0; i < rucksacks.size(); i += 3) {
			String s1 = rucksacks.get(i);
			String s2 = rucksacks.get(i + 1);
			String s3 = rucksacks.get(i + 2);

			for(int j = 0; j < s1.length(); j++) {
				if(s2.indexOf(s1.charAt(j)) != -1 && s3.indexOf(s1.charAt(j)) != -1) {
					sum += priority.indexOf(s1.charAt(j));
					break;
				}
			}
		}
		print(sum);
	}
}
