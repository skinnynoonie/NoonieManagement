package me.skinnynoonie.nooniemanagement.database.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalJsonManagementDB implements ManagementDatabase {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(PunishmentPortfolio.class, new PortfolioSerializer())
            .create();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final File dataDir;
    private final JavaPlugin plugin;

    public LocalJsonManagementDB(JavaPlugin plugin, File dataDir) {
        this.plugin = plugin;
        this.dataDir = dataDir;
    }

    @Override
    public boolean initiate() {
        if(!dataDir.isDirectory()) {
            plugin.getLogger().severe("Data dir must be a directory!");
            return false;
        }
        dataDir.mkdirs();
        return true;
    }

    @NotNull
    @Override
    public CompletableFuture<Boolean> savePunishmentPortfolio(@NotNull PunishmentPortfolio portfolio) {
        Preconditions.checkNotNull(portfolio, "Portfolio cannot be null!");

        return CompletableFuture.supplyAsync(() -> {

            File portfolioFile = new File(dataDir, portfolio.uuid()+".json");

            try (PrintWriter printWriter = new PrintWriter(portfolioFile)) {
                printWriter.print(gson.toJson(portfolio));
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }

        }, executorService);

    }

    @NotNull
    @Override
    public CompletableFuture<PunishmentPortfolio> getPunishmentPortfolioByUniqueId(@NotNull UUID uuid) {
        Preconditions.checkNotNull(uuid, "UUID cannot be null!");

        return CompletableFuture.supplyAsync(() -> {

            File file = new File(dataDir, uuid+".json");

            if(!file.exists()) {
                return new PunishmentPortfolio(uuid, new ArrayList<>());
            }

            try {
                String content = Files.readString(file.toPath());
                return gson.fromJson(content, PunishmentPortfolio.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }, executorService);

    }

    private static class PortfolioSerializer implements JsonSerializer<PunishmentPortfolio>, JsonDeserializer<PunishmentPortfolio> {

        @Override
        public PunishmentPortfolio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());

            List<Punishment> punishmentList = new ArrayList<>();
            for(JsonElement jsonElement : jsonObject.getAsJsonArray("punishments")) {
                punishmentList.add(gson.fromJson(jsonElement, Punishment.class));
            }

            return new PunishmentPortfolio(uuid, punishmentList);
        }

        @Override
        public JsonElement serialize(PunishmentPortfolio src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();

            for(Punishment p : src.punishments()) {
                jsonArray.add(gson.toJsonTree(p));
            }

            jsonObject.addProperty("uuid", src.uuid().toString());
            jsonObject.add("punishments", jsonArray);

            return jsonObject;
        }

    }

}
