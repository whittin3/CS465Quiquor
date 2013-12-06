package client.controller;

import client.Main;
import client.View;
import client.ViewController;
import client.readOnly.Drink;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Observable;


public class Home implements View {
	private ViewController viewController;

	@FXML
	static ListView<String> drinkListView;

	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	public static void init() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		List<Drink> drinkList = Main.drinkLibrary.getDrinkList();
		ObservableList<String> observableDrinkList = FXCollections.observableArrayList();
		for (Drink drink : drinkList)
		{
			observableDrinkList.add(drink.getName());
		}
		FXCollections.sort(observableDrinkList);
		drinkListView.setItems(observableDrinkList);

//		drinkListView.addEventHandler(new EventType<KeyStroke>());
	}
}
