package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.readOnly.Drink;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.List;


public class Home implements View {
	private ViewController viewController;
	ObservableList<String> observableDrinkList;
	@FXML
	static ListView<String> drinkListView;

	@FXML
	TextField drinkSearchField;
	@FXML
	AnchorPane guiControllerPane;
	private GUIDrinkController guiDrinkController;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	/**
	 * This function will be called whenever this screen loads
	 */
	@Override
	public void init() {
		observableDrinkList = Main.drinkLibrary.getDrinkList();
		drinkListView.setItems(observableDrinkList);
		drinkSearchField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue observable, String oldVal, String newVal) {
				ObservableList<String> searchResults = search(oldVal, newVal);
				drinkListView.setItems(searchResults);
			}
		});
		guiDrinkController = new GUIDrinkController();
		guiControllerPane.getChildren().add(guiDrinkController);
		drinkListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				if (new_val != null && !observableDrinkList.contains(new_val)) {
					guiDrinkController.addDrink(Main.drinkLibrary.getDrink(new_val));
					System.out.println(new_val);
				}
//				drinkListView.getSelectionModel().clearSelection();
			}
		});
	}

	private ObservableList<String> search(final String oldQuery, final String newQuery) {
		if ((newQuery.length() < oldQuery.length())) {
			drinkListView.setItems(observableDrinkList);
		}
		String query = newQuery.toLowerCase();
		ObservableList<String> subentries = FXCollections.observableArrayList();
		for (Object ingredient : drinkListView.getItems()) {
			String ingredientName = (String) ingredient;
			if (ingredientName.toLowerCase().contains(query) || ingredientName.toLowerCase().startsWith(query)) {
				subentries.add(ingredientName);
			}
		}
		return subentries;
	}
}
