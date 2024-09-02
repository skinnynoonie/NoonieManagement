package me.skinnynoonie.nooniemanagement.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DurationUtilTest {
    @Test
    void parse_failsOnBadArgumentsOnly() {
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.parse(null));
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.parse("111"));
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.parse("aaa"));
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.parse("1day5se_c"));
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.parse("1day 5sec"));
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.parse("_"));
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.parse(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.parse("day1"));
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.parse("ha1"));

        assertThrowsExactly(UnsupportedOperationException.class, () -> DurationUtil.parse("1ha"));
        assertThrowsExactly(UnsupportedOperationException.class, () -> DurationUtil.parse("155day555ha"));

        assertDoesNotThrow(() -> DurationUtil.parse("1d"));
        assertDoesNotThrow(() -> DurationUtil.parse("1day1sec1sec1sec5sec5hr5hour5h"));
    }

    @Test
    void parse_works() {
        assertEquals(Duration.ZERO.plusDays(1).plusHours(44).plusSeconds(3000), DurationUtil.parse("1d44hr3000s"));
        assertEquals(Duration.ZERO, DurationUtil.parse("0days0hours000000seconds"));
    }

    @Test
    void format_works() {
        assertThrowsExactly(IllegalArgumentException.class, () -> DurationUtil.format(null));
        assertEquals("2 days 20 hours 50 minutes 30 seconds", DurationUtil.format(DurationUtil.parse("2days20hours50minutes30seconds")));
        assertEquals("59 seconds", DurationUtil.format(Duration.ofSeconds(59)));
        assertEquals("1 day 1 hour 1 minute 1 second", DurationUtil.format(DurationUtil.parse("1day1hour1minute1second")));
        assertEquals("0 seconds", DurationUtil.format(Duration.ofSeconds(-1)));
        assertEquals("0 seconds", DurationUtil.format(Duration.ofSeconds(0)));
    }
}