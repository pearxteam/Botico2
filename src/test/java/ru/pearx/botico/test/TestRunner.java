package ru.pearx.botico.test;

import ru.pearx.botico.Botico;
import ru.pearx.botico.model.BResponse;
import ru.pearx.botico.model.BUser;

import java.io.IOException;
import java.util.Scanner;

/*
 * Created by mrAppleXZ on 03.08.17 17:27.
 */
public class TestRunner
{
    public static Botico botico = new Botico("Test");

    public static void main(String... args) throws IOException
    {
        botico.prepare();
        try(Scanner scan = new Scanner(System.in))
        {
            while(true)
            {
                String s = scan.nextLine();
                if(s.equals("quit"))
                    return;
                BUser user = new BUser(System.getProperty("user.name"), Integer.toString(System.getProperty("user.name").hashCode()));
                if(botico.hasCommand(s, user))
                {
                    BResponse resp = botico.useCommand(s, user);
                    if(!resp.getText().equals(""))
                    {
                        System.out.println(resp.getText());
                    }
                }
            }
        }
    }
}
