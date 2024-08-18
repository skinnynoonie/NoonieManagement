package me.skinnynoonie.nooniemanagement.database.punishment.repository.postgresql;

import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.punishment.repository.PlayerMutePunishmentRepository;
import me.skinnynoonie.nooniemanagement.database.punishment.SavedPunishment;
import me.skinnynoonie.nooniemanagement.database.source.PostgreSqlDatabaseSource;

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

    public PostgreSqlPlayerMutePunishmentRepository(PostgreSqlDatabaseSource databaseSource) {
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
                        target        TEXT    NOT NULL,
                        issuer        TEXT,
                        reason        TEXT,
                        time_occurred BIGINT  NOT NULL,
                        pardoned      BOOLEAN NOT NULL,
                        pardoner      TEXT,
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
    public SavedPunishment<PlayerMutePunishment> save(PlayerMutePunishment punishment) {
        this.lock.lock();
        try (
            Connection connection = this.databaseSource.getConnection();
            PreparedStatement findNextIdStatement = connection.prepareStatement("SELECT COUNT(*) FROM player_mute_punishment;");
            ResultSet nextIdResult = findNextIdStatement.executeQuery()
        ) {
            nextIdResult.next();
            int nextId = nextIdResult.getInt(1) + 1;
            SavedPunishment<PlayerMutePunishment> savedPunishment = new SavedPunishment<>(nextId, punishment);
            this.update(savedPunishment);
            return savedPunishment;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void update(SavedPunishment<PlayerMutePunishment> savedPunishment) {
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
            PlayerMutePunishment punishment = savedPunishment.getPunishment();
            updateByIdStatement.setInt(1, savedPunishment.getId());
            updateByIdStatement.setString(2, punishment.getTarget().toString());
            updateByIdStatement.setString(3, punishment.getIssuer() == null ? null : punishment.getIssuer().toString());
            updateByIdStatement.setString(4, punishment.getReason());
            updateByIdStatement.setLong(5, punishment.getTimeOccurred());
            updateByIdStatement.setBoolean(6, punishment.isPardoned());
            updateByIdStatement.setString(7, punishment.getPardoner() == null ? null : punishment.getPardoner().toString());
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
    public List<SavedPunishment<PlayerMutePunishment>> findByTarget(UUID target) throws DatabaseException {
        this.lock.lock();
        try (
            Connection connection = this.databaseSource.getConnection();
            PreparedStatement findByTargetStatement = connection.prepareStatement("SELECT * FROM player_mute_punishment WHERE target = ?;")
        ) {
            findByTargetStatement.setString(1, target.toString());
            try (ResultSet findByTargetResult = findByTargetStatement.executeQuery()) {
                List<SavedPunishment<PlayerMutePunishment>> mutePunishments = new ArrayList<>();
                while (findByTargetResult.next()) {
                    mutePunishments.add(this.mapResultSetToSavedPunishment(findByTargetResult));
                }
                return mutePunishments;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            this.lock.unlock();
        }
    }

    private SavedPunishment<PlayerMutePunishment> mapResultSetToSavedPunishment(ResultSet resultSet) throws SQLException {
        try {
            PlayerMutePunishment mutePunishment = new PlayerMutePunishment(
                    this.parseUuidSupportingNull(resultSet.getString(2)),
                    this.parseUuidSupportingNull(resultSet.getString(3)),
                    resultSet.getString(4),
                    resultSet.getLong(5),
                    resultSet.getBoolean(6),
                    this.parseUuidSupportingNull(resultSet.getString(7)),
                    resultSet.getString(8),
                    resultSet.getLong(9)
            );
            return new SavedPunishment<>(resultSet.getInt(1), mutePunishment);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        }
    }

    private UUID parseUuidSupportingNull(String string) {
        return string == null ? null : UUID.fromString(string);
    }
}
