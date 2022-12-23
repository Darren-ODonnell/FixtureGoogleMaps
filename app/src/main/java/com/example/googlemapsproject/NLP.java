package com.example.googlemapsproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NLP {
    private static List<String> numbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
    private static List<String> numberStrings = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight");

    static int parseNumber(String input){
        //index returned is one less than recognised from speech as requesting one
        //should return the first in the list;
        input = input.toLowerCase(Locale.ROOT);

        if(numbers.contains(input))
            return numbers.indexOf(input);
        if(numberStrings.contains(input))
            return numberStrings.indexOf(input);
        return -1;
    }
}
