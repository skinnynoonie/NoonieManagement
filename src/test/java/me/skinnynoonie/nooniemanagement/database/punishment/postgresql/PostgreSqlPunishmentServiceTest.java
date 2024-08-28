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
import java.util.Map;
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
    @Order(3)
    void testSavePlayerMute__findPlayerMuteById_worksOnNonNulls_returnsClone_incrementsPunishmentsProperly() {
        PlayerMutePunishment muteToSave = new PlayerMutePunishment(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "reason!",
                System.currentTimeMillis(),
                true,
                UUID.randomUUID(),
                "pardon!",
                Duration.ofSeconds(1234)
        );

        Saved<PlayerMutePunishment> savedMute = service.savePlayerMute(muteToSave);
        assertEquals(1, savedMute.getId());
        assertNotSame(muteToSave, savedMute.get());
        assertEquals(muteToSave, savedMute.get());

        service.savePlayerMute(new Saved<>(100, muteToSave));
        Saved<PlayerMutePunishment> savedMuteSkipped98Ids = service.findPlayerMuteById(100);
        assertNotNull(savedMuteSkipped98Ids);
        assertEquals(100, savedMuteSkipped98Ids.getId());
        assertEquals(muteToSave, savedMuteSkipped98Ids.get());
    }

    @Test
    @Order(4)
    void testSavePlayerMute_findPlayerMuteById_worksOnNulls_returnsClone_incrementsPunishmentsProperly() {
        PlayerMutePunishment muteToSave = new PlayerMutePunishment(
                UUID.randomUUID(),
                null,
                null,
                System.currentTimeMillis(),
                false,
                null,
                null,
                Duration.ofSeconds(-1)
        );

        Saved<PlayerMutePunishment> savedMute = service.savePlayerMute(muteToSave);
        assertEquals(101, savedMute.getId());
        assertNotSame(muteToSave, savedMute.get());
        assertEquals(muteToSave, savedMute.get());

        service.savePlayerMute(new Saved<>(200, muteToSave));
        Saved<PlayerMutePunishment> savedMuteSkipped98Ids = service.findPlayerMuteById(200);
        assertNotNull(savedMuteSkipped98Ids);
        assertEquals(200, savedMuteSkipped98Ids.getId());
        assertEquals(muteToSave, savedMuteSkipped98Ids.get());
    }

    @Test
    @Order(5)
    void testGetPlayerMuteHistory_works_returnsClones() {
        UUID target = UUID.randomUUID();
        PlayerMutePunishment muteToSave = new PlayerMutePunishment(
                target,
                UUID.randomUUID(),
                "reason!",
                System.currentTimeMillis(),
                true,
                UUID.randomUUID(),
                "pardon!",
                Duration.ofSeconds(1234)
        );

        service.savePlayerMute(muteToSave);
        service.savePlayerMute(muteToSave);
        service.savePlayerMute(new Saved<>(300, muteToSave));

        Map<Integer, PlayerMutePunishment> muteMap = service.getPlayerMuteHistory(target).getPunishmentsMap();
        assertEquals(3, muteMap.size());
        assertTrue(muteMap.containsKey(201));
        assertTrue(muteMap.containsKey(202));
        assertTrue(muteMap.containsKey(300));

        assertNotSame(muteToSave, muteMap.get(201));
        assertEquals(muteToSave, muteMap.get(201));

        assertNotSame(muteToSave, muteMap.get(202));
        assertEquals(muteToSave, muteMap.get(202));

        assertNotSame(muteToSave, muteMap.get(300));
        assertEquals(muteToSave, muteMap.get(300));
    }

    @Test
    @Order(1000)
    void shutdown_doesNotThrow() {
        assertDoesNotThrow(service::shutdown);
    }
}