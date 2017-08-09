package ru.pearx.botico.config;

import ru.pearx.botico.model.BFile;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by mrAppleXZ on 09.08.17 14:36.
 */
public class CustomCommandsConfig
{
    public Entry[] entries;

    public static class Entry
    {
        public boolean hidden;
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
            public String[] names;
            public String description;
            public String text;
            public FileEntry[] files;

            public static class FileEntry
            {
                public String path;
                public BFile.Type type;
            }
        }
    }
}
