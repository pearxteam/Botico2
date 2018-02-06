package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;
import ru.pearx.lib.thirdparty.ithappens.ItHappensUtils;

import java.io.IOException;

/*
 * Created by mrAppleXZ on 06.02.18 13:40.
 */
public class CommandItHappens extends CommandImpl
{
    public CommandItHappens()
    {
        super("command.ithappens.names", "command.ithappens.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        try
        {
            return new BResponse(ItHappensUtils.getRandom(args.getRand()));
        }
        catch (Exception e)
        {
            return new BResponse(args.getI18n().format("command.ithappens.error", e.getMessage()));
        }
    }
}
