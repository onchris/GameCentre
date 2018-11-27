package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.SmallTest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

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
    public void test1_testSelectionGames(){
        try {
            Intent intent = new Intent();
            intent.putExtra("currentUser", "123");
            testRule.launchActivity(intent);
            intended(hasComponent(GameSelection.class.getName()));
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
