package me.skinnynoonie.nooniemanagement.duration;

import me.skinnynoonie.nooniemanagement.config.durationformat.StandardDurationFormatConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class StandardDurationParserTest {
    static DurationParser durationParser;

    @BeforeAll
    static void setUp() {
        ConfigurationSection englishConfig = YamlConfiguration.loadConfiguration(new StringReader(
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
        durationParser = new StandardDurationParser(StandardDurationFormatConfig.from(englishConfig));
    }

    @Test
    void parse_failsOnBadArgumentsOnly_englishParser() {
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse(null));
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse("111"));
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse("aaa"));
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse("1day5se_c"));
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse("1day 5sec"));
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse("_"));
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse("day1"));
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse("ha1"));

        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse("1ha"));
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.parse("155day555ha"));

        assertDoesNotThrow(() -> durationParser.parse("1d"));
        assertDoesNotThrow(() -> durationParser.parse("1day1sec1sec1sec5sec5hr5hour5h"));
    }

    @Test
    void parse_works_englishParser() {
        assertEquals(Duration.ZERO.plusDays(1).plusHours(44).plusSeconds(3000), durationParser.parse("1D44hR3000S"));
        assertEquals(Duration.ZERO, durationParser.parse("0daYS0hOUrs000000seCONds"));
    }

    @Test
    void format_works_englishParser() {
        assertThrowsExactly(IllegalArgumentException.class, () -> durationParser.format(null));
        assertEquals("2 days 20 hours 50 minutes 30 seconds", durationParser.format(durationParser.parse("2days20hours50minutes30seconds")));
        assertEquals("59 seconds", durationParser.format(Duration.ofSeconds(59)));
        assertEquals("1 day 1 hour 1 minute 1 second", durationParser.format(durationParser.parse("1day1hour1minute1second")));
        assertEquals("0 seconds", durationParser.format(Duration.ofSeconds(-1)));
        assertEquals("0 seconds", durationParser.format(Duration.ofSeconds(0)));
    }
}
