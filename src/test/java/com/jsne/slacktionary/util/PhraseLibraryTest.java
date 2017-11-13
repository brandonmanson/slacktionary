package com.jsne.slacktionary.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by brandonmanson on 11/12/17.
 */
@RunWith(SpringRunner.class)
public class PhraseLibraryTest {

    @Test
    public void getRandomPhraseShouldNotBeNull() {
        assertNotNull(PhraseLibrary.getRandomPhrase());
    }
}
