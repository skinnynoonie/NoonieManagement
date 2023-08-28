package me.skinnynoonie.nooniemanagement.permission;

public interface EnumPermissionManagerImpl {

    <T extends Enum<T> & EnumPermission> void registerPermissions(Class<T> permissibleEnumClass);

    <T extends Enum<T> & EnumPermission> void reloadPermissions(Class<T> permissibleEnumClass);

}
