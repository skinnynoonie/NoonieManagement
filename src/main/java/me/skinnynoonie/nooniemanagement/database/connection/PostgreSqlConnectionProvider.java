package me.skinnynoonie.nooniemanagement.database.connection;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class PostgreSqlConnectionProvider implements ConnectionProvider {
    private final HikariDataSource dataSource;

    public PostgreSqlConnectionProvider(@NotNull ConnectionProviderOptions options) throws ConnectionException {
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

        try {
            this.dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new ConnectionException(e);
        }
    }

    @Override
    public @NotNull Connection getConnection() throws ConnectionException {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

    public @NotNull HikariDataSource getSource() {
        return this.dataSource;
    }
}
