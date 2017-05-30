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
 * Created by Jasmine D on 5/30/2017.
 */

public class InventoryItemTest {


    @Test
    public void testGetColor_RedExpireColor() {

        Date todaysDate = new Date();
        DateFormat todaysDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDateString = todaysDateFormat.format(todaysDate);

        InventoryItem item3 = new InventoryItem("4", "Ice Cream", "1", "5.25", todaysDateString);

        assertEquals(Color.RED, item3.getColor());
    }

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

    @Test
    public void nullItem() {
        InventoryItem item5 = new InventoryItem("5", "Cherries", "1", "7.25", "");
    }

}
