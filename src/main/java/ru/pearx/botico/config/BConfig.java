package ru.pearx.botico.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/*
 * Created by mrAppleXZ on 04.08.17 10:37.
 */
public class BConfig
{
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String defaultLanguage;
    public String[] admins;
    public String prefix;
    public boolean linksInsteadOfImages;
    public boolean lineBreaks;
    public int textLimit;
    public Map<String, String> aliases;
    public int commandsOnOneHelpPage;
    public String[] googleApiKeys;
}
