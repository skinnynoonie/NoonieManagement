package me.skinnynoonie.nooniemanagement.database.punishment.postgresql;

import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.connection.ConnectionProviderOptions;
import me.skinnynoonie.nooniemanagement.database.connection.PostgreSqlConnectionProvider;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostgreSqlPunishmentServiceTest {

    static PostgreSqlConnectionProvider connectionProvider;
    static PostgreSqlPunishmentService service;

    @BeforeAll
    static void setUp() throws Exception {
        Properties properties = new Properties();
        ClassLoader classLoader = PostgreSqlPunishmentServiceTest.class.getClassLoader();
        InputStream propertyStream = classLoader.getResourceAsStream("properties/postgresql/properties.properties");
        properties.load(new InputStreamReader(propertyStream));

        connectionProvider = new PostgreSqlConnectionProvider(ConnectionProviderOptions.fromProperties(properties));

        try (
            Connection connection = connectionProvider.getConnection();
            Statement statement = connection.createStatement()
        ) {
            statement.execute("DROP SCHEMA IF EXISTS PUBLIC CASCADE; CREATE SCHEMA IF NOT EXISTS PUBLIC;");
        }
    }

    @Test
    @Order(1)
    void constructor_throwsIfNullArgs_doesNotThrowIfValidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new PostgreSqlPunishmentService(null));
        assertDoesNotThrow(() -> service = new PostgreSqlPunishmentService(connectionProvider));
    }

    @Test
    @Order(2)
    void init_doesNotThrow() {
        assertDoesNotThrow(service::init);
    }

    @Test
    @Order(1000)
    void shutdown_doesNotThrow() {
        assertDoesNotThrow(service::shutdown);
    }

    @Test
    @Order(3)
    void testSavePlayerMute_testGetPlayerHistory_testGetPlayerMuteHistory_worksOnNonNulls() {
        UUID target = UUID.randomUUID();
        UUID issuer = UUID.randomUUID();
        String reason = "reason!";
        long timeOccurred = System.currentTimeMillis();
        boolean pardoned = true;
        UUID pardoner = UUID.randomUUID();
        String pardonReason = "pardon!";
        Duration duration = Duration.ofSeconds(1234);

        PlayerMutePunishment muteToSave = new PlayerMutePunishment(
                target,
                issuer,
                reason,
                timeOccurred,
                pardoned,
                pardoner,
                pardonReason,
                duration
        );
        Saved<PlayerMutePunishment> savedMute = service.savePlayerMute(muteToSave);
        PlayerMutePunishment mute = savedMute.get();

        assertEquals(1, savedMute.getId());
        assertNotSame(muteToSave, savedMute.get()); // They can have the same fields, but not be the same reference.

        assertEquals(target, mute.getTarget());
        assertEquals(issuer, mute.getIssuer());
        assertEquals(reason, mute.getReason());
        assertEquals(timeOccurred, mute.getTimeOccurred());
        assertEquals(pardoned, mute.isPardoned());
        assertEquals(pardoner, mute.getPardoner());
        assertEquals(pardonReason, mute.getPardonReason());
        assertEquals(duration, mute.getDuration());
    }

    @Test
    @Order(4)
    void testSavePlayerMute_testGetPlayerHistory_testGetPlayerMuteHistory_worksOnNulls() {
        UUID target = UUID.randomUUID();
        UUID issuer = null;
        String reason = null;
        long timeOccurred = System.currentTimeMillis();
        boolean pardoned = false;
        UUID pardoner = null;
        String pardonReason = null;
        Duration duration = Duration.ofSeconds(12341);

        PlayerMutePunishment muteToSave = new PlayerMutePunishment(
                target,
                issuer,
                reason,
                timeOccurred,
                pardoned,
                pardoner,
                pardonReason,
                duration
        );
        Saved<PlayerMutePunishment> savedMute = service.savePlayerMute(muteToSave);
        PlayerMutePunishment mute = savedMute.get();

        assertEquals(2, savedMute.getId());
        assertNotSame(muteToSave, savedMute.get()); // They can have the same fields, but not be the same reference.

        assertEquals(target, mute.getTarget());
        assertEquals(issuer, mute.getIssuer());
        assertEquals(reason, mute.getReason());
        assertEquals(timeOccurred, mute.getTimeOccurred());
        assertEquals(pardoned, mute.isPardoned());
        assertEquals(pardoner, mute.getPardoner());
        assertEquals(pardonReason, mute.getPardonReason());
        assertEquals(duration, mute.getDuration());
    }
}