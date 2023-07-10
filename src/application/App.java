package application;
	

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class App extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {	
			Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
			Scene scene = new Scene(root,1280,800);
			Image icon = new Image(getClass().getResourceAsStream("Title.png"));
			String cssPath = "application.css";
			scene.getStylesheets().add(cssPath);
			
			
			primaryStage.setResizable(false);
		    primaryStage.setScene(scene);
		    primaryStage.setTitle("A* Visualisator");
		    primaryStage.getIcons().add(icon);
		    primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
