package gui;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node extends Circle{
	 private int index = 0;
	 private double absY;
	 private String name;
	 private ArrayList<Node> incident = new ArrayList<>();
	 private ArrayList<Edge> incidentEdges = new ArrayList<>();
	
	  
	  
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		name = new String("y"+index);
		this.index = index;
	}
	
	public ArrayList<Node> getIncidents() {
		return incident;
	}
	
	public ArrayList<Edge> getIncidentEdges() {
		return incidentEdges;
	}
	
	public String getName() {
		return name;
	}
	
	public void handleEnter() {
		fillProperty().set(Color.ALICEBLUE);
	}
	public void handleOut() {
		fillProperty().set(Color.CYAN);
	}
	
	public void setAbsY(double y) {
		this.absY = y;
	}
	
	public double getAbsY() {
		return absY;
	}
}
