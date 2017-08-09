package ru.pearx.botico.model;

import java.util.ArrayList;
import java.util.Arrays;
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

    public BResponse(String text, BFile... files)
    {
        this.text = text;
        this.files = Arrays.asList(files);
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

    public void setText(String text)
    {
        this.text = text;
    }

    public List<BFile> getFiles()
    {
        return files;
    }

    public void setFiles(List<BFile> files)
    {
        this.files = files;
    }
}
