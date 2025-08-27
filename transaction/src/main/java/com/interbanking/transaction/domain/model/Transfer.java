package com.interbanking.transaction.domain.model;

import java.time.LocalDateTime;

public class Transfer {
    private Long id;
    private Amount amount;
    private Long companyId;
    private AccountNumber debitAccount;
    private AccountNumber creditAccount;
    private LocalDateTime transferDate;
    private TransferStatus status;

    public Transfer() {}

    public Transfer(Long id, Amount amount, Long companyId, AccountNumber debitAccount, 
                   AccountNumber creditAccount, LocalDateTime transferDate, TransferStatus status) {
        this.id = id;
        this.amount = amount;
        this.companyId = companyId;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.transferDate = transferDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public AccountNumber getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(AccountNumber debitAccount) {
        this.debitAccount = debitAccount;
    }

    public AccountNumber getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(AccountNumber creditAccount) {
        this.creditAccount = creditAccount;
    }

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDateTime transferDate) {
        this.transferDate = transferDate;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public boolean isFromLastMonth() {
        return transferDate.isAfter(LocalDateTime.now().minusMonths(1));
    }

    public void complete() {
        this.status = TransferStatus.COMPLETED;
    }

    public void cancel() {
        this.status = TransferStatus.CANCELLED;
    }

    public boolean isCompleted() {
        return status == TransferStatus.COMPLETED;
    }

    public enum TransferStatus {
        PENDING,
        COMPLETED,
        CANCELLED,
        FAILED
    }
}