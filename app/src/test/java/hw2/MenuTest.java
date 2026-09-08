package hw2;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuTest {

    @Test
    void billShowsItemsSubtotalTaxAndTotal() {
        Map<String, BigDecimal> items = new LinkedHashMap<>();
        items.put("Egg Sandwich", Menu.FOOD.get("Egg Sandwich"));
        items.put("Coffee", Menu.DRINKS.get("Coffee"));

        String bill = Menu.formatBill("Your Order", items);

        assertTrue(bill.contains("Egg Sandwich"), bill);
        assertTrue(bill.matches("(?s).*Subtotal\\s+9\\.98.*"), bill);
        assertTrue(bill.matches("(?s).*Tax \\(7%\\)\\s+0\\.70.*"), bill);
        assertTrue(bill.matches("(?s).*\nTotal\\s+10\\.68.*"), bill);
    }

    /** Half-cent tax rounds up. Doubles get this one wrong (0.245 lands just under). */
    @Test
    void halfCentTaxRoundsUp() {
        Map<String, BigDecimal> items = new LinkedHashMap<>();
        items.put("Bagel", new BigDecimal("2.50"));
        items.put("Dollar Item", new BigDecimal("1.00"));

        String bill = Menu.formatBill("Your Order", items);

        assertTrue(bill.matches("(?s).*Tax \\(7%\\)\\s+0\\.25.*"), bill);
        assertTrue(bill.matches("(?s).*\nTotal\\s+3\\.75.*"), bill);
    }

    @Test
    void menuHasEveryItemAtTheAssignedPrice() {
        assertEquals(4, Menu.FOOD.size());
        assertEquals(4, Menu.DRINKS.size());
        assertEquals(new BigDecimal("4.49"), Menu.FOOD.get("Potato Salad"));
        assertEquals(new BigDecimal("2.25"), Menu.DRINKS.get("Orange Juice"));
    }
}
