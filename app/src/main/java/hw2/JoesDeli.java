package hw2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Breakfast ordering window for Joe's Deli.
 *
 * <p>The customer selects any number of food items and at most one drink. <em>Order</em> shows
 * the bill, <em>Cancel</em> clears the selections and the bill and <em>Confirm</em> clears the
 * selections while leaving the final bill on screen.
 *
 * <p>The layout is built in code; Prices and bill formatting are handled by {@link Menu}.
 */
public class JoesDeli extends Application {

    private final List<CheckBox> foodChoices = new ArrayList<>();
    private final ToggleGroup drinkChoices = new ToggleGroup();
    private final TextArea bill = new TextArea();

    /** Creates the application; JavaFX builds the window in {@link #start(Stage)}. */
    public JoesDeli() {
        // Nothing to set up here: the controls are created and laid out in start().
    }

    /**
     * Builds the window and shows it.
     *
     * @param stage the primary stage supplied by JavaFX
     */
    @Override
    public void start(Stage stage) {
        Label title = new Label("Joe's Deli");
        title.setFont(Font.font(24));

        VBox eatBox = new VBox(12, new Label("Eat:"));
        for (String name : Menu.FOOD.keySet()) {
            CheckBox food = new CheckBox(name);
            foodChoices.add(food);
            eatBox.getChildren().add(food);
        }

        VBox drinkBox = new VBox(12, new Label("Drink:"));
        for (String name : Menu.DRINKS.keySet()) {
            RadioButton drink = new RadioButton(name);
            drink.setToggleGroup(drinkChoices);
            drinkBox.getChildren().add(drink);
        }

        bill.setEditable(false);
        bill.setPrefColumnCount(22);
        bill.setPrefRowCount(12);
        VBox billBox = new VBox(6, new Label("Bill"), bill);

        Button order = new Button("Order");
        Button cancel = new Button("Cancel");
        Button confirm = new Button("Confirm");

        order.setOnAction(event -> bill.setText(billText("Your Order")));
        cancel.setOnAction(event -> {
            clearSelections();
            bill.clear();
        });
        confirm.setOnAction(event -> {
            String finalBill = billText("Order Confirmed - Thank You!");
            clearSelections();
            bill.setText(finalBill);
        });

        HBox buttons = new HBox(60, order, cancel, confirm);
        buttons.setAlignment(Pos.CENTER);

        HBox columns = new HBox(50, eatBox, drinkBox, billBox);
        columns.setAlignment(Pos.CENTER);

        VBox root = new VBox(25, title, columns, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        stage.setTitle("Joe's Deli");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Collects the current selections: every checked food item plus the selected drink, if any.
     *
     * @return the selected items in menu order, each mapped to its price
     */
    private Map<String, BigDecimal> selectedItems() {
        Map<String, BigDecimal> items = new LinkedHashMap<>();
        for (CheckBox food : foodChoices) {
            if (food.isSelected()) {
                items.put(food.getText(), Menu.FOOD.get(food.getText()));
            }
        }
        Labeled drink = (Labeled) drinkChoices.getSelectedToggle();
        if (drink != null) {
            items.put(drink.getText(), Menu.DRINKS.get(drink.getText()));
        }
        return items;
    }

    /**
     * Formats the bill for the current selections.
     *
     * @param header title shown at the top of the bill
     * @return the bill text, or a prompt if nothing is selected
     */
    private String billText(String header) {
        Map<String, BigDecimal> items = selectedItems();
        return items.isEmpty() ? "Please select at least one item." : Menu.formatBill(header, items);
    }

    /** Clears every food checkbox and the drink selection. */
    private void clearSelections() {
        for (CheckBox food : foodChoices) {
            food.setSelected(false);
        }
        drinkChoices.selectToggle(null);
    }

    /**
     * Launches the application.
     *
     * @param args command line arguments, passed through to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}
