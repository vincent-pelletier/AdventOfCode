package y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import common.AdventOfCode;

public class Day21 extends AdventOfCode {

	private List<Integer> values = Arrays.asList(10, 1, 2, 3, 4, 5, 6, 7, 8, 9);

	@Test
	public void day21() {
		printHeader();
		int p1pos = 8;
		int p2pos = 10;
		int p1pts = 0;
		int p2pts = 0;

		Dice d = new Dice();
		while(true) {
			p1pos += d.roll3x();
			p1pts += values.get(p1pos % 10);
			if(p1pts >= 1000) break;
			p2pos += d.roll3x();
			p2pts += values.get(p2pos % 10);
			if(p2pts >= 1000) break;
		}
		print(Math.min(p1pts, p2pts) * d.rolls);

		br();

		for(int i = 1; i <= 3; i++) {
			for(int j = 1; j <= 3; j++) {
				for(int k = 1; k <= 3; k++) {
					int v = i+j+k;
					if(!rollCounts.containsKey(v)) {
						rollCounts.put(v, 0);
					}
					rollCounts.put(v, rollCounts.get(v) + 1);
				}
			}
		}

		rollValues = new ArrayList<>(rollCounts.keySet());

		computeGameResult(8, 0, 10, 0, "", true);
		print(Math.max(p1wins, p2wins));
	}

	private Map<Integer, Integer> rollCounts = new HashMap<>();
	private List<Integer> rollValues = new ArrayList<>();

	private long p1wins = 0L;
	private long p2wins = 0L;

	private void computeGameResult(int p1pos, int p1pts, int p2pos, int p2pts, String seq, boolean p1turn) {
		if(p1turn) {
			if(p2pts >= 21) {
				long wins = 1L;
				for(int i = 0; i < seq.length(); i++) {
					int v = Integer.valueOf(seq.charAt(i) + "");
					wins *= rollCounts.get(v);
				}
				p2wins += wins;
				return;
			}
			for(int rv : rollValues) {
				computeGameResult(p1pos + rv, p1pts + values.get((p1pos + rv) % 10), p2pos, p2pts, seq + rv, !p1turn);
			}
		} else {
			if(p1pts >= 21) {
				long wins = 1L;
				for(int i = 0; i < seq.length(); i++) {
					int v = Integer.valueOf(seq.charAt(i) + "");
					wins *= rollCounts.get(v);
				}
				p1wins += wins;
				return;
			}
			for(int rv : rollValues) {
				computeGameResult(p1pos, p1pts, p2pos + rv, p2pts + values.get((p2pos + rv) % 10), seq + rv, !p1turn);
			}
		}
	}

	private class Dice {
		int lastValue;
		int rolls = 0;

		public int roll3x() {
			int value = 0;
			for(int i = 0; i < 3; i++) {
				if(lastValue == 100) {
					lastValue = 0;
				}
				value += ++lastValue;
			}
			rolls += 3;
			return value;
		}
	}
}
