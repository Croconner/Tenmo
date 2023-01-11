package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    @Min(1001)
    private int toUserId;
    @Min(1001)
    private int fromUserId;
    @DecimalMin("0.01")
    private BigDecimal amountTransferred;
    private String transferStatus;



    public Transfer() {
    }

    public Transfer(int transferId, int toUserId, int fromUserId, BigDecimal amountTransferred, String transferStatus) {
        this.transferId = transferId;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.amountTransferred = amountTransferred;
        this.transferStatus = transferStatus;

    }


    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public BigDecimal getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(BigDecimal amountTransferred) {
        this.amountTransferred = amountTransferred;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", toUserId=" + toUserId +
                ", fromUserId=" + fromUserId +
                ", amountTransferred=" + amountTransferred +
                '}';
    }
}
