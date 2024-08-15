package me.skinnynoonie.nooniemanagement.storage.database.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.skinnynoonie.nooniemanagement.storage.database.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class PostgreSqlDatabaseSource implements DatabaseSource {
    private final HikariDataSource dataSource;

    public PostgreSqlDatabaseSource(DatabaseSourceOptions options) {
        Properties properties = new Properties();
        properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.setProperty("dataSource.username", options.getUsername());
        properties.setProperty("dataSource.password", options.getPassword());
        properties.setProperty("dataSource.serverName", options.getHost());
        properties.setProperty("dataSource.portNumber", options.getPort());
        properties.setProperty("dataSource.databaseName", options.getDatabaseName());

        this.dataSource = new HikariDataSource(new HikariConfig(properties));
    }

    @Override
    public Connection getConnection() throws DatabaseException {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
