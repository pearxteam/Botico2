package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;
import ru.pearx.lib.thirdparty.bashim.BashImUtils;

import java.io.IOException;

/*
 * Created by mrAppleXZ on 06.02.18 12:53.
 */
public class CommandBash extends CommandImpl
{
    public CommandBash()
    {
        super("command.bash.names", "command.bash.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        try
        {
            return new BResponse(BashImUtils.getRandomQuote(args.getRand()));
        }
        catch (Exception e)
        {
            return new BResponse(args.getI18n().format("command.bash.error", e.getMessage()));
        }
    }
}
