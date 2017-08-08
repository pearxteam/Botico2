package ru.pearx.botico;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Created by mrAppleXZ on 08.08.17 17:54.
 */
public class BSqlManager
{
    public Path pathDb;
    private Botico botico;

    public BSqlManager(Botico b)
    {
        this.botico = b;
        pathDb = botico.path.resolve("botico.db");
    }

    public Connection connect() throws SQLException
    {
        return DriverManager.getConnection("jdbc:sqlite:" + pathDb.toString());
    }
}
