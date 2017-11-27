package ru.pearx.botico.config;

import com.google.gson.annotations.SerializedName;
import ru.pearx.botico.model.BFile;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by mrAppleXZ on 09.08.17 14:36.
 */
public class CustomCommandsConfig
{
    @SerializedName("entries")
    public Entry[] entries;

    public static class Entry
    {
        @SerializedName("hidden")
        public boolean hidden;
        @SerializedName("entries")
        public Map<String, LocaleSpecificEntry> entries = new HashMap<>();

        public LocaleSpecificEntry getLocaleSpecificEntry(String locale, String def)
        {
            if(entries.containsKey(locale))
                return entries.get(locale);
            if(entries.containsKey(def))
                return entries.get(def);
            return null;
        }

        public static class LocaleSpecificEntry
        {
            @SerializedName("names")
            public String[] names;
            @SerializedName("description")
            public String description;
            @SerializedName("text")
            public String text;
            @SerializedName("files")
            public FileEntry[] files;

            public static class FileEntry
            {
                @SerializedName("path")
                public String path;
                @SerializedName("type")
                public BFile.Type type;
                @SerializedName("mimeType")
                public String mimeType;
            }
        }
    }
}
