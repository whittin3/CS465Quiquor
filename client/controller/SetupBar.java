package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.control.AutoFillTextBox;
import client.transitions.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Set;

/**
 * User: Neal Eric
 * Date: 12/6/13
 */
public class SetupBar implements View {
	private ViewController viewController;
	public int numberOfPumps = 0;

	@FXML
	FlowPane pumpLayout;
	private ObservableList<String> ingredients;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@Override
	public void init() {
		Set<String> ingredientNames = Main.drinkLibrary.getIngredients().keySet();
		ingredients = FXCollections.observableArrayList(ingredientNames);

		testingSetup();
	}

	private void testingSetup() {
		addPump();
		addPump();
		addPump();

	}

	@FXML
	public void addPump() {
		pumpLayout.getChildren().add(new PumpItem(ingredients, numberOfPumps + 1));
		numberOfPumps++;
	}

	@FXML
	public void removePump() {
		ObservableList<Node> children = pumpLayout.getChildren();
		children.remove(children.size() - 1);
		numberOfPumps--;

	}

	@FXML
	public void gotoHome() {
		savePumpSelections();
		viewController.setScreen(ViewController.Home, new FadeTransition());
	}

	private void savePumpSelections() {
		ObservableList<Node> children = pumpLayout.getChildren();
		int i = 0;
		for (Node node : children) {
			PumpItem pumpItem = (PumpItem) node;
			String ingredient = pumpItem.getIngredient();
			Main.pumpMap.put(Main.drinkLibrary.getIngredient(ingredient), i);
			i++;
		}
	}

	private static class PumpItem extends HBox {

		private final AutoFillTextBox autoFillTextBox;

		public PumpItem(ObservableList<String> ingredients, int number) {
			getChildren().add(new Text("Pump " + String.valueOf(number) + ": "));
			autoFillTextBox = new AutoFillTextBox(ingredients);
			getChildren().add(autoFillTextBox);
		}

		public String getIngredient() {
			return autoFillTextBox.getText();
		}
	}
}
