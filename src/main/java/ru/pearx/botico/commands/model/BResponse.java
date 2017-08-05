package ru.pearx.botico.commands.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by mrAppleXZ on 04.08.17 9:38.
 */
public class BResponse
{
    private String text;
    private List<BFile> files;

    public BResponse(String text, List<BFile> files)
    {
        this.text = text;
        this.files = files;
    }

    public BResponse(String text)
    {
        this.text = text;
        this.files = new ArrayList<>();
    }

    public BResponse(List<BFile> files)
    {
        this.files = files;
    }

    public String getText()
    {
        return text;
    }

    public List<BFile> getFiles()
    {
        return files;
    }
}
