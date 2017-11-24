package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;

/*
 * Created by mrAppleXZ on 24.11.17 16:31.
 */
public class CommandGC extends CommandImpl
{
    public CommandGC()
    {
        super("command.gc.names", "command.gc.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(args.getSender().isAdmin(args.getBotico()))
        {
            long prevKb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
            System.gc();
            long nowKb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
            long change = prevKb - nowKb;
            return new BResponse(args.getI18n().format("command.gc.done", prevKb, nowKb, change));
        }
        return new BResponse(args.getI18n().format("command.gc.notAdmin"));
    }
}
