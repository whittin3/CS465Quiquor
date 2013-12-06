package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.transitions.FadeTransition;
import javafx.fxml.FXML;
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


	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@FXML
	private void gotoHome() {
		if (Main.userPassword == null)
		{
			passwordStatus.setText("");
			passwordModal.setVisible(true);
		}
		else
		{
			viewController.setScreen(ViewController.Home, new FadeTransition());
			Home.init();
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
		if (!password.equals(password2))
		{
			passwordStatus.setText("Passwords do not match");
		}
		else
		{
			Main.userPassword = password;
			passwordStatus.setText("Success");
			gotoHome();
		}

	}


}
