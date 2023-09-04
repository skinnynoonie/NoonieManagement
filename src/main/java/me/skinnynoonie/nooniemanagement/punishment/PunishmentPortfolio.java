package me.skinnynoonie.nooniemanagement.punishment;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record PunishmentPortfolio(UUID uuid, List<Punishment> punishments) {

    public PunishmentPortfolio {
        Preconditions.checkNotNull(punishments, "Punishments cannot be null inside a portfolio!");
    }

    public Punishment getCurrentBan() {
        return getCurrentPunishmentOfType(PunishmentType.BAN);
    }

    public Punishment getCurrentMute() {
        return getCurrentPunishmentOfType(PunishmentType.MUTE);
    }

    public List<Punishment> getAllBans() {
        return getCurrentPunishmentsOfType(PunishmentType.BAN);
    }

    public List<Punishment> getAllMutes() {
        return getCurrentPunishmentsOfType(PunishmentType.MUTE);
    }

    private Punishment getCurrentPunishmentOfType(PunishmentType type) {
        return punishments.stream()
                .filter(p -> p.getPunishmentType() == type)
                .filter(Punishment::isActive)
                .findFirst()
                .orElse(null);
    }

    private List<Punishment> getCurrentPunishmentsOfType(PunishmentType type) {
        return punishments.stream()
                .filter(p -> p.getPunishmentType() == type)
                .collect(Collectors.toList());
    }

}
