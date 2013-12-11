package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.transitions.FadeTransition;
import de.jensd.fx.fontawesome.AwesomeDude;
import de.jensd.fx.fontawesome.AwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Welcome implements View {
	private ViewController viewController;

	@FXML
	AnchorPane passwordModal;
	@FXML
	PasswordField passwordField1;
	@FXML
	PasswordField passwordField2;
	@FXML
	Text passwordStatus;
	@FXML
	Button orderADrinkButton;


	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@Override
	public void init() {
		passwordModal.setVisible(false);
		stylize();
	}

	private void stylize() {
//		ToggleButton configurationButton = AwesomeDude.createIconToggleButton(AwesomeIcon.GEARS, "Config", "3em", ContentDisplay.TOP);
		AwesomeDude.setIcon(orderADrinkButton, AwesomeIcon.BEER, "3em", ContentDisplay.LEFT);
	}

	/**
	 * Called when you click order a drink
	 */
	@FXML
	private void gotoBarSetup() {
		if (Main.userPassword == null) {
			passwordStatus.setText("");
			passwordModal.setVisible(true);
		} else {
			viewController.setScreen(ViewController.SetupBar, new FadeTransition());
		}
	}

	@FXML
	private void gotoSettings() {
		System.out.println("test");
//		viewController.setScreen(ViewController.Home, new FadeTransition());
	}

	@FXML
	private void confirmPassword() {
		String password = passwordField1.getText();
		String password2 = passwordField2.getText();
		if (!password.equals(password2)) {
			passwordStatus.setText("Passwords do not match");
		} else {
			Main.userPassword = password;
			gotoBarSetup();
		}

	}


}
