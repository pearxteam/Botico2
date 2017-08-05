package ru.pearx.botico.commands.model;

import ru.pearx.botico.Botico;
import ru.pearx.lib.i18n.I18n;

/*
 * Created by mrAppleXZ on 03.08.17 10:47.
 */
public interface ICommand
{
    String[] getNames(Botico b, I18n i18n);
    boolean isHidden(Botico b);
    String getDescription(Botico b, I18n i18n);
    BResponse use(BArgs args);
}
