package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.transitions.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


public class Home implements View {
	private ViewController viewController;
	ObservableList<String> observableDrinkList;
	@FXML
	static ListView<String> drinkListView;

	@FXML
	TextField drinkSearchField;
	@FXML
	AnchorPane guiControllerPane;
	@FXML
	Text drinkNameText;
	@FXML
	AnchorPane modal;
	@FXML
	TextField password;
	@FXML
	Text passwordStatus;
	@FXML
	HBox topHboxBar;

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
		modal.setVisible(false);
		drinkNameText.setText("");
		observableDrinkList = Main.getDrinkables();
		drinkListView.setItems(observableDrinkList);
		drinkSearchField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue observable, String oldVal, String newVal) {
				ObservableList<String> searchResults = search(oldVal, newVal);
				drinkListView.setItems(searchResults);
			}
		});
		guiDrinkController = new GUIDrinkController(false);
		guiControllerPane.getChildren().add(guiDrinkController);
		drinkListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String selection) {
				if (selection != null && observableDrinkList.contains(selection)) {
					guiDrinkController.addDrink(Main.drinkLibrary.getDrink(selection));
					drinkNameText.setText(selection);
				}
//				drinkListView.getSelectionModel().clearSelection();
			}
		});

		//Create handler for Modal De-Selection from Overlay
		modal.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				AnchorPane passwordModal = (AnchorPane) mouseEvent.getSource();
				Node dialog = passwordModal.getChildren().get(0);
				Bounds dialogBounds = dialog.getBoundsInParent();
				double x = mouseEvent.getX();
				double y = mouseEvent.getY();
				if (!(x > dialogBounds.getMinX() && x < dialogBounds.getMaxX() &&
						y > dialogBounds.getMinY() && y < dialogBounds.getMaxY())) {
					modal.setVisible(false);
				}
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

	private void initFavoritesBar() {

	}

	@FXML
	public void promptForPassword() {
		passwordStatus.setText("");
		password.setText("");
		modal.setVisible(true);
	}

	@FXML
	public void gotoCreateYourOwnDrink() {
		viewController.setScreen(ViewController.CreateADrink, new FadeTransition());
	}

	@FXML
	public void pourMyDrink() {

	}
}
