package client;

import client.controller.GUIDrinkController;
import client.transitions.ViewTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.util.HashMap;

/**
 * User: Neal Eric
 * Date: 11/11/13
 */

/**
 * If you ever want to change from one (view, frame, fxml, screen, etc) to another, please use
 * this class and object.
 */
public class ViewController extends StackPane {

	// Please define any Views/Controllers you have created here
	// For ease of use, please keep the FXML and the Controller name the same
	public static final String Welcome = "Welcome";
	public static final String Home = "Home";
	public static final String CreateADrink = "CreateADrink";

	public HashMap<String, Node> getViews() {
		return views;
	}

	private HashMap<String, Node> views = new HashMap<>();
	private HashMap<String, View> controllers = new HashMap<>();

	public ViewController() {
		super();
		loadScreen(Welcome);
		loadScreen(Home);
		loadScreen(CreateADrink);
	}

	public void addView(String id, Node node) {
		views.put(id, node);
	}

	public void addController(String id, View view) {
		controllers.put(id, view);
	}

	public void loadScreen(String name) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(GUIDrinkController.class.getResource(name + ".fxml"));
			Parent parent = (Parent) fxmlLoader.load();
			View view = fxmlLoader.getController();
			view.setViewController(this);
			addView(name, parent);
			addController(name, view);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void setScreen(final String name, ViewTransition viewTransition) {
		if (views.containsKey(name)) {
			if (!getChildren().isEmpty()) {
				viewTransition.out(name, this);
			} else {
				viewTransition.in(name, this);
			}
			View view = controllers.get(name);
			view.init();
		} else {
			System.out.println("The view " + name + " doesn't exist \n");
		}
	}
}