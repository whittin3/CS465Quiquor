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

    public GUIDrinkController() {
        ingredientList = new ArrayList<>();
    }

    public GUIDrinkController(Drink drink) {
        for (Ingredient ingredient : drink.getIngredients())
        {
            addIngredient(ingredient);
        }
    }
    public void addIngredient(Ingredient ingredient)
    {
        ingredientList.add(ingredient.getName());

    }

    public void init(Stage primaryStage) {
        final VBox root = new VBox();
        primaryStage.setScene(new Scene(root, 600, 675));
//        String css = Main.class.getResource("default.css").toExternalForm();
        root.getChildren().add(new Cell(Color.ALICEBLUE));
        root.getChildren().add(new Cell(Color.BISQUE));

    }

    private static class Cell extends StackPane {

        private static final int MAX_WIDTH = 100;
        private double y;
        private boolean dragging;
        private Rectangle rectangle = new Rectangle(MAX_WIDTH,210);
        private Text text = new Text("sdsd");

        private Cell(Color color) {
            super();
            init(this);
            stylize(color);
        }

        private void stylize(Color color)
        {
            this.setAlignment(Pos.TOP_CENTER);
            this.setMaxWidth(MAX_WIDTH);

            rectangle.setFill(color);

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
            if(isInResizableMargin(event) || dragging) {
                this.setCursor(Cursor.S_RESIZE);
            }
            else {
                this.setCursor(Cursor.DEFAULT);
            }
        }

        private boolean isInResizableMargin(MouseEvent event) {
            return event.getY() > (this.getHeight() - 7);
        }

        private void mouseDragged(MouseEvent event) {
            if(!dragging) {
                return;
            }

            double mouseY = event.getY();
            double newHeight = rectangle.getHeight() + mouseY - y;
            rectangle.setHeight(newHeight);
            y = mouseY;
        }

        private void mousePressed(MouseEvent event) {

            // @todo Create a DraggableZone
            if(!isInResizableMargin(event)) {
                return;
            }
            dragging = true;
            y = event.getY();
        }
    }
}
