package com.example.fingerprintauthentication;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

//    @Before
//    public void setUp() throws Exception{
//
//    }

    @Test
    public void test_appName(){
        onView(withText("Authentication")).check(matches(isDisplayed()));
    }

    @Test
    public void test_prompt(){
        onView(withText("Please enable lockscreen security in your device's Settings")).check(matches(isDisplayed()));
    }

    @Test
    public void test_button() {

        onView(isClickable());
    }


    public void test_Displayed(){

        onView(isDisplayed());
    }


}