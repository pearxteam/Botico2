package ru.pearx.botico.model;

import ru.pearx.botico.Botico;

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

    @Override
    public String toString()
    {
        return getName() + "[" + getId() + "]";
    }

    public boolean isAdmin(Botico b)
    {
        for(String s : b.config.admins)
        {
            if(s.equals(getId()))
                return true;
        }
        return false;
    }
}
