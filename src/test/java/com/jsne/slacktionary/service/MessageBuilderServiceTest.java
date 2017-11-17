package com.jsne.slacktionary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
@SpringBootTest
public class MessageBuilderServiceTest {

    private ObjectMapper mapper = new ObjectMapper();

    @SpyBean
    private MessageBuilderService service;

    @Test
    public void newGameMessageShouldBeReturned() {
        Mockito.when(service.createNewGameMessage()).thenReturn(createNewGameMessage());
        JsonNode newGameMessage = service.createNewGameMessage();
        assertEquals(createNewGameMessage(), newGameMessage);
    }

    @Test
    public void newPhraseMessageShouldBeReturned() {
        when(service.createPhraseMessage()).thenReturn(createPhraseMessage());
        JsonNode phraseMessage = service.createPhraseMessage();
        assertEquals(createPhraseMessage(), phraseMessage);
    }

    @Test
    public void joinResponseMessageShouldBeReturned() {
        when(service.createJoinResponseMessage()).thenReturn(createJoinResponseMessage());
        JsonNode joinResponseMessage = service.createJoinResponseMessage();
        assertEquals(createJoinResponseMessage(), joinResponseMessage);
    }

    @Test
    public void guessResponseMessageShouldBeReturned() {
        when(service.createGuessResponseMessage()).thenReturn(createGuessResponseMessage());
        JsonNode guessResponseMessage = service.createGuessResponseMessage();
        assertEquals(createGuessResponseMessage(), guessResponseMessage);
    }

    @Test
    public void winnerNotificationMessageShouldBeReturned() {
        when(service.createWinnerNotificationMessage()).thenReturn(createWinnerNotificationMessage());
        JsonNode winnerNotificationMessage = createWinnerNotificationMessage();
        assertEquals(createWinnerNotificationMessage(), winnerNotificationMessage);
    }

    private JsonNode createNewGameMessage() {
        ObjectNode node = mapper.createObjectNode();
        ArrayNode attachments = mapper.createArrayNode();
        ObjectNode attachmentNode = mapper.createObjectNode();
        attachmentNode.put("text", "Enter `/slacktionary join` to join the fun!");
        attachments.add(attachmentNode);
        node.put("response_type", "in_channel");
        node.put("text", "Attention! :speaker: New game of Slacktionary starting soon!");
        node.putPOJO("attachments", attachments);
        return node;
    }

    private JsonNode createPhraseMessage() {
        ObjectNode node = mapper.createObjectNode();
        ArrayNode attachments = mapper.createArrayNode();
        ObjectNode attachment = mapper.createObjectNode();
        attachment.put("title", "Your Phrase :eyes:");
        attachment.put("text", "A horse of a different color");
        attachments.add(attachment);
        node.putPOJO("attachments", attachments);
        return node;
    }

    private JsonNode createJoinResponseMessage() {
        ObjectNode node = mapper.createObjectNode();
        node.put("response_type", "ephemeral");
        node.put("text", "You're in! Be on the :telescope: :eyes: for some emojis soon.");
        return node;
    }

    private JsonNode createGuessResponseMessage() {
        ObjectNode node = mapper.createObjectNode();
        node.put("response_type", "ephemeral");
        node.put("text", "Guess recorded. Just remember, if you ain't first you're last!");
        return node;
    }

    private JsonNode createWinnerNotificationMessage() {
        ObjectNode node = mapper.createObjectNode();
        ArrayNode attachments = mapper.createArrayNode();
        ObjectNode attachment = mapper.createObjectNode();
        attachment.put("title", "USER TBD :clap: :tada: :white_check_mark:");
        node.put("text", ":checkered_flag: WE HAVE A WINNER :checkered_flag:");
        node.putPOJO("attachments", attachments);
        return node;
    }
}
