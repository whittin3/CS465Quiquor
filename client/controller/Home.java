package client.controller;

import client.View;
import client.ViewController;

public class Home implements View {
	private ViewController viewController;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}
}
