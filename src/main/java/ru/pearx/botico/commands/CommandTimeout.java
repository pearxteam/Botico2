package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;

import java.time.LocalDateTime;

/*
 * Created by mrAppleXZ on 06.08.17 0:01.
 */
public class CommandTimeout extends CommandImpl
{
    public CommandTimeout()
    {
        super("command.timeout.names", "command.timeout.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(!args.getSender().isAdmin(args.getBotico()))
            return new BResponse(args.getI18n().format("command.timeout.notAdmin"));
        if(args.getArguments().length >= 2)
        {
            try
            {
                String id = args.getArguments()[0];
                int timeout = Integer.parseInt(args.getArguments()[1]);
                if(timeout < 0)
                    throw new NumberFormatException("timeout is less than 0!");
                args.getBotico().timeouts.setTimeout(id, LocalDateTime.now().plusSeconds(timeout));
                return new BResponse(args.getI18n().format("command.timeout.success", id, timeout));
            }
            catch(NumberFormatException e)
            {
                return new BResponse(args.getI18n().format("command.timeout.invalidTimeout", Integer.MAX_VALUE));
            }
        }
        return new BResponse(getDescription(args.getBotico(), args.getI18n()));
    }
}
