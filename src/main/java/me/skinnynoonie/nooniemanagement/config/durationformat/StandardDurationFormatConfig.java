package me.skinnynoonie.nooniemanagement.config.durationformat;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class StandardDurationFormatConfig implements DurationFormatConfig {
    private static final Map<TimeUnit, String> timeUnitToConfigTimeName =
            Map.of(
                    TimeUnit.DAYS, "days",
                    TimeUnit.HOURS, "hours",
                    TimeUnit.MINUTES, "minutes",
                    TimeUnit.SECONDS, "seconds"
            );

    public static StandardDurationFormatConfig from(@NotNull ConfigurationSection config) {
        Preconditions.checkArgument(config != null, "config");

        Map<TimeUnit, String> timeUnitPluralNames = new HashMap<>();
        Map<TimeUnit, String> timeUnitSingularNames = new HashMap<>();
        Map<TimeUnit, Set<String>> timeUnitIdentifiers = new HashMap<>();

        for (TimeUnit timeUnit : timeUnitToConfigTimeName.keySet()) {
            String configTimeName = timeUnitToConfigTimeName.get(timeUnit);
            timeUnitPluralNames.put(timeUnit, config.getString("duration-format." + configTimeName + ".plural"));
            timeUnitSingularNames.put(timeUnit, config.getString("duration-format." + configTimeName + ".singular"));
            timeUnitIdentifiers.put(
                    timeUnit,
                    new HashSet<>(config.getStringList("duration-format." + configTimeName + ".identifiers"))
            );
        }

        return new StandardDurationFormatConfig(timeUnitPluralNames, timeUnitSingularNames, timeUnitIdentifiers);
    }

    private final Map<TimeUnit, String> timeUnitPluralNames;
    private final Map<TimeUnit, String> timeUnitSingularNames;
    private final Map<TimeUnit, Set<String>> timeUnitIdentifiers;

    public StandardDurationFormatConfig(
            @NotNull Map<@NotNull TimeUnit, @NotNull String> timeUnitPluralNames,
            @NotNull Map<@NotNull TimeUnit, @NotNull String> timeUnitSingularNames,
            @NotNull Map<@NotNull TimeUnit, @NotNull Set<@NotNull String>> timeUnitIdentifiers
    ) {
        Preconditions.checkArgument(timeUnitPluralNames != null, "timeUnitPluralNames");
        Preconditions.checkArgument(timeUnitSingularNames != null, "timeUnitSingularNames");
        Preconditions.checkArgument(timeUnitIdentifiers != null, "timeUnitIdentifiers");
        throwIfMapDoesNotSupportTimeUnits(timeUnitPluralNames, "timeUnitPluralNames");
        throwIfMapDoesNotSupportTimeUnits(timeUnitSingularNames, "timeUnitSingularNames");
        throwIfMapDoesNotSupportTimeUnits(timeUnitIdentifiers, "timeUnitIdentifiers");

        this.timeUnitPluralNames = new ConcurrentHashMap<>(timeUnitPluralNames);
        this.timeUnitSingularNames = new ConcurrentHashMap<>(timeUnitSingularNames);
        this.timeUnitIdentifiers = new ConcurrentHashMap<>();

        // Copy all Sets provided in the provided timeUnitIdentifiers variable.
        for (TimeUnit timeUnit : timeUnitToConfigTimeName.keySet()) {
            Set<String> set = ConcurrentHashMap.newKeySet();
            set.addAll(timeUnitIdentifiers.get(timeUnit));
            this.timeUnitIdentifiers.put(timeUnit, set);
        }

        Set<String> set = new HashSet<>();
        this.timeUnitIdentifiers.values().stream().flatMap(Set::stream).forEach(identifier -> {
            if (!set.add(identifier)) {
                throw new IllegalArgumentException("duplicate identifier " + identifier + " found; duplicate identifiers are not permitted");
            }
        });
    }

    private static void throwIfMapDoesNotSupportTimeUnits(Map<TimeUnit, ?> timeUnitMap, String argName) {
        boolean hasSameKeySet = timeUnitMap.keySet().equals(timeUnitToConfigTimeName.keySet());
        if (!hasSameKeySet) {
            throw new IllegalArgumentException(argName + " does not support the proper time units");
        } else if (timeUnitMap.containsValue(null)) {
            throw new IllegalArgumentException(argName + " should not have null values");
        }
    }

    @Override
    public @NotNull String getPluralForm(@NotNull TimeUnit timeUnit) {
        throwIfUnsupportedTimeUnit(timeUnit);

        return this.timeUnitPluralNames.get(timeUnit);
    }

    @Override
    public @NotNull String getSingularForm(@NotNull TimeUnit timeUnit) {
        throwIfUnsupportedTimeUnit(timeUnit);

        return this.timeUnitSingularNames.get(timeUnit);
    }

    @Override
    public @NotNull Set<@NotNull String> getIdentifiers(@NotNull TimeUnit timeUnit) {
        throwIfUnsupportedTimeUnit(timeUnit);

        return this.timeUnitIdentifiers.get(timeUnit);
    }

    private static void throwIfUnsupportedTimeUnit(TimeUnit timeUnit) {
        Preconditions.checkArgument(timeUnit != null, "timeUnit");
        Preconditions.checkArgument(timeUnitToConfigTimeName.containsKey(timeUnit), "timeUnit %s is not supported", timeUnit);
    }
}
