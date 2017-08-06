package ru.pearx.botico.model;

import ru.pearx.botico.Botico;
import ru.pearx.lib.i18n.I18n;

/*
 * Created by mrAppleXZ on 04.08.17 18:54.
 */
public abstract class CommandImpl implements ICommand
{
    protected String namesUnlocalized;
    protected String descUnlocalized;

    public CommandImpl(String namesUnlocalized, String descUnlocalized)
    {
        this.namesUnlocalized = namesUnlocalized;
        this.descUnlocalized = descUnlocalized;
    }

    @Override
    public String[] getNames(Botico b, I18n i18n)
    {
        return i18n.format(namesUnlocalized).split(",");
    }

    @Override
    public boolean isHidden(Botico b)
    {
        return false;
    }

    @Override
    public String getDescription(Botico b, I18n i18n)
    {
        StringBuilder sb = new StringBuilder();
        String[] names = getNames(b, i18n);
        for(int i = 1; i < names.length; i++)
        {
            sb.append(b.config.prefix);
            sb.append(names[i]);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return i18n.format(descUnlocalized, b.config.prefix + names[0], sb.toString());
    }

    @Override
    public abstract BResponse use(BArgs args);
}
