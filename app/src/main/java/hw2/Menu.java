package hw2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Menu prices and bill formatting for Joe's Deli.
 *
 * <p>This class contains no JavaFX code, so the pricing logic can be unit tested without
 * starting the user interface.
 */
public final class Menu {

    /** Sales tax applied to every order. */
    private static final BigDecimal TAX_RATE = new BigDecimal("0.07");

    /** Food items in menu order, each mapped to its price. */
    public static final Map<String, BigDecimal> FOOD = menu(
        "Egg Sandwich", "7.99",
        "Chicken Sandwich", "9.99",
        "Bagel", "2.50",
        "Potato Salad", "4.49");

    /** Drink items in menu order, each mapped to its price. */
    public static final Map<String, BigDecimal> DRINKS = menu(
        "Black Tea", "1.25",
        "Green Tea", "0.99",
        "Coffee", "1.99",
        "Orange Juice", "2.25");

    private Menu() {
    }

    /**
     * Builds an unmodifiable menu from alternating name and price arguments. Prices are parsed
     * from strings rather than doubles so that amounts remain exact to the cent.
     *
     * @param nameThenPrice item name followed by its price, repeated for each item
     * @return the items, in the order given
     * @throws IllegalArgumentException if an item name has no matching price
     */
    private static Map<String, BigDecimal> menu(String... nameThenPrice) {
        if (nameThenPrice.length % 2 != 0) {
            throw new IllegalArgumentException("Every item name needs a price");
        }
        Map<String, BigDecimal> items = new LinkedHashMap<>();
        for (int i = 0; i < nameThenPrice.length; i += 2) {
            items.put(nameThenPrice[i], new BigDecimal(nameThenPrice[i + 1]));
        }
        return Collections.unmodifiableMap(items);
    }

    /**
     * Formats a receipt listing each ordered item with its price, followed by the subtotal,
     * sales tax and total.
     *
     * @param header title shown at the top of the receipt
     * @param items  ordered items, each mapped to its price
     * @return the receipt text
     */
    public static String formatBill(String header, Map<String, BigDecimal> items) {
        StringBuilder receipt = new StringBuilder(header).append("\n\n");
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> item : items.entrySet()) {
            subtotal = subtotal.add(item.getValue());
            receipt.append(line(item.getKey(), item.getValue()));
        }
        // Tax is rounded to whole cents before it is added, the way a register charges it.
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        receipt.append(String.format("%n"));
        receipt.append(line("Subtotal", subtotal));
        receipt.append(line("Tax (7%)", tax));
        receipt.append(line("Total", subtotal.add(tax)));
        return receipt.toString();
    }

    /** Formats one receipt line as a label and a right-aligned amount. */
    private static String line(String label, BigDecimal amount) {
        return String.format("%-18s %6s%n", label, amount.setScale(2, RoundingMode.HALF_UP).toPlainString());
    }
}
