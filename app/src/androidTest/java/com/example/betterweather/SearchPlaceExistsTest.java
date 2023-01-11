package com.example.betterweather;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
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
public class SearchPlaceExistsTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void searchPlaceExistsTest() {
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
        appCompatEditText4.perform(scrollTo(), replaceText("Madrid"), closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Madrid"),
                        withParent(withParent(withId(R.id.panelBusqueda))),
                        isDisplayed()));
        editText.check(matches(withText("Madrid")));

        ViewInteraction scrollView = onView(
                allOf(withId(R.id.panelDatosGenerales),
                        withParent(allOf(withId(R.id.main_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        scrollView.check(matches(isDisplayed()));

        ViewInteraction horizontalScrollView = onView(
                allOf(withId(R.id.panelDatosSemana),
                        withParent(allOf(withId(R.id.main_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        horizontalScrollView.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Madrid"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText5.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Madrid"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText6.perform(scrollTo(), replaceText("Oviedo"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Oviedo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText7.perform(closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Oviedo"),
                        withParent(withParent(withId(R.id.panelBusqueda))),
                        isDisplayed()));
        editText2.check(matches(withText("Oviedo")));

        ViewInteraction scrollView2 = onView(
                allOf(withId(R.id.panelDatosGenerales),
                        withParent(allOf(withId(R.id.main_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        scrollView2.check(matches(isDisplayed()));

        ViewInteraction horizontalScrollView2 = onView(
                allOf(withId(R.id.panelDatosSemana),
                        withParent(allOf(withId(R.id.main_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        horizontalScrollView2.check(matches(isDisplayed()));
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
