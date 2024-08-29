package me.skinnynoonie.nooniemanagement.database.punishment.postgresql;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.connection.PostgreSqlConnectionProvider;
import me.skinnynoonie.nooniemanagement.database.punishment.PunishmentService;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class PostgreSqlPunishmentService implements PunishmentService {
    private final PostgreSqlConnectionProvider connectionProvider;
    private final Jdbi jdbi;
    private final PostgreSqlPlayerMutePunishmentRepository playerMutePunishmentRepo;
    private final Lock lock;

    public PostgreSqlPunishmentService(@NotNull PostgreSqlConnectionProvider connectionProvider) {
        Preconditions.checkArgument(connectionProvider != null, "connectionProvider");

        this.connectionProvider = connectionProvider;
        this.jdbi = Jdbi.create(this.connectionProvider.getSource())
                .installPlugin(new PostgresPlugin());
        this.playerMutePunishmentRepo = new PostgreSqlPlayerMutePunishmentRepository(this.jdbi);
        this.lock = new ReentrantLock();
    }

    @Override
    public void init() throws DatabaseException {
        this.lock.lock();
        try {
            Flyway.configure(NoonieManagement.class.getClassLoader())
                    .locations("/db/migration/postgresql/")
                    .dataSource(this.connectionProvider.getSource())
                    .baselineVersion("0")
                    .baselineOnMigrate(true)
                    .load()
                    .migrate();
        } catch (FlywayException e) {
            throw new DatabaseException(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void shutdown() throws DatabaseException {
        this.lock.lock();
        try {
            this.connectionProvider.getSource().close();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @Nullable Saved<PlayerMutePunishment> findPlayerMuteById(int id) throws DatabaseException {
        this.lock.lock();
        try {
            return this.playerMutePunishmentRepo.findById(id);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull PlayerMutePunishmentHistory getPlayerMuteHistory(@NotNull UUID target) throws DatabaseException {
        Preconditions.checkArgument(target != null, "target");

        this.lock.lock();
        try {
            return new PlayerMutePunishmentHistory(this.playerMutePunishmentRepo.findByTarget(target));
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull Saved<PlayerMutePunishment> savePlayerMute(@NotNull PlayerMutePunishment mute) throws DatabaseException {
        Preconditions.checkArgument(mute != null, "mute");

        this.lock.lock();
        try {
            return this.playerMutePunishmentRepo.save(mute);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void savePlayerMute(@NotNull Saved<PlayerMutePunishment> savedMute) throws DatabaseException {
        Preconditions.checkArgument(savedMute != null, "savedMute");

        this.lock.lock();
        try {
            this.playerMutePunishmentRepo.save(savedMute);
        } finally {
            this.lock.unlock();
        }
    }
}
