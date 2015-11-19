package com.stoyanov.developer.instanotifier.model.multipleaccounts;

import android.content.Context;
import android.util.Log;

import com.stoyanov.developer.instanotifier.model.pojo.Account;
import java.util.ArrayList;

public class AccountManager {

    private static final int MAX_ACCOUNTS = 2;
    private StorageAccount storage;
    private ChangeAccountListener changeAccountListener;

    public AccountManager(Context context) {
        this.storage = new StorageAccount(context);
        changeAccountListener = null;
    }

    public void insert(Account account) {
        storage.add(account);
    }

    public void remove(Account account) {
        storage.delete(account);
    }

    public ArrayList<Account> getAll() {
        return storage.getAccounts();
    }

    public Account getCurrent() {
        Account current = null;
        String userID = storage.getParameter();
        ArrayList<Account> accounts = storage.getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUserId().equals(userID)) {
                current = accounts.get(i);
                break;
            }
        }
        return current;
    }

    public void setCurrent(Account account) {
        if (account != null) {
            storage.setParameter(account.getUserId());
            if (changeAccountListener != null) changeAccountListener.onChangeAccount(null, account);
        } else {
            throw new NullPointerException();
        }
    }

    public Account find(String userName) {
        Account account = null;
        ArrayList<Account> list = storage.getAccounts();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUsername().equals(userName)) {
                account = list.get(i);
                break;
            }
        }
        return account;
    }

    public boolean contain(Account account) {
        ArrayList<Account> list = storage.getAccounts();
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserId().equals(account.getUserId())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public int getCount() {
        return storage.getAmount();
    }

    public boolean isAvaliableAdd() {
        Log.i("DBG", "[AccountManager]Amount - " + storage.getAmount());
        if (storage.getAmount() < MAX_ACCOUNTS) {
            return true;
        } else {
            return false;
        }
    }

    public void setChangeAccountListener(ChangeAccountListener changeAccountListener) {
        this.changeAccountListener = changeAccountListener;
    }
}
