package ru.pearx.botico.commands.model;

import ru.pearx.botico.Botico;
import ru.pearx.lib.i18n.I18n;

import java.util.List;
import java.util.Random;

/*
 * Created by mrAppleXZ on 04.08.17 9:50.
 */
public class BArgs
{
    // /test 14 | test 14
    private String rawCommand;
    private BUser sender;
    private boolean isInGroupChat;
    private Botico botico;
    private Random rand;
    private List<BUser> groupChatMembers;
    private String prefix;
    private I18n i18n;

    private String commandName;
    private String argumentsJoined;
    private String[] arguments;

    public BArgs(String rawCommand, String commandName, String argumentsJoined, String[] arguments, BUser sender, boolean isInGroupChat, Botico botico, Random rand, List<BUser> groupChatMembers, String prefix, I18n i18n)
    {
        this.rawCommand = rawCommand;
        this.sender = sender;
        this.isInGroupChat = isInGroupChat;
        this.botico = botico;
        this.rand = rand;
        this.groupChatMembers = groupChatMembers;
        this.prefix = prefix;
        this.i18n = i18n;
        this.commandName = commandName;
        this.argumentsJoined = argumentsJoined;
        this.arguments = arguments;
    }

    /**
     * Returns the raw command. For example, if user entered "/testcmd 15 15 20", raw command should be "testcmd 15 15 20"
     */
    public String getRawCommand()
    {
        return rawCommand;
    }

    /**
     * Returns the command sender.
     */
    public BUser getSender()
    {
        return sender;
    }

    /**
     * Returns true if command executed in group chat, otherwise returns false.
     */
    public boolean isInGroupChat()
    {
        return isInGroupChat;
    }

    /**
     * A {@link Botico} instance.
     */
    public Botico getBotico()
    {
        return botico;
    }

    /**
     * A {@link Random} instance.
     */
    public Random getRand()
    {
        return rand;
    }

    /**
     * Returns a list of members in group chat if command executed in group chat, otherwise returns null.
     */
    public List<BUser> getGroupChatMembers()
    {
        return groupChatMembers;
    }

    /**
     * Returns the command prefix.
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * Gets the command name. For example, if user entered "/testcommand 15 15 20", command name should be "testcommand".
     */
    public String getCommandName()
    {
        return commandName;
    }

    /**
     * Gets the joined arguments. For example, if user entered "/testcommand 15 15 20", joined arguments should be "15 15 20".
     */
    public String getArgumentsJoined()
    {
        return argumentsJoined;
    }

    /**
     * Gets the arguments. For example, if user entered "/testcommad 15 15 20", arguments should be [0] 15, [1] 15, [2] 20.
     */
    public String[] getArguments()
    {
        return arguments;
    }

    /**
     * An {@link I18n} instance.
     */
    public I18n getI18n()
    {
        return i18n;
    }
}
