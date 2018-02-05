package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;
import ru.pearx.lib.PXL;

/*
 * Created by mrAppleXZ on 06.08.17 10:09.
 */

public class CommandTurn extends CommandImpl
{
    public CommandTurn()
    {
        super("command.turn.names", "command.turn.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(PXL.isNullOrEmpty(args.getArgumentsJoined()))
            return new BResponse(getDescription(args.getBotico(), args.getI18n()));
        return new BResponse(new StringBuilder(args.getArgumentsJoined()).reverse().toString());
    }
}
