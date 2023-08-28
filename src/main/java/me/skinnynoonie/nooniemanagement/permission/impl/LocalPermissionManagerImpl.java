package me.skinnynoonie.nooniemanagement.permission.impl;

import me.skinnynoonie.nooniemanagement.permission.EnumPermission;
import me.skinnynoonie.nooniemanagement.permission.EnumPermissionManagerImpl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LocalPermissionManagerImpl implements EnumPermissionManagerImpl {

    private final Plugin plugin;

    public LocalPermissionManagerImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public <T extends Enum<T> & EnumPermission> void registerPermissions(Class<T> permissibleEnumClass) {
        File file = new File(plugin.getDataFolder(), permissibleEnumClass.getSimpleName()+".yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        loadFileConfigIntoEnum(fileConfiguration, permissibleEnumClass);
        clearConfig(fileConfiguration);
        loadEnumIntoFileConfig(permissibleEnumClass, fileConfiguration);
        handledSave(file, fileConfiguration);
    }

    @Override
    public <T extends Enum<T> & EnumPermission> void reloadPermissions(Class<T> permissibleEnumClass) {
        registerPermissions(permissibleEnumClass); // This is basically a cosmetic method in this case.
    }

    private <T extends Enum<T> & EnumPermission> void loadFileConfigIntoEnum(FileConfiguration fileConfiguration, Class<T> permissibleEnumClass) {
        for (T enumConstant : permissibleEnumClass.getEnumConstants()) {
            String enumName = enumConstant.toString();
            String configValueOrDefault = Objects.requireNonNullElse(fileConfiguration.getString(enumName), enumConstant.getDefaultPermission());
            enumConstant.setPermission(configValueOrDefault);
        }
    }

    private void clearConfig(FileConfiguration fileConfiguration) {
        fileConfiguration.getKeys(true).forEach(key -> {
            fileConfiguration.set(key, null);
        });
    }


    private <T extends Enum<T> & EnumPermission> void loadEnumIntoFileConfig(Class<T> permissibleEnumClass, FileConfiguration fileConfiguration) {
        for (T enumConstant : permissibleEnumClass.getEnumConstants()) {
            String enumName = enumConstant.toString();
            fileConfiguration.set(enumName, enumConstant.getPermission());
        }
    }

    private void handledSave(File configFile, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
