package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.EditorAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.SmallTest;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

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
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests initiates in alphabetical order to achieve desired testing results
 */
@RunWith(JUnit4.class)
@SmallTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LaunchCentreTest{
    /**
     * Activity that would be instantiated for assertions
     */
    private LaunchCentre activity;
    /**
     * Username/password fields for ViewInteractions used for View Actions
     */
    private ViewInteraction usernameField, passwordField;
    /**
     * Obtains preferences from previous usage/tests
     */
    private SharedPreferences sharedPreferences;
    /**
     * Custom isolated account lists specifically for testing
     */
    private List<Account> testAccountList = new ArrayList<>();

    /**
     * Creates a rule before the 'Before' life cycle for specialized data input
     */
    @Rule
    public IntentsTestRule<LaunchCentre> launchCentreActivityTestRule = new IntentsTestRule<LaunchCentre>(LaunchCentre.class){
        @Override
        protected void afterActivityLaunched() {
            super.afterActivityLaunched();
            testAccountList.add(new Account("123", "123"));
            getActivity().getAccountManager().setAccountsList(testAccountList);
        }
    };

    /**
     * Sets up the UI view in LaunchCentre
     */
    @Before
    public void setUp(){
        activity = launchCentreActivityTestRule.getActivity();
        usernameField = onView(withId(R.id.text_username));
        passwordField = onView(withId(R.id.text_password));
    }

    /**
     * General test for all components in LaunchCentre
     */
    @Test
    public void test1_generalTest() {
        //Retrieve preferences prior testing condition.
        PreferenceManager pManager = activity.getPreferenceManager();

        // If preference for remember login details was previously set to true, set to false
        if(pManager.retrieveBool("remember", false))
            onView(withId(R.id.cb_remember)).perform(click());
        List<Account> originalAccountList = testAccountList;
        AccountManager am = activity.getAccountManager();
        am.setAccountsList(new ArrayList<Account>());
        assertFalse(am.checkExistingUser("123"));

        // Replace username and password with 123 which was instantiated in an account list
        // TestRule cycle
        usernameField.perform(replaceText("123"));
        passwordField.perform(replaceText("123"));
        am.setAccountsList(originalAccountList);

        // Ensure credentials are save on login
        onView(withId(R.id.cb_remember))
                .perform(click())
                .check(matches(isChecked()));
        onView(withId(R.id.button_login)).perform(click());

        // Should be able to login with intended credentials
        intended(hasComponent(GameSelection.class.getName()));
    }

    /**
     * Test for login button for both valid and invalid credentials
     */
    @Test
    public void test2_loginButtonOnClick() {
        //Retrieve preferences prior testing condition.
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        // Remember me option should be toggled to true from @Test test1_onCreate().
        assertTrue(sharedPreferences.getBoolean("remember", false));

        // Incorrect login credentials testing.
        assertNotEquals(sharedPreferences.getString("previousUser", ""), "1234");
        assertNotEquals(sharedPreferences.getString("previousPass", ""), "1234");

        // Previous correct login credentials from @Test test1_onCreate().
        assertEquals(sharedPreferences.getString("previousUser", ""), "123");
        assertEquals(sharedPreferences.getString("previousPass", ""), "123");

        // Test against false credentials
        usernameField.perform(replaceText("1234"));
        passwordField.perform(replaceText("1234"));
        onView(withId(R.id.button_login)).perform(click());
        // Check View Assertions for strings "lc_wrong_credentials" displayed by a Toast
        // when wrong credentials are used for logging in
        onView(withText(R.string.lc_wrong_credentials))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Replace username and password with 123 which was instantiated in an account list
        // TestRule cycle
        usernameField.perform(replaceText("123"));
        passwordField.perform(replaceText(""));
        passwordField.perform(typeText("123"));
        // Un-toggle remember checkbox and perform a login action
        onView(withId(R.id.cb_remember)).perform(click());
        onView(withId(R.id.text_password)).perform(pressImeActionButton());
        intended(hasComponent(GameSelection.class.getName()));
    }

    @Test
    public void test3_registerButtonOnClick() {
        // Instantiate bogus credentials that are not in the account list
        usernameField.perform(replaceText("1"));
        passwordField.perform(replaceText("1"));
        onView(withId(R.id.button_register)).perform(click());
        onView(withText(R.string.am_invalid_field))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Force fail registration by using "guest" as username which is reserved for guests only
        usernameField.perform(replaceText("guest"));
        passwordField.perform(replaceText("12345"));
        onView(withId(R.id.button_register)).perform(click());
        onView(withText(R.string.am_guest_reserved))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Force fail registration with existing username
        usernameField.perform(replaceText("123"));
        passwordField.perform(replaceText("12345"));
        onView(withId(R.id.button_register)).perform(click());
        onView(withText(R.string.am_existing_user))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Force fail registration with empty fields
        usernameField.perform(replaceText(""));
        passwordField.perform(replaceText(""));
        onView(withId(R.id.button_register)).perform(click());
        onView(withText(R.string.am_empty_field))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Create account with valid credentials
        usernameField.perform(replaceText("12345"));
        passwordField.perform(replaceText("12345"));

        // Check that username is not in account list and not existing
        AccountManager am = launchCentreActivityTestRule.getActivity().getAccountManager();
        assertNull(am.getAccountFromUsername("12345"));

        // Perform registration
        onView(withId(R.id.button_register)).perform(click());
        assertNotNull(am.getAccountFromUsername("12345"));

        // Check View Assertions for strings "am_credentials_saved" displayed by a Toast
        // when no existing user with input username and valid credentials
        onView(withText(R.string.am_credentials_saved))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        // Login with the new account created
        onView(withId(R.id.button_login)).perform(click());
        intended(hasComponent(GameSelection.class.getName()));
    }

    @Test
    public void test4_guestButtonOnClick() {
        // Simple guests logging in action
        onView(withId(R.id.button_guest)).perform(click());
        intended(hasComponent(GameSelection.class.getName()));
    }
}