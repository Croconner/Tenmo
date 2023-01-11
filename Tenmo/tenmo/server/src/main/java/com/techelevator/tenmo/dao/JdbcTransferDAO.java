package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TransferQueue;

@Component
public class JdbcTransferDAO implements TransferDAO {


    private JdbcTemplate jdbcTemplate;


    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer getTransfer(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id,to_user_id, from_user_id, amount_transferred, transfer_status " +
                "FROM transfer WHERE transfer_id=?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfersForUser(int userId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id,to_user_id, from_user_id, amount_transferred, transfer_status FROM transfer t " +
                "JOIN tenmo_user tu ON tu.user_id=t.to_user_id " +
                "WHERE to_user_id=? OR from_user_id=?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);

        while (results.next()) {
                transferList.add(mapRowToTransfer(results));
        }
        return transferList;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql="INSERT INTO transfer (to_user_id, from_user_id, amount_transferred, transfer_status) "+
                   "VALUES(?,?,?,?) RETURNING transfer_id;";
        Integer transferId;
        String status="Approved";

        transferId=jdbcTemplate.queryForObject(sql,Integer.class,transfer.getToUserId(),
                                              transfer.getFromUserId(),transfer.getAmountTransferred(),status);

        return getTransfer(transferId);
    }

    @Override
    public void updateTransferStatus(String status, int transferId){
        String sql="UPDATE transfer SET transfer_status=? WHERE transfer_id=?";
        jdbcTemplate.update(sql,status,transferId);
    }
    @Override
    public Transfer requestTransfer(Transfer transfer){
        Transfer newTransfer=createTransfer(transfer);
        if(transfer.getFromUserId()!= transfer.getToUserId()) {
            updateTransferStatus("Pending", newTransfer.getTransferId());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You can't request money from yourself/you can't request negative amounts");
        }
        return newTransfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setToUserId(results.getInt("to_user_id"));
        transfer.setFromUserId(results.getInt("from_user_id"));
        transfer.setAmountTransferred(results.getBigDecimal("amount_transferred"));
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferStatus(results.getString("transfer_status"));

        return transfer;

    }


}
