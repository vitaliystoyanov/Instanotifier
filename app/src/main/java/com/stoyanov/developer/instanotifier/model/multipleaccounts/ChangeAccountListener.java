package com.stoyanov.developer.instanotifier.model.multipleaccounts;

import com.stoyanov.developer.instanotifier.model.pojo.Account;

public interface ChangeAccountListener {

    void onChangeAccount(Account oldAccount, Account newAccount);

}
