package ru.pearx.botico.commands.model;

/*
 * Created by mrAppleXZ on 04.08.17 10:13.
 */
public class BUser
{
    private String name;
    private String id;

    public BUser(String name, String id)
    {
        this.name = name;
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public String getId()
    {
        return id;
    }
}
