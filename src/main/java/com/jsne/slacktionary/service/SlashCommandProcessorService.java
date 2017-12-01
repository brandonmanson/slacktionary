package com.jsne.slacktionary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jsne.slacktionary.model.Channel;
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
        Channel channel = stateManagerService.setChannelToActive(channelId, userId, teamId);
        HttpEntity<JsonNode> entity = setupHttpClient(channel, builderService.createPhraseMessage(channel.getChannelId(), channel.getActiveUserId(), channel.getToken()));
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
                    channel.setToInactive();
                    return builderService.createWinnerNotificationMessage(channelId, winner, channel.getToken());
                }
                return builderService.createNoWinnerMessage();
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
        System.out.println("PLAYER " + player);
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
