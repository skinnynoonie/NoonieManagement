package me.skinnynoonie.nooniemanagement.permission.permissions;

import me.skinnynoonie.nooniemanagement.permission.EnumPermission;

public enum NooniePermissions implements EnumPermission {

    VIEW_SILENT_PUNISHMENTS("view.silent.punishments");

    private final String defaultPermission;
    private String permission;

    NooniePermissions(String defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

    @Override
    public String getDefaultPermission() {
        return this.defaultPermission;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

}
