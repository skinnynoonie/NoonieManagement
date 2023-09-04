package me.skinnynoonie.nooniemanagement.permission.permissions;

import me.skinnynoonie.nooniemanagement.permission.EnumPermission;

public enum PunishmentPermissions implements EnumPermission {

    BYPASS_BANS("nooniemanagement.bypass_ban"),
    BYPASS_MUTES("nooniemanagement.bypass_mute"),
    BYPASS_WARNS("nooniemanagement.bypass_warn"),
    BYPASS_KICKS("nooniemanagement.bypass_kick"),
    VIEW_SILENT_PUNISHMENTS("nooniemanagement.view_silent");

    private final String defaultPermission;
    private String permission;

    PunishmentPermissions(String defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

    @Override
    public String getDefaultPermission() {
        return defaultPermission;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

}
