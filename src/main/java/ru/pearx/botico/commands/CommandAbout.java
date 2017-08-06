package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;

/*
 * Created by mrAppleXZ on 04.08.17 18:54.
 */
public class CommandAbout extends CommandImpl
{
    public CommandAbout()
    {
        super("command.about.names", "command.about.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        return new BResponse(args.getI18n().format(args.getBotico().config.lineBreaks ? "command.about.text" : "command.about.text.oneline", args.getI18n().getCurrentLocale(), args.getBotico().clientName, System.getProperty("java.vm.name"), System.getProperty("java.vm.version"), System.getProperty("java.runtime.name"), System.getProperty("java.runtime.version"), System.getProperty("os.name"), (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024, Runtime.getRuntime().totalMemory() / 1024));
    }
}
