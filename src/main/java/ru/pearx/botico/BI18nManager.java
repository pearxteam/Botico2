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
    private I18n defaultI18n;

    private Botico botico;

    public void prepare(Botico b)
    {
        botico = b;
        defaultI18n = createI18n(b.config.defaultLanguage);
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
        I18n<I18nLoaderResources> i18n = new I18n<>(new I18nLoaderResources(), "en");
        i18n.getLoader().getPaths().add("assets/botico/lang");
        i18n.load(locale);
        return i18n;
    }

    public I18n getForUser(String id)
    {
        return localesForUsers.containsKey(id) ? get(localesForUsers.get(id)) : defaultI18n;
    }

    public void setForUser(String id, String locale)
    {
        localesForUsers.put(id, locale);
        save();
    }
}
