package me.skinnynoonie.nooniemanagement.database;

import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ManagementDatabase {

    /**
     * Initiates this database. This may have no function, or it may just be a
     * test connection to see if this database works. This also may create things
     * in order to make this database work. If this initiation process fails,
     * this database should be handled in some sort of way. If this database is not
     * handled correctly, other functions of this database will not function properly.
     *
     * @return false if and only if this initiate process fails, otherwise true.
     */
    boolean initiate();

    /**
     * Saves a {@link me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio} to this database asynchronously.
     *
     * @param portfolio The portfolio that will be saved to this database.
     * @return CompletableFuture of true if and only if the portfolio was saved successfully, otherwise false.
     */
    @Nonnull
    CompletableFuture<Boolean> savePunishmentPortfolio(@Nonnull PunishmentPortfolio portfolio);

    /**
     * Attempts to get a {@link me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio} from this database.
     *
     * @param uuid The UUID of the target.
     * @return The portfolio if saved, otherwise an empty portfolio.
     */
    @Nonnull
    CompletableFuture<PunishmentPortfolio> getPunishmentPortfolioByUniqueId(@Nonnull UUID uuid);

}
