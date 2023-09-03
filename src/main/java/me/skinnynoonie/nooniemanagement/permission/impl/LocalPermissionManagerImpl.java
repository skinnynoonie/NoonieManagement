package me.skinnynoonie.nooniemanagement.permission.impl;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.permission.EnumPermission;
import me.skinnynoonie.nooniemanagement.permission.EnumPermissionManagerImpl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Objects;

public class LocalPermissionManagerImpl implements EnumPermissionManagerImpl {

    private final File permissionsDir;

    public LocalPermissionManagerImpl(Plugin plugin) {
        this.permissionsDir = new File(plugin.getDataFolder(), "permissions");
    }

    @Override
    public void initiate() throws Exception {
        permissionsDir.mkdirs();
    }

    @Override
    public <T extends Enum<T> & EnumPermission> void registerPermissions(Class<T> permissibleEnumClass) throws Exception {
        Preconditions.checkNotNull(permissibleEnumClass, "Enum permission class cannot be null!");
        File file = new File(permissionsDir, permissibleEnumClass.getSimpleName()+".yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        loadAndClearUnknown(fileConfiguration, permissibleEnumClass);
        fileConfiguration.save(file);
    }

    @Override
    public <T extends Enum<T> & EnumPermission> void reloadPermissions(Class<T> permissibleEnumClass) throws Exception {
        registerPermissions(permissibleEnumClass); // This is basically a cosmetic method in this case.
    }

    private <T extends Enum<T> & EnumPermission> void loadAndClearUnknown(FileConfiguration fileConfiguration, Class<T> permissibleEnumClass) {
        loadFileConfigIntoEnum(fileConfiguration, permissibleEnumClass);
        clearConfig(fileConfiguration);
        loadEnumIntoFileConfig(permissibleEnumClass, fileConfiguration);
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

}
