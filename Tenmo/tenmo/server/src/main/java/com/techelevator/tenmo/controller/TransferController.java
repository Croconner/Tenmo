package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping(path = "/transfers")
@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

    @Autowired
    private TransferDAO transferDAO;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDAO accountDAO;

    @GetMapping(path = "/{id}")
    public Transfer getTransferById(@Valid @PathVariable int id) {
        return getTransferById(id);
    }

    @GetMapping()
    public List<Transfer> getAllTransfersForUser(Principal principal) {
        return transferDAO.getAllTransfersForUser(userDao.findIdByUsername(principal.getName()));
    }

    @PostMapping()
    public Transfer createTransfer(@Valid @RequestBody Transfer transfer, Principal principal) {

        if (transfer.getFromUserId() == userDao.findIdByUsername(principal.getName())) {
            boolean updated = accountDAO.transferFunds(transfer.getToUserId(), transfer.getFromUserId(), transfer.getAmountTransferred());
            Transfer newTransfer = transferDAO.createTransfer(transfer);
            if (!updated) {
                newTransfer.setTransferStatus("Denied");
                transferDAO.updateTransferStatus("Denied", newTransfer.getTransferId());
            }
            return newTransfer;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer money from other peoples accounts");
        }

    }

    @PostMapping(path = "/request")
    public Transfer requestTransfer(@Valid @RequestBody Transfer transfer) {
        Transfer newRequest = transferDAO.requestTransfer(transfer);
        newRequest.setTransferStatus("Pending");
        return newRequest;
    }

    @PutMapping(path = "/approval/{id}")
    public void approveDeny(@PathVariable int id, @RequestParam String approval, Principal principal) {
        Transfer updatedTransfer = transferDAO.getTransfer(id);
        if (approval.equalsIgnoreCase("Y") && updatedTransfer.getTransferStatus().equalsIgnoreCase("Pending") &&
                updatedTransfer.getAmountTransferred().compareTo(accountDAO.getBalance(updatedTransfer.getFromUserId())) <= 0 && updatedTransfer.getFromUserId() == userDao.findIdByUsername(principal.getName())) {
            transferDAO.updateTransferStatus("Approved", id);
            accountDAO.transferFunds(updatedTransfer.getToUserId(), updatedTransfer.getFromUserId(), updatedTransfer.getAmountTransferred());
        } else if (approval.equalsIgnoreCase("N")) {
            transferDAO.updateTransferStatus("Denied", id);
        } else if (!approval.equalsIgnoreCase("Y") && updatedTransfer.getTransferStatus().equalsIgnoreCase("Pending")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must enter Y or N to approve/deny request and you can't approve/deny more then once");
        } else if (updatedTransfer.getFromUserId() != userDao.findIdByUsername(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not approve requests for other users");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not have enough money in your account to complete the transaction");
        }

    }


}
