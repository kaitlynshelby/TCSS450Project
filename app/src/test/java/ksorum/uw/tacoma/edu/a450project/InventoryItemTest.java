package ksorum.uw.tacoma.edu.a450project;

import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;

import static junit.framework.Assert.assertEquals;

/**
 * JUnit testing for the InventoryItem class.
 *
 * @author Jasmine Dacones
 */
public class InventoryItemTest {

    /**
     * Tests inventory item creation.
     */
    @Test
    public void testConstructor() {
        InventoryItem item4 = new InventoryItem("6", "Soda", "20", "11.00", "2017-07-01");
        assertEquals("Soda", item4.getItemName());
        assertEquals("6", item4.getId());
        assertEquals("20", item4.getQuantity());
        assertEquals("11.00", item4.getPrice());
        assertEquals("2017-07-01", item4.getExpiration());
    }

    /**
     * Tests for an empty item name field.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBadConstructor_InvalidItemName() {
        new InventoryItem("7", "", "2", "11.00", "2017-07-01");
    }

    /**
     * Tests for a negative quantity field.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBadConstructor_InvalidQuantity_QuantityLessThanZero() {
        new InventoryItem("8", "Crepes", "-1", "11.25", "2017-07-04");
    }

    /**
     * Tests for an empty quantity field..
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBadConstructor_InvalidQuantity_EmptyField() {
        new InventoryItem("9", "Cheese", "", "1.50", "2017-06-25");
    }

    /**
     * Tests for an empty price field.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testBadConstructor_InvalidPrice_EmptyField() {
        new InventoryItem("10", "Watermelon", "3", "", "2017-06-25");
    }



    /**
     * Tests the list row color for an item that is already expired.
     */
    @Test
    public void testGetColor_RedExpireColor() {

        Date todaysDate = new Date();
        DateFormat todaysDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDateString = todaysDateFormat.format(todaysDate);

        InventoryItem item3 = new InventoryItem("4", "Ice Cream", "1", "5.25", todaysDateString);

        assertEquals(Color.RED, item3.getColor());
    }

    /**
     * Tests the list row color for an item that within 2-3 days of its expiration date.
     */
    @Test
    public void testGetColor_YellowExpireColor() {

        Date todaysDate = new Date();
        DateFormat todaysDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.setTime(todaysDate);
        c.add(Calendar.DATE, 2);

        Date newDate = c.getTime();

        String twoDaysFromNow = todaysDateFormat.format(newDate);

        InventoryItem item3 = new InventoryItem("5", "Cherries", "1", "7.25", twoDaysFromNow);

        assertEquals(Color.YELLOW, item3.getColor());

    }

    /**
     * Tests the list row color for an item that is more than 3 days away from its expiration date.
     */
    @Test
    public void testGetColor_TransparentExpireColor() {

        Date todaysDate = new Date();
        DateFormat todaysDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.setTime(todaysDate);
        c.add(Calendar.DATE, 4);

        Date newDate = c.getTime();

        String fourDaysFromNow = todaysDateFormat.format(newDate);

        InventoryItem item4 = new InventoryItem("6", "Soda", "12", "10.00", fourDaysFromNow);

        assertEquals(Color.TRANSPARENT, item4.getColor());

    }



}
