package com.example.appbanhang.model;

import java.util.Date;

public class SentMessages {
    private int sentId;
    private int receivedId;
    private String messages;
    private String dataTime;
    private Date dateMessage;

    public SentMessages() {
    }

    public SentMessages(int sentId, int receivedId, String messages, String dataTime, Date dateMessage) {
        this.sentId = sentId;
        this.receivedId = receivedId;
        this.messages = messages;
        this.dataTime = dataTime;
        this.dateMessage = dateMessage;
    }

    public int getSentId() {
        return sentId;
    }

    public void setSentId(int sentId) {
        this.sentId = sentId;
    }

    public int getReceivedId() {
        return receivedId;
    }

    public void setReceivedId(int receivedId) {
        this.receivedId = receivedId;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public Date getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(Date dateMessage) {
        this.dateMessage = dateMessage;
    }
}
