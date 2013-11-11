package client;

import client.controller.GUIDrinkController;
import client.readOnly.Ingredient;
import client.transitions.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {
    public static HashMap<Ingredient, Integer> pumpMap = new HashMap<>();
    private String styleSheet;

    @Override
    public void start(Stage primaryStage) throws Exception {
        styleSheet = getClass().getResource("default.css").toExternalForm();
        init(primaryStage);
    }

    private void init(Stage primaryStage) throws IOException {
        ViewController stage = new ViewController();
        stage.setScreen(ViewController.Welcome, new FadeTransition());
        Group root = new Group();
        root.getStylesheets().add(styleSheet);
        root.getChildren().addAll(stage);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Welcome to Quiqour");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void demo(Stage primaryStage) {
        Ingredient test1 = new Ingredient("test1");
        Ingredient test2 = new Ingredient("test2");
        pumpMap.put(test1, 0);
        pumpMap.put(test2, 1);
        GUIDrinkController guiDrinkController = new GUIDrinkController();
        guiDrinkController.addIngredient(test1);
        guiDrinkController.addIngredient(test2);
        Scene root = new Scene(guiDrinkController, 550, 550);
        root.getStylesheets().add(styleSheet);
        primaryStage.setScene(root);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
