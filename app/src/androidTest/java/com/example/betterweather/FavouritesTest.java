package com.example.betterweather;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.betterweather.TestUtil.waitFor;
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
public class FavouritesTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void favouritesTest() {
        onView(isRoot()).perform(waitFor(8000));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.addFav), withContentDescription("Add el lugar a favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                2)));
        appCompatImageButton.perform(scrollTo(), click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.favourites), withContentDescription("Favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.home), withContentDescription("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Langreo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("Madrid"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Madrid"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Madrid"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText3.perform(scrollTo(), click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Madrid"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText4.perform(scrollTo(), click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("Madrid"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                0)));
        appCompatEditText5.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.addFav), withContentDescription("Add el lugar a favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                2)));
        appCompatImageButton2.perform(scrollTo(), click());

        ViewInteraction bottomNavigationItemView3 = onView(
                allOf(withId(R.id.favourites), withContentDescription("Favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView3.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.reciclerView),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction editText = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("langreo"),
                        withParent(withParent(withId(R.id.panelBusqueda))),
                        isDisplayed()));
        editText.check(matches(withText("langreo")));

        ViewInteraction bottomNavigationItemView4 = onView(
                allOf(withId(R.id.favourites), withContentDescription("Favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView4.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.reciclerView),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("madrid"),
                        withParent(withParent(withId(R.id.panelBusqueda))),
                        isDisplayed()));
        editText2.check(matches(withText("madrid")));

        ViewInteraction bottomNavigationItemView5 = onView(
                allOf(withId(R.id.favourites), withContentDescription("Favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView5.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.botonBorrar),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.reciclerView),
                                        1),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction bottomNavigationItemView6 = onView(
                allOf(withId(R.id.home), withContentDescription("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView6.perform(click());

        ViewInteraction bottomNavigationItemView7 = onView(
                allOf(withId(R.id.favourites), withContentDescription("Favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView7.perform(click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.reciclerView),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView3.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.addFav), withContentDescription("Add el lugar a favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.panelBusqueda),
                                        0),
                                2)));
        appCompatImageButton3.perform(scrollTo(), click());

        ViewInteraction bottomNavigationItemView8 = onView(
                allOf(withId(R.id.favourites), withContentDescription("Favoritos"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView8.perform(click());

        ViewInteraction bottomNavigationItemView9 = onView(
                allOf(withId(R.id.home), withContentDescription("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_menu),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView9.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.editTextPlaceSearch), withText("langreo"),
                        withParent(withParent(withId(R.id.panelBusqueda))),
                        isDisplayed()));
        editText3.check(matches(withText("langreo")));
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
