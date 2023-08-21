package me.skinnynoonie.nooniemanagement.punishment;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public final class Punishment {

    private final long occurred;
    private final UUID target;
    private final UUID issuer;
    private final String reason;
    private final PunishmentType type;

    private boolean pardoned;
    private UUID pardoner;
    private String pardonReason;

    private final long duration;

    private Punishment(UUID target, UUID issuer, String reason, PunishmentType type, boolean pardoned, UUID pardoner, String pardonReason, long duration) {
        //Preconditions.checkState((pardoner != null || pardonReason != null) && !pardoned, "Punishment cannot have a pardon reason or pardoner while be un-pardoned.");
        this.occurred = System.currentTimeMillis();
        this.target = target;
        this.issuer = issuer;
        this.reason = reason;
        this.type = type;
        this.pardoned = pardoned;
        this.pardoner = pardoner;
        this.pardonReason = pardonReason;
        this.duration = duration;
    }

    /**
     * @return Time in milliseconds this punishment occurred.
     * @see System#currentTimeMillis()
     */
    public long getOccurred() {
        return this.occurred;
    }

    /** @return UUID of the target for this punishment (the punished, the victim, etc.). */
    @Nonnull
    public UUID getTarget() {
        return this.target;
    }

    /** @return UUID of the issuer who issued this punishment. Will return null if the issuer is not a player. */
    @Nullable
    public UUID getIssuer() {
        return this.issuer;
    }

    /** @return Reason associated with this punishment. */
    @Nullable
    public String getReason() {
        return this.reason;
    }

    /** @return The type of this punishment. */
    @Nonnull
    public PunishmentType getType() {
        return this.type;
    }

    /** Pardons this punishment. */
    public void pardon(UUID issuer, String reason) {
        this.pardoned = true;
        this.pardoner = issuer;
        this.pardonReason = reason;
    }

    /** @return true if this punishment has been pardoned, otherwise false. */
    public boolean isPardoned() {
        return this.pardoned;
    }

    /** @return UUID of the pardoner for this punishment. Will return null if the pardoner is not a player. */
    @Nullable
    public UUID getPardoner() {
        return this.pardoner;
    }

    /** @return Reason for this punishment's pardon. */
    @Nullable
    public String getPardonReason() {
        return this.pardonReason;
    }

    /** @return Duration in milliseconds. This will return -1 if this punishment has no duration, or is permanent. */
    public long getDuration() {
        return this.duration;
    }

    /** @return true if this punishment is permanent, otherwise false. Returns true if this punishment has no duration. */
    public boolean isPermanent() {
        return this.duration == -1;
    }

    public static class Builder {

        private UUID target;
        private UUID issuer;
        private String reason;
        private PunishmentType type;

        private boolean pardoned;
        private UUID pardoner;
        private String pardonReason;

        private long duration;

        public Builder() {
            pardoned = false;
            duration = -1;
        }

        public Punishment build() {
            Preconditions.checkNotNull(target, "Target UUID cannot be null!");
            Preconditions.checkNotNull(type, "Type cannot be null!");

            // todo: Add checks to the pardon fields
            return new Punishment(
                    target,
                    issuer,
                    reason,
                    type,
                    pardoned,
                    pardoner,
                    pardonReason,
                    duration
            );
        }

        public Builder setTarget(@Nonnull UUID target) {
            this.target = target;
            return this;
        }

        public Builder setIssuer(@Nullable UUID issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder setReason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder setType(PunishmentType type) {
            this.type = type;
            return this;
        }

        public Builder setPardoned(boolean pardoned) {
            this.pardoned = pardoned;
            return this;
        }

        public Builder setPardoner(UUID pardoner) {
            this.pardoner = pardoner;
            return this;
        }

        public Builder setPardonReason(String pardonReason) {
            this.pardonReason = pardonReason;
            return this;
        }

        public Builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder setPermanent() {
            this.duration = -1;
            return this;
        }

    }

}
