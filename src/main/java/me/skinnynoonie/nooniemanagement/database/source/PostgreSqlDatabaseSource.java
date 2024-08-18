package me.skinnynoonie.nooniemanagement.database.source;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class PostgreSqlDatabaseSource implements DatabaseSource {
    private final HikariDataSource dataSource;

    public PostgreSqlDatabaseSource(@NotNull DatabaseSourceOptions options) {
        Preconditions.checkArgument(options != null, "options");
        Preconditions.checkArgument(options.getHost() != null, "options.getHost()");
        Preconditions.checkArgument(options.getPort() != null, "options.getPort()");
        Preconditions.checkArgument(options.getDatabaseName() != null, "options.getDatabaseName()");
        Preconditions.checkArgument(options.getUsername() != null, "options.getUsername()");
        Preconditions.checkArgument(options.getPassword() != null, "options.getPassword()");

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
    public @NotNull Connection getConnection() throws DatabaseException {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
