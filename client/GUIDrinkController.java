package client;
/**
 * User: Neal Eric
 * Date: 11/10/13
 */

import client.readOnly.Drink;
import client.readOnly.Ingredient;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GUIDrinkController {
    List<String> ingredientList;
    private final VBox vBox = new VBox();

    public GUIDrinkController() {
        ingredientList = new ArrayList<>();
    }

    public GUIDrinkController(Drink drink) {
        for (Ingredient ingredient : drink.getIngredients()) {
            addIngredient(ingredient);
        }
    }

    public void addIngredient(Ingredient ingredient) {
        ingredientList.add(ingredient.getName());

    }

    public void init(Stage primaryStage) {
        primaryStage.setScene(new Scene(vBox, 600, 675));
        vBox.setAlignment(Pos.BOTTOM_CENTER);
//        String css = Main.class.getResource("default.css").toExternalForm();
        vBox.getChildren().add(new Cell(Color.DARKGREEN));
        vBox.getChildren().add(new Cell(Color.GREEN));

    }

    /**
     * Each Cell is a (drink) row in the GUIDrinkController
     */
    private static class Cell extends StackPane {

        private static final int MAX_WIDTH = 100;
        private static final double MIN_HEIGHT = 20;
        private static final int STARTING_HEIGHT = 310;
        private static final int HORIZONTAL_MARGIN = 7;
        private double y;
        private boolean dragging;
        private Rectangle rectangle = new Rectangle(MAX_WIDTH, STARTING_HEIGHT);
        private Text text = new Text("Test Drink");

        private Cell(Color color) {
            super();
            init(this);
            stylize(color);
        }

        private void stylize(Color color) {
            //Stackpane
            this.setAlignment(Pos.TOP_CENTER);
            this.setMaxWidth(MAX_WIDTH);

            //Rectangle
            rectangle.setFill(color);

            //Text
        }

        public void init(final Cell cell) {
            this.getChildren().addAll(rectangle, text);
            this.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    cell.mousePressed(event);
                }
            });
            this.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    cell.mouseReleased(event);
                }
            });
            this.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    cell.mouseOver(event);
                }
            });
            this.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    cell.mouseDragged(event);
                }
            });
        }

        private void mouseReleased(MouseEvent event) {
            dragging = false;
            this.setCursor(Cursor.DEFAULT);
        }

        private void mouseOver(MouseEvent event) {
            if (isInResizableMargin(event) || dragging) {
                this.setCursor(Cursor.S_RESIZE);
            } else {
                this.setCursor(Cursor.DEFAULT);
            }
        }

        private boolean isInResizableMargin(MouseEvent event) {
            return event.getY() <= (HORIZONTAL_MARGIN);
        }

        private void mouseDragged(MouseEvent event) {
            if (!dragging) {
                return;
            }

            double mouseY = event.getSceneY();
            double newHeight = rectangle.getHeight() - mouseY + y;
            if (newHeight > MIN_HEIGHT) {
                rectangle.setHeight(newHeight);
                y = mouseY;
            }
        }

        private void mousePressed(MouseEvent event) {

            // @todo Create a DraggableZone
            if (!isInResizableMargin(event)) {
                return;
            }
            dragging = true;
            y = event.getSceneY();
        }
    }
}
