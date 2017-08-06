package ru.pearx.botico;

import com.google.gson.reflect.TypeToken;
import ru.pearx.botico.config.BConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*
 * Created by mrAppleXZ on 05.08.17 23:11.
 */
public class BTimeoutManager
{
    public Path path;

    private Botico botico;

    public Map<String, LocalDateTime> map;

    public BTimeoutManager(Botico b)
    {
        this.botico = b;
        path = b.path.resolve("timeout.json");
    }

    public void load()
    {
        try(FileReader rdr = new FileReader(path.toString()))
        {
            map = BConfig.GSON.fromJson(rdr, new TypeToken<HashMap<String, LocalDateTime>>(){}.getType());
        }
        catch (FileNotFoundException e)
        {
            map = new HashMap<>();
        }
        catch (IOException e)
        {
            botico.log.error("Can't load timeout manager's data!", e);
        }
    }

    public void save()
    {
        try(FileWriter wr = new FileWriter(path.toString()))
        {
            BConfig.GSON.toJson(map, wr);
        }
        catch (IOException e)
        {
            botico.log.error("Can't save timeout manager's data!", e);
        }
    }
    public void setTimeout(String id, LocalDateTime to)
    {
        map.put(id, to);
        save();
    }

    public LocalDateTime getTimeout(String id)
    {
        return map.get(id);
    }

    public boolean isFree(String id)
    {
        if(!map.containsKey(id))
            return true;
        if(map.get(id).isBefore(LocalDateTime.now()))
        {
            map.remove(id);
            save();
            return true;
        }
        return false;
    }
}
