package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDAO {


    //CRUD

    Account getAccount(String username);

    boolean updateAccount(Account account);

    boolean deleteAccount(String username);

    BigDecimal getBalance(int userId);

    boolean transferFunds(int toUserId, int fromUserId, BigDecimal transferAmount);

    String getAccountIdByUsername(String username);
}
