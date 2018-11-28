package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.KeyEventAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.base.IdlingResourceRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.filters.SmallTest;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
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
}
