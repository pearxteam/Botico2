package ru.pearx.botico.commands.model;

import java.io.InputStream;

/*
 * Created by mrAppleXZ on 04.08.17 9:43.
 */
public class BFile
{
    private String name;
    private InputStream stream;
    private Type type;

    public BFile(String name, InputStream stream, Type type)
    {
        this.name = name;
        this.stream = stream;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public InputStream getStream()
    {
        return stream;
    }

    public Type getType()
    {
        return type;
    }

    public enum Type
    {
        BYTES,
        IMAGE,
        ANIM_IMAGE
    }
}
