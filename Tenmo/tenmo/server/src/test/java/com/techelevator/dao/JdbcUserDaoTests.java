package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcAccountDAO;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class JdbcUserDaoTests extends BaseDaoTests{

    private JdbcUserDao sut;
    private JdbcAccountDAO dao;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
        dao=new JdbcAccountDAO(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }
    @Test
    public void test_create_send_in_joe_joe_new_account_created_with_balance_of_1000(){
        sut.create("joe","joe");
        int id=sut.findIdByUsername("joe");
        BigDecimal accountBalance=dao.getBalance(id);
        Assert.assertEquals(new BigDecimal("1000.00"),accountBalance);

    }


}
