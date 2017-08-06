package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;

import java.time.LocalDateTime;

/*
 * Created by mrAppleXZ on 05.08.17 23:03.
 */
public class CommandRussianRoulette extends CommandImpl
{
    public CommandRussianRoulette()
    {
        super("command.rr.names", "command.rr.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(args.getRand().nextInt(6) == 5)
        {
            args.getBotico().timeouts.setTimeout(args.getSender().getId(), LocalDateTime.now().plusMinutes(3));
            return new BResponse(args.getI18n().format("command.rr.lose"));
        }
        return new BResponse(args.getI18n().format("command.rr.win"));
    }
}
