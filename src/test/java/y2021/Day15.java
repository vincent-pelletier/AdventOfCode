package y2021;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import common.AdventOfCode;

public class Day15 extends AdventOfCode {

	@Test
	public void day15() {
		List<String> input = readFileAsStrings();
		int maxX = input.size();
		int maxY = input.get(0).length();
		int[][] grid = new int[maxX][maxY];
		Map<Point, Node> nodes = new HashMap<>();
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				grid[i][j] = Integer.valueOf(input.get(i).charAt(j) + "");
				nodes.put(new Point(i, j), new Node(i + "," + j));
			}
		}
		for(int i = 0; i < maxX; i++) {
			for(int j = 0; j < maxY; j++) {
				if(i > 0) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i-1,j)), grid[i-1][j]);
				}
				if(j > 0) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i,j-1)), grid[i][j-1]);
				}
				if(i < maxX - 1) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i+1,j)), grid[i+1][j]);
				}
				if(j < maxY - 1) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i,j+1)), grid[i][j+1]);
				}
			}
		}

		calculateShortestPathFromSource(nodes.get(new Point(0, 0)));

		List<Point> shortestPath = nodes.get(new Point(maxX-1, maxY-1)).shortestPath.stream()
				.map(n -> new Point(Integer.valueOf(n.name.split(",")[0]), Integer.valueOf(n.name.split(",")[1])))
				.collect(Collectors.toList());
		shortestPath.add(new Point(maxX - 1, maxY - 1));
		printGrid(grid, shortestPath);
		print(nodes.get(new Point(maxX-1, maxY-1)).distance);

		br();

		int factor = 5;
		int[][] bigGrid = new int[maxX * factor][maxY * factor];

		nodes.clear();
		for(int i = 0; i < maxX * factor; i++) {
			for(int j = 0; j < maxY * factor; j++) {
				bigGrid[i][j] = grid[i % maxX][j % maxY];
				bigGrid[i][j] += (i - (i % maxX)) / maxX;
				bigGrid[i][j] += (j - (j % maxY)) / maxY;
				bigGrid[i][j] = bigGrid[i][j] > 9 ? bigGrid[i][j] % 9 : bigGrid[i][j];
				nodes.put(new Point(i, j), new Node(i + "," + j));
			}
		}
		//printGrid(bigGrid);

		for(int i = 0; i < maxX * factor; i++) {
			for(int j = 0; j < maxY * factor; j++) {
				if(i > 0) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i-1,j)), bigGrid[i-1][j]);
				}
				if(j > 0) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i,j-1)), bigGrid[i][j-1]);
				}
				if(i < (maxX * factor) - 1) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i+1,j)), bigGrid[i+1][j]);
				}
				if(j < (maxY * factor) - 1) {
					nodes.get(new Point(i, j)).addDestination(nodes.get(new Point(i,j+1)), bigGrid[i][j+1]);
				}
			}
		}

		calculateShortestPathFromSource(nodes.get(new Point(0, 0)));

		shortestPath = nodes.get(new Point((maxX * factor) - 1, (maxY * factor) - 1)).shortestPath.stream()
			.map(n -> new Point(Integer.valueOf(n.name.split(",")[0]), Integer.valueOf(n.name.split(",")[1])))
			.collect(Collectors.toList());
		shortestPath.add(new Point((maxX * factor - 1), (maxY * factor - 1)));
		printGridToFile(bigGrid, shortestPath, "2021/day15output.txt");
		print(nodes.get(new Point((maxX * factor) - 1, (maxY * factor) - 1)).distance);
	}

	public class Node {
		private String name;
		private List<Node> shortestPath = new LinkedList<>();
		private Integer distance = Integer.MAX_VALUE;
		Map<Node, Integer> adjacentNodes = new HashMap<>();

		public Node(String name) {
			this.name = name;
		}

		public void addDestination(Node destination, int distance) {
			adjacentNodes.put(destination, distance);
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
