package com.multiapk.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.multiapk.modules.home.HomeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityInstrumentedTest {
    @Rule
    public ActivityTestRule<HomeActivity> homeActivity = new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void init() {
        // Initialize UiDevice instance
        UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

        // Perform a short press on the HOME button
//        mDevice.pressHome();

        // Bring up the default launcher by searching for
        // a UI component that matches the content-description for the launcher button
        UiObject allAppsButton = mDevice.findObject(new UiSelector().description("Apps"));

        // Perform a click on the button to bring up the launcher
        try {
            allAppsButton.clickAndWaitForNewWindow();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        onView(withId(com.multiapk.R.id.cardViewComputerModule)).perform(click());
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.multiapk", appContext.getPackageName());
    }
}