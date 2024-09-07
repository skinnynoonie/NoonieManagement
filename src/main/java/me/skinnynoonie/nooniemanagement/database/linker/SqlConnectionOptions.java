package me.skinnynoonie.nooniemanagement.database.linker;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.config.database.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public final class SqlConnectionOptions {
    public static @NotNull SqlConnectionOptions fromConfig(@NotNull DatabaseConfig config) {
        Preconditions.checkArgument(config != null, "config");

        return new SqlConnectionOptions(
                config.getHost(),
                config.getPort(),
                config.getName(),
                config.getUsername(),
                config.getPassword()
        );
    }

    public static @NotNull SqlConnectionOptions fromProperties(@NotNull Properties properties) {
        Preconditions.checkArgument(properties != null, "properties");

        return new SqlConnectionOptions(
                properties.getProperty("database.host"),
                properties.getProperty("database.port"),
                properties.getProperty("database.name"),
                properties.getProperty("database.username"),
                properties.getProperty("database.password")
        );
    }

    private final String host;
    private final String port;
    private final String databaseName;
    private final String username;
    private final String password;

    public SqlConnectionOptions(
            @NotNull String host,
            @NotNull String port,
            @NotNull String databaseName,
            @Nullable String username,
            @Nullable String password
    ) {
        Preconditions.checkArgument(host != null, "host");
        Preconditions.checkArgument(port != null, "port");
        Preconditions.checkArgument(databaseName != null, "databaseName");

        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public @NotNull String getHost() {
        return this.host;
    }

    public @NotNull String getPort() {
        return this.port;
    }

    public @NotNull String getDatabaseName() {
        return this.databaseName;
    }

    public @Nullable String getUsername() {
        return this.username;
    }

    public @Nullable String getPassword() {
        return this.password;
    }
}
