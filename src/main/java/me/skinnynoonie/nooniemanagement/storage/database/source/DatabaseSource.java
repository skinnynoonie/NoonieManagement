package me.skinnynoonie.nooniemanagement.storage.database.source;

import me.skinnynoonie.nooniemanagement.storage.database.DatabaseException;

import java.sql.Connection;

public interface DatabaseSource {
    Connection getConnection() throws DatabaseException;
}
