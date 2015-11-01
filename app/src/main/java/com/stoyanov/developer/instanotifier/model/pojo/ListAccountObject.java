package com.stoyanov.developer.instanotifier.model.pojo;

import java.util.ArrayList;

public class ListAccountObject {

    private ArrayList<Account> list;

    public ListAccountObject() {
        this.list = new ArrayList<>();
    }

    public ListAccountObject(ArrayList<Account> list) {
        this.list = list;
    }

    public ArrayList<Account> get() {
        return list;
    }

    public void setList(ArrayList<Account> list) {
        this.list = list;
    }
}
