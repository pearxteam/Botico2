package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;
import ru.pearx.lib.RandomUtils;

import java.io.UnsupportedEncodingException;

/*
 * Created by mrAppleXZ on 06.08.17 13:37.
 */
public class CommandAstral extends CommandImpl
{
    public CommandAstral()
    {
        super("command.astral.names", "command.astral.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        String txt = "<ERROR>";
        switch (args.getRand().nextInt(2))
        {
            case 0:
                byte[] bts = new byte[128];
                args.getRand().nextBytes(bts);
                try
                {
                    txt = new String(bts, "UTF-16");
                } catch (UnsupportedEncodingException e)
                {
                    args.getBotico().log.error("Can't decode astral output!", e);
                }
                break;
            case 1:
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < 128; i++)
                {
                    sb.append(RandomUtils.genChar(args.getRand(), true));
                }
                txt = sb.toString();
                break;

        }
        return new BResponse(args.getI18n().format("command.astral.text", txt));
    }
}
