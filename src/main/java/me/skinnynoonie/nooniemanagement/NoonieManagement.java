package me.skinnynoonie.nooniemanagement;

import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.database.impl.LocalJsonManagementDatabaseImpl;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentType;
import me.skinnynoonie.nooniemanagement.util.IndefiniteDuration;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class NoonieManagement extends JavaPlugin {

    private ManagementDatabase managementDatabase;

    @Override
    public void onEnable() {
        managementDatabase = new ManagementDatabase(new LocalJsonManagementDatabaseImpl(this));
        managementDatabase.initiate();

        UUID target = UUID.fromString("9f01efff-cf7c-44d9-9d01-093a06a9a907");
        managementDatabase.getPunishmentPortfolioAsync(target)
                .thenApply(portfolio -> {
                    Punishment p = new Punishment.Builder()
                            .setType(PunishmentType.BAN)
                            .setDuration(IndefiniteDuration.from(500))
                            .setPardoner(NameableUser.UNKNOWN)
                            .setPardonReason("Yawn")
                            .setPardoned(true)
                            .setReason("XD")
                            .setTarget(target)
                            .setIssuer(NameableUser.UNKNOWN)
                            .build();
                    portfolio.punishments().add(p);
                    return portfolio;
                })
                .thenAccept(p -> {
                    System.out.println(3);
                    managementDatabase.savePunishmentPortfolioAsync(p);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    return null;
                });

    }

    @Override
    public void onDisable() {
    }

    public ManagementDatabase getManagementDatabase() {
        return this.managementDatabase;
    }

    public static NoonieManagement getInstance() {
        return JavaPlugin.getPlugin(NoonieManagement.class);
    }

}
