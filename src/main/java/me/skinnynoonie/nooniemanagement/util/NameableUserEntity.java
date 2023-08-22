package me.skinnynoonie.nooniemanagement.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class NameableUserEntity {

    public static NameableUserEntity CONSOLE = new NameableUserEntity(null);

    /**
     * Creates a blocking request to get the UUID of the entity.
     * @param name The name of the entity.
     * @return An entity from the name.
     */
    public static NameableUserEntity fromName(@Nonnull String name) {
        Preconditions.checkNotNull(name, "Name cannot be null!");
        UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
        return new NameableUserEntity(uuid);
    }

    private final UUID entity;

    /**
     * Creates an entity.
     * @param entity UUID of the entity, null if the entity does not have an assigned UUID.
     */
    public NameableUserEntity(@Nullable UUID entity) {
        this.entity = entity;
    }

    /**
     * Creates a blocking request to get the name of this entity.
     * @param unknown Returns this if the entity is unknown. See {@link NameableUserEntity#isUnknown()}.
     * @return The displayable String of this entity. This may be a plain UUID if no name was found for this entity.
     */
    public String getDisplayableName(String unknown) {
        if(entity == null) {
            return unknown;
        }
        String name = Bukkit.getOfflinePlayer(entity).getName();
        return name != null ? name : entity.toString();
    }

    /**
     * @return true, if this entity is unknown, maybe a console sender, otherwise false.
     */
    public boolean isUnknown() {
        return entity == null;
    }

    /**
     * @return true if this entity has an assigned UUID, otherwise false.
     */
    public boolean isAssigned() {
        return entity != null;
    }

    /**
     * @return UUID that was assigned to this entity.
     */
    @Nullable
    public UUID getEntityUniqueId() {
        return entity;
    }

}
