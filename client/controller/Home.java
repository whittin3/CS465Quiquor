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
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;


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
	static HBox topHboxBar;
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
	private Queue<Drink> drinkQueue = new LinkedList();
	private static AtomicBoolean pouring = new AtomicBoolean(false);

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	/**
	 * This function will be called whenever this screen loads
	 */
	@Override
	public void init() {
		pourMyDrinkButton.setDisable(true);
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
					pourMyDrinkButton.setDisable(false);
				}
				if (selection == null) {
					pourMyDrinkButton.setDisable(true);
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
		showFavoritesBar();
		stylize();
	}

	private void stylize() {
		AwesomeDude.setIcon(createYourOwnDrinkButton, AwesomeIcon.EDIT, "1.8em", ContentDisplay.GRAPHIC_ONLY);
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

	private void showFavoritesBar() {
		ObservableList<Drink> favorites = getFavorites();
		favorites.subList(0, 4);
	}

	private ObservableList<Drink> getFavorites() {
		ObservableList<Drink> drinkableList = Main.getDrinkableList();
		FXCollections.sort(drinkableList, new Comparator<Drink>() {
			@Override
			public int compare(Drink o1, Drink o2) {
				return Double.compare(o1.getPopularity(), o2.getPopularity());
			}
		});
		return drinkableList;
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
		Drink drink = new Drink(selectedItem, guiDrinkController.getDrinkMapping(), 1.0);
		drinkQueue.add(drink);
		DrinkItem drinkItem = new DrinkItem(drink, DrinkType.Queue);
		topHboxBar.getChildren().add(drinkItem);
		if (!pouring.get()) {
			pouring.set(true);
			drinkItem.pour();
		}
	}

	private static class DrinkItem extends AnchorPane {
		private final int HEIGHT = 80;
		private final int WIDTH = 80;
		private ProgressBar progressBar;
		private Text name;
		private final Task pour;

		public DrinkItem(Drink drink, DrinkType type) {
			setMinSize(HEIGHT, WIDTH);
			setPrefSize(HEIGHT, WIDTH);
			progressBar = ProgressBarBuilder.create().progress(-1).minWidth(WIDTH).maxWidth(WIDTH).prefWidth(WIDTH).build();
			this.name = TextBuilder.create().text(drink.getName()).styleClass("drinkItem").wrappingWidth(WIDTH).build();
			Rectangle rectangle = RectangleBuilder.create().styleClass("").height(HEIGHT).width(WIDTH).build();
			getChildren().addAll(rectangle, this.name, this.progressBar);
			setBottomAnchor(progressBar, 10.0);
			setTopAnchor(this.name, 0.0);
			pour = new Task() {
				@Override
				protected Object call() throws Exception {
					System.out.println("Pouring");
					int max = 1000000;
					for (int i = 0; i < max; i++) {
						if (isCancelled()) {
							break;
						}
						System.out.println("Iteration " + i);

						updateProgress(i, max);
					}
					System.out.println("Finished");
					topHboxBar.getChildren().remove(0);

					if (!topHboxBar.getChildren().isEmpty()) {
						DrinkItem drinkItem = (DrinkItem) topHboxBar.getChildren().get(0);
						drinkItem.pour();
					} else {
						pouring.set(false);
					}
					return null;
				}
			};
			progressBar.progressProperty().bind(pour.progressProperty());
		}

		public void pour() {
			new Thread(pour).start();
		}
	}

	private static enum DrinkType {
		Queue, Popularity
	}
}
