package br.com.valdecipedroso.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by gemeos_valdeci on 22/12/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule public ActivityTestRule<MainActivity> mMainActivityTest = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void openApp_ShowFailedLoad(){
        onView(withId(R.id.tv_load_failed)).check(matches(withText(R.string.load_failed)));
    }

    @Test
    public void clickGridViewItem_openRecipe(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.title_ingredients)).check(matches(isDisplayed()));
    }
}
