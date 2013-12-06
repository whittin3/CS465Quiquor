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

import java.util.List;


public class Home implements View {
	private ViewController viewController;
	ObservableList<String> observableDrinkList = FXCollections.observableArrayList();
	@FXML
	static ListView<String> drinkListView;

	@FXML
	TextField drinkSearchField;


	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	/**
	 * This function will be called whenever this screen loads
	 */
	@Override
	public void init() {
		System.out.println("asdasd");
		List<Drink> drinkList = Main.drinkLibrary.getDrinkList();
		for (Drink drink : drinkList) {
			observableDrinkList.add(drink.getName());
		}
		FXCollections.sort(observableDrinkList);
		drinkListView.setItems(observableDrinkList);
		drinkSearchField.textProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable, Object oldVal,	Object newVal) {
				ObservableList<String> searchResults = search((String) oldVal, (String) newVal);
				drinkListView.setItems(searchResults);
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
