package ru.pearx.botico.model;

import java.io.InputStream;

/*
 * Created by mrAppleXZ on 04.08.17 9:43.
 */
public class BFile
{
    private String name;
    private InputStream stream;
    private Type type;
    private String mimeType;

    public BFile(String name, InputStream stream, Type type, String mimeType)
    {
        this.name = name;
        this.stream = stream;
        this.type = type;
        this.mimeType = mimeType;
    }

    public String getName()
    {
        return name;
    }

    public InputStream getStream()
    {
        return stream;
    }

    public String getMimeType()
    {
        return mimeType;
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
