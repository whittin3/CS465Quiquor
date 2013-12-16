package client;

/**
 * User: Neal Eric
 * Date: 11/10/13
 */

import client.readOnly.Drink;
import client.readOnly.Ingredient;
import data.DataFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * todo @whittin3: Please note that this entire class was written by me a long time ago and I haven't checked to see if it works
 */
public class DrinkLibrary {

	public static final String LAST_BAR_SETUP_TXT = "LastBarSetup.txt";
	public HashMap<String, Ingredient> ingredients = new HashMap<>();
	public HashMap<String, Drink> drinks = new HashMap<>();

	public void addDrink(Drink drink) {
		drinks.put(drink.getName(), drink);
	}

	public void addDrink(String name, HashMap<Ingredient, Double> ingredientMap) {
		Drink newDrink = new Drink(name, ingredientMap, Math.random() * 100);
		addDrink(newDrink);
	}

	public ObservableList<String> parseLastBarSetup(String path) throws URISyntaxException, FileNotFoundException {
		File file = new File(path);
		ObservableList<String> ingredientPumpList = FXCollections.observableArrayList();
		if (file.exists()) {
			try (Scanner scanner = new Scanner(file).useDelimiter("[\\n]")) {
				while (scanner.hasNext()) {
					ingredientPumpList.add(scanner.next().replaceAll("\\r", ""));
				}
			}
		}
		return ingredientPumpList;
	}

	public static DrinkLibrary parseDrinkFile() throws FileNotFoundException, URISyntaxException {
		// I removed the csv file for execution purposes.
		DrinkLibrary drinkLibrary = new DrinkLibrary();
		try (Scanner scanner = new Scanner(DataFile.getData()).useDelimiter("[\\n]+")) {
			boolean errorFlag = false;
			while (scanner.hasNext()) {
				String line = scanner.next();
				Scanner linescanner = new Scanner(line.replace("\r", ",")).useDelimiter("\\s*,\\s*");
				String DrinkName = linescanner.next();
				HashMap<Ingredient, Double> ingredientMap = new HashMap<>();
				while (linescanner.hasNext()) {
					String ingredientName = linescanner.next();
					if (ingredientName.equals("")) {
						break;
					}
					String volume = linescanner.next();
					if (volume.equals("")) {
						System.out.println("IGNORING DRINK");
						System.out.println("Please enter a volume for " + ingredientName + " in " + DrinkName);
						errorFlag = true;
						break;
					}

					Ingredient ingredient = drinkLibrary.addOrFindIngredient(ingredientName, 1, false);
					ingredientMap.put(ingredient, Double.valueOf(volume));
				}
				if (!errorFlag && !ingredientMap.isEmpty()) {
					drinkLibrary.addDrink(DrinkName, ingredientMap);
				} else {
					if (ingredientMap.isEmpty()) {
						System.out.println("IGNORING DRINK");
						System.out.println("Please enter ingredients for " + DrinkName);
					}
					errorFlag = false;
				}
			}
		}
		return drinkLibrary;
	}

	private Ingredient addOrFindIngredient(String name, double density, boolean carbonated) {
		if (!ingredients.containsKey(name)) {
			Ingredient ingredient = new Ingredient(name, density, carbonated);
			ingredients.put(name, ingredient);
		}

		return ingredients.get(name);
	}

	public Drink getDrink(String drinkName) {
		return drinks.get(drinkName);
	}

	public ObservableList<String> getDrinkList() {
		ObservableList<String> drinkNames = FXCollections.observableArrayList(drinks.keySet());
		FXCollections.sort(drinkNames);
		return drinkNames;
	}

	public Ingredient getIngredient(String ingredient) {
		return ingredients.get(ingredient);
	}

	public HashMap<String, Ingredient> getIngredients() {
		return ingredients;
	}
}
