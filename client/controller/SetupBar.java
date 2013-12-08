package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import np.com.ngopal.control.AutoFillTextBox;

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
		addPump();
		Set<String> ingredientNames = Main.drinkLibrary.getIngredients().keySet();
		ingredients = FXCollections.observableArrayList(ingredientNames);
	}

	@FXML
	public void addPump() {
		pumpLayout.getChildren().add(new PumpItem(ingredients));
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

	}

	private static class PumpItem extends AnchorPane {

		public PumpItem(ObservableList<String> ingredients) {
			getStyleClass().setAll("autofill-text");

			getChildren().add(new AutoFillTextBox(ingredients));
		}
	}
}
