//package com.example.googlemapsproject;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class VoiceCommands {
//    List<String> numbers;
//    List<String> numberStrings;
//
//    public void init(){
//        numbers = new ArrayList<>();
//        for(Integer i = 0; i < 8; i++){
//            numbers.add(i.toString());
//
//        }
//        numberStrings = Arrays.asList("zero", "one", "two",   "three", "four",
//                "five", "six", "seven");
//    }
//    public int handleSpeech(String command){
//
//        String[] commandItems = command.split(" ");
//        int len = command.length();
//
//
//        if(commandItems[0].equals("navigate")){
//            String lastWord = commandItems[len-1];
//            if(numbers.contains(lastWord) || numberStrings.contains(lastWord)){
//                for(int i = 0; i < 8; i++){
//                    if(numbers.get(i).equals(lastWord)
//                    || numberStrings.get(i).equals(lastWord))
//                        return i;
//                }
//            }
//        }
//    }
//}
