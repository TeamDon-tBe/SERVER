package com.dontbe.www.DontBeServer.common.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class TextUtil {
    public static String cuttingText(int number, String text) {
        if(text == null) {
            return null;
        }
        if(text.length() > number) {
            return text.substring(0, number);
        } else {
            return text;
        }
    }
}
