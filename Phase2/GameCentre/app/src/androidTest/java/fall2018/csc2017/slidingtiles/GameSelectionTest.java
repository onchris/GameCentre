package fall2018.csc2017.slidingtiles;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.EspressoException;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.Root;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewFinder;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.KeyEventAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.base.DefaultFailureHandler;
import android.support.test.espresso.base.IdlingResourceRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.filters.LargeTest;
import android.support.test.filters.SmallTest;
import android.support.test.internal.runner.listener.InstrumentationRunListener;
import android.support.test.runner.lifecycle.ActivityLifecycleCallback;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.util.Collection;
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
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.isSystemAlertWindow;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
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
import static org.hamcrest.core.IsEqual.equalTo;

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
    /**
     * Custom matcher for comparing VectorDrawable of a button component, useful for debugging correct
     * tile digits being displayed
     * @param targetBackground the background to be matched against
     * @return A valid view matcher for check assertions
     */
    private static Matcher<View> checkButtonForVectorDrawable(final Drawable targetBackground){
        return new BoundedMatcher<View, Button>(Button.class) {
            @Override
            protected boolean matchesSafely(Button item) {
                if(item.getBackground() instanceof VectorDrawable)
                    return true;
                return false;
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
        onView(withId(R.id.button_newgame)).check(matches(isDisplayed())).perform(click());
        onView(withText("3x3")).inRoot(isPlatformPopup()).check(matches(isDisplayed())).perform(click());
        onView(allOf(withText("Load Game"), instanceOf(Button.class))).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(GameActivity.class.getName()));
        onData(withTagValue(is((Object) 1))).atPosition(0)
                .check(matches(checkTargetBackground(testRule.getActivity()
                        .getDrawable(R.drawable.bg_simplebg))))
                .check(matches(checkTargetBackground(testRule.getActivity()
                        .getDrawable(R.drawable.ic_1))));
        onData(withTagValue(is((Object) 9))).atPosition(0)
                .check(matches(checkButtonForVectorDrawable(testRule.getActivity()
                        .getDrawable(R.drawable.bg_simplebg))));
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
        onView(withId(R.id.button_gameselect1)).perform(click());
        onView(withId(R.id.button_slidingreset)).check(matches(isDisplayed())).perform(click());
        onView(withText("Delete all games?")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button1))).perform(click());
        onView(withId(R.id.button_newgame)).check(matches(isDisplayed())).perform(click());
        onView(withText("Other...")).inRoot(isPlatformPopup()).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.text_row)).perform(replaceText("10"));
        onView(withId(R.id.text_column)).perform(replaceText("10"));
        onView(withId(R.id.text_undos)).perform(replaceText("20"));
        onView(withId(R.id.button_confirm_difficulty)).perform(click());
        onView(allOf(withText("Load Game"), instanceOf(Button.class))).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(GameActivity.class.getName()),times(4));
        onData(withTagValue(is((Object) 1))).atPosition(0)
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.bg_simplebg))))
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.ic_1))));
        onData(withTagValue(is((Object) 99))).atPosition(0)
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.bg_simplebg))))
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.ic_9))));
        Espresso.pressBack();
        onView(withId(R.id.button_gameselect1)).perform(click());
        onView(withId(R.id.button_slidingreset)).check(matches(isDisplayed())).perform(click());
        onView(withText("Delete all games?")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button1))).perform(click());
        onView(withId(R.id.button_newgame)).check(matches(isDisplayed())).perform(click());
        onView(withText("Other...")).inRoot(isPlatformPopup()).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.text_row)).perform(replaceText("4"));
        onView(withId(R.id.text_column)).perform(replaceText("4"));
        onView(withId(R.id.text_undos)).perform(replaceText("4"));
        onView(withId(R.id.button_confirm_difficulty)).perform(click());
        onView(allOf(withText("Load Game"), instanceOf(Button.class))).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(GameActivity.class.getName()),times(5));
        onData(withTagValue(is((Object) 1))).atPosition(0)
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.bg_simplebg))))
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.ic_1))));
        onData(withTagValue(is((Object) 15))).atPosition(0)
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.bg_simplebg))))
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.ic_1))))
                .check(matches(checkTargetBackground(testRule.getActivity().getDrawable(R.drawable.ic_5))));
        Espresso.pressBack();
        onView(withId(R.id.button_gameselect1)).perform(click());
        onView(withId(R.id.button_slidingreset)).check(matches(isDisplayed())).perform(click());
        onView(withText("Delete all games?")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button1))).perform(click());
    }

    @Test
    public void test4_dialogExtensive(){
        Intent intent = new Intent();
        intent.putExtra("currentUser", "123");
        testRule.launchActivity(intent);
        onView(withId(R.id.text_loggedas)).check(matches(withText("123")));
        onView(withId(R.id.button_gameselect1)).perform(click());
        onView(withId(R.id.button_slidingreset)).check(matches(isDisplayed())).perform(click());
        onView(withText("Delete all games?")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button1))).perform(click());
        onView(withId(R.id.button_newgame)).check(matches(isDisplayed())).perform(click());
        onView(withText("Other...")).inRoot(isPlatformPopup()).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.text_row)).perform(replaceText(""));
        onView(withId(R.id.text_column)).perform(replaceText(""));
        onView(withId(R.id.button_confirm_difficulty)).perform(click());


        onView(allOf(withText(R.string.d_toast_empty_fields)))
                .inRoot(toastMatch())
                .check(matches(isDisplayed()));
        onView(withId(R.id.text_row)).perform(replaceText("1"));
        onView(withId(R.id.text_column)).perform(replaceText("1"));
        onView(withId(R.id.button_confirm_difficulty)).perform(click());
        onView(withText(R.string.d_toast_let_3))
                .inRoot(toastMatch())
                .check(matches(isDisplayed()));
        onView(withId(R.id.text_row)).perform(replaceText("40"));
        onView(withId(R.id.text_column)).perform(replaceText("40"));
        onView(withId(R.id.button_confirm_difficulty)).perform(click());
        onView(withText(R.string.d_toast_lat_31))
                .inRoot(toastMatch())
                .check(matches(isDisplayed()));
        onView(withId(R.id.text_row)).perform(replaceText("4"));
        onView(withId(R.id.text_column)).perform(replaceText("4"));
        onView(withId(R.id.text_undos)).perform(replaceText(""));
        onView(withId(R.id.button_confirm_difficulty)).perform(click());
        onView(withText("4x4"))
                .inRoot(toastMatch())
                .check(matches(isDisplayed()));
    }

    public Activity getCurrentActivity(){
        final Activity[] currentActivity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(Stage.RESUMED);
                if(!activities.isEmpty())
                    currentActivity[0] = activities.iterator().next();
            }
        });
        return  currentActivity[0];
    }
    public TypeSafeMatcher<Root> toastMatch(){
        return new TypeSafeMatcher<Root>() {
            @Override
            protected boolean matchesSafely(Root item) {
                int type = item.getWindowLayoutParams().get().type;
                if ((type == WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW + 5)) {
                    IBinder windowToken = item.getDecorView().getWindowToken();
                    IBinder appToken = item.getDecorView().getApplicationWindowToken();
                    if (windowToken == appToken) {
                        return true;
                    }
                }
                return false;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("toast matching");
            }
        };
    }

}
