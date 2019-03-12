package com.akgs.spring.interstellartravel.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Path {
	String fromNode;
	String toNode;
	List<Edge> edges;
	int distance;
	public Path(String fromNode, String toNode) {
		this();		
		this.fromNode = fromNode;
		this.toNode = toNode;
	}
	public Path() { 
		edges = new ArrayList<Edge>();
	}
	public void prependEdge() {
		
	}
}
