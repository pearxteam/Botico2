package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;
import ru.pearx.lib.i18n.Locale;

import java.util.Arrays;

/*
 * Created by mrAppleXZ on 05.08.17 10:04.
 */
public class CommandLocale extends CommandImpl
{
    public CommandLocale()
    {
        super("command.locale.names", "command.locale.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(args.getArguments().length > 0)
        {
            if (Arrays.asList(args.getI18n().format("command.locale.list").split(",")).contains(args.getArguments()[0].toLowerCase()))
            {
                StringBuilder bld = new StringBuilder();
                for (Locale loc : args.getI18n().getAvailable())
                {
                    bld.append(loc.getLocale());
                    bld.append(" - ");
                    bld.append(loc.getDisplayName());
                    bld.append(", ");
                }
                bld.delete(bld.length() - 2, bld.length());
                return new BResponse(args.getI18n().format("command.locale.list.text", bld.toString()));
            }
            if (Arrays.asList(args.getI18n().format("command.locale.set").split(",")).contains(args.getArguments()[0].toLowerCase()))
            {
                if(args.getArguments().length > 1)
                {
                    for(Locale loc : args.getI18n().getAvailable())
                    {
                        if(loc.getLocale().equals(args.getArguments()[1]))
                        {
                            args.getBotico().i18n.setForUser(args.getSender().getId(), loc.getLocale());
                            return new BResponse(args.getBotico().i18n.getForUser(args.getSender().getId()).format("command.locale.set.success"));
                        }
                    }
                    return new BResponse(args.getI18n().format("command.locale.set.notFound"));
                }
            }
        }
        return new BResponse(getDescription(args.getBotico(), args.getI18n()));
    }
}
