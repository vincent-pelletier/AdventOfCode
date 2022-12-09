package y2021;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import common.AdventOfCode;

public class Day6 extends AdventOfCode {

	@Test
	public void day6() {
		List<Integer> initialFishes = readSingleLineAsInts();

		List<AtomicInteger> fishes = new ArrayList<>();
		for(Integer fish : initialFishes) {
			fishes.add(new AtomicInteger(fish));
		}

		for(int i = 0; i < 80; i++) {
			List<AtomicInteger> newFishes = new ArrayList<>();
			for(AtomicInteger fish : fishes) {
				if(fish.get() > 0) {
					fish.decrementAndGet();
				} else {
					fish.set(6);
					newFishes.add(new AtomicInteger(8));
				}
			}
			fishes.addAll(newFishes);
		}
		print(fishes.size());

		br();

		// loop to 256 days = OutOfMemoryError: Java heap space... change tactic
		List<AtomicLong> fishPerAge = new ArrayList<>();
		for(int i = 0; i < 9; i++) {
			fishPerAge.add(new AtomicLong(0L));
		}
		for(Integer fish : initialFishes) {
			fishPerAge.get(fish).incrementAndGet();
		}

		for(int i = 0; i < 256; i++) {
			AtomicLong fishes0 = fishPerAge.remove(0);
			fishPerAge.get(6).set(fishPerAge.get(6).get() + fishes0.get());
			fishPerAge.add(new AtomicLong(fishes0.get()));
		}
		print(fishPerAge.stream().mapToLong(AtomicLong::get).sum());
	}
}
