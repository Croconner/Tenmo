package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {


    //CRUD

    Transfer getTransfer(int transferId);

    List<Transfer> getAllTransfersForUser(int userId);

    Transfer createTransfer(Transfer transfer);

    void updateTransferStatus(String status, int transferId);

    Transfer requestTransfer(Transfer transfer);









}
