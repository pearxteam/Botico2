package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BFile;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;
import ru.pearx.lib.PXL;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by mrAppleXZ on 09.08.17 9:06.
 */
public class CommandReactor extends CommandImpl
{
    public CommandReactor()
    {
        super("command.reactor.names", "command.reactor.desc");
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(!PXL.isNullOrEmpty(args.getArgumentsJoined()))
        {
            String url = "http://joyreactor.cc/tag/" + PXL.encodeUrl(args.getArgumentsJoined());
            try(Scanner scan = new Scanner(new URL(url).openStream()).useDelimiter("\\A"))
            {
                Matcher mat = Pattern.compile("<a href='/tag/[^\"'/]*/([0-9]+)' class='next'>").matcher(scan.next());
                if(mat.find())
                {
                    int pagesCount = Integer.parseInt(mat.group(1));
                    try (Scanner scanPage = new Scanner(new URL(url + "/" + args.getRand().nextInt(pagesCount + 1)).openStream()).useDelimiter("\\A"))
                    {
                        String s = scanPage.next();
                        Matcher matImg = Pattern.compile("<div class=\"image\">.{0,500}?<img src=\"([^\"]*)\"", Pattern.DOTALL).matcher(s);
                        Matcher matGif = Pattern.compile("<span class=\"video_gif_holder\"><a href=\"([^\"]+)\"", Pattern.DOTALL).matcher(s);
                        List<String> imgs = new ArrayList<>();
                        while (matImg.find())
                        {
                            imgs.add(matImg.group(1));
                        }
                        while (matGif.find())
                        {
                            imgs.add(matGif.group(1));
                        }
                        String img = imgs.get(args.getRand().nextInt(imgs.size()));
                        if (args.getBotico().config.linksInsteadOfImages)
                        {
                            return new BResponse(img);
                        } else
                        {
                            URL u = new URL(img);
                            return new BResponse("", new BFile(PXL.decodeUrl(u.getPath().substring(u.getPath().lastIndexOf("/") + 1)), u.openStream(), u.getPath().endsWith(".gif") ? BFile.Type.ANIM_IMAGE : BFile.Type.IMAGE));
                        }
                    }
                }
                return new BResponse(args.getI18n().format("command.reactor.notFound"));
            }
            catch (IOException e)
            {
                args.getBotico().log.error("An IOException occurred when parsing JoyReactor page.", e);
                return new BResponse(args.getI18n().format("command.reactor.error", e.getMessage()));
            }
        }
        return new BResponse(getDescription(args.getBotico(), args.getI18n()));
    }
}
