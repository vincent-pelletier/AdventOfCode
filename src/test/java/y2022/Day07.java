package y2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import common.AdventOfCode;

public class Day07 extends AdventOfCode {

	Map<String, Directory> allDirectories = new HashMap<>();

	@Test
	public void test() {
		List<String> cmds = readFileAsStrings();

		List<Directory> orderedDirectories = new ArrayList<>();
		for(int i = 0; i < cmds.size(); i++) {
			String cmd = cmds.get(i);
			if(cmd.startsWith("$ cd ")) {
				if(cmd.equals("$ cd ..")) {
					// go back in dir list
					orderedDirectories.remove(orderedDirectories.size() - 1);
				} else {
					if(cmds.get(i + 1).startsWith("$ cd ")) {
						continue;
					} else if(cmds.get(i + 1).startsWith("$ ls")) {
						i++;
						// get content
						String dir = cmd.replace("$ cd ", "");
						// update to full dir name
						if(!dir.equals("/")) {
							dir = orderedDirectories.get(orderedDirectories.size() - 1).name + "/" + dir;
						}
						dir = dir.replace("//", "/");
						if(!allDirectories.containsKey(dir)) {
							allDirectories.put(dir, new Directory(dir, orderedDirectories.size()));
						}
						Directory d = allDirectories.get(dir);
						while(i + 1 < cmds.size() && !cmds.get(i + 1).startsWith("$")) {
							String innerCmd = cmds.get(i + 1);
							if(innerCmd.startsWith("dir ")) {
								String innerDir = innerCmd.replace("dir ", "");
								// update to full dir name
								innerDir = dir + "/" + innerDir;
								innerDir = innerDir.replace("//", "/");
								d.directories.add(innerDir);
							} else {
								d.files.add(new File(innerCmd.split(" ")[1], Long.valueOf(innerCmd.split(" ")[0])));
							}
							i++;
						}
						orderedDirectories.add(d);
					}
				}
			}
		}

		// print(allDirectories.get("/"));

		long total = 0L;
		for(Directory d : allDirectories.values()) {
			long size = d.size();
			if(size <= 100000L) {
				total += size;
			}
		}
		print(total);

		br();

		long mustFreeUp = 30000000 - (70000000L - allDirectories.get("/").size());
		long minOverMustFreeUp = Long.MAX_VALUE;
		for(Directory d : allDirectories.values()) {
			if(d.size() >= mustFreeUp && d.size() < minOverMustFreeUp) {
				minOverMustFreeUp = d.size();
			}
		}
		print(minOverMustFreeUp);
	}

	class Directory {
		final String name;
		final int level;
		final Set<String> directories;
		final Set<File> files;

		long computedSize = -1L;
		String computedString = null;

		Directory(String name, int level) {
			this.name = name;
			this.level = level;
			directories = new HashSet<>();
			files = new HashSet<>();
		}

		long size() {
			if(computedSize == -1L) {
				computedSize = 0L;
				for(String d : directories) {
					computedSize += allDirectories.get(d).size();
				}
				for(File f : files) {
					computedSize += f.size;
				}
			}
			return computedSize;
		}

		@Override
		public String toString() {
			if(computedString == null) {
				computedString = name + " (dir, size=" + size() + ")";
				String space = "\r";
				for(int i = 0; i < level + 1; i++) {
					space += "  ";
				}
				for(String d : directories) {
					computedString += space + allDirectories.get(d);
				}
				for(File f : files) {
					computedString += space + f;
				}
			}
			return computedString;
		}
	}

	class File {
		final String name;
		final long size;

		File(String name, long size) {
			this.name = name;
			this.size = size;
		}

		@Override
		public String toString() {
			return name + " (file, size=" + size + ")";
		}
	}
}
