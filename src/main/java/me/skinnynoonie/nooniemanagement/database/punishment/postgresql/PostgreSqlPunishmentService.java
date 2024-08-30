package me.skinnynoonie.nooniemanagement.database.punishment.postgresql;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.exception.ConnectionException;
import me.skinnynoonie.nooniemanagement.database.exception.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.linker.PostgreSqlDatabaseLinker;
import me.skinnynoonie.nooniemanagement.database.punishment.PunishmentService;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.CoreErrorCode;
import org.flywaydb.core.api.FlywayException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class PostgreSqlPunishmentService implements PunishmentService {
    private final PostgreSqlDatabaseLinker databaseLinker;
    private final Jdbi jdbi;
    private final PostgreSqlPlayerMutePunishmentRepository playerMutePunishmentRepo;
    private final Lock lock;

    public PostgreSqlPunishmentService(@NotNull PostgreSqlDatabaseLinker databaseLinker) {
        Preconditions.checkArgument(databaseLinker != null, "databaseLinker");

        this.databaseLinker = databaseLinker;
        this.jdbi = Jdbi.create(this.databaseLinker.getSource())
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
                    .dataSource(this.databaseLinker.getSource())
                    .baselineVersion("0")
                    .baselineOnMigrate(true)
                    .load()
                    .migrate();
        } catch (FlywayException e) {
            if (e.getErrorCode() == CoreErrorCode.DB_CONNECTION) {
                throw new ConnectionException(e);
            } else {
                throw new DatabaseException(e);
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void shutdown() throws DatabaseException {
        this.lock.lock();
        try {
            this.databaseLinker.getSource().close();
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
