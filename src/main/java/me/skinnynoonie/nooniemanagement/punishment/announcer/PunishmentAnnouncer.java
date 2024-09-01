package me.skinnynoonie.nooniemanagement.punishment.announcer;

import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import org.jetbrains.annotations.NotNull;

public interface PunishmentAnnouncer {
    void announceIssued(@NotNull Saved<? extends Punishment> savedPunishment);

    void announcePardoned(@NotNull Saved<? extends Punishment> savedPunishment);
}
