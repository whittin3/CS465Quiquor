package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.readOnly.Drink;
import client.readOnly.Ingredient;
import client.transitions.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

import java.util.HashMap;

public class CreateADrink implements View {
	private ViewController viewController;

	@FXML
	FlowPane flowPane;
	@FXML
	AnchorPane guiControllerPane;
	@FXML
	Text saveStatus;
	@FXML
	TextField drinkNameText;

	private GUIDrinkController guiDrinkController = new GUIDrinkController(true);

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@Override
	public void init() {
		guiDrinkController.clear();
		guiControllerPane.getChildren().clear();
		guiControllerPane.getChildren().add(guiDrinkController);
		ObservableList<String> ingredients = Main.getIngredients();
		flowPane.getChildren().clear();
		for (String ingredient : ingredients) {
			flowPane.getChildren().add(new IngredientCell(guiDrinkController, ingredient));
		}
		saveStatus.setVisible(false);
	}

	@FXML
	public void saveDrink() {
		HashMap<Ingredient, Double> drinkMapping = guiDrinkController.getDrinkMapping();
		String drinkName = drinkNameText.getText();
		String sanitizedDrinkName = drinkName.replaceAll("\\s", "");
		if (sanitizedDrinkName.equals("")) {
			saveStatus.setText("Enter in a drink name");
			saveStatus.setVisible(true);
		} else if (drinkMapping.isEmpty()) {
			saveStatus.setText("Select an ingredient");
			saveStatus.setVisible(true);
		} else {
			Drink drink = new Drink(drinkName, drinkMapping, 1.0);
			Main.addUserCreatedDrink(drink);
			this.gotoHome();
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
			Ingredient ingredient = Main.getDrinkLibrary().getIngredient(ingredientName);
			if (selected) {
				guiDrinkController.addIngredient(ingredientName, 1);
				styleClass.setAll("drink-cell-" + String.valueOf(Main.getPumpMap().get(ingredient)));
			} else {
				guiDrinkController.removeIngredient(ingredientName);
				styleClass.setAll("drink-cell-unselected");
			}
		}
	}
}
