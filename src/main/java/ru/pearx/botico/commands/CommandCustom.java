package ru.pearx.botico.commands;

import ru.pearx.botico.Botico;
import ru.pearx.botico.config.CustomCommandsConfig;
import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BFile;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.ICommand;
import ru.pearx.lib.i18n.I18n;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by mrAppleXZ on 09.08.17 14:34.
 */
public class CommandCustom implements ICommand
{
    private CustomCommandsConfig.Entry entry;

    public CommandCustom(CustomCommandsConfig.Entry e)
    {
        this.entry = e;
    }

    @Override
    public String[] getNames(Botico b, I18n i18n)
    {
        return entry.getLocaleSpecificEntry(i18n.getCurrentLocale(), i18n.getDefaultLocale()).names;
    }

    @Override
    public boolean isHidden(Botico b)
    {
        return entry.hidden;
    }

    @Override
    public String getDescription(Botico b, I18n i18n)
    {
        StringBuilder sb = new StringBuilder();
        String[] names = getNames(b, i18n);
        for(int i = 1; i < names.length; i++)
        {
            sb.append(b.config.prefix);
            sb.append(names[i]);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return String.format(entry.getLocaleSpecificEntry(i18n.getCurrentLocale(), i18n.getDefaultLocale()).description, b.config.prefix + names[0], sb.toString());
    }

    @Override
    public BResponse use(BArgs args)
    {
        CustomCommandsConfig.Entry.LocaleSpecificEntry entr = entry.getLocaleSpecificEntry(args.getI18n().getCurrentLocale(), args.getI18n().getDefaultLocale());
        List<BFile> lst = new ArrayList<>();
        for(CustomCommandsConfig.Entry.LocaleSpecificEntry.FileEntry f : entr.files)
        {
            try
            {
                lst.add(new BFile(Paths.get(f.path).getFileName().toString(), new FileInputStream(f.path), f.type));
            }
            catch (FileNotFoundException e)
            {
                args.getBotico().log.error("Can't find the custom command file: " + f.path + "!", e);
            }
        }
        return new BResponse(entr.text, lst);
    }
}
