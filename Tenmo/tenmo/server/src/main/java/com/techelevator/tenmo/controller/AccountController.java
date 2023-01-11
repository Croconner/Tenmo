package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    @Autowired
    private AccountDAO accountDAO;

    @RequestMapping (path = "/account", method = RequestMethod.GET)
    public Account getAccount(Principal principal){
        return accountDAO.getAccount(principal.getName());
    }

    @RequestMapping(path="/account",method = RequestMethod.PUT)
    public void updateAccount(Principal principal, @Valid @RequestBody Account account){
        int id=Integer.parseInt(accountDAO.getAccountIdByUsername(principal.getName()));
        account.setAccountId(id);
        boolean updated=accountDAO.updateAccount(account);
        if(!updated){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account Not Updated");
        }
    }
    @DeleteMapping(path = "/account")
    public void deleteAccount(Principal principal){
        boolean deleted=accountDAO.deleteAccount(principal.getName());
        if(!deleted){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account Not Deleted");
        }
    }





}
