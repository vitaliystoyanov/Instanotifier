package com.stoyanov.developer.instanotifier.model.multiaccounts;

import android.content.Context;
import android.util.Log;

import com.stoyanov.developer.instanotifier.model.pojo.Account;
import com.stoyanov.developer.instanotifier.model.pojo.ListAccountObject;
import com.stoyanov.developer.instanotifier.model.utills.ComplexPreferences;

import java.util.ArrayList;

/**
 * Created by Vitaliy Stoyanov on 10/29/2015.
 */
public class StorageAccounts {

    private static final String ACCOUNTS_PREF = "accounts_pref";
    private static final String KEY = "accounts";
    private static final String TAG = "DBG";
    private ComplexPreferences complexPreferences;

    public StorageAccounts(Context context) {
        this.complexPreferences = ComplexPreferences.getComplexPreferences(context,
                ACCOUNTS_PREF,
                Context.MODE_PRIVATE);
    }

    public void insert(Account account) {
        ArrayList<Account> list = getAccounts();
        list.add(account);
        setAccounts(list);
        Log.i(TAG, "[StorageAccounts]Insert: ID - " + account.getUserId() + ", TOKEN - " + account.getToken());
    }

    public void delete(Account account) {
        ArrayList<Account> list = getAccounts();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserId().equals(account.getUserId())) {
                list.remove(list.get(i));
                i -= 1;
                Log.i(TAG, "[StorageAccounts]Delete: N - " + i + ", ID - " + list.get(i));
            }
        }
        setAccounts(list);
    }

    public int getCount() {
        return getAccounts().size();
    }

    public ArrayList<Account> getListAccount() {
        return getAccounts();
    }

    private ArrayList<Account> getAccounts() {
        if (complexPreferences.getObject(KEY, ListAccountObject.class) == null) {
            setAccounts(new ArrayList<Account>());
            Log.i(TAG, "[StorageAccounts]getAccounts() == null");
        }
        ListAccountObject object = complexPreferences.getObject(KEY, ListAccountObject.class);
        return object.get();
    }

    private void setAccounts(ArrayList<Account> list) {
        complexPreferences.putObject(KEY, new ListAccountObject(list));
        complexPreferences.commit();
    }
}
