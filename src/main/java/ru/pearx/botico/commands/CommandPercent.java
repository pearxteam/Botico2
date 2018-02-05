package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;

/*
 * Created by mrAppleXZ on 05.02.18 16:30.
 */
public class CommandPercent extends CommandImpl
{
    public CommandPercent()
    {
        super("command.percent.names", "command.percent.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        return new BResponse(args.getI18n().format("command.percent.response", args.getRand().nextInt(101)));
    }
}
