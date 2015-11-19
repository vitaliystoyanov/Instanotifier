package com.stoyanov.developer.instanotifier.model.pojo;

import java.util.ArrayList;

public class ComplexListAccount {

    private ArrayList<Account> list;

    public ComplexListAccount() {
        this.list = new ArrayList<>();
    }

    public ComplexListAccount(ArrayList<Account> list) {
        this.list = list;
    }

    public ArrayList<Account> get() {
        return list;
    }

    public void setList(ArrayList<Account> list) {
        this.list = list;
    }
}
