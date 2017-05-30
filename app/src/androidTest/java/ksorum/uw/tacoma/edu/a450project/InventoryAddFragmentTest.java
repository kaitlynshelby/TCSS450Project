package ksorum.uw.tacoma.edu.a450project;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ksorum.uw.tacoma.edu.a450project.home.HomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Jasmine D on 5/29/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class InventoryAddFragmentTest {

    public ArrayList<String> nItemNames = new ArrayList<String>();

    public List<String> mPrices = new ArrayList<String>();

    public List<String> mExpire = new ArrayList<String>();

    Random random = new Random();


    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test.
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);

    @Before
    public void setUp() {
        nItemNames.add("Avocado");
        nItemNames.add("Salmon");
        nItemNames.add("Ice Cream");
        nItemNames.add("Cherries");
        nItemNames.add("Fish Sticks");

        mPrices.add("3.25");
        mPrices.add("4.50");
        mPrices.add("2.25");
        mPrices.add("7.90");
        mPrices.add("13.50");

        mExpire.add("2017-06-09");
        mExpire.add("2017-06-15");
        mExpire.add("2017-07-11");
        mExpire.add("2017-07-01");
        mExpire.add("2017-06-29");

        onView(withId(R.id.button_login))
                .perform(click());

        onView(withId(R.id.email_login))
                .perform(click())
                .perform(typeText("admin@uw.edu"));

        onView(withId(R.id.pwd_login))
                .perform(click())
                .perform(typeText("Testing1!"));


        onView(withId(R.id.login_button))
                .perform(click());
    }


    @Test
    public void testAddItem() {
        onView(withId(R.id.fab))
                .perform(click());

        onView(withId(R.id.add_item_name))
                .perform(typeText(nItemNames.get(random.nextInt(4))));

        onView(withId(R.id.add_item_quantity))
                .perform(typeText(Integer.toString(random.nextInt(9) + 1)));

        onView(withId(R.id.add_item_price))
                .perform(typeText(mPrices.get(random.nextInt(4))));

        onView(withId(R.id.add_item_expiration))
                .perform(typeText(mExpire.get(random.nextInt(4))));

        onView(withId(R.id.add_item_button))
                .perform(click());

    }
}
