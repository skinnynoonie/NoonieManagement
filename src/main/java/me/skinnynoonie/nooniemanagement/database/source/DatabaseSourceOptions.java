package me.skinnynoonie.nooniemanagement.database.source;

import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;

public final class DatabaseSourceOptions {
    public static DatabaseSourceOptions fromConfig(DatabaseConfig config) {
        return new DatabaseSourceOptions()
                .setHost(config.getHost())
                .setPort(config.getPort())
                .setDatabaseName(config.getName())
                .setUsername(config.getUsername())
                .setPassword(config.getPassword());
    }

    private String username;
    private String password;
    private String host;
    private String port;
    private String databaseName;

    public DatabaseSourceOptions() {
    }

    public String getUsername() {
        return this.username;
    }

    public DatabaseSourceOptions setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    public DatabaseSourceOptions setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHost() {
        return this.host;
    }

    public DatabaseSourceOptions setHost(String host) {
        this.host = host;
        return this;
    }

    public String getPort() {
        return this.port;
    }

    public DatabaseSourceOptions setPort(String port) {
        this.port = port;
        return this;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public DatabaseSourceOptions setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }
}
