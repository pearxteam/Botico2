package ru.pearx.botico.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/*
 * Created by mrAppleXZ on 04.08.17 10:37.
 */
public class BConfig
{
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SerializedName("defaultLanguage")
    public String defaultLanguage;
    @SerializedName("admins")
    public String[] admins;
    @SerializedName("prefix")
    public String prefix;
    @SerializedName("linksInsteadOfImages")
    public boolean linksInsteadOfImages;
    @SerializedName("lineBreaks")
    public boolean lineBreaks;
    @SerializedName("textLimit")
    public int textLimit;
    @SerializedName("aliases")
    public Map<String, String> aliases;
    @SerializedName("commandsOnOneHelpPage")
    public int commandsOnOneHelpPage;
    @SerializedName("googleApiKeys")
    public String[] googleApiKeys;
    @SerializedName("wikiSources")
    public WikiSource[] wikiSources;
    @SerializedName("mentions")
    public boolean mentions;

    public static class WikiSource
    {
        @SerializedName("displayName")
        public String displayName;
        @SerializedName("apiUrl")
        public String apiUrl;
    }
}
