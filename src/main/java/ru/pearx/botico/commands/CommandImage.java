package ru.pearx.botico.commands;

import ru.pearx.botico.model.BArgs;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.CommandImpl;
import ru.pearx.lib.PXL;
import ru.pearx.lib.thirdparty.GoogleImageSearchResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by mrAppleXZ on 06.08.17 17:51.
 */
public class CommandImage extends CommandImpl
{
    public Map<String, ImageCache> cache = new HashMap<>();

    public CommandImage()
    {
        super("command.image.names", "command.image.desc");
    }

    public void load()
    {

    }

    public void save()
    {

    }

    @Override
    public BResponse use(BArgs args)
    {
        if(!PXL.isNullOrEmpty(args.getArgumentsJoined()))
        {
            try
            {
                GoogleImageSearchResult res = args.getBotico().gapi.searchImages(args.getArgumentsJoined(), "001650684090692243479:q5zk7hv6vqg");

            }
            catch(IOException e)
            {
                args.getBotico().log.error("Can't load image!", e);
                return new BResponse(args.getI18n().format("command.image.error", e.getMessage()));
            }
        }
        return new BResponse(getDescription(args.getBotico(), args.getI18n()));
    }

    public static class ImageCache
    {
        private List<String> urls;
        private LocalDateTime expiresIn;

        public ImageCache(List<String> urls, LocalDateTime expiresIn)
        {
            this.urls = urls;
            this.expiresIn = expiresIn;
        }

        public List<String> getUrls()
        {
            return urls;
        }

        public void setUrls(List<String> urls)
        {
            this.urls = urls;
        }

        public LocalDateTime getExpiresIn()
        {
            return expiresIn;
        }

        public void setExpiresIn(LocalDateTime expiresIn)
        {
            this.expiresIn = expiresIn;
        }
    }
}
