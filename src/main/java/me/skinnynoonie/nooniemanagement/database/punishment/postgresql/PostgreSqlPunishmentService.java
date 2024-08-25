package me.skinnynoonie.nooniemanagement.database.punishment.postgresql;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.connection.PostgreSqlConnectionProvider;
import me.skinnynoonie.nooniemanagement.database.punishment.PunishmentService;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerPunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class PostgreSqlPunishmentService implements PunishmentService {
    private final PostgreSqlConnectionProvider connectionProvider;
    private final PostgreSqlPlayerMutePunishmentRepository playerMutePunishmentRepo;
    private final Lock lock;

    public PostgreSqlPunishmentService(@NotNull PostgreSqlConnectionProvider connectionProvider) {
        Preconditions.checkArgument(connectionProvider != null, "connectionProvider");

        this.connectionProvider = connectionProvider;
        this.playerMutePunishmentRepo = new PostgreSqlPlayerMutePunishmentRepository(connectionProvider);
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
    public @NotNull PlayerPunishmentHistory getPlayerHistory(@NotNull UUID target) throws DatabaseException {
        this.lock.lock();
        try {
            return new PlayerPunishmentHistory(
                    new PlayerMutePunishmentHistory(this.playerMutePunishmentRepo.findByTarget(target))
            );
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull PlayerMutePunishmentHistory getPlayerMuteHistory(@NotNull UUID target) throws DatabaseException {
        this.lock.lock();
        try {
            return new PlayerMutePunishmentHistory(this.playerMutePunishmentRepo.findByTarget(target));
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull Saved<PlayerMutePunishment> savePlayerMute(@NotNull PlayerMutePunishment mute) throws DatabaseException {
        this.lock.lock();
        try {
            return this.playerMutePunishmentRepo.save(mute);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void savePlayerMute(@NotNull Saved<PlayerMutePunishment> savedMute) throws DatabaseException {
        this.lock.lock();
        try {
            this.playerMutePunishmentRepo.save(savedMute);
        } finally {
            this.lock.unlock();
        }
    }
}
