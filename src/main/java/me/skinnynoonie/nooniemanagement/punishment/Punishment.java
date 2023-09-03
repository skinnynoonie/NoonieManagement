package me.skinnynoonie.nooniemanagement.punishment;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.util.IndefiniteDuration;
import me.skinnynoonie.nooniemanagement.util.NameableUser;

import java.util.UUID;

public final class Punishment {

    private final long occurredMillis;
    private final UUID target;
    private final UUID issuer;
    private final String reason;
    private final PunishmentType type;

    private boolean pardoned;
    private UUID pardoner;
    private String pardonReason;

    private final long durationMillis;

    private Punishment(UUID target, UUID issuer, String reason, PunishmentType type, boolean pardoned, UUID pardoner, String pardonReason, long durationMillis) {
        this.occurredMillis = System.currentTimeMillis();
        this.target = target;
        this.issuer = issuer;
        this.reason = reason;
        this.type = type;
        this.pardoned = pardoned;
        this.pardoner = pardoner;
        this.pardonReason = pardonReason;
        this.durationMillis = durationMillis;
    }

    public long getOccurredMillis() {
        return this.occurredMillis;
    }

    public NameableUser getTarget() {
        return NameableUser.fromUUID(target);
    }

    public NameableUser getIssuer() {
        return NameableUser.fromUUID(issuer);
    }

    public String getReason() {
        return this.reason;
    }

    public PunishmentType getPunishmentType() {
        return this.type;
    }

    public void pardon(UUID issuer, String reason) {
        this.pardoned = true;
        this.pardoner = issuer;
        this.pardonReason = reason;
    }

    public boolean isPardoned() {
        return this.pardoned;
    }

    public NameableUser getPardoner() {
        if(pardoned) {
            return NameableUser.fromUUID(pardoner);
        }
        return null;
    }

    public String getPardonReason() {
        return this.pardonReason;
    }

    public IndefiniteDuration getDurationMillis() {
        return this.durationMillis < 0 ? IndefiniteDuration.INFINITE : IndefiniteDuration.fromMillis(durationMillis);
    }

    public boolean isActive() {
        return !hasExpiredDuration() && !pardoned;
    }

    public boolean hasExpiredDuration() {
        if(hasPermanentDuration()) return false;
        return System.currentTimeMillis() > occurredMillis + durationMillis;
    }

    public boolean hasPermanentDuration() {
        return durationMillis < 0;
    }

    public static class Builder {

        private UUID target;
        private NameableUser issuer = NameableUser.UNKNOWN;
        private String reason;
        private PunishmentType type;

        private boolean pardoned = false;
        private NameableUser pardoner;
        private String pardonReason;

        private IndefiniteDuration indefiniteDuration = IndefiniteDuration.INFINITE;

        public Punishment build() {
            checkImportantFields();

            return new Punishment(
                    target,
                    issuer.fetchUUID(),
                    reason,
                    type,
                    pardoned,
                    pardoner != null ? pardoner.fetchUUID() : null,
                    pardonReason,
                    indefiniteDuration.isInfinite() ? -1 : indefiniteDuration.getMillis()
            );
        }

        public Builder setTarget(UUID target) {
            this.target = target;
            return this;
        }

        public Builder setIssuer(NameableUser issuer) {
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

        public Builder setPardoner(NameableUser pardoner) {
            this.pardoner = pardoner;
            return this;
        }

        public Builder setPardonReason(String pardonReason) {
            this.pardonReason = pardonReason;
            return this;
        }

        public Builder setDuration(IndefiniteDuration indefiniteDuration) {
            this.indefiniteDuration = indefiniteDuration;
            return this;
        }

        private void checkImportantFields() {
            Preconditions.checkNotNull(target, "Target UUID cannot be null!");
            Preconditions.checkNotNull(issuer, "Issuer cannot be null!");
            Preconditions.checkNotNull(type, "Type cannot be null!");
            boolean invalidPardonState = !pardoned && (pardoner != null || pardonReason != null);
            if(invalidPardonState) {
                throw new IllegalStateException("Punishment cannot have a pardon reason or pardoner while un-pardoned.");
            }
        }

    }

}
