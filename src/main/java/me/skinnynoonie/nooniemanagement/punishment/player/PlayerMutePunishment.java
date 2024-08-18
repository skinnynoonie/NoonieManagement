package me.skinnynoonie.nooniemanagement.punishment.player;

import me.skinnynoonie.nooniemanagement.punishment.ActivePunishment;
import me.skinnynoonie.nooniemanagement.punishment.DurationalPunishment;
import me.skinnynoonie.nooniemanagement.punishment.PardonablePunishment;

import java.util.UUID;

public final class PlayerMutePunishment implements PlayerPunishment, PardonablePunishment, DurationalPunishment, ActivePunishment {
    public static final String TYPE = "player_mute";
    private final UUID target;
    private final UUID issuer;
    private final String reason;
    private final long timeOccurred;
    private boolean pardoned;
    private UUID pardoner;
    private String pardonReason;
    private final long duration;

    public PlayerMutePunishment(
            UUID target,
            UUID issuer,
            String reason,
            long duration
    ) {
        this(target, issuer, reason, System.currentTimeMillis(), false, null, null, duration);
    }

    public PlayerMutePunishment(
            UUID target,
            UUID issuer,
            String reason,
            long timeOccurred,
            boolean pardoned,
            UUID pardoner,
            String pardonReason,
            long duration
    ) {
        this.target = target;
        this.issuer = issuer;
        this.reason = reason;
        this.timeOccurred = timeOccurred;
        this.pardoned = pardoned;
        this.pardoner = pardoner;
        this.pardonReason = pardonReason;
        this.duration = duration;
    }

    @Override
    public UUID getTarget() {
        return this.target;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public UUID getIssuer() {
        return this.issuer;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public long getTimeOccurred() {
        return this.timeOccurred;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public UUID getPardoner() {
        return this.pardoner;
    }

    @Override
    public boolean isPardoned() {
        return this.pardoned;
    }

    @Override
    public synchronized void pardon(UUID pardoner, String reason) {
        if (!this.pardoned) {
            this.pardoned = true;
            this.pardoner = pardoner;
            this.pardonReason = reason;
        }
    }

    @Override
    public String getPardonReason() {
        return this.pardonReason;
    }

    @Override
    public boolean isActive() {
        boolean timeActive = this.isPermanent() || this.timeOccurred + this.duration > System.currentTimeMillis();
        return !this.isPardoned() && timeActive;
    }
}
