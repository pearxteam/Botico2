package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.BUser;
import ru.pearx.botico.model.CommandImpl;

/*
 * Created by mrAppleXZ on 11.03.18 19:14.
 */
public class CommandShip extends CommandImpl
{
    public CommandShip()
    {
        super("command.ship.names", "command.ship.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(args.isInGroupChat())
        {
            return new BResponse(args.getI18n().format("command.ship.text", args.getBotico().createMention(args.getRandomGroupChatMember()), args.getBotico().createMention(args.getRandomGroupChatMember())));
        }
        return new BResponse(args.getI18n().format("command.ship.notInGroupChat"));
    }
}
