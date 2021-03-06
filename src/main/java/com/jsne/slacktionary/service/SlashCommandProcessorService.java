package com.jsne.slacktionary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jsne.slacktionary.model.Channel;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by brandonmanson on 11/22/17.
 */
@Service
public class SlashCommandProcessorService {

    private String EPHEMERAL_URL = "https://slack.com/api/chat.postEphemeral";
    private String MESSAGE_URL = "https://slack.com/api/chat.postMessage";

    @Autowired
    ChannelStateManagerService stateManagerService;

    @Autowired
    MessageBuilderService builderService;

    @Autowired
    RestTemplate restTemplate;

    public JsonNode processNewGameCommand(String channelId, String userId, String teamId) {
        System.out.println("SETTING CHANNEL " + channelId + " TO ACTIVE");
        Channel channel = stateManagerService.setChannelToActive(channelId, userId, teamId);
        System.out.println("CHANNEL CREATED. CURRENT STATUS: "
                + "\nCHANNEL ID: " + channel.getChannelId()
                + "\nACTIVE USER: " + channel.getActiveUserId()
                + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                + "\nPLAYERS: " + channel.getPlayers()
                + "\nGUESSES: " + channel.getGuesses());
        HttpEntity<JsonNode> entity = setupHttpClient(channel, builderService.createPhraseMessage(channel, channel.getActiveUserId(), channel.getToken()));
        restTemplate.postForEntity(EPHEMERAL_URL, entity, JsonNode.class);
        return builderService.createNewGameMessage();
    }

    public JsonNode processJoinCommand(String channelId, String userId) {
        if (stateManagerService.addPlayerToActiveChannel(channelId, userId) != null)
        {
            return builderService.createJoinResponseMessage();
        }
        return builderService.createJoinMessageForActiveUser();
    }

    public JsonNode processGuessCommand(String channelId, String userId, String phrase) {
        Channel channel = stateManagerService.addGuessToGuessList(channelId, userId, phrase);
        if (channel != null)
        {
            if (channel.getGuesses().size() == channel.getPlayers().size())
            {
                if (getWinnerFromChannel(channel) != null)
                {
                    String winner = getWinnerFromChannel(channel).getKey();
                    Channel updatedChannel = stateManagerService.setChannelToInactive(channelId);
                    if (updatedChannel != null)
                    {
                        return builderService.createWinnerNotificationMessage(channelId, winner, channel.getToken());
                    }
                }
                Channel updatedChannel = stateManagerService.setChannelToInactive(channelId);
                if (updatedChannel != null)
                {
                    return builderService.createNoWinnerMessage();
                }
            }
            return builderService.createGuessResponseMessage();
        }
        return null;
    }

    private Map.Entry<String, String> getWinnerFromChannel(Channel channel) {
        Map.Entry<String, String> player = channel.getGuesses().entrySet().stream()
                .filter(guess -> guess.getValue().equals(channel.getActivePhrase()))
                .findFirst()
                .orElse(null);
        return player;
    }

    private HttpEntity<JsonNode> setupHttpClient(Channel channel, JsonNode body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + channel.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<JsonNode> entity = new HttpEntity<JsonNode>(body, headers);
        return entity;
    }
}
