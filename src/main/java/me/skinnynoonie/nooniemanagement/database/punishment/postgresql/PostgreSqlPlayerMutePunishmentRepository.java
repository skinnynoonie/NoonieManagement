package me.skinnynoonie.nooniemanagement.database.punishment.postgresql;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.punishment.PlayerMutePunishmentRepository;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;
import org.jdbi.v3.core.statement.StatementContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public final class PostgreSqlPlayerMutePunishmentRepository implements PlayerMutePunishmentRepository {
    private final Jdbi jdbi;

    public PostgreSqlPlayerMutePunishmentRepository(@NotNull Jdbi jdbi) {
        Preconditions.checkArgument(jdbi != null, "jdbi");

        this.jdbi = jdbi;
    }

    @Override
    public @Nullable Saved<PlayerMutePunishment> findById(int id) throws DatabaseException {
        try {
            return this.jdbi.withHandle(handle -> {
                return handle.createQuery("SELECT * FROM player_mute_punishment WHERE id = :id;")
                        .bind("id", id)
                        .map(this::resultSetToSavedPlayerMute)
                        .findFirst()
                        .orElse(null);
            });
        } catch (JdbiException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public @NotNull List<@NotNull Saved<PlayerMutePunishment>> findByTarget(@NotNull UUID target) throws DatabaseException {
        Preconditions.checkArgument(target != null, "target");

        try {
            return this.jdbi.withHandle(handle -> {
                return handle.createQuery("SELECT * FROM player_mute_punishment WHERE target = :target;")
                        .bind("target", target)
                        .map(this::resultSetToSavedPlayerMute)
                        .collectIntoList();
            });
        } catch (JdbiException e) {
            throw new DatabaseException(e);
        }
    }

    private Saved<PlayerMutePunishment> resultSetToSavedPlayerMute(ResultSet resultSet, StatementContext ctx) throws SQLException {
        PlayerMutePunishment mutePunishment = new PlayerMutePunishment(
                (UUID) resultSet.getObject("target"),
                (UUID) resultSet.getObject("issuer"),
                resultSet.getString("reason"),
                resultSet.getLong("time_occurred"),
                resultSet.getBoolean("pardoned"),
                (UUID) resultSet.getObject("pardoner"),
                resultSet.getString("pardon_reason"),
                Duration.ofSeconds(resultSet.getLong("duration"))
        );
        return new Saved<>(resultSet.getInt("id"), mutePunishment);
    }

    @Override
    public @NotNull Saved<PlayerMutePunishment> save(@NotNull PlayerMutePunishment mute) {
        Preconditions.checkArgument(mute != null, "mute");

        try {
            return this.jdbi.withHandle(handle -> {
                int nextId = handle.createQuery("SELECT COALESCE(MAX(id), 0) + 1 FROM player_mute_punishment;")
                        .mapTo(Integer.class)
                        .findOne()
                        .orElseThrow();
                Saved<PlayerMutePunishment> savedMute = new Saved<>(nextId, mute.clone());
                this.saveUsingHandle(savedMute, handle);
                return savedMute;
            });
        } catch (IllegalStateException | JdbiException | NoSuchElementException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void save(@NotNull Saved<PlayerMutePunishment> savedMute) {
        Preconditions.checkArgument(savedMute != null, "savedMute");

        try {
            this.jdbi.useHandle(handle -> this.saveUsingHandle(savedMute, handle));
        } catch (JdbiException e) {
            throw new DatabaseException(e);
        }
    }

    private void saveUsingHandle(Saved<PlayerMutePunishment> savedMute, Handle handle) {
        handle.createUpdate("DELETE FROM player_mute_punishment WHERE id = :id;")
                .bind("id", savedMute.getId())
                .execute();

        PlayerMutePunishment mute = savedMute.get();
        handle.createUpdate(
                    """
                    INSERT INTO player_mute_punishment (
                        id,  target,  issuer,  reason,  time_occurred,  pardoned,  pardoner,  pardon_reason,  duration
                    )
                    VALUES (
                        :id, :target, :issuer, :reason, :time_occurred, :pardoned, :pardoner, :pardon_reason, :duration
                    );
                    """
                )
                .bind("id", savedMute.getId())
                .bind("target", mute.getTarget())
                .bind("issuer", mute.getIssuer())
                .bind("reason", mute.getReason())
                .bind("time_occurred", mute.getTimeOccurred())
                .bind("pardoned", mute.isPardoned())
                .bind("pardoner", mute.getPardoner())
                .bind("pardon_reason", mute.getPardonReason())
                .bind("duration", mute.getDuration().toSeconds())
                .execute();
    }
}
