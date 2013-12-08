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
import javafx.scene.input.MouseEvent;
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
	private GUIDrinkController guiDrinkController;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@Override
	public void init() {
		guiDrinkController = new GUIDrinkController(true);
		guiControllerPane.getChildren().add(guiDrinkController);

		ObservableList<String> ingredients = Main.getIngredients();
		for (String ingredient : ingredients) {
			flowPane.getChildren().add(new IngredientCell(guiDrinkController, ingredient));
		}
	}

	@FXML
	public void saveDrink() {

	}

	@FXML
	public void gotoHome() {
		viewController.setScreen(ViewController.Home, new FadeTransition());
	}

	private static class IngredientCell extends StackPane {
		private final GUIDrinkController guiDrinkController;
		Rectangle rectangle;
		private boolean selected = false;
		private final Text text;

		public IngredientCell(final GUIDrinkController guiDrinkController, final String ingredient) {
			this.guiDrinkController = guiDrinkController;

			text = TextBuilder.create().text(ingredient).styleClass("ingredientItem").build();
			Bounds rectBounds = text.getBoundsInParent();

			rectangle = new Rectangle(rectBounds.getWidth(), rectBounds.getHeight());
			rectangle.getStyleClass().setAll("drink-cell-unselected");
			getChildren().add(rectangle);
			getChildren().add(text);

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
			if (selected) {
				String ingredientName = text.getText();
				guiDrinkController.addIngredient(ingredientName);
				Ingredient ingredient = Main.drinkLibrary.getIngredient(ingredientName);
				styleClass.setAll("drink-cell-" + String.valueOf(Main.pumpMap.get(ingredient)));
			} else {
				styleClass.setAll("drink-cell-unselected");
			}
		}
	}
}
