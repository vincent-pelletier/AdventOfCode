package y2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import common.AdventOfCode;

public class Day12 extends AdventOfCode {

	private int paths = 0;

	@Test
	public void day12() {
		List<String> input = readFileAsStrings();

		Map<String, Node> nodeNames = new HashMap<>();
		for(String line : input) {
			List<Node> twoNodes = Stream.of(line.split("-"))
					.map(n -> nodeNames.containsKey(n) ? nodeNames.get(n) : new Node(n))
					.collect(Collectors.toList());
			for(int i = 0; i < 2; i++) {
				twoNodes.get(i).addChild(twoNodes.get(1-i));
				nodeNames.put(twoNodes.get(i).name, twoNodes.get(i));
			}
		}

		for(Node node : nodeNames.values()) {
			print(node + " -> " + node.children);
		}
		Node start = nodeNames.get("start");
		for(Node n : start.children) {
			drill("start,", n);
		}
		print(paths);
	}

	private void drill(String path, Node n) {
		if("start".equals(n.name)) {
			return;
		} else if("end".equals(n.name)) {
			paths++;
			return;
		} else if(wasVisited(path, n.name) && isSmall(n.name) && pathContainsSmallCaveTwice(path)) {
			return;
		}
		for(Node c : n.children) {
			drill(path + n + ",", c);
		}
	}

	private boolean isSmall(String cave) {
		return cave.equals(cave.toLowerCase());
	}

	private boolean wasVisited(String path, String cave) {
		return path.contains(cave);
	}

	// part 2
	private boolean pathContainsSmallCaveTwice(String path) {
		String[] caves = path.split(",");
		Map<String, Integer> counts = new HashMap<>();
		for(String cave : caves) {
			if(isSmall(cave)) {
				if(!counts.containsKey(cave)) {
					counts.put(cave, 0);
				}
				counts.put(cave, counts.get(cave) + 1);
			}
		}
		return counts.size() > 0 && Collections.max(counts.values()) == 2;
	}

	private class Node {
		public String name;
		public List<Node> children = new ArrayList<>();

		public Node(String name) {
			this.name = name;
		}

		public void addChild(Node n) {
			children.add(n);
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
