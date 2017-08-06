package ru.pearx.botico;

import com.google.gson.reflect.TypeToken;
import ru.pearx.botico.config.BConfig;
import ru.pearx.lib.i18n.I18n;
import ru.pearx.lib.i18n.I18nLoaderResources;
import ru.pearx.lib.i18n.I18nManager;

import java.awt.image.BandCombineOp;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/*
 * Created by mrAppleXZ on 04.08.17 19:18.
 */
public class BI18nManager extends I18nManager
{
    public Path pathConfig;
    private Map<String, String> localesForUsers;

    private Botico botico;

    public BI18nManager(Botico b)
    {
        botico = b;
        pathConfig = b.path.resolve("locales.json");
    }

    public void save()
    {
        try(FileWriter wr = new FileWriter(pathConfig.toString()))
        {
            BConfig.GSON.toJson(localesForUsers, wr);
        }
        catch (IOException e)
        {
            botico.log.error("Can't save the locales config file!");
        }
    }

    public void load()
    {
        try(FileReader rdr = new FileReader(pathConfig.toString()))
        {
            localesForUsers = BConfig.GSON.fromJson(rdr, new TypeToken<HashMap<String, String>>(){}.getType());
        }
        catch (FileNotFoundException e)
        {
            localesForUsers = new HashMap<>();
        }
        catch (IOException e)
        {
            botico.log.error("Can't load the locales config file!");
        }
    }

    @Override
    public I18n createI18n(String locale)
    {
        I18nLoaderResources loader = new I18nLoaderResources();
        loader.getPaths().add("assets/botico/lang");
        I18n i18n = new I18n(loader, "en");
        i18n.load(locale);
        return i18n;
    }

    public I18n getForUser(String id)
    {
        return localesForUsers.containsKey(id) ? get(localesForUsers.get(id)) : get(botico.config.defaultLanguage);
    }

    public void setForUser(String id, String locale)
    {
        localesForUsers.put(id, locale);
        save();
    }
}
