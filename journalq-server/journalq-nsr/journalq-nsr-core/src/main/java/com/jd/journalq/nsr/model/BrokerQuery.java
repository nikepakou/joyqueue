package com.jd.journalq.nsr.model;

import com.jd.journalq.model.Query;

import java.util.List;

public class BrokerQuery implements Query {
    /**
     * brokerId
     */
    private int brokerId;
    /**
     * IP
     */
    private String ip;
    /**
     * 端口
     */
    private int port;

    /**
     * 重试类型
     */
    private String retryType;

    private String keyword;

    private List<Long> brokerList;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(int brokerId) {
        this.brokerId = brokerId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRetryType() {
        return retryType;
    }

    public void setRetryType(String retryType) {
        this.retryType = retryType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Long> getBrokerList() {
        return brokerList;
    }

    public void setBrokerList(List<Long> brokerList) {
        this.brokerList = brokerList;
    }
}