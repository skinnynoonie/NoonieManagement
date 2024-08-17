package me.skinnynoonie.nooniemanagement.database.source;

import me.skinnynoonie.nooniemanagement.database.DatabaseException;

import java.sql.Connection;

public interface DatabaseSource {
    Connection getConnection() throws DatabaseException;
}
