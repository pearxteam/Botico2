package ru.pearx.botico.commands;

import ru.pearx.botico.Botico;
import ru.pearx.botico.model.*;
import ru.pearx.lib.LiteMimeMap;
import ru.pearx.lib.PXL;
import ru.pearx.lib.thirdparty.GoogleImageSearchResult;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Created by mrAppleXZ on 06.08.17 17:51.
 */
public class CommandImage extends CommandImpl
{
    public CommandImage()
    {
        super("command.image.names", "command.image.desc");
    }

    public void load(Botico b)
    {
        try(Connection conn = b.sql.connect())
        {
            try(Statement st = conn.createStatement())
            {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS `image_cache` (`query` VARCHAR(255) NOT NULL, `expiresIn` DATETIME NOT NULL, `urls` VARCHAR(16383) NOT NULL, `mimes` VARCHAR(16383) NOT NULL, UNIQUE (`query`));");
            }
        }
        catch (SQLException e)
        {
            b.log.error("Can't create the image cache table!", e);
        }
    }

    public Entry getRandomImage(String query, Botico b, Random rand) throws IOException
    {
        try(Connection conn = b.sql.connect())
        {
            try(PreparedStatement st = conn.prepareStatement("SELECT `expiresIn`, `urls`, `mimes` FROM `image_cache` WHERE `query` = ?;"))
            {
                st.setString(1, query);
                try(ResultSet set = st.executeQuery())
                {
                    if(set.next())
                    {
                        if(set.getTimestamp("expiresIn").toLocalDateTime().isAfter(LocalDateTime.now()))
                        {
                            String[] arr = set.getString("urls").split("\n");
                            String[] mimes = set.getString("mimes").split("\n");
                            int i = rand.nextInt(arr.length);
                            return new Entry(arr[i], mimes[i]);
                        }
                    }
                }
            }

            GoogleImageSearchResult res = b.gApi.searchImages(query, "001650684090692243479:q5zk7hv6vqg");
            List<String> urls = new ArrayList<>();
            List<String> mimes = new ArrayList<>();
            for(GoogleImageSearchResult.Item itm : res.items)
            {
                urls.add(itm.link);
                mimes.add(itm.mime);
            }
            try(PreparedStatement st = conn.prepareStatement("INSERT OR REPLACE INTO `image_cache` (query, expiresIn, urls, mimes) VALUES(?, ?, ?, ?);"))
            {
                st.setString(1, query);
                st.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusDays(3)));
                st.setString(3, String.join("\n", urls));
                st.setString(4, String.join("\n", mimes));
                st.executeUpdate();
            }
            int i = rand.nextInt(res.items.size());
            return new Entry(urls.get(i), mimes.get(i));
        }
        catch (SQLException e)
        {
            b.log.error("Can't get the image cache from DB!");
        }
        return null;
    }

    @Override
    public BResponse use(BArgs args)
    {
        if(!PXL.isNullOrEmpty(args.getArgumentsJoined()))
        {
            try
            {
                Entry entr = getRandomImage(args.getArgumentsJoined().toLowerCase().replaceAll("\\p{P}", ""), args.getBotico(), args.getRand());;
                if(args.getBotico().config.linksInsteadOfImages)
                    return new BResponse(entr.getUrl());
                else
                    return new BResponse("", new BFile("image." + (LiteMimeMap.map.containsKey(entr.getMime()) ? LiteMimeMap.map.get(entr.getMime())[0] : "png"), new URL(entr.url).openStream(),
                            entr.getMime().equals("image/svg+xml-compressed") ||
                                    entr.getMime().equals("image/svg+xml") ||
                                    entr.getMime().equals("image/webp") ||
                                    entr.getMime().equals("image/gif") ||
                                    entr.getMime().equals("image/x-webp") ? BFile.Type.ANIM_IMAGE : BFile.Type.IMAGE, entr.getMime()));
            }
            catch(IOException e)
            {
                args.getBotico().log.error("Can't load image!", e);
                return new BResponse(args.getI18n().format("command.image.error", e.getMessage()));
            }
        }
        return new BResponse(getDescription(args.getBotico(), args.getI18n()));
    }

    public static class Entry
    {
        private String url;
        private String mime;

        public Entry(String url, String mime)
        {
            this.url = url;
            this.mime = mime;
        }

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }

        public String getMime()
        {
            return mime;
        }

        public void setMime(String mime)
        {
            this.mime = mime;
        }
    }
}
