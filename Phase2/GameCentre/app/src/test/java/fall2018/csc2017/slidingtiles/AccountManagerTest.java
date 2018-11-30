package fall2018.csc2017.slidingtiles;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccountManagerTest {
    private List<Account> testAccountList = new ArrayList<>();
    @Test
    public void checkExistingUser() {
        testAccountList.add(new Account("123","123"));
        AccountManager am = new AccountManager(testAccountList);
        assertTrue(am.checkExistingUser("123"));
        List<Account> emptyAccounts = new ArrayList<>();
        am.setAccountsList(emptyAccounts);
        assertFalse(am.checkExistingUser("123"));
        am.setAccountsList(null);
        assertFalse(am.checkExistingUser("123"));
    }

    @Test
    public void setAccountsList() {
        List<Account> newList = new ArrayList<>();
        AccountManager am = new AccountManager(testAccountList);
        am.setAccountsList(newList);
        assertEquals(am.getAccountsList(), newList);
        am.setAccountsList(null);
        assertNull(am.getAccountsList());
    }

    @Test
    public void authenticateCredentials() {
        testAccountList.add(new Account("123","123"));
        AccountManager am = new AccountManager(testAccountList);
        assertTrue(am.authenticateCredentials("123", "123"));
    }

    @Test
    public void getAccountFromUsername() {
        Account firstAccount = new Account("123","123");
        testAccountList.add(firstAccount);
        AccountManager am = new AccountManager(testAccountList);
    }

    @Test
    public void getCurrentAccountBoardList() {
    }

    @Test
    public void createNewAccount() {
    }

    @Test
    public void saveCredentials() {
    }
}