import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileUtil {

	private final static String prefix = "src/test/resources/";

	public static List<String> readFileAsStrings(String filename) {
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

	public static List<Integer> readFileAsInts(String filename) {
		return readFileAsStrings(filename).stream().map(Integer::valueOf).collect(Collectors.toList());
	}
}
