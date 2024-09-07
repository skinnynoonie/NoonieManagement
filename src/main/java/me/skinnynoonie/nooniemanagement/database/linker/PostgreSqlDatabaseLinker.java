package me.skinnynoonie.nooniemanagement.database.linker;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariDataSource;
import me.skinnynoonie.nooniemanagement.database.exception.ConnectionException;
import me.skinnynoonie.nooniemanagement.log.HikariCpLogFilter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class PostgreSqlDatabaseLinker implements DatabaseLinker {
    private final HikariDataSource dataSource;

    public PostgreSqlDatabaseLinker(@NotNull SqlConnectionOptions options) {
        Preconditions.checkArgument(options != null, "options");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("postgresql driver could not be located");
        }

        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl("jdbc:postgresql://" + options.getHost() + ":" + options.getPort() + "/" + options.getDatabaseName());
        this.dataSource.setUsername(options.getUsername());
        this.dataSource.setPassword(options.getPassword());
        HikariCpLogFilter.init();
    }

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
