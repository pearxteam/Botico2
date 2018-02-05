package ru.pearx.botico.commands;

import ru.pearx.botico.Botico;
import ru.pearx.botico.model.*;

/*
 * Created by mrAppleXZ on 06.08.17 10:18.
 */
public class CommandHelp extends CommandImpl
{
    public CommandHelp(Botico b)
    {
        super("command.help.names", b.config.lineBreaks ? "command.help.desc" : "command.help.desc.oneline");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(args.getArguments().length >= 1)
        {
            String nm = args.getArguments()[0];
            if(nm.startsWith(args.getPrefix()))
                nm = nm.substring(args.getPrefix().length());
            for(ICommand cmd : args.getBotico().commands)
            {
                for(String name : cmd.getNames(args.getBotico(), args.getI18n()))
                {
                    if(name.equals(nm))
                    {
                        return new BResponse(cmd.getDescription(args.getBotico(), args.getI18n()));
                    }
                }
            }
        }
        if(args.getBotico().config.lineBreaks)
        {
            int pagesCount = (int) Math.ceil((float) args.getBotico().commands.size() / args.getBotico().config.commandsOnOneHelpPage);
            int page = 1;
            if (args.getArguments().length >= 1)
            {
                try
                {
                    page = Integer.parseInt(args.getArguments()[0]);
                    if (page < 0 || page > pagesCount)
                        throw new NumberFormatException();
                }
                catch (NumberFormatException e)
                {
                    return new BResponse(args.getI18n().format("command.help.invalidPage", pagesCount));
                }
            }

            StringBuilder sb = new StringBuilder();
            int from = (page - 1) * args.getBotico().config.commandsOnOneHelpPage;
            sb.append("---");
            sb.append("\n");
            for(int i = from; i < from + args.getBotico().config.commandsOnOneHelpPage; i++)
            {
                if (i >= args.getBotico().commands.size())
                    break;
                ICommand cmd = args.getBotico().commands.get(i);
                if (!cmd.isHidden(args.getBotico()))
                {
                    sb.append(cmd.getDescription(args.getBotico(), args.getI18n()));
                    sb.append("\n");
                    sb.append("---");
                    sb.append("\n");
                }
            }
            sb.append(args.getI18n().format("command.help.page", page, pagesCount));
            return new BResponse(sb.toString());
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            for(ICommand cmd : args.getBotico().commands)
            {
                if(!cmd.isHidden(args.getBotico()))
                {
                    sb.append(args.getPrefix());
                    sb.append(cmd.getNames(args.getBotico(), args.getI18n())[0]);
                    sb.append(", ");
                }
            }
            sb.delete(sb.length() - 2, sb.length());
            return new BResponse(args.getI18n().format("command.help.oneline", sb.toString()));
        }
    }
}
