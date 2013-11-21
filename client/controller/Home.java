package client.controller;

import client.View;
import client.ViewController;
import javafx.fxml.FXML;

import javax.swing.text.html.ListView;

public class Home implements View {
	private ViewController viewController;

	@FXML
	ListView drinkListView;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}
}
