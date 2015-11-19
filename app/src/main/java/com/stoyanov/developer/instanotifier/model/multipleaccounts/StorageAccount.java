package com.stoyanov.developer.instanotifier.model.multipleaccounts;

import android.content.Context;
import android.util.Log;

import com.stoyanov.developer.instanotifier.model.pojo.Account;
import com.stoyanov.developer.instanotifier.model.pojo.ComplexListAccount;
import com.stoyanov.developer.instanotifier.model.utills.ComplexPreferences;

import java.util.ArrayList;

public class StorageAccount {

    private static final String ACCOUNTS_PREF = "accounts_pref";
    private static final String KEY_PARAMETER = "current_acc";
    private static final String KEY_LIST = "accounts";
    private static final String TAG = "DBG";
    private ComplexPreferences preferences;

    public StorageAccount(Context context) {
        this.preferences = ComplexPreferences.getComplexPreferences(context,
                ACCOUNTS_PREF,
                Context.MODE_PRIVATE);
    }

    public void add(Account account) {
        ArrayList<Account> list = getList();
        list.add(account);
        setAccounts(list);
        Log.i(TAG, "[StorageAccount]add: ID - " + account.getUserId() + ", TOKEN - " + account.getToken());
    }

    public void delete(Account account) {
        ArrayList<Account> list = getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserId().equals(account.getUserId())) {
                list.remove(list.get(i));
                Log.i(TAG, "[StorageAccount]Delete: - " + i + " account");
                break;
            }
        }
        setAccounts(list);
    }

    private ArrayList<Account> getList() {
        if (!isInitializationList()) {
            setAccounts(new ArrayList<Account>());
            Log.i(TAG, "[StorageAccount]getList() == null");
        }
        ComplexListAccount object = preferences.getObject(KEY_LIST, ComplexListAccount.class);
        return object.get();
    }

    private boolean isInitializationList() {
        if (preferences.getObject(KEY_LIST, ComplexListAccount.class) == null) {
            return false;
        } else {
            return true;
        }
    }

    public String getParameter() {
        if (!isInitializationParameter()) {
            preferences.putObject(KEY_PARAMETER, new String());
            preferences.commit();
        }
        return preferences.getObject(KEY_PARAMETER, String.class);
    }

    private boolean isInitializationParameter() {
        if (preferences.getObject(KEY_PARAMETER, String.class) == null) {
            return false;
        } else {
            return true;
        }
    }

    public void setParameter(String parametr) {
        preferences.putObject(KEY_PARAMETER, parametr);
        preferences.commit();
    }

    public ArrayList<Account> getAccounts() {
        return getList();
    }

    public void setAccounts(ArrayList<Account> list) {
        preferences.putObject(KEY_LIST, new ComplexListAccount(list));
        preferences.commit();
    }

    public int getAmount() {
        return getList().size();
    }
}
