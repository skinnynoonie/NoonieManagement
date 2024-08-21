package me.skinnynoonie.nooniemanagement.database.connection;

import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;

public interface ConnectionProvider {
    @NotNull Connection getConnection() throws ConnectionException;

    @NotNull DataSource getSource();
}
