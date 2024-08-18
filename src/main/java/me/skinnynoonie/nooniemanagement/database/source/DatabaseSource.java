package me.skinnynoonie.nooniemanagement.database.source;

import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public interface DatabaseSource {
    @NotNull Connection getConnection() throws DatabaseException;
}
