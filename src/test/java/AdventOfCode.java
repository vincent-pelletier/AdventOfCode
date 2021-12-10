import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
			print(filename + " not found");
			throw new IllegalArgumentException();
		}
		return strings;
	}

	protected List<Integer> readFileAsInts(String filename) {
		return readFileAsStrings(filename).stream().map(Integer::valueOf).collect(Collectors.toList());
	}

	protected List<Integer> readSingleLineAsInts(String filename) {
		return csv2list(readFileAsStrings(filename).get(0));
	}

	protected List<Integer> csv2list(String csv) {
		return Stream.of(csv.split(",")).map(Integer::valueOf).collect(Collectors.toList());
	}

	protected void print(Object o) {
		System.out.println(o);
	}

	protected void print2(Object o) {
		System.out.print(o);
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
