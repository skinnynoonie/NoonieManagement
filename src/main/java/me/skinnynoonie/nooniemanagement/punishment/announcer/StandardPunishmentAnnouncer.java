package me.skinnynoonie.nooniemanagement.punishment.announcer;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.config.ConfigManager;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class StandardPunishmentAnnouncer implements PunishmentAnnouncer {
    private final Map<Class<?>, PunishmentAnnouncer> announcerMap;

    public StandardPunishmentAnnouncer(NoonieManagement noonieManagement) {
        this.announcerMap = new ConcurrentHashMap<>();

        ConfigManager configManager = noonieManagement.getConfigManager();
        this.announcerMap.put(PlayerMutePunishment.class, new PlayerMutePunishmentAnnouncer(configManager));
    }

    @Override
    public void announceIssued(@NotNull Saved<? extends Punishment> savedPunishment) {
        Preconditions.checkArgument(savedPunishment != null, "savedPunishment");

        PunishmentAnnouncer punishmentAnnouncer = this.announcerMap.get(savedPunishment.get().getClass());
        if (punishmentAnnouncer != null) {
            punishmentAnnouncer.announceIssued(savedPunishment);
        }
    }

    @Override
    public void announcePardoned(@NotNull Saved<? extends Punishment> savedPunishment) {
        Preconditions.checkArgument(savedPunishment != null, "savedPunishment");

        PunishmentAnnouncer punishmentAnnouncer = this.announcerMap.get(savedPunishment.get().getClass());
        if (punishmentAnnouncer != null) {
            punishmentAnnouncer.announcePardoned(savedPunishment);
        }
    }
}
