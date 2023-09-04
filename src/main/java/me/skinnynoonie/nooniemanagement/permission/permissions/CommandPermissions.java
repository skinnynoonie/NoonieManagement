package me.skinnynoonie.nooniemanagement.permission.permissions;

import me.skinnynoonie.nooniemanagement.permission.EnumPermission;

public enum CommandPermissions implements EnumPermission {

    BAN_COMMAND("nooniemanagement.ban"),
    UNBAN_COMMAND("nooniemanagement.unban"),
    MUTE_COMMAND("nooniemanagement.mute"),
    UNMUTE_COMMAND("nooniemanagement.unmute");

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
