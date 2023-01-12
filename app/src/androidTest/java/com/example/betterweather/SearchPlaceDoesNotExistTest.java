package com.example.betterweather;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.betterweather.TestUtil.waitFor;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchPlaceDoesNotExistTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void searchPlaceDoesNotExistTest() {
        onView(isRoot()).perform(waitFor(5000));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Langreo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText(""));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextPlaceSearch),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextPlaceSearch),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText3.perform(scrollTo(), click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextPlaceSearch),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText4.perform(scrollTo(), replaceText("Uniovi"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Uniovi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText5.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Uniovi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText6.perform(scrollTo(), click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Uniovi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText7.perform(scrollTo(), replaceText("Inform�tica "));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Inform�tica "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Inform�tica "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText9.perform(pressImeActionButton());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
