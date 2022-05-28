package com.rafaelhosaka.shareme.utils;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Format {
    public static String escapeMetaCharacters(String str) {
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","%","@"};

        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(str.contains(metaCharacters[i])){
                str = str.replace(metaCharacters[i],"\\"+metaCharacters[i]);
            }
        }
        return str;
    }
}
