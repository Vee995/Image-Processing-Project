package application;
	
import org.opencv.core.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Template.fxml"));
			loader.setController(new FXController());
			
			VBox root = loader.load();
			Scene scene = new Scene(root,1300,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
		
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("South African Bank Notes Recognition System: Comp 707 Mini Project");
			primaryStage.show();
			
			
			
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
