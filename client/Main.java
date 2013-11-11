package client;

import client.readOnly.Ingredient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {
    public static HashMap<Ingredient, Integer> pumpMap = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
       demo(primaryStage);
    }

    private void init(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        root.getStylesheets().add(getClass().getResource("default.css").toExternalForm());
        primaryStage.setTitle("Welcome to Quiqour");
        primaryStage.setScene(new Scene(root, 600, 675));
        primaryStage.show();
    }

    private void demo(Stage primaryStage)
    {
        GUIDrinkController guiDrinkController = new GUIDrinkController();
        guiDrinkController.init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
