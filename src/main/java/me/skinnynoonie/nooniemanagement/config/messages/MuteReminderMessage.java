package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.util.IndefiniteDuration;

@DefaultMessageConfigValue(defaultValue =
        """
        <gray>You are currently muted!
        
        <white>Reason: <aqua>{reason}
        <white>Duration: <aqua>{duration}
        """)
public class MuteReminderMessage extends ConfigurableMessage {

    private final Punishment punishment;

    public MuteReminderMessage(Punishment punishment) {
        this.punishment = punishment;
    }

    @Override
    protected String getFormatted() {
        String displayDuration = getDisplayDurationLeft(punishment.getDurationMillis());
        String displayReason = punishment.getReason();
        return getUnformatted()
                .replace("{reason}", displayReason)
                .replace("{duration}", displayDuration);
    }

    private String getDisplayDurationLeft(IndefiniteDuration duration) {
        if(duration.isInfinite()) return "Permanent";
        long timeLeftOfPunishmentMillis = calculateTimeLeftOfPunishmentMillis(duration);
        return IndefiniteDuration.fromMillis(timeLeftOfPunishmentMillis)
                .getFormatted("Permanent"); // Should not be possible but who knows.
    }

    private long calculateTimeLeftOfPunishmentMillis(IndefiniteDuration duration) {
        long punishmentDurationMillis = duration.getMillis();
        long timePunishmentOccurredMillis = punishment.getOccurredMillis();
        long timePunishmentExpiresMillis = timePunishmentOccurredMillis + punishmentDurationMillis;
        long timeLeftOfPunishmentMillis = timePunishmentExpiresMillis - System.currentTimeMillis();
        return timeLeftOfPunishmentMillis;
    }

}
