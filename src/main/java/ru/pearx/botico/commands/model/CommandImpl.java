package ru.pearx.botico.commands.model;

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
        return i18n.format(descUnlocalized, b.config.prefix + getNames(b, i18n)[0]);
    }

    @Override
    public abstract BResponse use(BArgs args);
}
