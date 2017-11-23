package ru.pearx.botico.commands;

import ru.pearx.botico.config.BConfig;
import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;
import ru.pearx.lib.PXL;
import ru.pearx.lib.thirdparty.MediawikiException;
import ru.pearx.lib.thirdparty.MediawikiParseResult;
import ru.pearx.lib.thirdparty.MediawikiUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by mrAppleXZ on 11.08.17 21:25.
 */
public class CommandWiki extends CommandImpl
{
    public CommandWiki()
    {
        super("command.wiki.names", "command.wiki.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if (args.getArguments().length >= 1)
        {
            if (Arrays.asList(args.getI18n().format("command.wiki.list").split(",")).contains(args.getArguments()[0].toLowerCase()))
            {
                StringBuilder sb = new StringBuilder();
                for (BConfig.WikiSource ws : args.getBotico().config.wikiSources)
                {
                    sb.append(ws.displayName);
                    sb.append(", ");
                }
                sb.delete(sb.length() - 2, sb.length());
                return new BResponse(args.getI18n().format("command.wiki.list.text", sb.toString()));
            }
            if (args.getArguments().length >= 2)
            {
                boolean auto = Arrays.asList(args.getI18n().format("command.wiki.auto").split(",")).contains(args.getArguments()[0].toLowerCase());
                String page = args.getArgumentsJoined().substring(args.getArguments()[0].length() + 1);
                for (BConfig.WikiSource source : args.getBotico().config.wikiSources)
                {
                    if (auto || source.displayName.equalsIgnoreCase(args.getArguments()[0]))
                    {
                        try
                        {
                            return new BResponse(MediawikiUtils.parsePage(source.apiUrl, page).parse.extractSummary("*"));
                        }
                        catch (MediawikiException e)
                        {
                            if(auto) continue;
                            if (e.getError().code.equals("missingtitle"))
                                return new BResponse(args.getI18n().format("command.wiki.notFound"));
                            args.getBotico().log.error("An exception occurred when trying to get MediaWiki result!", e);
                            return new BResponse(args.getI18n().format("command.wiki.error", e.getMessage()));
                        }
                        catch (Exception e)
                        {
                            if(auto) continue;
                            args.getBotico().log.error("An exception occurred when trying to get MediaWiki result!", e);
                            return new BResponse(args.getI18n().format("command.wiki.error", e.getMessage()));
                        }
                    }
                }
            }
        }
        return new BResponse(getDescription(args.getBotico(), args.getI18n()));
    }
}
