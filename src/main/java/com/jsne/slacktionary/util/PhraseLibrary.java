package com.jsne.slacktionary.util;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by brandonmanson on 11/12/17.
 */
public class PhraseLibrary {

    public static String getRandomPhrase() {
        ArrayList<String> phrases = new ArrayList<>();
        phrases.add("A horse of a different color");
        phrases.add("Under the weather");
        phrases.add("Raining cats and dogs");
        phrases.add("One smart cookie");
        phrases.add("Put your foot in your mouth");
        phrases.add("Get cold feet");
        phrases.add("Fly on the wall");

        Random random = new Random();
        int index = random.nextInt(phrases.size() - 1);
        return phrases.get(index);
    }
}
