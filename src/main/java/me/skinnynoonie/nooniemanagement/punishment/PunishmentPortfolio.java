package me.skinnynoonie.nooniemanagement.punishment;

import com.google.common.base.Preconditions;

import java.util.List;

public record PunishmentPortfolio(List<Punishment> punishments) {

    public PunishmentPortfolio {
        Preconditions.checkNotNull(punishments, "Punishments cannot be null inside a portfolio!");
    }

    public List<Punishment> getAllBans() {
        return List.of();
    }

}
