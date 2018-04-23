package controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gui.Edge;
import gui.InputBox;
import gui.Node;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;


public class Controller implements Initializable {
	enum mode {
		nodes, forwardPathes, fpPathes, freeNodes, selfLoop, none
	}

	float nodeRadius = 10;
	Node[] forwardPathNodeVisited = new Node[2];
	Node[] fpNodes = new Node[2];
	public AnchorPane pane;
	public ToggleGroup drawMode;
	public Button calculateResult;
	public TextArea screen;
	public MenuItem newProblem;
	private ArrayList<Node> allNodes = new ArrayList<>();
	private ArrayList<Edge> forwardPathes = new ArrayList<>();
	private ArrayList<Edge> fpPathes = new ArrayList<>();
	private int forwardStep = 0;
	private int fpStep = 0;
	private int numberOfNode = 0;
	private int numberOfEdges = 0;
	private int numberOfFPEdges = 0;
	private boolean hieghtClick = false;
	private Edge effect;
	private Polygon arrowEffect;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		calculateResult.setOnAction(e -> {
			SFGAdapter sfg = new SFGAdapter(numberOfNode, allNodes);
			screen.setText(sfg.getResult());
		});
		newProblem.setOnAction(e -> {
			pane.getChildren().clear();
			allNodes.clear();
			forwardPathes.clear();
			fpPathes.clear();
			numberOfNode = 0;
			numberOfEdges = 0;
			numberOfFPEdges = 0;
			screen.clear();
		});
	}

	public void paneClicked(MouseEvent e) {
		ObservableList<Toggle> modes = drawMode.getToggles();
		switch (getSuitableAction(modes)) {
		case nodes:
			addNode(e);
			break;
		case fpPathes:
			if(fpStep == 2) {
				if(hieghtClick) {
					handlefpPath(e);
				} else {
					hieghtClick = true;
				}
			}
			break;
		case freeNodes:
			
			addNode(e);
			break;
		default:
			break;
		}

	}
	public void hanleMouseMoving (MouseEvent e){
		pane.getChildren().remove(effect);
		pane.getChildren().remove(arrowEffect);
		mode currentMode = getSuitableAction(drawMode);
		if(currentMode == mode.nodes && !allNodes.isEmpty()) {
			resetCounters();
			try {
				Robot mouseController = new Robot();
				mouseController.mouseMove((int) e.getScreenX(), (int)allNodes.get(0).getAbsY());
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		} else if(currentMode == mode.forwardPathes) {
			forwardVisualEffect(e);
		} else if(currentMode == mode.fpPathes) {
			fpVisualEffect(e);
		}
	}
	

	private void nodeClicked(MouseEvent e, Node x) {
		ObservableList<Toggle> modes = drawMode.getToggles();
		switch (getSuitableAction(modes)) {
		case forwardPathes:
			if(forwardStep < 2) {
				forwardPathNodeVisited[forwardStep++] = x;
			}	
			handleforwardpath();
			break;
		case fpPathes:
			if(fpStep < 2) {
				fpNodes[fpStep++] = x;
			}
			break;
		default:
			break;
		}
	}
	
	private void addNode(MouseEvent e) {
		Node temp = new Node();
		temp.setCenterX(e.getX());
		temp.setCenterY(e.getY());
		temp.setRadius(nodeRadius);

		temp.setOnMouseEntered(v -> {
			temp.handleEnter();
		});

		temp.setOnMouseExited(v -> {
			temp.handleOut();
		});

		temp.setOnMouseClicked(v -> {
			nodeClicked(v, temp);
		});

		temp.setIndex(numberOfNode++);
		temp.setAbsY(e.getScreenY());
		allNodes.add(temp);
		Label nodeName = new Label();
		nodeName.setText(temp.getName());
		int offsetX = (int) (nodeRadius * 2);
		int offsetY = (int) (nodeRadius + 5);
		nodeName.setLayoutX(temp.getCenterX() - offsetX);
		nodeName.setLayoutY(temp.getCenterY() + offsetY);
		pane.getChildren().addAll(temp,nodeName);
	}
	
	private void handleforwardpath() {
		if(forwardStep == 2) {
			forwardStep = 0;
			
			if(forwardPathNodeVisited[0].getCenterX() > forwardPathNodeVisited[1].getCenterX()) {
				Node temp = forwardPathNodeVisited[0];
				forwardPathNodeVisited[0] = forwardPathNodeVisited[1];
				forwardPathNodeVisited[1] = temp;
			}
			
			
			Edge temp = new Edge();
			
			temp.setFill(Color.TRANSPARENT);
			temp.setStroke(Color.BLACK);
			temp.setCordinates((int)forwardPathNodeVisited[0].getCenterX() + (int)nodeRadius, (int)forwardPathNodeVisited[0].getCenterY()
					, (int)forwardPathNodeVisited[1].getCenterX() - (int)nodeRadius, (int)forwardPathNodeVisited[1].getCenterY());
			
			
			
			String path = getPath(temp.getStartX(), temp.getStartY(),(temp.getStartX() + temp.getEndX() )/ 2,temp.getStartY(),(temp.getStartX() + temp.getEndX() )/ 2
					,temp.getEndY(), temp.getEndX(), temp.getEndY());
		
			temp.setIndex(numberOfEdges++);
			
			Label tLabel = new Label("Gain");
			tLabel.setId("gainLabel");
			
			InputBox.getInput("Gain: ", "Gain Input", "Submit", "Enter Gain Here!");
			tLabel.setText(InputBox.getInput());
			tLabel.setLayoutX((temp.getStartX() + temp.getEndX() )/ 2 - 5);
			tLabel.setLayoutY((temp.getStartY() + temp.getEndY() )/ 2 - 10);
			
			temp.setGainLabel(tLabel);
			temp.setContent(path);
			Polygon arrow = getSuitableArrow(forwardPathNodeVisited[1], temp);
		    
		    forwardPathes.add(temp);
		    
			forwardPathNodeVisited[0].getIncidentEdges().add(temp);
			forwardPathNodeVisited[0].getIncidents().add(forwardPathNodeVisited[1]);
			
			pane.getChildren().addAll(temp,tLabel,arrow);
		}
	}
	
	private void handlefpPath(MouseEvent e) {
		fpStep = 0;
		hieghtClick = false;
		Edge temp = new Edge();
			
		temp.setFill(Color.TRANSPARENT);
		temp.setStroke(Color.BLACK);
		setfpCordinates(temp,e);
		
		
		
		String path = getPath(temp.getStartX(), temp.getStartY(),temp.getStartX(),(int)e.getY(),temp.getEndX()
					,(int)e.getY(), temp.getEndX(), temp.getEndY());
		temp.setContent(path);
		
		temp.setIndex(numberOfFPEdges++);
		
		Label tLabel = new Label("Gain");
		tLabel.setId("gainLabel");
		tLabel.setLayoutX((temp.getStartX() + temp.getEndX() )/ 2 - 5);
		tLabel.setLayoutY(e.getY() - 10);
		
		InputBox.getInput("Gain: ", "Gain Input", "Submit", "Enter Gain Here!");
		tLabel.setText(InputBox.getInput());
		
		temp.setGainLabel(tLabel);
		
		
		Polygon arrow = getSuitableArrow(fpNodes[1],temp);
		
		fpPathes.add(temp);
		fpNodes[0].getIncidentEdges().add(temp);
		fpNodes[0].getIncidents().add(fpNodes[1]);
		
		pane.getChildren().addAll(temp,tLabel,arrow);
		
	}
	
	private String getPath(int mx, int my, int cx1, int cy1, int cx2, int cy2, int ex, int ey) {
		String path = new String("M "+mx + " " + my + " L " + cx1 + " " + cy1 + ", "+cx2 + " " + cy2+", "+ex + " " + ey);
		return path;
	}
	private String getLine(int mx, int my, int lx1, int ly1) {
		String path = new String("M "+mx + " " + my + " L " + lx1 + " " + ly1);
		return path;
	}

	private mode getSuitableAction(ObservableList<Toggle> modes) {
		if (modes.get(0).isSelected()) {
			return mode.nodes;
		} else if (modes.get(1).isSelected()) {
			return mode.forwardPathes;
		} else if(modes.get(2).isSelected()) {
			return mode.fpPathes;
		} else if (modes.get(3).isSelected()) {
			return mode.freeNodes;
		} else if(modes.get(4).isSelected()) {
			return mode.selfLoop;
		}
		return mode.none;
	}
	
	private mode getSuitableAction(ToggleGroup group) {
		ObservableList<Toggle> modes = group.getToggles();
		if (modes.get(0).isSelected()) {
			return mode.nodes;
		} else if (modes.get(1).isSelected()) {
			return mode.forwardPathes;
		} else if(modes.get(2).isSelected()) {
			return mode.fpPathes;
		} else if (modes.get(3).isSelected()) {
			return mode.freeNodes;
		} else if(modes.get(4).isSelected()) {
			return mode.selfLoop;
		}
		return mode.none;
	}
	
	private void setfpCordinates(Edge path, MouseEvent e) {
		int offset1 = 0,offset2 = 0;
		if(fpNodes[0].getCenterY() < e.getY()) {
			offset1 = (int) nodeRadius;
		} else {
			offset1 = (int) -nodeRadius;
		}
		if(fpNodes[1].getCenterY() < e.getY()) {
			offset2 = (int) nodeRadius;
		} else {
			offset2 = (int) -nodeRadius;
		}
		path.setCordinates((int)fpNodes[0].getCenterX(), (int)fpNodes[0].getCenterY() + offset1
				, (int)fpNodes[1].getCenterX(), (int)fpNodes[1].getCenterY() + offset2);
	}
	
	private Polygon getSuitableArrow(Node endNode,Edge incidentEdge) {
		Polygon arrow = new Polygon();
		double offset1 = 0, offset2 = 0,offset3 = 0, offset4 = 0;
		if((int)endNode.getCenterX() == incidentEdge.getEndX()) {
			if((int)endNode.getCenterY() < incidentEdge.getEndY()) {
				offset1 = 5;
				offset2 = 5;
				offset3 = -5;
				offset4 = 5;
			}else {
				offset1 = 5;
				offset2 = -5;
				offset3 = -5;
				offset4 = -5;
			}	
		} else {
			if((int)endNode.getCenterX() < incidentEdge.getEndX()) {
				offset1 = 5;
				offset2 = -5;
				offset3 = 5;
				offset4 = 5;
			}else {
				offset1 = -5;
				offset2 = -5;
				offset3 = -5;
				offset4 = 5;
			}
		}
		arrow.getPoints().addAll(new Double[] {
				(double)incidentEdge.getEndX(), (double)incidentEdge.getEndY()
				,(double)incidentEdge.getEndX() + offset1, (double)incidentEdge.getEndY() + offset2
				,(double)incidentEdge.getEndX() + offset3, (double)incidentEdge.getEndY() + offset4
		});
		return arrow;
	}
	
	private void resetCounters() {
		forwardStep = 0;
		fpStep = 0;
	}
	
	private void forwardVisualEffect(MouseEvent e) {
		if(forwardStep == 1) {
			effect = new Edge();
			effect.setFill(Color.TRANSPARENT);
			effect.setStroke(Color.BLACK);
			effect.setContent(getLine((int)forwardPathNodeVisited[0].getCenterX(), (int)forwardPathNodeVisited[0].getCenterY(),
					(int)e.getX() - 1, (int)e.getY() - 1));
			pane.getChildren().add(effect);
		}
		
	}
	
	private void fpVisualEffect(MouseEvent e) {
		if(fpStep == 1) {
			effect = new Edge();
			effect.setFill(Color.TRANSPARENT);
			effect.setStroke(Color.BLACK);
			effect.setContent(getLine((int)fpNodes[0].getCenterX(), (int)fpNodes[0].getCenterY(),
					(int)e.getX() - 1, (int)e.getY() - 1));
			pane.getChildren().add(effect);
		} else if (fpStep == 2) {
			effect = new Edge();
			effect.setFill(Color.TRANSPARENT);
			effect.setStroke(Color.BLACK);
			setfpCordinates(effect,e);
			
			
			
			String path = getPath(effect.getStartX(), effect.getStartY(),effect.getStartX(),(int)e.getY(),effect.getEndX()
						,(int)e.getY(), effect.getEndX(), effect.getEndY());
			effect.setContent(path);
			
			effect.setIndex(numberOfFPEdges++);
			
			
			
			arrowEffect = getSuitableArrow(fpNodes[1],effect);
			
			
			pane.getChildren().addAll(effect,arrowEffect);
		
		}
	}
}
