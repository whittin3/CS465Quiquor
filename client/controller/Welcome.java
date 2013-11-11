package client.controller;

import client.View;
import client.ViewController;
import client.transitions.FadeTransition;
import javafx.fxml.FXML;

public class Welcome implements View {
    private ViewController viewController;

    @Override
    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }

    @FXML
    private void gotoHome()
    {
        System.out.println("test");
        viewController.setScreen(ViewController.Home, new FadeTransition());
    }
}
