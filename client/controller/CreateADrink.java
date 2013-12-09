package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.readOnly.Ingredient;
import client.transitions.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import au.com.bytecode.opencsv.CSVWriter;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;
import se.mbaeumer.fxmessagebox.MessageBoxResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class CreateADrink implements View {
    private ViewController viewController;

    private static HashSet<String> newDrinkIngredients;
    @FXML
    FlowPane flowPane;
    @FXML
    AnchorPane guiControllerPane;
    private GUIDrinkController guiDrinkController;
    @FXML
    javafx.scene.control.TextField drinkName;

    @FXML
    Text saveFeedback;

    @Override
    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }

    @Override
    public void init() {
        guiDrinkController = new GUIDrinkController(true);
        guiControllerPane.getChildren().add(guiDrinkController);
        newDrinkIngredients = new HashSet<String>();
        ObservableList<String> ingredients = Main.getIngredients();
        for (String ingredient : ingredients) {
            flowPane.getChildren().add(new IngredientCell(guiDrinkController, ingredient));
        }
    }

    @FXML
    public void saveDrink() throws IOException {
        MessageBox saveDrink = new MessageBox("Are you sure you want to save this drink?", MessageBoxType.YES_NO);
        saveDrink.showAndWait();
        if (saveDrink.getMessageBoxResult() == MessageBoxResult.YES){
            CSVWriter writer = new CSVWriter(new FileWriter("data/customDrinks.csv",true), '\t');
            Iterator<String> iterator = newDrinkIngredients.iterator();
            String drinks[] = new String[20];
            drinks[0] = drinkName.getText();
            int index =1;
            while(iterator.hasNext()) {
                drinks[index++] = iterator.next();
            }
            writer.writeNext(drinks);
            writer.close();
            saveFeedback.setText("Drink Saved Successfully");
        }
        else{
            //do nothing
            saveFeedback.setText("Custom Drink not Saved");
        }
    }
    @FXML
    public void gotoHome() {
        viewController.setScreen(ViewController.Home, new FadeTransition());
    }

    private static class IngredientCell extends StackPane {
        private final GUIDrinkController guiDrinkController;
        private static final int padding = 20;
        Rectangle rectangle;
        private boolean selected = false;
        private final Text text;

        public IngredientCell(final GUIDrinkController guiDrinkController, final String ingredient) {
            this.guiDrinkController = guiDrinkController;

            // whittin3 - I set the font here and not in the CSS because I must set the bounds of the rectangle.
            text = TextBuilder.create().text(ingredient).font(new Font(32)).styleClass("ingredientItem").build();
            Bounds rectBounds = text.getBoundsInParent();

            int rectPadding = padding / 2;
            rectangle = new Rectangle(rectBounds.getWidth() + rectPadding, rectBounds.getHeight() + rectPadding);
            rectangle.getStyleClass().setAll("drink-cell-unselected");
            setAlignment(Pos.CENTER);
            getChildren().add(rectangle);
            getChildren().add(text);
            setMinSize(rectBounds.getWidth() + padding, rectBounds.getHeight() + padding);

            final IngredientCell cell = this;
            this.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    cell.mouseReleased(event);
                }
            });
        }

        private void mouseReleased(MouseEvent event) {
            IngredientCell source = (IngredientCell) event.getSource();
            source.toggleSelected();
        }

        public void toggleSelected() {
            selected = !selected;
            ObservableList<String> styleClass = rectangle.getStyleClass();
            styleClass.clear();
            String ingredientName = text.getText();
            Ingredient ingredient = Main.drinkLibrary.getIngredient(ingredientName);
            if (selected) {
                guiDrinkController.addIngredient(ingredientName, 1);
                newDrinkIngredients.add(ingredientName);
                styleClass.setAll("drink-cell-" + String.valueOf(Main.pumpMap.get(ingredient)));
            } else {
                guiDrinkController.removeIngredient(ingredientName);
                newDrinkIngredients.remove(ingredientName);
                styleClass.setAll("drink-cell-unselected");
            }
        }
    }
}
