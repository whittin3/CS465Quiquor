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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * User: Neal Eric
 * Date: 12/6/13
 */
public class SetupBar implements View {
	private ViewController viewController;
	private List<PumpItem> pumpItemList;
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
		pumpItemList = new ArrayList<>();
		testAndDemoSetup();
	}

	private void testAndDemoSetup() {
		List<String> testIngredients = Arrays.asList("Vodka", "Water", "Sprite", "Cola", "Amaretto", "Red Bull", "Spiced Rum");
		for (int i = 0; i < testIngredients.size(); i++) {
			addPump();
			pumpItemList.get(i).setIngredient(testIngredients.get(i));
		}
	}

	@FXML
	public void addPump() {
		PumpItem pumpItem = new PumpItem(ingredients, numberOfPumps + 1);
		pumpLayout.getChildren().add(pumpItem);
		pumpItemList.add(pumpItem);
		numberOfPumps++;
	}

	@FXML
	public void removePump() {
		ObservableList<Node> children = pumpLayout.getChildren();
		children.remove(numberOfPumps - 1);
		pumpItemList.remove(numberOfPumps - 1);
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
		for (PumpItem pumpItem : pumpItemList) {
			String ingredient = pumpItem.getIngredient();
			Main.pumpMap.put(Main.drinkLibrary.getIngredient(ingredient), i);
			i++;
		}
	}


	private static class PumpItem extends HBox {

		private final AutoFillTextBox autoFillTextBox;
		private final Text pumpText;

		public PumpItem(ObservableList<String> ingredients, int number) {
			pumpText = new Text("Pump " + String.valueOf(number) + ": ");
			autoFillTextBox = new AutoFillTextBox(ingredients);
			getChildren().add(pumpText);
			getChildren().add(autoFillTextBox);
		}

		public String getIngredient() {
			return autoFillTextBox.getText();
		}

		public void setIngredient(String ingredient) {
			if (Main.drinkLibrary.ingredients.keySet().contains(ingredient)) {
				autoFillTextBox.setText(ingredient);
			} else {
				System.out.println("When setting " + pumpText.getText() + "we could not locate the ingredient " + ingredient);
			}
		}
	}
}
