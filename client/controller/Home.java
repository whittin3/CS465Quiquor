package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.readOnly.Drink;
import client.transitions.FadeTransition;
import de.jensd.fx.fontawesome.AwesomeDude;
import de.jensd.fx.fontawesome.AwesomeIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
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
	@FXML
	Button pourMyDrinkButton;
	@FXML
	Button createYourOwnDrinkButton;
	@FXML
	Button settings;
	@FXML
	Button alphabeticalSort;
	@FXML
	Button userCreatedSort;
	@FXML
	Button popularitySort;

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
		stylize();
	}

	private void stylize() {
		AwesomeDude.setIcon(createYourOwnDrinkButton, AwesomeIcon.EDIT, "2em", ContentDisplay.GRAPHIC_ONLY);
		AwesomeDude.setIcon(settings, AwesomeIcon.COG, "2em", ContentDisplay.GRAPHIC_ONLY);
		AwesomeDude.setIcon(alphabeticalSort, AwesomeIcon.SORT_ALPHA_ASC, "1em", ContentDisplay.GRAPHIC_ONLY);
		AwesomeDude.setIcon(popularitySort, AwesomeIcon.STAR, "1em", ContentDisplay.GRAPHIC_ONLY);
		AwesomeDude.setIcon(userCreatedSort, AwesomeIcon.USER_MD, "1em", ContentDisplay.GRAPHIC_ONLY);
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
		String selectedItem = drinkListView.getSelectionModel().getSelectedItem();
		Drink drink = new Drink(selectedItem, guiDrinkController.getDrinkMapping());
		DrinkItem drinkItem = new DrinkItem(selectedItem);
		topHboxBar.getChildren().add(drinkItem);
	}

	private static class DrinkItem extends StackPane {
		private final int HEIGHT = 10;
		private final int WIDTH = 10;
		private ProgressBar progressBar;
		private Rectangle rectangle;
		private Text name;

		public DrinkItem(String name) {
			this.name = new Text(name);
			rectangle = RectangleBuilder.create().styleClass("drinkItem").height(HEIGHT).width(WIDTH).build();
			getChildren().add(rectangle);
		}
	}

	private static enum DrinkType {
		Shot, Mixed, Cocktail
	}
}
