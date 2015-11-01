package com.stoyanov.developer.instanotifier.model.multiaccounts;

import android.content.Context;
import android.util.Log;

import com.stoyanov.developer.instanotifier.model.pojo.Account;

import java.util.ArrayList;

/**
 * Manage access token and user name. Uses shared preferences to store access
 * token and user name.
 *
 * @author Vitaliy Stoyanov <developer.stoyanov@gmail.com>
 */
public class AccountManager {


    private static final int MAX_ACCOUNTS = 3;
    private Context context;
    private StorageAccounts storage;

    public AccountManager(Context context) {
        this.context = context;
        storage = new StorageAccounts(context);
/*        Account acc = new Account("6346", "4654");
        storage.insert(acc);
        Log.i("DBG", "Size - " + storage.getListAccount().size());
        for (Account a: storage.getListAccount()) {
            Log.i("DBG", a.getToken() + ", " + a.getUserId());
        }
        storage.delete(acc);
        Log.i("DBG", "Size - " + storage.getListAccount().size());
        for (Account a: storage.getListAccount()) {
            Log.i("DBG", a.getToken() + ", " + a.getUserId());
        }*/
    }

    public void add(Account account) {
        storage.insert(account);
    }

    public void remove(Account account) {
        storage.delete(account);
    }

    public ArrayList<Account> getAllAccounts() {
        return storage.getListAccount();
    }

    public Account getCurrent() {
        // TODO implement here
        return null;
    }

    public void setCurrent() {
        // TODO implement here
    }

    public boolean containe(Account account) {
        ArrayList<Account> list = storage.getListAccount();
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserId().equals(account.getUserId())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean isAvaliableAdd() {
        if (storage.getCount() <= MAX_ACCOUNTS) {
            return true;
        } else {
            return false;
        }
    }

    /*
    private static final int MAX_ACCONTS = 2;
    private static final String SHARED = "preferences_id";
    private static final String ARRAY_ACCOUNT_ID = "id";
    private static final String delimiters = ",";
    private Context context;

    public AccountManager(Context context) {
        super(context, SHARED);
        this.context = context;
    }

    public boolean addAccount(Account object) {
        if (object != null) {
            String savedString = getString(ARRAY_ACCOUNT_ID);
            if (savedString == null) {
                putString(ARRAY_ACCOUNT_ID, object.getAccountUserId());
            } else {
                putString(ARRAY_ACCOUNT_ID, savedString + delimiters
                        + object.getAccountUserId());
            }
            commit();
            Log.i("DBG", "Add a account to memory with id - " + object.getAccountUserId());
            return true;
        } else
            return false;
    }

    public boolean checkAddAccount() {
        Log.i("DBG","Account amount - " + String.valueOf(getListAccounts().size()));
        if (getListAccounts().size() < MAX_ACCONTS)
            return true;
        else
            return false;
    }

    public Account getAccountById(String userID) {
        if (userID != null) {
            Account account = new Account(userID);
            if (containsByUserID(account))
                return account;
            else
                return null;
        } else
            throw new RuntimeException("getAccountById() - String userID is null!");
    }

    public void removeAccount(Account account) {
        if (containsByUserID(account)) {
            ArrayList<Account> accountsList = getListAccounts();
            for (int i = 0; i < accountsList.size(); i++) {
                if (accountsList.getParameters(i).getAccountUserId().equals(account.getAccountUserId())) {
                    accountsList.remove(i);
                    Log.i("DBG", "Token " + account.getAccountUserId() + " - removed!");
                }
            }
            storeListAccounts(accountsList);
        }
    }

    public int amountOfAccounts() {
        Log.i("DBG", "Amount of accounts - "+String.valueOf(getListAccounts().size()));
        return getListAccounts().size();
    }

    public boolean containsByUserID(Account account) {
        ArrayList<Account> accounts;
        accounts = getListAccounts();

        boolean flagResult = false;
        if (accounts != null) {
            int sizeArrayList = accounts.size();
            if (sizeArrayList > 0) {
                for (int i = 0; i < sizeArrayList; i++) {
                    if (accounts.getParameters(i).getAccountUserId().equals(account.getAccountUserId())) {
                        flagResult = true;
                        break;
                    }
                }
            }
        }
        return flagResult;
    }

    public Account getAccountByUsername(String username) {
        ArrayList<Account> list = getListAccounts();

        for (int i = 0; i < list.size(); i++) {
            String userId = list.getParameters(i).getAccountUserId();
            InstagramSessionAccount session = getSession(new Account(userId));
            if (session.getUsername().equals(username)) {
                return new Account(userId);
            }
        }
        return null;
    }

    public void resetAllAccounts() {
        Log.i("DBG", "Reset all accounts!");
        putString(ARRAY_ACCOUNT_ID, null);
        commit();
    }

    public InstagramSessionAccount getSession(Account account) {
        return new InstagramSessionAccount(context, account);
    }

    public void storeListAccounts(ArrayList<Account> accounts) {
        if (accounts != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < accounts.size(); i++) {
                stringBuilder.append(accounts.getParameters(i).getAccountUserId()).append(delimiters);
            }
            Log.i("DBG", "storeListAccounts() input arg - " + stringBuilder.toString());
            putString(ARRAY_ACCOUNT_ID, stringBuilder.toString());
            commit();
        }
    }

    public ArrayList<Account> getListAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        String savedString = getString(ARRAY_ACCOUNT_ID);
        if (savedString != null) {
            StringTokenizer st = new StringTokenizer(savedString, delimiters);
            Log.i("DBG", "getListAccounts() return - " + savedString);

            while (st.hasMoreTokens()) {
                accounts.add(new Account(st.nextToken()));
            }
        }
        return accounts;
    }
    */
}
