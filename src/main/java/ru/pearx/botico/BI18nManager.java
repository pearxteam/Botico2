package ru.pearx.botico;

import ru.pearx.lib.i18n.I18n;
import ru.pearx.lib.i18n.I18nLoaderResources;
import ru.pearx.lib.i18n.I18nManager;

import java.sql.*;

/*
 * Created by mrAppleXZ on 04.08.17 19:18.
 */
public class BI18nManager extends I18nManager
{
    private Botico botico;

    public BI18nManager(Botico b)
    {
        botico = b;
    }

    public void load()
    {
        try(Connection conn = botico.sql.connect())
        {
            try(Statement st = conn.createStatement())
            {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS `i18n` (`id` VARCHAR(255) NOT NULL, `locale` VARCHAR(255) NOT NULL, UNIQUE (`id`));");
            }
        }
        catch (SQLException e)
        {
            botico.log.error("Can't create the I18n manager table!", e);
        }
    }

    @Override
    public I18n createI18n(String locale)
    {
        I18nLoaderResources loader = new I18nLoaderResources();
        loader.getPaths().add("assets/botico/lang");
        I18n i18n = new I18n(loader, botico.config.defaultLanguage);
        i18n.load(locale);
        return i18n;
    }

    public I18n getForUser(String id)
    {
        try(Connection conn = botico.sql.connect())
        {
            try(PreparedStatement st = conn.prepareStatement("SELECT `locale` FROM `i18n` WHERE `id` = ?;"))
            {
                st.setString(1, id);
                try(ResultSet set = st.executeQuery())
                {
                    if (set.next())
                    {
                        return get(set.getString("locale"));
                    }
                }
            }
        }
        catch (SQLException e)
        {
            botico.log.error("Can't get a locale for user!", e);
        }
        return get(botico.config.defaultLanguage);
    }

    public void setForUser(String id, String locale)
    {
        try(Connection conn = botico.sql.connect())
        {
            try(PreparedStatement st = conn.prepareStatement("INSERT OR REPLACE INTO `i18n`(`id`, `locale`) VALUES(?, ?)"))
            {
                st.setString(1, id);
                st.setString(2, locale);
                st.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            botico.log.error("Can't set locale for user!", e);
        }
    }
}
