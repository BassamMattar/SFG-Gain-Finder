package controller;

import java.awt.Point;
import java.util.ArrayList;

import gui.Edge;
import gui.Node;
import interfaces.SFGI;
import sfg.SFG;

public class SFGAdapter {
	int nNodes;
	Point[] connections;
	int[] gains;
	SFGI sfg;
	
	public SFGAdapter(int nNodes, ArrayList<Node> connections) {
		sfg = new SFG(nNodes);
		System.out.println(nNodes);
		for(Node parentNode : connections) {
			for(int i = 0; i < parentNode.getIncidents().size();i++) {
				System.out.println(parentNode.getIndex() + " " + parentNode.getIncidents().get(i).getIndex() + " " + parentNode.getIncidentEdges().get(i).getGainValue());
				sfg.connectXToY(parentNode.getIndex(), parentNode.getIncidents().get(i).getIndex(),
						parentNode.getIncidentEdges().get(i).getGainValue());
			}
		}
	}	
	
	public ArrayList<ArrayList<Integer>> getAllForwarPaths() {
		return sfg.getAllForwardPaths();
	}
	
	public ArrayList<ArrayList<Integer>> getAllLoops() {
		return sfg.getAllLoops();
	}
	
	public String getResult() {
		return sfg.getResult();
	}
}
