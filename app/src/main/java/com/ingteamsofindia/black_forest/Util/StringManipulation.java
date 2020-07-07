package com.ingteamsofindia.black_forest.Util;

public class StringManipulation {
    public static String expandUsername(String username){
        return username.replace("."," ");
    }
    public static String condenseUsername(String username){
        return username.replace(" ",".");
    }
}
