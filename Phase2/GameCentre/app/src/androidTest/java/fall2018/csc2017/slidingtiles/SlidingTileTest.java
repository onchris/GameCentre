package fall2018.csc2017.slidingtiles;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.CursorMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.filters.MediumTest;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static android.content.Context.MODE_PRIVATE;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static fall2018.csc2017.slidingtiles.UtilityManager.ACCOUNTS_FILENAME;
import static fall2018.csc2017.slidingtiles.UtilityManager.makeCustomToastText;
import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardManagerToFile;
import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardsToAccounts;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
@SmallTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SlidingTileTest {
    /**
     * BoardManager that sets up a nearly solved 4x4 board
     */
    private BoardManager board4x4 = BoardSetup.setUp4x4NearSolved();
    /**
     * The activity that each test cycle will store
     */
    private Activity activity;
    /**
     * Overriding certain lifecycle of IntentsTestRule for relaying data that requires previous
     * intent to pass in.
     */
    @Rule
    public IntentsTestRule<GameActivity> testRule = new IntentsTestRule<GameActivity>(GameActivity.class){
        /**
         * Ensure that the board is 1 move away from solving by swapping the last tile.
         * Also saves board manager to a isolated file system for scoreboard retrieval.
         */
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            saveBoardManagerToFile(UtilityManager.TEMP_SAVE_FILENAME,
                    board4x4, InstrumentationRegistry.getTargetContext());
        }

        /**
         * Overriding the intent creation cycle
         * @return Intent that has all the data from required previous intent
         */
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            List<BoardManager> boardManagerList = new ArrayList<>();
            boardManagerList.add(board4x4);

            intent.putExtra("account", new Account("123","123"));
            intent.putExtra("boardIndex", 0);
            intent.putExtra("boardList", (ArrayList)boardManagerList);

            return intent;
        }
        /**
         * Initializes activity after activity has launched
         */
        @Override
        protected void afterActivityLaunched() {
            activity = getActivity();
            super.afterActivityLaunched();

        }
    };

    /**
     * Custom ViewAction for waiting on a certain view component, primarily used for debugging
     * the view component.
     * @param viewId the view id of a view widget/component displayed on the screen
     * @param seconds milliseconds of delay to be performed
     * @return ViewAction that is performable by a runner
     */
    private static ViewAction waitView(final int viewId, final long seconds)
    {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "waiting";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + seconds;
                final Matcher<View> viewMatcher = withId(viewId);
                do {
                    for (View childView : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (viewMatcher.matches(childView)) {
                            return;
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

            }
        };
    }

    /**
     * Wait until certain text field ends with an input targetString
     * @param viewId the view to be matched against
     * @param targetString the suffix which the string should end it
     * @param timeoutThreshold the threshold that this ViewAction will run for
     * @return ViewAction that is performable
     */
    private static ViewAction waitUntil(final int viewId,
                                        final String targetString,
                                        final long timeoutThreshold){
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return instanceOf(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Waiting until string matches target string: " + targetString;
            }

            @Override
            public void perform(UiController uiController, View view) {
                boolean reached = false, foundView = false;
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + timeoutThreshold;
                final Matcher<View> viewMatcher = withId(viewId);
                TextView textfield = null;
                do {
                    if(!foundView) {
                        for (View childView : TreeIterables.breadthFirstViewTraversal(view)) {
                            if (viewMatcher.matches(childView)) {
                                foundView = true;
                                textfield = (TextView) childView;
                            }
                        }
                    }
                    else{
                        if(System.currentTimeMillis() >= endTime) {
                            throw new PerformException.Builder()
                                    .withActionDescription(this.getDescription())
                                    .withViewDescription(HumanReadables.describe(view))
                                    .withCause(new TimeoutException())
                                    .build();
                        }
                        else if(textfield != null) {
                            if(textfield.getText().toString().endsWith(targetString))
                                reached = true;
                            uiController.loopMainThreadForAtLeast(500);
                        }
                    }
                }
                while(!reached && foundView);
            }
        };
    }


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
     * General test for solving, moving, displaying, switching intent to scoreboard.
     */
    @Test
    public void test1_setupBoardUnsolved() {
        List<Account> accountsList = new ArrayList<>();
        accountsList.add(new Account("123","123"));
        try{
            ObjectOutputStream outputStream =
                    new ObjectOutputStream(activity.openFileOutput(ACCOUNTS_FILENAME, MODE_PRIVATE));
            outputStream.writeObject(accountsList);
            outputStream.close();
        } catch (IOException e){
        }
        onView(withId(R.id.grid)).check(matches(isDisplayed())).check(matches(hasChildCount(16)));
        onData(withTagValue(is((Object) 1))).atPosition(0)
                .check(matches(checkTargetBackground(activity.getDrawable(R.drawable.ic_1))));
        onData(withTagValue(is((Object) 2))).check(matches(not(checkTargetBackground(activity.getDrawable(R.drawable.ic_1)))));
        onData(instanceOf(Button.class)).inAdapterView(withId(R.id.grid)).atPosition(0).perform(click());
        onView(withText(R.string.mc_invalid_tap))
                .inRoot(withDecorView(not(is(testRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.SaveButton)).perform(click());
        onView(withText(R.string.ga_manual_save))
                .inRoot(withDecorView(not(is(testRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onData(instanceOf(Button.class)).inAdapterView(withId(R.id.grid)).atPosition(10).perform(click());
        onView(isRoot()).perform(waitView(R.id.text_undos, 500));
        onData(instanceOf(Button.class)).inAdapterView(withId(R.id.grid)).atPosition(6).perform(click());
        onView(isRoot()).perform(waitView(R.id.text_undos, 500));
        onData(instanceOf(Button.class)).inAdapterView(withId(R.id.grid)).atPosition(2).perform(click());
        onView(isRoot()).perform(waitView(R.id.text_undos, 500));
        ViewInteraction undoButton = onView(allOf(instanceOf(Button.class), withId(R.id.UndoButton)));
        undoButton.perform(click()).perform(click()).perform(click()).perform(click());
        onView(withText(R.string.ga_cannot_undo))
                .inRoot(withDecorView(not(is(testRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.chronometer)).perform(waitUntil(R.id.chronometer, "10", 15000));
        onView(withText(R.string.ga_auto_saved))
                .inRoot(withDecorView(not(is(testRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onData(instanceOf(Button.class)).inAdapterView(withId(R.id.grid)).atPosition(15).perform(click());
        onView(isRoot()).perform(waitView(R.id.text_undos, 1000));
        intended(hasComponent(ScoreBoard.class.getName()));
        onData(instanceOf(String.class)).atPosition(0).check(matches(withText(startsWith("123:      9"))));
        onView(withId(R.id.lastscore)).check(matches(withText(startsWith("9"))));
    }

    @Test
    public void test2_guestSaveTest(){
        testRule.getActivity().setCurrentAccount(null);
        onView(withId(R.id.text_currentUserGame)).check(matches(withText("Guest")));
        onView(withId(R.id.SaveButton)).perform(click());
        onView(withText(R.string.ga_guest_save))
                .inRoot(withDecorView(not(is(testRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}