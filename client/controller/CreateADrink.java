package client.controller;

import client.View;
import client.ViewController;
import client.transitions.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class CreateADrink implements View {
	private ViewController viewController;
	@FXML
	FlowPane flowPane;
	@FXML
	AnchorPane guiControllerPane;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@Override
	public void init() {
	}

	@FXML
	public void saveDrink() {

	}

	@FXML
	public void gotoHome() {
		viewController.setScreen(ViewController.Home, new FadeTransition());
	}
}
