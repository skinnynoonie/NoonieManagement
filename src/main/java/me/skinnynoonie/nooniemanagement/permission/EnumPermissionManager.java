package me.skinnynoonie.nooniemanagement.permission;

public class EnumPermissionManager {

    private final EnumPermissionManagerImpl permissionManagerImpl;

    public EnumPermissionManager(EnumPermissionManagerImpl permissionManagerImpl) {
        this.permissionManagerImpl = permissionManagerImpl;
    }

    public void initiate() {
        try {
            permissionManagerImpl.initiate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Enum<T> & EnumPermission> void registerEnumPermissions(Class<T> permissionEnum) {
        try {
            permissionManagerImpl.registerPermissions(permissionEnum);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Enum<T> & EnumPermission> void reloadEnumPermissions(Class<T> permissionEnum) {
        try {
            permissionManagerImpl.reloadPermissions(permissionEnum);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
