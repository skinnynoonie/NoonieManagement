package me.skinnynoonie.nooniemanagement.config;

public interface DatabaseConfig extends Config {
    String getDatabaseType();

    String getHost();

    String getPort();

    String getName();

    String getUsername();

    String getPassword();
}
