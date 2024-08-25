CREATE TABLE player_mute_punishment (
    id            INT     NOT NULL PRIMARY KEY,
    target        UUID    NOT NULL,
    issuer        UUID,
    reason        TEXT,
    time_occurred BIGINT  NOT NULL,
    pardoned      BOOLEAN NOT NULL,
    pardoner      UUID,
    pardon_reason TEXT,
    duration      BIGINT  NOT NULL
);