import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.Test;

public class Day6 extends AdventOfCode {

	@Test
	public void day6() {
		printHeader();
		List<Integer> initialFishes = readSingleLineAsInts("day6.txt");

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

		// loop to 256 days = OutOfMemoryError: Java heap space...
		// so find result and growth after half, then compute
		int[] growthAfter128 = new int[9];
		long[][] countAfter128 = new long[9][9];
		for(int i = 0; i < 9; i++) {
			fishes.clear();
			fishes.add(new AtomicInteger(i)); // add only 1 fish of "i" days
			for(int j = 0; j < 128; j++) {
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
			// print("In 128 days, " + i + " becomes " + fishes.size());
			growthAfter128[i] = fishes.size();
			for(AtomicInteger fish : fishes) {
				countAfter128[i][fish.get()]++;
			}
		}

		int[] counts = new int[9];
		counts[1] = initialFishes.stream().filter(f -> f == 1).collect(Collectors.toList()).size();
		counts[2] = initialFishes.stream().filter(f -> f == 2).collect(Collectors.toList()).size();
		counts[3] = initialFishes.stream().filter(f -> f == 3).collect(Collectors.toList()).size();
		counts[4] = initialFishes.stream().filter(f -> f == 4).collect(Collectors.toList()).size();
		counts[5] = initialFishes.stream().filter(f -> f == 5).collect(Collectors.toList()).size();

		long sum = 0L;
		for(int i = 0; i < 9; i++) {
			long sumPerFish = 0L;
			for(int j = 0; j < 9; j++) { // compute amount of each new fish after 128 days
				sumPerFish += countAfter128[i][j] * growthAfter128[j];
			}
			sum += sumPerFish * counts[i]; // x initial amount
		}
		print(sum);
	}
}
