package me.skinnynoonie.nooniemanagement.punishment;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.UUID;

public record PunishmentPortfolio(UUID uuid, List<Punishment> punishments) {

    public PunishmentPortfolio {
        Preconditions.checkNotNull(punishments, "Punishments cannot be null inside a portfolio!");
    }

    // todo: make a pricate method like getPunishmentsOfType(Type), and getCurrentPunishmentOfType(Type)
    // todo: then wrap these with getCurrentBan, Mute, etc, getAllBans, gwtAllMutes, etc. 
    public Punishment getCurrentBan() {
        return punishments.stream()
                .filter(p -> p.getPunishmentType() == PunishmentType.BAN)
                .filter(Punishment::isActive)
                .findFirst()
                .orElse(null);
    }

}
