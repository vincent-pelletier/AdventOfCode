package y2022;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import common.AdventOfCode;

public class Day12 extends AdventOfCode {

	/**
	 * Mostly copied from 2021-12-15 :P
	 */
	@Test
	public void test() {
		List<String> input = readFileAsStrings();
		String elevation = "abcdefghijklmnopqrstuvwxyz";
		int maxX = input.size();
		int maxY = input.get(0).length();
		Map<Point, Node> nodes = new HashMap<>();

		Node start = null;
		Node end = null;
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				String c = input.get(i).charAt(j) + "";
				int value = elevation.indexOf(c.replace("S", "a").replace("E", "z"));
				Node n = new Node(i + "," + j, value);
				nodes.put(new Point(i, j), n);
				if("S".equals(c)) {
					start = n;
				} else if("E".equals(c)) {
					end = n;
				}
			}
		}
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				if(i > 0) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i-1,j)), 1);
				}
				if(j > 0) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i,j-1)), 1);
				}
				if(i < maxX - 1) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i+1,j)), 1);
				}
				if(j < maxY - 1) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i,j+1)), 1);
				}
			}
		}

		calculateShortestPathFromSource(start);

		print(end.distance);

		br();

		int min = end.distance;
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				String c = input.get(i).charAt(j) + "";
				if("a".equals(c)) {
					calculateShortestPathFromSource(nodes.get(new Point(i, j)));
					if(end.distance < min) {
						min = end.distance;
					}
				}
			}
		}
		print(min);
	}

	public class Node {
		private String name;
		private List<Node> shortestPath = new LinkedList<>();
		private Integer distance = Integer.MAX_VALUE;
		Map<Node, Integer> adjacentNodes = new HashMap<>();
		int value;

		public Node(String name, int value) {
			this.name = name;
			this.value = value;
		}

		public void addDestination(Node destination, int distance) {
			if(destination.value <= value + 1) {
				adjacentNodes.put(destination, distance);
			}
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public void calculateShortestPathFromSource(Node source) {
		source.distance = 0;

		Set<Node> settledNodes = new HashSet<>();
		Set<Node> unsettledNodes = new HashSet<>();

		unsettledNodes.add(source);

		while(unsettledNodes.size() != 0) {
			Node currentNode = getLowestDistanceNode(unsettledNodes);
			unsettledNodes.remove(currentNode);
			for(Entry<Node, Integer> adjacencyPair : currentNode.adjacentNodes.entrySet()) {
				Node adjacentNode = adjacencyPair.getKey();
				Integer adjacentDistance = adjacencyPair.getValue();
				if(!settledNodes.contains(adjacentNode)) {
					calculateMinimumDistance(adjacentNode, adjacentDistance, currentNode);
					unsettledNodes.add(adjacentNode);
				}
			}
			settledNodes.add(currentNode);
		}
	}

	private Node getLowestDistanceNode(Set<Node> unsettledNodes) {
		Node lowestDistanceNode = null;
		int lowestDistance = Integer.MAX_VALUE;
		for(Node node : unsettledNodes) {
			int nodeDistance = node.distance;
			if(nodeDistance < lowestDistance) {
				lowestDistance = nodeDistance;
				lowestDistanceNode = node;
			}
		}
		return lowestDistanceNode;
	}

	private void calculateMinimumDistance(Node evaluationNode, Integer adjacentDistance, Node sourceNode) {
		Integer sourceDistance = sourceNode.distance;
		if(sourceDistance + adjacentDistance < evaluationNode.distance) {
			evaluationNode.distance = sourceDistance + adjacentDistance;
			LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.shortestPath);
			shortestPath.add(sourceNode);
			evaluationNode.shortestPath = shortestPath;
		}
	}
}