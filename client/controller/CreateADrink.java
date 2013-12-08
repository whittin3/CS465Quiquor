package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.transitions.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
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

	private static class IngredientCell extends AnchorPane {

		public IngredientCell(String ingredient) {
			int index = Main.getIngredients().indexOf(ingredient);
//			Text text = TextBuilder.create().text(ingredient).styleClass("ingredient" + String.valueOf(index)).build();
			Text text = TextBuilder.create().text(ingredient).styleClass("ingredientItem").build();
			getChildren().add(text);
		}
	}
}
