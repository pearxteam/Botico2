package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.BUser;
import ru.pearx.botico.model.CommandImpl;

/*
 * Created by mrAppleXZ on 11.08.17 16:03.
 */
public class CommandWho extends CommandImpl
{
    public CommandWho()
    {
        super("command.who.names", "command.who.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(args.isInGroupChat())
        {
            BUser user = args.getGroupChatMembers().get(args.getRand().nextInt(args.getGroupChatMembers().size()));
            String end = args.getArgumentsJoined().substring(args.getArgumentsJoined().length() - 1);
            return new BResponse(args.getI18n().format("command.who.text", args.getBotico().getClientConfig().createMention(user), end.matches("\\p{P}") ? args.getArgumentsJoined().substring(0, args.getArgumentsJoined().length() - 1) : args.getArgumentsJoined()));
        }
        return new BResponse(args.getI18n().format("command.who.notInGroupChat"));
    }
}
