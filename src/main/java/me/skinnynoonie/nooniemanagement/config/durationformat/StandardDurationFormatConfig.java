package me.skinnynoonie.nooniemanagement.config.durationformat;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.config.AbstractStandardConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class StandardDurationFormatConfig extends AbstractStandardConfig implements DurationFormatConfig {
    private static final Map<TimeUnit, String> timeUnitToConfigTimeName =
            Map.of(
                    TimeUnit.DAYS, "days",
                    TimeUnit.HOURS, "hours",
                    TimeUnit.MINUTES, "minutes",
                    TimeUnit.SECONDS, "seconds"
            );

    public StandardDurationFormatConfig(ConfigurationSection config) {
        super(config);
    }

    @Override
    public @NotNull String getPluralForm(TimeUnit timeUnit) {
        String configTimeName = this.getConfigTimeNameOrThrow(timeUnit);

        return super.config.getString("duration-format."+configTimeName+"plural", "");
    }

    @Override
    public @NotNull String getSingularForm(TimeUnit timeUnit) {
        String configTimeName = this.getConfigTimeNameOrThrow(timeUnit);

        return super.config.getString("duration-format."+configTimeName+"singular", "");
    }

    @Override
    public @NotNull Set<@NotNull String> getIdentifiers(TimeUnit timeUnit) {
        String configTimeName = this.getConfigTimeNameOrThrow(timeUnit);

        return new HashSet<>(super.config.getStringList("duration-format."+configTimeName+"identifiers"));
    }

    private String getConfigTimeNameOrThrow(TimeUnit timeUnit) {
        Preconditions.checkArgument(timeUnit != null, "timeUnit");

        String configTimeName = timeUnitToConfigTimeName.get(timeUnit);
        if (configTimeName == null) {
            throw new IllegalArgumentException("unsupported time unit provided");
        } else {
            return configTimeName;
        }
    }

    @Override
    public boolean isValid() {
        Set<String> allIdentifiers = new HashSet<>();
        int lengthOfAllIdentifiers = 0;

        for (TimeUnit timeUnit : timeUnitToConfigTimeName.keySet()) {
            Set<String> identifiers = this.getIdentifiers(timeUnit);
            allIdentifiers.addAll(identifiers);
            lengthOfAllIdentifiers += identifiers.size();
        }

        return allIdentifiers.size() == lengthOfAllIdentifiers;
    }
}
