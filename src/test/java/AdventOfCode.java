import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AdventOfCode {

	private final static String prefix = "src/test/resources/";

	protected List<String> readFileAsStrings(String filename) {
		List<String> strings = new ArrayList<>();
		try {
			Scanner reader = new Scanner(new File(prefix + filename));
			while (reader.hasNextLine()) {
				strings.add(reader.nextLine());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println(filename + " not found");
			throw new IllegalArgumentException();
		}
		return strings;
	}

	protected List<Integer> readFileAsInts(String filename) {
		return readFileAsStrings(filename).stream().map(Integer::valueOf).collect(Collectors.toList());
	}

	protected void print(Object o) {
		System.out.println(o);
	}

	protected void printHeader() {
		String day = Thread.currentThread().getStackTrace()[2].getMethodName();
		print("[" + String.valueOf(day.charAt(0)).toUpperCase() +
				day.substring(1, 3) + " " + day.charAt(day.length() - 1) + "]");
	}

	protected void br() {
		print("--------");
	}

	protected int bin2dec(String binary) {
		return Integer.parseInt(binary, 2);
	}
}
