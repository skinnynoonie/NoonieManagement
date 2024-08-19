package me.skinnynoonie.nooniemanagement.database.punishment.repository.postgresql;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.punishment.repository.PlayerMutePunishmentRepository;
import me.skinnynoonie.nooniemanagement.database.source.PostgreSqlDatabaseSource;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class PostgreSqlPlayerMutePunishmentRepository implements PlayerMutePunishmentRepository {
    private final PostgreSqlDatabaseSource databaseSource;
    private final Lock lock;

    public PostgreSqlPlayerMutePunishmentRepository(@NotNull PostgreSqlDatabaseSource databaseSource) {
        Preconditions.checkArgument(databaseSource != null, "databaseSource");

        this.databaseSource = databaseSource;
        this.lock = new ReentrantLock();
    }

    @Override
    public void init() throws DatabaseException {
        this.lock.lock();
        try (
            Connection connection = this.databaseSource.getConnection();
            Statement initStatement = connection.createStatement()
        ) {
            initStatement.execute(
                    """
                    CREATE TABLE IF NOT EXISTS player_mute_punishment (
                        id            INT     NOT NULL PRIMARY KEY,
                        target        UUID    NOT NULL,
                        issuer        UUID,
                        reason        TEXT,
                        time_occurred BIGINT  NOT NULL,
                        pardoned      BOOLEAN NOT NULL,
                        pardoner      UUID,
                        pardon_reason TEXT,
                        duration      BIGINT  NOT NULL
                    );
                    """
            );
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull Saved<PlayerMutePunishment> save(@NotNull PlayerMutePunishment mute) {
        Preconditions.checkArgument(mute != null, "mute");

        this.lock.lock();
        try (
            Connection connection = this.databaseSource.getConnection();
            PreparedStatement findNextIdStatement = connection.prepareStatement("SELECT MAX(id) FROM player_mute_punishment;");
            ResultSet nextIdResult = findNextIdStatement.executeQuery()
        ) {
            nextIdResult.next();
            int nextId = nextIdResult.getInt(1) + 1;
            Saved<PlayerMutePunishment> savedPunishment = new Saved<>(nextId, mute.clone());
            this.save(savedPunishment);
            return savedPunishment;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void save(@NotNull Saved<PlayerMutePunishment> savedMute) {
        Preconditions.checkArgument(savedMute != null, "savedMute");

        this.lock.lock();
        try (
            Connection connection = this.databaseSource.getConnection();
            PreparedStatement updateByIdStatement = connection.prepareStatement(
                    """
                    INSERT INTO player_mute_punishment (
                        id,
                        target,
                        issuer,
                        reason,
                        time_occurred,
                        pardoned,
                        pardoner,
                        pardon_reason,
                        duration
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    ON CONFLICT (id) DO UPDATE
                    SET
                        target        = EXCLUDED.target,
                        issuer        = EXCLUDED.issuer,
                        reason        = EXCLUDED.reason,
                        time_occurred = EXCLUDED.time_occurred,
                        pardoned      = EXCLUDED.pardoned,
                        pardoner      = EXCLUDED.pardoner,
                        pardon_reason = EXCLUDED.pardon_reason,
                        duration      = EXCLUDED.duration;
                    """
            )
        ) {
            PlayerMutePunishment punishment = savedMute.get();
            updateByIdStatement.setInt(1, savedMute.getId());
            updateByIdStatement.setObject(2, punishment.getTarget());
            updateByIdStatement.setObject(3, punishment.getIssuer());
            updateByIdStatement.setString(4, punishment.getReason());
            updateByIdStatement.setLong(5, punishment.getTimeOccurred());
            updateByIdStatement.setBoolean(6, punishment.isPardoned());
            updateByIdStatement.setObject(7, punishment.getPardoner());
            updateByIdStatement.setString(8, punishment.getPardonReason());
            updateByIdStatement.setLong(9, punishment.getDuration());
            updateByIdStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @Nullable Saved<PlayerMutePunishment> findById(int id) throws DatabaseException {
        this.lock.lock();
        try (
            Connection connection = this.databaseSource.getConnection();
            PreparedStatement findByIdStatement = connection.prepareStatement("SELECT * FROM player_mute_punishment WHERE id = ?;")
        ) {
            findByIdStatement.setInt(1, id);
            try (ResultSet findByIdResult = findByIdStatement.executeQuery()) {
                if (findByIdResult.next()) {
                    return this.resultSetToSavedPlayerMute(findByIdResult);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull List<@NotNull Saved<PlayerMutePunishment>> findByTarget(@NotNull UUID target) throws DatabaseException {
        Preconditions.checkArgument(target != null, "target");

        this.lock.lock();
        try (
            Connection connection = this.databaseSource.getConnection();
            PreparedStatement findByTargetStatement = connection.prepareStatement("SELECT * FROM player_mute_punishment WHERE target = ?;")
        ) {
            findByTargetStatement.setObject(1, target);
            try (ResultSet findByTargetResult = findByTargetStatement.executeQuery()) {
                List<Saved<PlayerMutePunishment>> mutePunishments = new ArrayList<>();
                while (findByTargetResult.next()) {
                    mutePunishments.add(this.resultSetToSavedPlayerMute(findByTargetResult));
                }
                return mutePunishments;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            this.lock.unlock();
        }
    }

    private Saved<PlayerMutePunishment> resultSetToSavedPlayerMute(ResultSet resultSet) throws SQLException {
        PlayerMutePunishment mutePunishment = new PlayerMutePunishment(
                (UUID) resultSet.getObject(2),
                (UUID) resultSet.getObject(3),
                resultSet.getString(4),
                resultSet.getLong(5),
                resultSet.getBoolean(6),
                (UUID) resultSet.getObject(7),
                resultSet.getString(8),
                resultSet.getLong(9)
        );
        return new Saved<>(resultSet.getInt(1), mutePunishment);
    }
}
