package me.skinnynoonie.nooniemanagement.punishment;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.UUID;

public record PunishmentPortfolio(UUID uuid, List<Punishment> punishments) {

    public PunishmentPortfolio {
        Preconditions.checkNotNull(punishments, "Punishments cannot be null inside a portfolio!");
    }

}
