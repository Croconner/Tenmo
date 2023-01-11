package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserDao userDao;

    @GetMapping(path = "/users")
    public List<String> getUsernames(Principal principal){
        List<User> userList = userDao.findAll();
        List<String> userNameList = new ArrayList<>();
        for(User user: userList){
            if(!user.getUsername().equals(principal.getName())){
                userNameList.add("Username: "+user.getUsername()+"| ID: "+user.getId());
            }
        }
        return userNameList;
    }
}
