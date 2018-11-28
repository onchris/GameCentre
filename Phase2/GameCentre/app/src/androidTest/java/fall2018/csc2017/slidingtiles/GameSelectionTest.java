package fall2018.csc2017.slidingtiles;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.KeyEventAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.base.IdlingResourceRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.filters.SmallTest;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fall2018.csc2017.slidingtiles.ObstacleDodger.GamePanel;
import fall2018.csc2017.slidingtiles.ObstacleDodger.ObGameActivity;
import fall2018.csc2017.slidingtiles.UltimateTTT.UltimateTTTGameActivity;

import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.Espresso.getIdlingResources;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.isSystemAlertWindow;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(JUnit4.class)
@SmallTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameSelectionTest {
    @Rule
    public IntentsTestRule<GameSelection> testRule = new IntentsTestRule<GameSelection>(GameSelection.class, false , false){
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
        }
    };
    /**
     * Custom matcher for comparing drawables of a view component, useful for debugging correct
     * tile digits being displayed
     * @param targetBackground the background to be matched against
     * @return A valid view matcher for check assertions
     */
    private static Matcher<View> checkTargetBackground(final Drawable targetBackground){
        return new BoundedMatcher<View, Button>(Button.class) {
            @Override
            protected boolean matchesSafely(Button item) {
                LayerDrawable convertedLayers = (LayerDrawable) item.getBackground();
                boolean matching = false;
                Bitmap targetBitmap = getBitmap(targetBackground);
                for(int layer = 0; layer < convertedLayers.getNumberOfLayers(); layer++){
                    if(convertedLayers.getDrawable(layer).getClass() == targetBackground.getClass()) {
                        Bitmap comparisonBitmap = getBitmap(convertedLayers.getDrawable(layer));
                        matching = comparisonBitmap.sameAs(targetBitmap);
                        if(matching)
                            return true;
                    }
                }
                return matching;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Should match specific drawable background")
                        .appendText(targetBackground.toString());
            }

            private Bitmap getBitmap(Drawable drawable) {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        };
    }
    @Test
    public void test1_testSelectionGames() throws Throwable {
        Intent intent = new Intent();
        intent.putExtra("currentUser", "123");
        testRule.launchActivity(intent);
        onView(withId(R.id.text_loggedas)).check(matches(withText("123")));
        onView(withId(R.id.button_gameselect1)).perform(click());
        onView(withId(R.id.scrollable_loadablegames)).check(matches(isDisplayed()));
        Espresso.pressBack();
        onView(withId(R.id.text_loggedas)).check(matches(withText("123")));
        onView(withId(R.id.button_gameselect2)).perform(click());
        onView(withId(R.id.scrollable_loadablegames)).check(matches(isDisplayed()));
        Espresso.pressBack();
        onView(withId(R.id.text_loggedas)).check(matches(withText("123")));
        onView(withId(R.id.button_gameselect3)).perform(click());
        intended(hasComponent(ObGameActivity.class.getName()));
        onView(is(Matchers.<View>instanceOf(GamePanel.class))).check(matches(isDisplayed()));
        Thread.sleep(10000);
        onView(withId(R.id.button_game_selection)).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(GameSelection.class.getName()));
        onView(withId(R.id.button_gameselection_scoreboard)).perform(click());
        intended(hasComponent(GeneralScoreboard.class.getName()));
        onView(allOf(withId(R.id.fragment_layout), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withText("Sliding Tiles"), instanceOf(TextView.class)))
                .check(matches(isDisplayed()))
                .perform(swipeLeft());
        onView(allOf(withText("Obstacle Dodger"), instanceOf(TextView.class)))
                .check(matches(isDisplayed()));
        Espresso.pressBack();
        onView(withId(R.id.button_gameselect3)).perform(click());
        intended(hasComponent(ObGameActivity.class.getName()),times(2));
        Thread.sleep(10000);
        onView(withId(R.id.button_new_game)).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(ObGameActivity.class.getName()),times(3));
    }
    @Test
    public void test2_testGuestSelectionGames() throws InterruptedException {
        Intent intent = new Intent();
        intent.putExtra("currentUser", "-1");
        testRule.launchActivity(intent);
        onView(withId(R.id.text_loggedas)).check(matches(withText("Guest")));
        onView(withId(R.id.button_gameselection_scoreboard)).perform(click());
        intended(hasComponent(GeneralScoreboard.class.getName()));
        onView(allOf(withId(R.id.fragment_layout), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withText("Sliding Tiles"), instanceOf(TextView.class)))
                .check(matches(isDisplayed()))
                .perform(swipeLeft());
        onView(allOf(withText("Obstacle Dodger"), instanceOf(TextView.class)))
                .check(matches(isDisplayed()));
        Espresso.pressBack();
        onView(withId(R.id.text_loggedas)).check(matches(withText("Guest")));
        onView(withId(R.id.button_gameselect1)).perform(click());
        intended(hasComponent(GameActivity.class.getName()));
        Espresso.pressBack();
        onView(withId(R.id.text_loggedas)).check(matches(withText("Guest")));
        onView(withId(R.id.button_gameselect2)).perform(click());
        intended(hasComponent(UltimateTTTGameActivity.class.getName()));
        Espresso.pressBack();
        onView(withId(R.id.text_loggedas)).check(matches(withText("Guest")));
        onView(withId(R.id.button_gameselect3)).perform(click());
        intended(hasComponent(ObGameActivity.class.getName()));
        Thread.sleep(10000);
        onView(withId(R.id.button_game_selection)).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(GameSelection.class.getName()), times(4));
        onView(withId(R.id.button_gameselect3)).perform(click());
        intended(hasComponent(ObGameActivity.class.getName()),times(2));
        Thread.sleep(10000);
        onView(withId(R.id.button_new_game)).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(ObGameActivity.class.getName()),times(3));
    }
    @Test
    public void test3_testSlidingTileCreation(){
        Intent intent = new Intent();
        intent.putExtra("currentUser", "123");
        testRule.launchActivity(intent);
        onView(withId(R.id.text_loggedas)).check(matches(withText("123")));
        onView(withId(R.id.button_gameselect1)).perform(click());
        onView(withId(R.id.scrollable_loadablegames)).check(matches(isDisplayed()));
        onView(withId(R.id.button_slidingreset)).check(matches(isDisplayed())).perform(click());
        onView(withText("Delete all games?")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button1))).perform(click());
        onView(withId(R.id.button_slidingreset)).check(matches(isDisplayed())).perform(click());
        onView(withText("Delete all games?")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button1))).perform(click());

        //InstrumentationRegistry.getInstrumentation().getUiAutomation().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        //onView(withText("YES")).inRoot(isSystemAlertWindow()).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.button_newgame)).check(matches(isDisplayed())).perform(click());
        onView(withText("3x3")).inRoot(isPlatformPopup()).check(matches(isDisplayed())).perform(click());
        onView(allOf(withText("Load Game"), instanceOf(Button.class))).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(GameActivity.class.getName()));
        onData(withTagValue(is((Object) 1))).atPosition(0)
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.bg_simplebg))))
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.ic_1))));
        Espresso.pressBack();
        onView(withId(R.id.button_gameselect1)).perform(click());
        onView(withId(R.id.button_slidingreset)).check(matches(isDisplayed())).perform(click());
        onView(withText("Delete all games?")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button1))).perform(click());
        onView(withId(R.id.button_newgame)).check(matches(isDisplayed())).perform(click());
        onView(withText("4x4")).inRoot(isPlatformPopup()).check(matches(isDisplayed())).perform(click());
        onView(allOf(withText("Load Game"), instanceOf(Button.class))).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(GameActivity.class.getName()),times(2));
        onData(withTagValue(is((Object) 1))).atPosition(0)
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.bg_simplebg))))
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.ic_1))));
        Espresso.pressBack();
        onView(withId(R.id.button_gameselect1)).perform(click());
        onView(withId(R.id.button_slidingreset)).check(matches(isDisplayed())).perform(click());
        onView(withText("Delete all games?")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button1))).perform(click());
        onView(withId(R.id.button_newgame)).check(matches(isDisplayed())).perform(click());
        onView(withText("5x5")).inRoot(isPlatformPopup()).check(matches(isDisplayed())).perform(click());
        onView(allOf(withText("Load Game"), instanceOf(Button.class))).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(GameActivity.class.getName()),times(3));
        onData(withTagValue(is((Object) 1))).atPosition(0)
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.bg_simplebg))))
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.ic_1))));
        Espresso.pressBack();


        //Other...
        //onData(instanceOf(String.class)).inAdapterView(withId(R.id.scrollable_loadablegames)).atPosition(0).perform(click());
        // onData(hasToString(startsWith("Game 0"))).usingAdapterViewProtocol(new AdapterVi).check(matches(isDisplayed()));
        //onView(withTagValue(is((Object) LoaderAdapter.class))).check(matches(isDisplayed()));
        //onData(instanceOf(AdapterView.class)).check(matches(isDisplayed()));

    }
}
