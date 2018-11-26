package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.SmallTest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
@SmallTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LaunchCentreTest{
    private LaunchCentre activity;
    private ViewInteraction usernameField, passwordField;
    private SharedPreferences sharedPreferences;
    private List<Account> testAccountList = new ArrayList<>();

    @Rule
    public IntentsTestRule<LaunchCentre> launchCentreActivityTestRule = new IntentsTestRule<LaunchCentre>(LaunchCentre.class){
        @Override
        protected void afterActivityLaunched() {
            super.afterActivityLaunched();
            testAccountList.add(new Account("123", "123"));
            getActivity().getAccountManager().setAccountsList(testAccountList);
        }
    };

    @Before
    public void setUp() throws Exception {
        activity = launchCentreActivityTestRule.getActivity();
        usernameField = onView(withId(R.id.text_username));
        passwordField = onView(withId(R.id.text_password));
    }

    @Test
    public void test1_onCreate() {
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        // If preference for remember login details was previously set to true, set to false
        if(sharedPreferences.getBoolean("remember", false))
            onView(withId(R.id.cb_remember)).perform(click());


        usernameField.perform(replaceText("123"));
        passwordField.perform(replaceText("123"));
        //onView(withId(R.id.button_register)).perform(click());

        onView(withId(R.id.cb_remember))
                .perform(click())
                .check(matches(isChecked()));
        onView(withId(R.id.button_login)).perform(click());

        intended(hasComponent(GameSelection.class.getName()));
    }

    @Test
    public void test2_loginButtonOnClick() {
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        // Incorrect login credentials testing.
        assertNotEquals(sharedPreferences.getString("previousUser", ""), "1234");
        assertNotEquals(sharedPreferences.getString("previousPass", ""), "1234");

        // Previous correct login credentials from @Test test1_onCreate().
        assertEquals(sharedPreferences.getString("previousUser", ""), "123");
        assertEquals(sharedPreferences.getString("previousPass", ""), "123");

        // Remember me option should be toggled to true from @Test test1_onCreate().
        assertTrue(sharedPreferences.getBoolean("remember", false));

        onView(withId(R.id.cb_remember)).perform(click());
        onView(withId(R.id.button_login)).perform(click());
        intended(hasComponent(GameSelection.class.getName()));
    }

    @Test
    public void test3_registerButtonOnClick() {
        String username = "12345";
        String password = "12345";
        usernameField.perform(replaceText(username));
        passwordField.perform(replaceText(password));
        onView(withId(R.id.button_register)).perform(click());

        onView(withText(R.string.am_credentials_saved))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test4_guestButtonOnClick() {
        onView(withId(R.id.button_guest)).perform(click());
        intended(hasComponent(GameSelection.class.getName()));
    }
}