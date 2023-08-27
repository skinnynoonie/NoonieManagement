package me.skinnynoonie.nooniemanagement.database;

import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ManagementDatabase {

    private final ManagementDatabaseImpl databaseImpl;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ManagementDatabase(ManagementDatabaseImpl managementDatabaseImpl) {
        databaseImpl = managementDatabaseImpl;
    }

    public void initiate() {
        try {
            databaseImpl.initiate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Void> savePunishmentPortfolioAsync(PunishmentPortfolio portfolio) {
        return future(() -> {
            try {
                databaseImpl.savePunishmentPortfolio(portfolio);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<PunishmentPortfolio> getPunishmentPortfolioAsync(UUID uuid) {
        return future(() -> {
            try {
                return databaseImpl.getPunishmentPortfolioByUUID(uuid);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    private <T> CompletableFuture<T> future(Supplier<T> runnable) {
        return CompletableFuture.supplyAsync(runnable, executorService);
    }

    private CompletableFuture<Void> future(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executorService);
    }

    public ManagementDatabaseImpl getManagementDatabaseImpl() {
        return this.databaseImpl;
    }

}
