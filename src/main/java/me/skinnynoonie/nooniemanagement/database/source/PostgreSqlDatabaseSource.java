package me.skinnynoonie.nooniemanagement.database.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.skinnynoonie.nooniemanagement.database.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;

public final class PostgreSqlDatabaseSource implements DatabaseSource {
    private final HikariDataSource dataSource;

    public PostgreSqlDatabaseSource(DatabaseSourceOptions options) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("postgresql driver could not be located");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + options.getHost() + ":" + options.getPort() + "/" + options.getDatabaseName());
        config.setUsername(options.getUsername());
        config.setPassword(options.getPassword());

        this.dataSource = new HikariDataSource(config);
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
