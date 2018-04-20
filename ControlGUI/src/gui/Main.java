package gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	Scene scene;
	Stage window;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("FXML_Sheet.fxml"));
		window = primaryStage;
		primaryStage.setTitle("Comtrol");
		
		
		
		scene = new Scene(root,1000,1000);
		scene.getStylesheets().add("bassamStyle.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
