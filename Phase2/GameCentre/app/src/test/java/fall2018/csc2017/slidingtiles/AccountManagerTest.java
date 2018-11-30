package fall2018.csc2017.slidingtiles;

import android.app.Activity;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static fall2018.csc2017.slidingtiles.UtilityManager.ACCOUNTS_FILENAME;
import static org.junit.Assert.*;

/**
 *
 */
public class AccountManagerTest {
    private List<Account> testAccountList = new ArrayList<>();
    private Account firstAccount = new Account("123","123") ;
    private AccountManager am;
    @Before
    public void setUp(){
        testAccountList.add(firstAccount);
        am = new AccountManager(testAccountList);
    }

    @Test
    public void checkExistingUser() {
        assertTrue(am.checkExistingUser("123"));
        assertFalse(am.checkExistingUser("bogus"));
        List<Account> emptyAccounts = new ArrayList<>();
        am.setAccountsList(emptyAccounts);
        assertFalse(am.checkExistingUser("123"));
        am.setAccountsList(null);
        assertFalse(am.checkExistingUser("123"));
    }

    @Test
    public void setAccountsList() {
        List<Account> newList = new ArrayList<>();
        am.setAccountsList(newList);
        assertEquals(am.getAccountsList(), newList);
        am.setAccountsList(null);
        assertNull(am.getAccountsList());
    }

    @Test
    public void authenticateCredentials() {
        assertTrue(am.authenticateCredentials("123", "123"));
        assertFalse(am.authenticateCredentials("1234", "1234"));
    }

    @Test
    public void getAccountFromUsername() {
        assertEquals(firstAccount, am.getAccountFromUsername("123"));
        assertNull(am.getAccountFromUsername("bogus"));
        am.setAccountsList(null);
        assertNull(am.getAccountFromUsername("123"));
    }

    @Test
    public void getCurrentAccountBoardList() {
        List<BoardManager> boardList = new ArrayList<>();
        boardList.add(BoardSetup.setUp4x4Solved());
        firstAccount.setBoardList(boardList);
        assertNotNull(am.getCurrentAccountBoardList(firstAccount, false));
        assertEquals(new ArrayList<BoardManager>(), am.getCurrentAccountBoardList(firstAccount, true));
        assertEquals(new ArrayList<BoardManager>(), am.getCurrentAccountBoardList(null, true));
    }

    @Test
    public void createNewAccount() {
        Activity dummyActivity = new Activity();
        assertNull(am.createNewAccount("Guest", "1234", dummyActivity));
        assertNull(am.createNewAccount("Guest", "1234", null));
        assertNull(am.createNewAccount("1234", "", dummyActivity));
        assertNull(am.createNewAccount("", "1234", dummyActivity));
        assertNull(am.createNewAccount("12", "12", dummyActivity));
        assertNull(am.createNewAccount("12", "1234", dummyActivity));
        assertNull(am.createNewAccount("1234", "12", dummyActivity));
        assertNull(am.createNewAccount("12", "12", dummyActivity));
        assertNull(am.createNewAccount("123", "123", dummyActivity));
        assertTrue(am.createNewAccount("validusername", "12345", dummyActivity)
                .equals(new Account("validusername", "12345")));
    }

    @Test
    public void saveCredentials() {
        Activity dummyActivity = new Activity();
        dummyActivity.setIntent(new Intent(dummyActivity, LaunchCentre.class));
        am.saveCredentials(ACCOUNTS_FILENAME, dummyActivity);
        am.saveCredentials("bogus", null);
    }
}