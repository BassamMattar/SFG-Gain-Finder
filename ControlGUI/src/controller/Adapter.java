package controller;

import java.awt.Point;
import java.util.ArrayList;

import gui.Edge;
import gui.Node;
import interfaces.SFGI;
import sfg.SFG;

public class Adapter {
	int nNodes;
	Point[] connections;
	int[] gains;
	SFGI sfg;
	
	public Adapter(int nNodes, ArrayList<Node> connections) {
		sfg = new SFG(nNodes);
		for(Node parentNode : connections) {
//			for(int i = 0; i < pa) {
//				
//			}
		}
		
		this.nNodes = nNodes;		
	}	
}
