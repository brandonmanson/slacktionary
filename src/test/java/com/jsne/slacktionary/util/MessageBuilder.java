package com.jsne.slacktionary.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by brandonmanson on 11/16/17.
 */
public class MessageBuilder {

    public ObjectMapper mapper = new ObjectMapper();

    public MessageBuilder(){};

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

    public JsonNode createPhraseMessage(String channel, String user, String token) {
        ObjectNode node = mapper.createObjectNode();
        ArrayNode attachments = mapper.createArrayNode();
        ObjectNode attachment = mapper.createObjectNode();
        attachment.put("title", "Your Phrase :eyes:");
        attachment.put("text", "A horse of a different color");
        attachments.add(attachment);
        node.put("token", token);
        node.put("channel", channel);
        node.put("user", user);
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

    public JsonNode createWinnerNotificationMessage(String channel, String user, String token) {
        String title = String.format("<@%s> :clap: :tada: :white_check_mark:", user);
        ObjectNode node = mapper.createObjectNode();
        ArrayNode attachments = mapper.createArrayNode();
        ObjectNode attachment = mapper.createObjectNode();
        attachment.put("title", title);
        node.put("channel", channel);
        node.put("token", token);
        node.put("text", ":checkered_flag: WE HAVE A WINNER :checkered_flag:");
        node.putPOJO("attachments", attachments);
        return node;
    }

    public JsonNode createHelpMessage() {
        ObjectNode node = mapper.createObjectNode();
        ArrayNode attachments = mapper.createArrayNode();
        attachments.add(createAttachment("New Game", "Start a new game", "new"));
        attachments.add(createAttachment("Join Game", "Join a new game", "join"));
        attachments.add(createAttachment("Take a Guess", "Submit a guess", "guess [your guess]"));
        attachments.add(createAttachment("Get Help", "See the list of available commands for Slacktionary", "help"));
        node.put("response_type", "ephemeral");
        node.put("text", "Slacktionary is the game that gives you a random phrase that you'll transcribe into emojis for your friends to guess.\n\nFor instance, for \"A horse of a different color\", you might post something like:\n :horse: :blue_heart: :green_heart: :yellow_heart: :heart:\n\nMake sense? :thumbsup: :raised_hands: Here's how to get started.");
        node.putPOJO("attachments", attachments);
        return node;
    }

    private JsonNode createAttachment(String title, String pretext, String text) {
        ObjectNode attachment = mapper.createObjectNode();
        ArrayNode markdown = mapper.createArrayNode();
        markdown.add("text");
        markdown.add("pretext");
        attachment.put("title", title);
        attachment.put("pretext", "_" + pretext + "_");
        attachment.put("text", "`/slacktionary" + text + "`");
        attachment.putPOJO("mrkdwn_in", markdown);
        return attachment;
    }
}
