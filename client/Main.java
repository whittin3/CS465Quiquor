package client;

import client.controller.GUIDrinkController;
import client.readOnly.Ingredient;
import client.transitions.FadeTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {
	public static HashMap<Ingredient, Integer> pumpMap = new HashMap<>();
	public static DrinkLibrary drinkLibrary;
	public static String userPassword;

	private String styleSheet;

	@Override
	public void start(Stage primaryStage) throws Exception {
		styleSheet = getClass().getResource("default.css").toExternalForm();
		drinkLibrary = DrinkLibrary.parseDrinkFile();
		init(primaryStage);
//		demo(primaryStage);
	}

	/**
	 * Called by FXML
	 * @param primaryStage
	 * @throws IOException
	 */
	private void init(Stage primaryStage) throws IOException {
		ViewController stage = new ViewController();
		stage.setScreen(ViewController.Welcome, new FadeTransition());
		Group root = new Group();
		root.getStylesheets().add(styleSheet);
		root.getChildren().addAll(stage);
		Scene scene = new Scene(root);
//		primaryStage.setFullScreen(true);
		primaryStage.setTitle("Welcome to Quiqour");
		primaryStage.setScene(scene);
		primaryStage.show();
		//
//		todo @whittin3 init function for welcome
	}

	private void demo(Stage primaryStage) {
		Ingredient test1 = new Ingredient("test1");
		Ingredient test2 = new Ingredient("test2");
		Ingredient test3 = new Ingredient("test3");
		Ingredient test4 = new Ingredient("test4");
		pumpMap.put(test1, 0);
		pumpMap.put(test2, 1);
		pumpMap.put(test3, 2);
		pumpMap.put(test4, 3);
		GUIDrinkController guiDrinkController = new GUIDrinkController();
		guiDrinkController.addIngredient(test1);
		guiDrinkController.addIngredient(test2);
		guiDrinkController.addIngredient(test3);
		guiDrinkController.addIngredient(test4);
		Scene root = new Scene(guiDrinkController, 500, 400);
		root.getStylesheets().add(styleSheet);
		primaryStage.setScene(root);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private static String getUserPassword() {
		return userPassword;
	}

	public static String setUserPassword(String userPassword) {
		if (userPassword != null) {
			return "Error";
		} else {
			userPassword = userPassword;
			return "Success";
		}
	}


}
