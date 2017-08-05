package ru.pearx.botico;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;
import ru.pearx.botico.commands.CommandAbout;
import ru.pearx.botico.commands.model.BArgs;
import ru.pearx.botico.commands.model.BResponse;
import ru.pearx.botico.commands.model.BUser;
import ru.pearx.botico.commands.model.ICommand;
import ru.pearx.botico.config.BConfig;
import ru.pearx.lib.D;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
 * Created by mrAppleXZ on 03.08.17 10:46.
 */
public class Botico
{
    public String clientName;

    public BConfig config;
    public Logger log;
    public List<ICommand> commands = new ArrayList<>();
    public BI18nManager i18n = new BI18nManager();
    public Random rand = new Random();

    public Path path;
    public Path pathConfig;
    public Path pathLogs;

    public Botico(String clientName, Path path)
    {
        this.clientName = clientName;
        this.path = path;
        pathConfig = path.resolve("config.json");
        pathLogs = path.resolve("logs");
        log = createLogger(pathLogs, clientName);
    }

    public Botico(String clientName)
    {
        this(clientName, D.getWorkingDir().resolve("Botico"));
    }

    public static Logger createLogger(Path pathLogs, String clientName)
    {
        Logger log = (Logger) LoggerFactory.getLogger("Botico-" + clientName);
        log.setLevel(Level.DEBUG);

        LoggerContext cont = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder pat = new PatternLayoutEncoder();
        pat.setPattern("[%date] [%logger{10}|%thread] [%level] %msg%n");
        pat.setContext(cont);
        pat.start();

        FileAppender<ILoggingEvent> fapp = new FileAppender<>();
        fapp.setFile(pathLogs.resolve("botico-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".log").toString());
        fapp.setEncoder(pat);
        fapp.setContext(cont);
        fapp.start();

        ConsoleAppender<ILoggingEvent> capp = new ConsoleAppender<>();
        capp.setTarget("System.out");
        capp.setContext(cont);
        capp.setEncoder(pat);
        capp.start();

        log.addAppender(fapp);
        log.addAppender(capp);
        log.setAdditive(false);
        return log;
    }

    public void prepare()
    {
        log.info("Preparing Botico...");

        if(!Files.exists(path))
        {
            try
            {
                Files.createDirectory(path);
            } catch (IOException e)
            {
                log.error("Can't create Botico directory!", e);
            }
        }
        loadConfig();

        loadI18n();

        log.info("Adding commands...");
        addCommands();
        log.info("Commands added!");

        log.info("Botico prepared!");
    }

    private void loadConfig()
    {
        if(!Files.exists(pathConfig))
        {
            log.error("Config file doesn't exist! An example config should be created at " + pathConfig + ". Please set it up!");
            BConfig ex = new BConfig();
            ex.defaultLanguage = "en_us";
            ex.lineBreaks = true;
            ex.linksInsteadOfImages = false;
            ex.textLimit = 4096;
            ex.prefix = "/";
            HashMap<String, String> map = new HashMap<>();
            map.put("alias1", "help 2");
            map.put("numberpls", "random 10");
            ex.aliases = map;
            ex.owners = new String[] {"0", "1"};
            try(FileWriter wr = new FileWriter(pathConfig.toString()))
            {
                BConfig.GSON.toJson(ex, wr);
            }
            catch (IOException e)
            {
                log.error("Can't create an example config!", e);
            }
            System.exit(-1);
        }
        log.info("Loading the config file...");
        try(FileReader rdr = new FileReader(pathConfig.toString()))
        {
            config = BConfig.GSON.fromJson(rdr, BConfig.class);
            log.info("The config file successfully loaded!");
        }
        catch (IOException e)
        {
            log.error("Can't load the config file!", e);
        }
    }

    private void loadI18n()
    {
        log.info("Loading I18n...");
        i18n.prepare(this);
        i18n.load();
        log.info("I18n loaded!");
    }

    public void addCommands()
    {
        commands.add(new CommandAbout());
    }

    public boolean hasCommand(String input, BUser user)
    {
        if(!input.startsWith(config.prefix))
            return false;

       String cmd = clearCommandWithArgs(input);

        for(String alias : config.aliases.keySet())
        {
            if(alias.equals(cmd))
                return true;
        }
        for(ICommand com : commands)
        {
            for(String name : com.getNames(this, i18n.getForUser(user.getId())))
            {
                if(name.equals(cmd))
                    return true;
            }
        }

        return false;
    }

    //todo support for config's lineBreaks
    public BResponse useCommand(String input, BUser user, boolean group, List<BUser> chatMembers)
    {
        if(!input.startsWith(config.prefix))
            return new BResponse("");
        String cmdName = clearCommandWithArgs(input);
        String cmd = clearCommand(input);
        for(Map.Entry<String, String> entr : config.aliases.entrySet())
        {
            if(cmdName.equals(entr.getKey()))
            {
                cmd = entr.getValue();
                cmdName = entr.getValue();
            }
        }
        for(ICommand com : commands)
        {
            for(String name : com.getNames(this, i18n.getForUser(user.getId())))
            {
                if(name.equals(cmdName))
                {
                    String jargs = cmd.contains(" ") ? cmd.substring(cmd.indexOf(" ") + 1) : "";
                    return com.use(new BArgs(cmd, cmdName, jargs, jargs.split(" "), user, group, this, rand, chatMembers, config.prefix, i18n.getForUser(user.getId())));
                }
            }
        }
        return new BResponse("");
    }

    public BResponse useCommand(String input, BUser user)
    {
        return useCommand(input, user, false, null);
    }

    private String clearCommand(String input)
    {
        String cmd = input.toLowerCase().substring(config.prefix.length());
        while(cmd.startsWith(" "))
        {
            cmd = cmd.substring(1);
        }
        return cmd;
    }

    private String clearCommandWithArgs(String input)
    {
        String cmd = clearCommand(input);
        if(cmd.contains(" "))
            cmd = cmd.substring(0, cmd.indexOf(" "));
        return cmd;
    }
}
