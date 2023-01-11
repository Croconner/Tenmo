package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDAO;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransferDAOTest extends BaseDaoTests {

    private static final Account ACCOUNT_1 = new Account(2001, 1001, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_3 = new Account(2003, 1003, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_4 = new Account(2004, 1004, new BigDecimal("1000.00"));
    private static final User USER_1 = new User(1001, "bob", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "ROLE_USER");
    private static final User USER_2 = new User(1002, "user", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy", "ROLE_USER");
    private static final User USER_3 = new User(1003, "jim", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEb", "ROLE_USER");
    private static final User USER_4 = new User(1004, "sam", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEi", "ROLE_USER");
    private static final Transfer TRANSFER_1 = new Transfer(3001, 1001, 1002, new BigDecimal("50.00"), "Approved");
    private static final Transfer TRANSFER_2 = new Transfer(3002, 1001, 1001, new BigDecimal("50.00"), "Denied");
    private static final Transfer TRANSFER_3 = new Transfer(3003, 1003, 1002, new BigDecimal("1000.00"), "Approved");
    private static final Transfer TRANSFER_4 = new Transfer(3004, 1001, 1002, new BigDecimal("-1"), "Denied");

    private JdbcTransferDAO dao;
    private Transfer testTransfer;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTransferDAO(jdbcTemplate);
        testTransfer =new Transfer(3005,1001,1002,new BigDecimal("100.00"),"Approved");


    }

    @Test
    public void test_getTransfer_send_in_3001_return_TRANSFER_1() {
        Transfer expected = TRANSFER_1;
        Transfer actual = dao.getTransfer(3001);
        assertTransfersMatch(expected, actual);
    }

    @Test
    public void test_getAllTransfers_send_in_1002_return_3_transfers() {
        List<Transfer> actualTransfer = dao.getAllTransfersForUser(1002);
        int expectedSize = 3;
        int actualSize = actualTransfer.size();
        Assert.assertEquals(expectedSize, actualSize);

    }

    @Test
    public void test_create_transfer_send_in_testTransfer_retrieve_testTransfer(){
        dao.createTransfer(testTransfer);
        Transfer retrievedTransfer=dao.getTransfer(testTransfer.getTransferId());
        assertTransfersMatch(testTransfer,retrievedTransfer);
    }

    @Test
    public void test_updateTransferStatus_send_in_Approved_and_3004_updated_status_matches_approved(){
        dao.updateTransferStatus("Approved",3004);
        Transfer transfer=dao.getTransfer(3004);
        String actualStatus=transfer.getTransferStatus();
        Assert.assertEquals("Approved",actualStatus);
    }


    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getToUserId(), actual.getToUserId());
        Assert.assertEquals(expected.getFromUserId(), actual.getFromUserId());
        Assert.assertEquals(expected.getAmountTransferred(), actual.getAmountTransferred());
        Assert.assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
    }


}