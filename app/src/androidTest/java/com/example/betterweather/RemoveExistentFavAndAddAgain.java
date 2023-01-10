package com.example.betterweather;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
public class RemoveExistentFavAndAddAgain {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void removeExistentFavAndAddAgain() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.addFav), withContentDescription("A�adir el lugar a favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                2)));
        appCompatImageButton.perform(scrollTo(), click());

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
        appCompatEditText4.perform(scrollTo(), replaceText("langreo"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.addFav), withContentDescription("A�adir el lugar a favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                2)));
        appCompatImageButton2.perform(scrollTo(), click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.favourites), withContentDescription("Favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.reciclerView),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(3, click()));
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
