package com.akgs.spring.interstellartravel.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * @param edges
 * @param name
 * @param paths
 */
public class Graph {
	private List<Edge> edges;
	private String name;
	private HashMap<String, HashMap<String, Path>> paths;
	private HashMap<String, HashMap<String, Integer>> connectedNodes;
	public Graph(String name){
		this.name = name;
		edges = new ArrayList<Edge>();
		paths = new HashMap<String, HashMap<String, Path>>();
	}
	public void addEdge(Edge e) {
		if(e == null)
			return;
		if(connectedNodes.get(e.getFromNode()) == null) {
			HashMap<String, Integer> toNodes = new HashMap<String, Integer>();
			connectedNodes.put(e.getFromNode(), toNodes);
		}
		//Add min weight, when multiple edges exist
		Integer weight = connectedNodes.get(e.getFromNode()).get(e.getToNode());
		if(weight == null || (weight != null && weight > e.getWeight())) {
			connectedNodes.get(e.getFromNode()).put(e.getToNode(), e.getWeight());
			edges.add(e);
		}
	}
	public HashMap<String, Integer> getConnectedNodes(String frmNode) {
		return connectedNodes.get(frmNode);
	}
	public HashMap<String, HashMap<String, Path>> getPaths() {
		return paths;
	}
	public Optional<Path> getPath(String fromNode, String toNode) {
		if(paths.get(fromNode) == null)
			return Optional.empty();
		else if(paths.get(fromNode).get(toNode) == null)
			return Optional.empty();
		else {
			return Optional.ofNullable(paths.get(fromNode).get(toNode));
		}
	}
	public void addPath(String fromNode, String toNode, Path p) {
		if(paths.get(fromNode) == null)
			paths.put(fromNode, new HashMap<String, Path>());
		paths.get(fromNode).put(toNode, p);
	}
}
