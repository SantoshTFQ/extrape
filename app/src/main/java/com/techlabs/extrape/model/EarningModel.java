package com.techlabs.extrape.model;

public class EarningModel {
    private String store, status, visitorId, orderId, approvalDate;
    private double amount, earning;

    public String getStore() { return store; }
    public void setStore(String store) { this.store = store; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getApprovalDate() { return approvalDate; }
    public void setApprovalDate(String approvalDate) { this.approvalDate = approvalDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getEarning() { return earning; }
    public void setEarning(double earning) { this.earning = earning; }
}
