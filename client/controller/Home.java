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
import javafx.scene.layout.AnchorPaneBuilder;
import javafx.scene.layout.HBox;
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
	AnchorPane passwordModal;
	@FXML
	AnchorPane settingsModal;
	@FXML
	TextField password;
	@FXML
	Text passwordStatus;
	@FXML
	static HBox drinkItemBox;
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

	private GUIDrinkController guiDrinkController = new GUIDrinkController(false);
	private Queue<Drink> drinkQueue = new LinkedList();
	private static AtomicBoolean isCurrentlyPouringDrink = new AtomicBoolean(false);
	private static int maxFavoriteBar = 6;

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
		hideSettings();
		hidePasswordModal();
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
		guiDrinkController.clear();
		guiControllerPane.getChildren().clear();
		guiControllerPane.getChildren().add(guiDrinkController);
		drinkListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String selection) {
				if (selection != null && observableDrinkList.contains(selection)) {
					guiDrinkController.addDrink(Main.getDrinkLibrary().getDrink(selection));
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
		passwordModal.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				AnchorPane passwordModal = (AnchorPane) mouseEvent.getSource();
				Node dialog = passwordModal.getChildren().get(0);
				Bounds dialogBounds = dialog.getBoundsInParent();
				double x = mouseEvent.getX();
				double y = mouseEvent.getY();
				if (!(x > dialogBounds.getMinX() && x < dialogBounds.getMaxX() &&
						y > dialogBounds.getMinY() && y < dialogBounds.getMaxY())) {
					passwordModal.setVisible(false);
				}
			}
		});
		showFavoritesBar();
		stylize();
	}

	private void stylize() {
		AwesomeDude.setIcon(createYourOwnDrinkButton, AwesomeIcon.EDIT, "1.6em", ContentDisplay.GRAPHIC_ONLY);
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

	private static void showFavoritesBar() {
		if (!isCurrentlyPouringDrink.get()) {
			drinkItemBox.getChildren().clear();
			ObservableList<Drink> favorites = getFavorites();
			for (int i = 0; i < maxFavoriteBar; i++) {
				Drink drink = favorites.get(i);
				DrinkItem drinkItem = new DrinkItem(drink, DrinkType.Popularity);
				drinkItemBox.getChildren().add(drinkItem);
			}
		}
	}

	private static ObservableList<Drink> getFavorites() {
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
		passwordStatus.setVisible(false);
		password.setText("");
		passwordModal.setVisible(true);
	}

	@FXML
	public void showSettings() {
		if (password.getText().equals(Main.userPassword)) {
			hidePasswordModal();
			settingsModal.setVisible(true);
		} else {
			passwordStatus.setVisible(true);
			password.setText("");
		}
	}

	private void hidePasswordModal() {
		passwordStatus.setVisible(false);
		password.setText("");
		passwordModal.setVisible(false);
	}

	@FXML
	public void saveSettings() {
		hideSettings();
	}

	@FXML
	public void hideSettings() {
		settingsModal.setVisible(false);
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
		ObservableList<Node> drinkItemBoxChildren = drinkItemBox.getChildren();
		if (!Home.isCurrentlyPouringDrink.get()) {
			if (!drinkItemBoxChildren.isEmpty()) {
				drinkItemBoxChildren.clear();
			}
			Home.isCurrentlyPouringDrink.set(true);
			new Thread(drinkItem.getPourTask()).start();
		}
		drinkItemBoxChildren.add(drinkItem);
	}

	@FXML
	private void sortByPopularity() {
		ObservableList<String> favorites = FXCollections.observableArrayList();
		for (Drink drink : getFavorites()) {
			favorites.add(drink.getName());
		}
		observableDrinkList = favorites;
		drinkListView.setItems(favorites);
	}

	@FXML
	private void sortByAlphabet() {
		observableDrinkList = Main.getDrinkables();
		drinkListView.setItems(observableDrinkList);
	}

	@FXML
	private void sortByUserCreated() {
		ObservableList<String> userCreatedDrinks = FXCollections.observableArrayList();
		for (Drink drink : Main.getUserCreated()) {
			userCreatedDrinks.add(drink.getName());
		}
		observableDrinkList = userCreatedDrinks;
		drinkListView.setItems(userCreatedDrinks);
	}

	@FXML
	private void gotoBarSetup() {
		hideSettings();
		viewController.setScreen(ViewController.SetupBar, new FadeTransition());
	}

	private static class DrinkItem extends AnchorPane {
		private final int HEIGHT = 80;
		private final int WIDTH = 80;
		private final DrinkType type;
		private ProgressBar progressBar;
		private Text name;
		private Task pourTask = null;

		public DrinkItem(Drink drink, DrinkType type) {
			setMinSize(HEIGHT, WIDTH);
			setPrefSize(HEIGHT, WIDTH);
			progressBar = ProgressBarBuilder.create().progress(-1).minWidth(WIDTH).maxWidth(WIDTH).prefWidth(WIDTH).build();
			this.type = type;
			this.name = TextBuilder.create().text(drink.getName()).styleClass("drinkItem").wrappingWidth(WIDTH).build();
			AnchorPane rectangle = AnchorPaneBuilder.create().styleClass("drinkItemImage").prefHeight(HEIGHT).prefWidth(WIDTH).build();
			getChildren().addAll(rectangle, this.name);
			setBottomAnchor(progressBar, 10.0);
			setTopAnchor(this.name, 0.0);
			if (this.type.equals(DrinkType.Popularity)) {
				System.out.println("fav");
			} else {
				pourTask = new Task() {
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
						if (drinkItemBox.getChildren().size() == 1) {
							isCurrentlyPouringDrink.set(false);
						}
						wait();
						return null;
					}
				};
				getChildren().add(this.progressBar);
				progressBar.progressProperty().bind(pourTask.progressProperty());

				setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						onClick();
					}
				});
			}
		}

		public Task getPourTask() {
			return pourTask;
		}

		public void onClick() {
			if (type.equals(DrinkType.Queue)) {
				ObservableList<Node> children = Home.drinkItemBox.getChildren();
				int index = children.indexOf(this);
				if (index == 0) {
					double workDone = getPourTask().getWorkDone();
					System.out.println(workDone);
					if (workDone == 999999.0) {
						children.remove(this);
						if (children.isEmpty()) {
							Home.showFavoritesBar();
						}
					} else if (workDone == -1.0) {
						new Thread(getPourTask()).start();
					}
				} else {
					children.remove(this);
				}
			}
		}
	}

	private static enum DrinkType {
		Queue, Popularity
	}
}
