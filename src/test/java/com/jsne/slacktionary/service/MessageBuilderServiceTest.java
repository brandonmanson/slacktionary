package com.jsne.slacktionary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsne.slacktionary.model.Channel;
import com.jsne.slacktionary.util.MessageBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by brandonmanson on 11/16/17.
 */
@RunWith(SpringRunner.class)
public class MessageBuilderServiceTest {

    private MessageBuilder builder = new MessageBuilder();

    private Channel channel;

    @SpyBean
    MessageBuilderService service;

    @Before
    public void setup() {
        channel = new Channel("C678293", "ab457893dg23");
        channel.setToActive("A horse of a different color", "U123456");
    }

    @Test
    public void newGameMessageShouldBeReturned() {
        Mockito.when(service.createNewGameMessage()).thenReturn(builder.createNewGameMessage());
        JsonNode newGameMessage = service.createNewGameMessage();
        assertEquals(builder.createNewGameMessage(), newGameMessage);
    }

    @Test
    public void newPhraseMessageShouldBeReturned() {
        when(service.createPhraseMessage(channel, "user", "token")).thenReturn(builder.createPhraseMessage(channel, "user", "token"));
        JsonNode phraseMessage = service.createPhraseMessage(channel, "user", "token");
        assertEquals(phraseMessage, builder.createPhraseMessage(channel, "user", "token"));
    }

    @Test
    public void joinResponseMessageShouldBeReturned() {
        when(service.createJoinResponseMessage()).thenReturn(builder.createJoinResponseMessage());
        JsonNode joinResponseMessage = service.createJoinResponseMessage();
        assertEquals(builder.createJoinResponseMessage(), joinResponseMessage);
    }

    @Test
    public void guessResponseMessageShouldBeReturned() {
        when(service.createGuessResponseMessage()).thenReturn(builder.createGuessResponseMessage());
        JsonNode guessResponseMessage = service.createGuessResponseMessage();
        assertEquals(builder.createGuessResponseMessage(), guessResponseMessage);
    }

    @Test
    public void winnerNotificationMessageShouldBeReturned() {
        when(service.createWinnerNotificationMessage("channel", "user", "token")).thenReturn(builder.createWinnerNotificationMessage("channel", "user", "token"));
        JsonNode winnerNotificationMessage = service.createWinnerNotificationMessage("channel", "user", "token");
        assertEquals(builder.createWinnerNotificationMessage("channel", "user", "token"), winnerNotificationMessage);
    }

    @Test
    public void helpMessageShouldBeReturned() {
        when(service.createHelpMessage()).thenReturn(builder.createHelpMessage());
        JsonNode helpMessage = service.createHelpMessage();
        assertEquals(builder.createHelpMessage(), helpMessage);

    }

    @Test
    public void noWinnerMessageShouldBeReturned() {
        when(service.createNoWinnerMessage()).thenReturn(builder.createNoWinnerMessage());
        assertEquals(builder.createNoWinnerMessage(), service.createNoWinnerMessage());
    }
}
