package com.jsne.slacktionary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jsne.slacktionary.util.PhraseLibrary;
import org.springframework.stereotype.Service;

/**
 * Created by brandonmanson on 11/16/17.
 */
@Service
public class MessageBuilderService {

    private PhraseLibrary library = new PhraseLibrary();
    private ObjectMapper mapper = new ObjectMapper();

    public JsonNode createNewGameMessage() {
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

    public JsonNode createPhraseMessage() {
        ObjectNode node = mapper.createObjectNode();
        ArrayNode attachments = mapper.createArrayNode();
        ObjectNode attachment = mapper.createObjectNode();
        attachment.put("title", "Your Phrase :eyes:");
        attachment.put("text", library.getRandomPhrase());
        attachments.add(attachment);
        node.putPOJO("attachments", attachments);
        return node;
    }

    public JsonNode createJoinResponseMessage() {
        ObjectNode node = mapper.createObjectNode();
        node.put("response_type", "ephemeral");
        node.put("text", "You're in! Be on the :telescope: :eyes: for some emojis soon.");
        return node;
    }

    public JsonNode createGuessResponseMessage() {
        ObjectNode node = mapper.createObjectNode();
        node.put("response_type", "ephemeral");
        node.put("text", "Guess recorded. Just remember, if you ain't first you're last!");
        return node;
    }

    public JsonNode createWinnerNotificationMessage() {
        ObjectNode node = mapper.createObjectNode();
        ArrayNode attachments = mapper.createArrayNode();
        ObjectNode attachment = mapper.createObjectNode();
        attachment.put("title", "USER TBD :clap: :tada: :white_check_mark:");
        node.put("text", ":checkered_flag: WE HAVE A WINNER :checkered_flag:");
        node.putPOJO("attachments", attachments);
        return node;
    }
}
