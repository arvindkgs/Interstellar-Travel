package com.akgs.spring.interstellartravel.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge {
	private String fromNode;
	private String toNode;
	private int weight;
}
