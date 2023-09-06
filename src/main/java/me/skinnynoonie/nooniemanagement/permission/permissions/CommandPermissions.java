package me.skinnynoonie.nooniemanagement.permission.permissions;

import me.skinnynoonie.nooniemanagement.permission.EnumPermission;

public enum CommandPermissions implements EnumPermission {

    BAN_COMMAND("nooniemanagement.ban"),
    UNBAN_COMMAND("nooniemanagement.unban"),
    MUTE_COMMAND("nooniemanagement.mute"),
    UNMUTE_COMMAND("nooniemanagement.unmute"),
    WARN_COMMAND("nooniemanagement.warn"),
    KICK_COMMAND("nooniemanagement.kick"),
    VIEW_PUNISHMENT_LOGS_COMMAND("nooniemanagement.view_logs");

    private final String defaultPermission;
    private String permission;

    CommandPermissions(String defaultPermission) {
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
