package gui;


import java.awt.Window;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InputBox {
	private static TextField userInput = new TextField();
	private static Stage window;
	private static boolean valid = false;
	public static void getInput(String message, String title, String action,String prompt) {
		valid = false;
		if(window == null) {
			window = new Stage();
		}
		window.setTitle(title);
		
		window.setMinWidth(100);
		userInput.setPromptText(prompt);
		
		Button submit = new Button(action);
		
		submit.setOnAction(e -> {
			checkInput(message,title,action,prompt);
			if(valid)
				window.close();
		});
		
		window.setOnCloseRequest(e -> {
			e.consume();
			checkInput(message,title,action,prompt);
			if(valid)
				window.close();
		});
		
		HBox hBox = new HBox(10);
		hBox.setPadding(new Insets(10));
		Label lMessage = new Label(message);
		hBox.getChildren().addAll(lMessage,userInput);
		
		VBox layout = new VBox(0);
		layout.getChildren().addAll(hBox, submit);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(5));
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		
		window.show();		
	}
	
	public static String getInput() {
		String input = userInput.getText();
		userInput.clear();
		return String.valueOf(Float.parseFloat(input));
	}
	
	public static void checkInput(String message, String title, String action,String prompt) {
		String input = userInput.getText();
			try {
				Integer.parseInt(input);
				
				valid = true;
			} catch (Exception e) {
				valid = false;
			}
		
	}
	
}
