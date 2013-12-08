package client.controller;

import client.View;
import client.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

/**
 * User: Neal Eric
 * Date: 12/6/13
 */
public class SetupBar implements View {
	private ViewController viewController;
	public int numberOfPumps = 0;

	@FXML
	FlowPane pumpLayout;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@Override
	public void init() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@FXML
	public void addPump() {
		pumpLayout.getChildren().add(new PumpItem());
	}

	@FXML
	public void removePump() {

	}

	@FXML
	public void gotoHome() {

	}

	private static class PumpItem extends AnchorPane {
		private PumpItem() {
		 	getChildren().add(new Text("pump"));

		}
	}
}
