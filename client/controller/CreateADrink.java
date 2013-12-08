package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.transitions.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

public class CreateADrink implements View {
	private ViewController viewController;

	@FXML
	FlowPane flowPane;
	@FXML
	AnchorPane guiControllerPane;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@Override
	public void init() {
		ObservableList<String> ingredients = Main.getIngredients();
		for (String ingredient : ingredients) {
			flowPane.getChildren().add(new IngredientCell(ingredient));
		}
		guiControllerPane.getChildren().add(new GUIDrinkController(true));
	}

	@FXML
	public void saveDrink() {

	}

	@FXML
	public void gotoHome() {
		viewController.setScreen(ViewController.Home, new FadeTransition());
	}

	private static class IngredientCell extends StackPane {

		public IngredientCell(String ingredient) {
			Text text = TextBuilder.create().text(ingredient).styleClass("ingredientItem").build();
			Bounds rectBounds = text.getBoundsInParent();

			Rectangle rectangle = new Rectangle(rectBounds.getWidth(), rectBounds.getHeight());
			int index = Main.getIngredients().indexOf(ingredient);
			rectangle.getStyleClass().setAll("drink-cell-" + String.valueOf(index));
			getChildren().add(rectangle);

			getChildren().add(text);

		}
	}
}
