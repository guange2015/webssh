package com.juunew.model;

public class ConnectInfo {
    private String host;
    private String port;
    private String username;
    private String secret;
    private String gameid;
    private String Rows;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return Integer.parseInt(port);
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getGameid() {
        return Integer.parseInt(gameid);
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getRows() {
        return Rows;
    }

    public void setRows(String rows) {
        Rows = rows;
    }
}
