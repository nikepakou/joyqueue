package com.jd.journalq.network.command;

import com.jd.journalq.network.session.ClientId;
import com.jd.journalq.network.session.Language;
import com.jd.journalq.network.transport.command.JMQPayload;

/**
 * AddConnection
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/11/29
 */
public class AddConnection extends JMQPayload {

    private String username;
    private String password;
    private String token;
    private String app;
    private String region;
    private String namespace;
    private Language language = Language.JAVA;
    private ClientId clientId;

    @Override
    public int type() {
        return JMQCommandType.ADD_CONNECTION.getCode();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ClientId getClientId() {
        return clientId;
    }

    public void setClientId(ClientId clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "AddConnection{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", app='" + app + '\'' +
                ", region='" + region + '\'' +
                ", namespace='" + namespace + '\'' +
                ", language=" + language +
                ", clientId=" + clientId +
                '}';
    }
}