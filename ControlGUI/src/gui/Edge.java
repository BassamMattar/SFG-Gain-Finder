package gui;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;

public class Edge extends SVGPath {
	private int index;
	private int startX, startY, endX, endY;
	Label gainLbel;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Label getGainLabel() {
		return gainLbel;
	}

	public void setGainLabel(Label gain) {
		this.gainLbel = gain;
	}

	public float getGainValue() {
		return Float.parseFloat(gainLbel.getText());
	}

	public void setCordinates(int x1, int y1, int x2, int y2) {
		startX = x1;
		startY = y1;
		endX = x2;
		endY = y2;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

}
