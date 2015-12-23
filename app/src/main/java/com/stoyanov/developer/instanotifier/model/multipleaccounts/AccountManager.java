package com.stoyanov.developer.instanotifier.model.multipleaccounts;

import android.content.Context;

import com.stoyanov.developer.instanotifier.model.pojo.Account;
import java.util.ArrayList;

public class AccountManager {

    private OnChangeAccountListener onChangeAccountListener;
    private StorageAccount storage;
    private static final int MAX_ACCOUNTS = 2;

    public AccountManager(Context context) {
        this.storage = new StorageAccount(context);
        onChangeAccountListener = null;
    }

    public void insert(Account account) {
        storage.add(account);
    }

    public void remove(Account account) {
        storage.delete(account);
        Account current = getCurrent();
        if (current == null) {
            ArrayList<Account> accounts = storage.getAccounts();
            if (!accounts.isEmpty()) {
                Account lastAccount = accounts.get(0);
                setCurrent(lastAccount);
            }
        }
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
        if (account == null) {
            throw new NullPointerException();
        }
        storage.setParameter(account.getUserId());
        if (onChangeAccountListener != null) {
            onChangeAccountListener.onChangeAccount(null, account);
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
            if (list.get(i).equals(account)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public int getCount() {
        return storage.getAmount();
    }

    public boolean isAvailableAdd() {
        return storage.getAmount() < MAX_ACCOUNTS;
    }

    public void setOnChangeAccountListener(OnChangeAccountListener onChangeAccountListener) {
        this.onChangeAccountListener = onChangeAccountListener;
    }
}
