package me.skinnynoonie.nooniemanagement.permission;

public interface EnumPermissionManagerImpl {

    void initiate() throws Exception;

    <T extends Enum<T> & EnumPermission> void registerPermissions(Class<T> permissibleEnumClass) throws Exception;

    <T extends Enum<T> & EnumPermission> void reloadPermissions(Class<T> permissibleEnumClass) throws Exception;

}
