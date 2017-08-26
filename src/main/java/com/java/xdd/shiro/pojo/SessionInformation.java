package com.java.xdd.shiro.pojo;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/26.
 */
public class SessionInformation implements java.io.Serializable{
    private static final long serialVersionUID = 7704283053313751883L;

    private String sessionId;
    private User user;
    private Date lastRequest;
    private boolean expired;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Date lastRequest) {
        this.lastRequest = lastRequest;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "SessionInformation{" +
                "user=" + user +
                ", lastRequest=" + lastRequest +
                ", expired=" + expired +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
