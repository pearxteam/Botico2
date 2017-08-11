package ru.pearx.botico;

import java.sql.*;
import java.time.LocalDateTime;

/*
 * Created by mrAppleXZ on 05.08.17 23:11.
 */
public class BTimeoutManager
{
    private Botico botico;

    public BTimeoutManager(Botico b)
    {
        this.botico = b;
    }

    public void load()
    {
        try(Connection conn = botico.sql.connect())
        {
            try(Statement st = conn.createStatement())
            {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS `timeouts` (`id` VARCHAR(255) NOT NULL, `expiresIn` DATETIME NOT NULL, UNIQUE (`id`));");
            }
        }
        catch (SQLException e)
        {
            botico.log.error("Can't create the timeouts manager table!", e);
        }
    }

    public void setTimeout(String id, LocalDateTime to)
    {
        try(Connection conn = botico.sql.connect())
        {
            try(PreparedStatement st = conn.prepareStatement("INSERT OR REPLACE INTO `timeouts` (`id`, `expiresIn`) VALUES(?, ?);"))
            {
                st.setString(1, id);
                st.setTimestamp(2, Timestamp.valueOf(to));
                st.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            botico.log.error("Can't set timeout!", e);
        }
    }

    public LocalDateTime getTimeout(String id)
    {
        try(Connection conn = botico.sql.connect())
        {
            try(PreparedStatement st = conn.prepareStatement("SELECT `expiresIn` FROM `timeouts` WHERE `id` = ?;"))
            {
                st.setString(1, id);
                try(ResultSet set = st.executeQuery())
                {
                    if(set.next())
                    {
                        return set.getTimestamp("expiresIn").toLocalDateTime();
                    }
                }
            }
        }
        catch (SQLException e)
        {
            botico.log.error("Can't get timeout!", e);
        }
        return null;
    }

    public boolean isFree(String id)
    {
        LocalDateTime to = getTimeout(id);
        if(to == null)
            return true;
        if(to.isBefore(LocalDateTime.now()))
        {
            try(Connection conn = botico.sql.connect())
            {
                try(PreparedStatement st = conn.prepareStatement("DELETE FROM `timeouts` WHERE `id` = ?;"))
                {
                    st.setString(1, id);
                    st.executeUpdate();
                }
            }
            catch (SQLException e)
            {
                botico.log.error("Can't remove expired timeout!", e);
            }
            return true;
        }
        return false;
    }
}
