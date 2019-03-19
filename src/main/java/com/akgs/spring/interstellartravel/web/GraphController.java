package com.akgs.spring.interstellartravel.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akgs.spring.interstellartravel.data.Edge;
import com.akgs.spring.interstellartravel.data.Graph;
import com.akgs.spring.interstellartravel.data.Path;

@RestController
@RequestMapping(path = "/graph", produces = "application/json")
@CrossOrigin(origins = "*")
public class GraphController {

	public static void main(String[] args) {
		GraphController gc = new GraphController();
		gc.getShortestPath(1, "A", "J");
	}

	@GetMapping("/{id}/shortestPath")
	public Path getShortestPath(@PathVariable Integer graphId, @RequestParam String fromNode,
			@RequestParam String toNode) {
		// Get graph
		Graph g = new Graph("G1");
		g.addEdge(new Edge("A", "B", 5));
		g.addEdge(new Edge("A", "K", 20));
		g.addEdge(new Edge("A", "E", 5));
		g.addEdge(new Edge("B", "C", 5));
		g.addEdge(new Edge("B", "I", 15));
		g.addEdge(new Edge("C", "D", 5));
		g.addEdge(new Edge("D", "E", 5));
		g.addEdge(new Edge("E", "F", 5));
		g.addEdge(new Edge("F", "G", 5));
		g.addEdge(new Edge("G", "H", 5));
		g.addEdge(new Edge("H", "I", 5));
		g.addEdge(new Edge("I", "J", 5));
		g.addEdge(new Edge("K", "J", 1));
		g.addEdge(new Edge("A", "L", 1));
		g.addEdge(new Edge("L", "M", 5));

		// Compute the shortest path fromNode --> toNode and store in paths
		Optional<Path> sp = g.getPath(fromNode, toNode);
		if (sp.isPresent())
			return sp.get();
		else {
			ShortestPath shortestPath = new ShortestPath(g, fromNode, toNode);
			shortestPath.crawl();
			Path p = shortestPath.minDistances.get(fromNode);
			g.addPath(fromNode, toNode, p);
			return p;
		}
	}

	private class ShortestPath {
		Set<String> visitedNodes;
		// Contains minimum distances from node n to target node, where n is key and path p
		// is value in map
		HashMap<String, Path> minDistances;
		Graph g;
		String fromNode;
		String toNode;

		ShortestPath(Graph g, String fromNode, String toNode) {
			visitedNodes = new HashSet<String>();
			minDistances = new HashMap<String, Path>();
			this.g = g;
			this.fromNode = fromNode;
			this.toNode = toNode;
		}

		void crawl() {
			HashSet<String> traveredPath = new HashSet<String>();
			traveredPath.add(fromNode);
			crawl(fromNode, traveredPath);
		}

		private void crawl(String currNode, Set<String> traversedPath) {
			// Get connectedNodes of currNode
			int currMinDistance = -1;
			Path p = new Path(currNode, toNode);
			p.setDistance(-1); // unreachable path

			// For each connectedNode
			if (g.getConnectedNodes(currNode) != null) {
				for (Entry<String, Integer> connectedNode : g.getConnectedNodes(currNode).entrySet()) {
					String node = connectedNode.getKey();
					Integer weight = connectedNode.getValue();
					// check, if node already visited, then check min distance
					if (visitedNodes.contains(node)) {
						if (minDistances.get(node).getDistance() != -1 && (currMinDistance == -1
								|| currMinDistance > (minDistances.get(node).getDistance() + weight))) {
							currMinDistance = minDistances.get(node).getDistance() + weight;
							p = new Path(currNode, toNode);
							p.setDistance(currMinDistance);
							p.getEdges().addAll(minDistances.get(node).getEdges());
							p.getEdges().add(0, new Edge(currNode, node, weight));
						}
					}
					// Check if node is toNode
					else if (node.equals(toNode)) {
						if (currMinDistance == -1 || currMinDistance > weight) {
							currMinDistance = weight;
							p = new Path(currNode, toNode);
							p.setDistance(currMinDistance);
							p.getEdges().add(new Edge(currNode, toNode, weight));
						}
					} else if (!traversedPath.contains(node)) {
						// visit the node
						traversedPath.add(node);
						crawl(node, traversedPath);
						if (visitedNodes.contains(node)) {
							if (minDistances.get(node).getDistance() != -1 && (currMinDistance == -1
									|| currMinDistance > (minDistances.get(node).getDistance() + weight))) {
								currMinDistance = minDistances.get(node).getDistance() + weight;
								p = new Path(currNode, toNode);
								p.setDistance(currMinDistance);
								p.getEdges().addAll(minDistances.get(node).getEdges());
								p.getEdges().add(0, new Edge(currNode, node, weight));
							}
						}
					}
				}
			}
			// Add currNode to VisitedNodes
			visitedNodes.add(currNode);
			minDistances.put(currNode, p);
		}
	}

}
