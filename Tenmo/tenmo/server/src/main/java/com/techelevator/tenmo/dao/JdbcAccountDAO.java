package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDAO implements AccountDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public String getAccountIdByUsername(String username){
        int accountID = 0;
        String sql="SELECT account_id FROM account a "+
                "JOIN tenmo_user tu ON tu.user_id=a.user_id "+
                "WHERE username=?";
        try {
            accountID = jdbcTemplate.queryForObject(sql, Integer.class, username);
        }
        catch(DataAccessException e){
            System.out.println("error");
        }
        return Integer.toString(accountID);
    }

    @Override
    public Account getAccount(String username) {
        String sql="SELECT account_id, user_id, balance FROM account "+
                   "WHERE account_id="+getAccountIdByUsername(username);
        SqlRowSet results= jdbcTemplate.queryForRowSet(sql);
        Account account= null;
        if(results.next()){
            account=mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public boolean updateAccount(Account account) {
        boolean success=false;
        String sql="UPDATE account SET balance=? WHERE account_id=?";
        int linesUpdated=jdbcTemplate.update(sql,account.getBalance(),account.getAccountId());
        if(linesUpdated==1){
            success=true;
        }
        return success;
    }

    @Override
    public boolean deleteAccount(String username) {
        boolean success=false;
        String sql="DELETE FROM account WHERE account_id=?";
        int id=Integer.parseInt(getAccountIdByUsername(username));
        int linesUpdated=jdbcTemplate.update(sql,id);
        if(linesUpdated==1){
            success=true;
        }
        return success;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        return balance;
    }

    @Override
    public boolean transferFunds(int toUserId, int fromUserId, BigDecimal transferAmount) {

        boolean success = false;
        if (getBalance(fromUserId).compareTo(transferAmount) >= 0 && toUserId!=fromUserId&&transferAmount.compareTo(new BigDecimal("0"))==1) {
            BigDecimal fromBalance = newFromBalance(transferAmount, fromUserId);
            BigDecimal toBalance = newToBalance(transferAmount, toUserId);
            String sql = "UPDATE account SET balance=? WHERE user_id=?";
            String sql2 ="UPDATE account SET balance=? WHERE user_id=?";
            int linesupdated = jdbcTemplate.update(sql, fromBalance, fromUserId);
            int linesupdated2 = jdbcTemplate.update(sql2, toBalance, toUserId);
            if (linesupdated == 1 && linesupdated2 == 1) {
                success = true;
            }
        }
        return success;
    }

    private Account mapRowToAccount(SqlRowSet results){
        Account account=new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));

        return account;
    }

    private BigDecimal newFromBalance(BigDecimal transferAmount, int fromUserId){
        BigDecimal balance = getBalance(fromUserId);
        BigDecimal newBalance=balance;
        if (balance.compareTo(transferAmount)>=0) {
            newBalance = balance.subtract(transferAmount);
        }
        return newBalance;
    }


    private BigDecimal newToBalance(BigDecimal transferAmount, int toUserId) {
        BigDecimal balance=getBalance(toUserId);
        BigDecimal newBalance=balance.add(transferAmount);
        return newBalance;
    }

}
