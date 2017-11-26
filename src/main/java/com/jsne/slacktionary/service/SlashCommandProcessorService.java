package com.jsne.slacktionary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jsne.slacktionary.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by brandonmanson on 11/22/17.
 */
@Service
public class SlashCommandProcessorService {

    private String EPHEMERAL_URL = "https://slack.com/api/chat.postMessage";

    @Autowired
    ChannelStateManagerService stateManagerService;

    @Autowired
    MessageBuilderService builderService;

    @Autowired
    RestTemplate restTemplate;

    public JsonNode processNewGameCommand(String channelId, String userId, String token) {
        Channel channel = stateManagerService.setChannelToActive(channelId, userId, token);
        HttpEntity<JsonNode> entity = setupHttpClient(channel, builderService.createPhraseMessage(channel.getChannelId(), channel.getActiveUserId(), channel.getToken()));
        restTemplate.postForEntity(EPHEMERAL_URL, entity, JsonNode.class);
        return builderService.createNewGameMessage();
    }

    public JsonNode processJoinCommand(String channelId, String userId) {
        stateManagerService.addPlayerToActiveChannel(channelId, userId);
        return builderService.createJoinResponseMessage();
    }

    private HttpEntity<JsonNode> setupHttpClient(Channel channel, JsonNode body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + channel.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<JsonNode> entity = new HttpEntity<JsonNode>(body, headers);
        return entity;
    }
}
