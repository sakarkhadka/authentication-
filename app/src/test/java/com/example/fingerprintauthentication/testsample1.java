//package com.example.fingerprintauthentication;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.filters.LargeTest;
//import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
//import androidx.test.rule.ActivityTestRule;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.junit.Assert.*;
//
//@RunWith(AndroidJUnit4ClassRunner.class)
//class MainActivityTest {
//
//    @Rule
//    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
//
//    @Before
//    public void setUp() throws Exception{
//
//    }
//    @Test
//    public void test_appName(){
//        onView(withText("Fingerprint Authentication")).check(matches(isDisplayed()));
//
//
//    }
//}