package ru.pearx.botico;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;
import ru.pearx.botico.commands.*;
import ru.pearx.botico.config.CustomCommandsConfig;
import ru.pearx.botico.model.*;
import ru.pearx.botico.config.BConfig;
import ru.pearx.lib.D;
import ru.pearx.lib.i18n.I18n;
import ru.pearx.lib.thirdparty.GoogleApiUser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/*
 * Created by mrAppleXZ on 03.08.17 10:46.
 */
public class Botico
{
    private IClientSpecificConfig clientConfig;

    public BConfig config;
    public CustomCommandsConfig configCustomCommands;
    public Logger log;
    public List<ICommand> commands = new ArrayList<>();
    public BI18nManager i18n;
    public BTimeoutManager timeouts;
    public Random rand = new Random();
    public GoogleApiUser gApi;
    public BSqlManager sql;

    public Path path;
    public Path pathConfig;
    public Path pathConfigCustomCommands;
    public Path pathLogs;

    public CommandImage cmdImg;

    public Botico(IClientSpecificConfig cfg, Path path)
    {
        this.clientConfig = cfg;
        this.path = path;
        pathConfig = path.resolve("config.json");
        pathLogs = path.resolve("logs");
        pathConfigCustomCommands = path.resolve("customcommands.json");
        log = createLogger(pathLogs, cfg.getName());
    }

    public Botico(IClientSpecificConfig cfg)
    {
        this(cfg, D.getWorkingDir().resolve("Botico"));
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
        loadCustomCommandsConfig();

        log.info("Loading the SQL manager...");
        sql = new BSqlManager(this);
        log.info("The SQL manager successfully loaded!");

        gApi = new GoogleApiUser(config.googleApiKeys);

        log.info("Loading I18n...");
        i18n = new BI18nManager(this);
        i18n.load();
        log.info("I18n loaded!");

        log.info("Loading the timeout manager...");
        timeouts = new BTimeoutManager(this);
        timeouts.load();
        log.info("The timeout manager successfully loaded!");

        log.info("Adding commands...");
        addCommands();
        log.info("Commands added!");

        log.info("Botico prepared!");
    }

    private void loadCustomCommandsConfig()
    {
        if (!Files.exists(pathConfigCustomCommands))
        {
            log.warn("Custom commands config doesn't exists! It will be created at " + pathConfigCustomCommands + ". You can set it up, if you want.");

            CustomCommandsConfig cfg = new CustomCommandsConfig();
            CustomCommandsConfig.Entry en = new CustomCommandsConfig.Entry();
            en.hidden = false;
            CustomCommandsConfig.Entry.LocaleSpecificEntry entr = new CustomCommandsConfig.Entry.LocaleSpecificEntry();
            entr.text = "A simple custom command response.";
            entr.description = "%1$s - try it!%nAlternatives: %2$s.";
            entr.names = new String[]{"customcommand1", "cc1"};
            CustomCommandsConfig.Entry.LocaleSpecificEntry.FileEntry file = new CustomCommandsConfig.Entry.LocaleSpecificEntry.FileEntry();
            file.path = "image.png";
            file.type = BFile.Type.IMAGE;
            entr.files = new CustomCommandsConfig.Entry.LocaleSpecificEntry.FileEntry[]{file};
            en.entries.put("en_us", entr);
            cfg.entries = new CustomCommandsConfig.Entry[]{en};

            try (FileWriter wr = new FileWriter(pathConfigCustomCommands.toString()))
            {
                BConfig.GSON.toJson(cfg, wr);
            } catch (IOException e)
            {
                log.error("Can't create an example custom commands config!", e);
            }
            configCustomCommands = new CustomCommandsConfig();
        }

        log.info("Loading the custom commands config file...");
        try (FileReader rdr = new FileReader(pathConfigCustomCommands.toString()))
        {
            configCustomCommands = BConfig.GSON.fromJson(rdr, CustomCommandsConfig.class);
            log.info("The custom commands config file successfully loaded!");
        } catch (IOException e)
        {
            log.error("Can't load the custom commands config file!", e);
        }
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
            ex.admins = new String[] {"0", "1"};
            ex.commandsOnOneHelpPage = 5;
            ex.googleApiKeys = new String[] {"key1", "key2"};
            BConfig.WikiSource ws = new BConfig.WikiSource();
            ws.displayName = "Wikipedia";
            ws.apiUrl = "https://en.wikipedia.org/w/api.php";
            ex.wikiSources = new BConfig.WikiSource[] {ws};
            ex.mentions = false;
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

    /*
    ToDo:
    -Question
    -Answer
    -Things
    -Dictionary
    -Wolfram
    -Wall
    -Random
     */
    protected void addCommands()
    {
        commands.add(new CommandHelp(this));
        commands.add(new CommandAbout());
        commands.add(new CommandLocale());
        commands.add(new CommandRussianRoulette());
        commands.add(new CommandTimeout());
        commands.add(new CommandTurn());
        commands.add(new CommandAstral());
        cmdImg = new CommandImage();
        cmdImg.load(this);
        commands.add(cmdImg);
        commands.add(new CommandReactor());
        for(CustomCommandsConfig.Entry e : configCustomCommands.entries)
        {
            commands.add(new CommandCustom(e));
        }
        commands.add(new CommandWho());
        commands.add(new CommandWiki());
        commands.add(new CommandGC());
    }

    public boolean hasCommand(String input, BUser user)
    {
        if(!input.startsWith(config.prefix))
            return false;

       String cmd = getCommandName(clearCommand(input));

        for(String alias : config.aliases.keySet())
        {
            if(alias.equals(cmd))
                return true;
        }
        I18n loc = i18n.getForUser(user.getId());
        for(ICommand com : commands)
        {
            for(String name : com.getNames(this, loc))
            {
                if(name.equals(cmd))
                    return true;
            }
        }

        return false;
    }

    public BResponse useCommand(String input, BUser user, boolean group, List<BUser> chatMembers)
    {
        if(!input.startsWith(config.prefix))
            return new BResponse("");
        String cmd = clearCommand(input);
        String cmdName = getCommandName(cmd);
        for(Map.Entry<String, String> entr : config.aliases.entrySet())
        {
            if(cmdName.equals(entr.getKey()))
            {
                cmd = entr.getValue();
                cmdName = getCommandName(entr.getValue());
            }
        }
        I18n loc = i18n.getForUser(user.getId());
        for(ICommand com : commands)
        {
            for(String name : com.getNames(this, loc))
            {
                if(name.equals(cmdName))
                {
                    if(!timeouts.isFree(user.getId()))
                        return new BResponse(loc.format("timeout.text", ChronoUnit.SECONDS.between(LocalDateTime.now(), timeouts.getTimeout(user.getId()))));

                    log.info(user.toString() + " executed a command \"" + input + "\".");
                    String jargs = cmd.contains(" ") ? cmd.substring(cmd.indexOf(" ") + 1) : "";
                    BResponse resp = com.use(new BArgs(cmd, cmdName, jargs, jargs.equals("") ? new String[]{} : jargs.split(" "), user, group, this, rand, chatMembers, config.prefix, loc));
                    if(!config.lineBreaks)
                        resp.setText(resp.getText().replace("\r\n", "; ").replace("\r", "; ").replace("\n", "; "));
                    if(resp.getText().length() > config.textLimit)
                        resp.setText(resp.getText().substring(0, config.textLimit));
                    return resp;
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
        String cmd = input.substring(config.prefix.length());
        while(cmd.startsWith(" "))
        {
            cmd = cmd.substring(1);
        }
        return cmd;
    }

    private String getCommandName(String cmdWithArgs)
    {
        if(cmdWithArgs.contains(" "))
            cmdWithArgs = cmdWithArgs.substring(0, cmdWithArgs.indexOf(" "));
        return cmdWithArgs.toLowerCase();
    }

    public IClientSpecificConfig getClientConfig()
    {
        return clientConfig;
    }
}
