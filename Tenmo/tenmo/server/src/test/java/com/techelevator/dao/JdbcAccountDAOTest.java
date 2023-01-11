package com.techelevator.dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.techelevator.dao.BaseDaoTests;
import com.techelevator.tenmo.dao.JdbcAccountDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class JdbcAccountDAOTest extends BaseDaoTests {

    private static final Account ACCOUNT_1=new Account(2001,1001,new BigDecimal("1000.00"));
    private static final Account ACCOUNT_2=new Account(2002,1002,new BigDecimal("1000.00"));
    private static final Account ACCOUNT_3=new Account(2003,1003,new BigDecimal("1000.00"));
    private static final Account ACCOUNT_4=new Account(2004,1004,new BigDecimal("1000.00"));
    private static final User USER_1=new User(1001,"bob","$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2","ROLE_USER");
    private static final User USER_2=new User(1002,"user","$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy","ROLE_USER");
    private static final User USER_3=new User(1003,"jim","$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEb","ROLE_USER");
    private static final User USER_4=new User(1004,"sam","$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEi","ROLE_USER");

    private JdbcAccountDAO dao;
    private Account testAccount;
    private Account testAccount2;
    private User testUser;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao=new JdbcAccountDAO(jdbcTemplate);
        testAccount=new Account(2001,1001,new BigDecimal("950.00"));
        testAccount2=new Account(2002,1002,new BigDecimal("1050.00"));

    }

    @Test
    public void test_getAccount_send_in_Bob_return_ACCOUNT_1(){
        Account actualAccount=dao.getAccount("bob");
        assertAccountsMatch(ACCOUNT_1,actualAccount);
    }

    @Test
    public void test_getAccount_send_in_name_that_does_not_exist_return_null(){
        Account account=dao.getAccount("Jerry");
        Assert.assertNull(account);
    }

    @Test
    public void test_getAccountIdByUsername_send_in_bob_return_2001(){
        String actualId=dao.getAccountIdByUsername("bob");
        String expected="2001";
        Assert.assertEquals(actualId,expected);
    }

    @Test
    public void test_updateAccount_testAccount_equals_updatedAccount(){
        dao.updateAccount(testAccount);
        Account retrievedAccount=dao.getAccount("bob");
       assertAccountsMatch(testAccount,retrievedAccount);
    }

    @Test
    public void test_deleteAccount_deleted_account_cant_be_retrieved(){
        dao.deleteAccount("bob");
        Account retrievedAccount=dao.getAccount("bob");
        Assert.assertNull(retrievedAccount);
    }

    @Test
    public void test_getBalance_send_in_1001_return_1000(){
        BigDecimal expected=new BigDecimal("1000.00");
        BigDecimal actual=dao.getBalance(1001);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void test_transferFunds_send_in_1001_1002_50_return_true(){
        Assert.assertTrue(dao.transferFunds(ACCOUNT_1.getUserId(),ACCOUNT_2.getUserId(),new BigDecimal("50.00")));
    }






    private void assertAccountsMatch(Account expected, Account actual){
        Assert.assertEquals(expected.getAccountId(),actual.getAccountId());
        Assert.assertEquals(expected.getUserId(),actual.getUserId());
        Assert.assertEquals(expected.getBalance(),actual.getBalance());
    }



}