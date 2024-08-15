package me.skinnynoonie.nooniemanagement.config;

public interface DatabaseConfig extends Config {
    String getDatabaseType();

    String getUsername();

    String getPassword();

    String getHost();

    String getPort();
}
