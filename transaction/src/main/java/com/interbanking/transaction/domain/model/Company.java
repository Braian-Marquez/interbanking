package com.interbanking.transaction.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Company {
    private Long id;
    private String cuit;
    private String companyName;
    private LocalDateTime registrationDate;
    private boolean active;
    private List<Transfer> transfers;

    public Company() {}

    public Company(Long id, String cuit, String companyName, LocalDateTime registrationDate, boolean active) {
        this.id = id;
        this.cuit = cuit;
        this.companyName = companyName;
        this.registrationDate = registrationDate;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isRegisteredInLastMonth() {
        return registrationDate.isAfter(LocalDateTime.now().minusMonths(1));
    }
}