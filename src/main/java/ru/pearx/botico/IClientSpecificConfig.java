package ru.pearx.botico;

import ru.pearx.botico.model.BUser;

/*
 * Created by mrAppleXZ on 09.08.17 13:58.
 */
public interface IClientSpecificConfig
{
    String getName();
    String createMention(BUser user);
}
