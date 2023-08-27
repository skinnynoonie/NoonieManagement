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
import me.skinnynoonie.nooniemanagement.database.ManagementDatabaseImpl;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocalJsonManagementDatabaseImpl implements ManagementDatabaseImpl {

    private static final Gson GSON_PARSER = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(PunishmentPortfolio.class, new PortfolioSerializer())
            .create();

    private final Plugin plugin;
    private final File punishmentPortfolioDirectory;

    public LocalJsonManagementDatabaseImpl(Plugin plugin) {
        this.plugin = plugin;
        punishmentPortfolioDirectory = new File(plugin.getDataFolder(), "portfolios/");
    }

    @Override
    public void initiate() throws Exception {
        punishmentPortfolioDirectory.mkdirs();
    }

    @Override
    public void savePunishmentPortfolio(PunishmentPortfolio portfolio) throws Exception {
        Preconditions.checkNotNull(portfolio, "Punishment portfolio cannot be null.");

        String content = GSON_PARSER.toJson(portfolio);
        File portfolioDestination = new File(punishmentPortfolioDirectory, portfolio.uuid() + ".json");
        PrintWriter printWriter = new PrintWriter(portfolioDestination);
        printWriter.print(content);
        printWriter.close();
    }

    @Override
    public PunishmentPortfolio getPunishmentPortfolioByUUID(UUID uuid) throws Exception {
        Preconditions.checkNotNull(uuid, "UUID cannot be null when fetching punishment portfolios.");

        File possiblePortfolio = new File(punishmentPortfolioDirectory, uuid+".json");
        if(!possiblePortfolio.exists()) {
            return new PunishmentPortfolio(uuid, new ArrayList<>());
        }
        String content = Files.readString(possiblePortfolio.toPath());
        return GSON_PARSER.fromJson(content, PunishmentPortfolio.class);
    }

    private static class PortfolioSerializer implements JsonSerializer<PunishmentPortfolio>, JsonDeserializer<PunishmentPortfolio> {

        @Override
        public PunishmentPortfolio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());

            List<Punishment> punishmentList = new ArrayList<>();
            for(JsonElement jsonElement : jsonObject.getAsJsonArray("punishments")) {
                punishmentList.add(GSON_PARSER.fromJson(jsonElement, Punishment.class));
            }

            return new PunishmentPortfolio(uuid, punishmentList);
        }

        @Override
        public JsonElement serialize(PunishmentPortfolio src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();

            for(Punishment p : src.punishments()) {
                jsonArray.add(GSON_PARSER.toJsonTree(p));
            }

            jsonObject.addProperty("uuid", src.uuid().toString());
            jsonObject.add("punishments", jsonArray);

            return jsonObject;
        }

    }

}
