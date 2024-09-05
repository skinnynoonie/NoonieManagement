package me.skinnynoonie.nooniemanagement.config.durationformat;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StandardDurationFormatConfigTest {
    static StandardDurationFormatConfig durationFormatConfig;

    @Test
    @Order(1)
    void from_works() {
        ConfigurationSection valid = YamlConfiguration.loadConfiguration(new StringReader(
                """
                duration-format:
                  days:
                    identifiers: ["d", "day", "days"]
                    plural: "days"
                    singular: "day"
                  hours:
                    identifiers: ["h", "hr", "hour", "hours"]
                    plural: "hours"
                    singular: "hour"
                  minutes:
                    identifiers: ["m", "min", "minute", "minutes"]
                    plural: "minutes"
                    singular: "minute"
                  seconds:
                    identifiers: ["s", "sec", "second", "seconds"]
                    plural: "seconds"
                    singular: "second"
                """
        ));
        ConfigurationSection invalidDuplicateIdentifiers = YamlConfiguration.loadConfiguration(new StringReader(
                """
                duration-format:
                  days:
                    identifiers: ["d", "day", "days"]
                    plural: "days"
                    singular: "day"
                  hours:
                    identifiers: ["h", "hr", "hour", "hours"]
                    plural: "hours"
                    singular: "hour"
                  minutes:
                    identifiers: ["m", "min", "minute", "minutes"]
                    plural: "minutes"
                    singular: "minute"
                  seconds:
                    identifiers: ["s", "sec", "second", "m", "seconds"]
                    plural: "seconds"
                    singular: "second"
                """
        ));
        ConfigurationSection invalidNullValues = YamlConfiguration.loadConfiguration(new StringReader(
                """
                duration-format:
                  days:
                    identifiers: ["d", "day", "days"]
                    plural: "days"
                  hours:
                    identifiers: ["h", "hr", "hour", "hours"]
                  minutes:
                    identifiers: ["m", "min", "minute", "minutes"]
                    plural: "minutes"
                  seconds:
                    identifiers: ["s", "sec", "second", "m", "seconds"]
                    plural: "seconds"
                    singular: "second"
                """
        ));

        assertDoesNotThrow(() -> durationFormatConfig = StandardDurationFormatConfig.from(valid));
        assertThrowsExactly(IllegalArgumentException.class, () -> StandardDurationFormatConfig.from(invalidDuplicateIdentifiers));
        assertThrowsExactly(IllegalArgumentException.class, () -> StandardDurationFormatConfig.from(invalidNullValues));
    }

    @Test
    @Order(2)
    void getPluralForm_works() {
        assertThrowsExactly(IllegalArgumentException.class, () -> durationFormatConfig.getPluralForm(TimeUnit.MILLISECONDS));
        assertEquals("days", durationFormatConfig.getPluralForm(TimeUnit.DAYS));
        assertEquals("hours", durationFormatConfig.getPluralForm(TimeUnit.HOURS));
        assertEquals("minutes", durationFormatConfig.getPluralForm(TimeUnit.MINUTES));
        assertEquals("seconds", durationFormatConfig.getPluralForm(TimeUnit.SECONDS));
    }

    @Test
    @Order(3)
    void getSingularForm_works() {
        assertThrowsExactly(IllegalArgumentException.class, () -> durationFormatConfig.getSingularForm(TimeUnit.MILLISECONDS));
        assertEquals("day", durationFormatConfig.getSingularForm(TimeUnit.DAYS));
        assertEquals("hour", durationFormatConfig.getSingularForm(TimeUnit.HOURS));
        assertEquals("minute", durationFormatConfig.getSingularForm(TimeUnit.MINUTES));
        assertEquals("second", durationFormatConfig.getSingularForm(TimeUnit.SECONDS));
    }

    @Test
    @Order(4)
    void getIdentifiers_works() {
        assertThrowsExactly(IllegalArgumentException.class, () -> durationFormatConfig.getIdentifiers(TimeUnit.MILLISECONDS));
        assertEquals(Set.of("d", "day", "days"), durationFormatConfig.getIdentifiers(TimeUnit.DAYS));
        assertEquals(Set.of("h", "hr", "hour", "hours"), durationFormatConfig.getIdentifiers(TimeUnit.HOURS));
        assertEquals(Set.of("m", "min", "minute", "minutes"), durationFormatConfig.getIdentifiers(TimeUnit.MINUTES));
        assertEquals(Set.of("s", "sec", "second", "seconds"), durationFormatConfig.getIdentifiers(TimeUnit.SECONDS));
    }
}