package client.controller;

import client.View;
import client.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * User: Neal Eric
 * Date: 12/6/13
 */
public class SetupBar implements View {
	private ViewController viewController;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@Override
	public void init() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@FXML
	public void addPump()
	{

	}

	@FXML
	public void removePump()
	{

	}

	@FXML
	public void gotoHome()
	{

	}
}
