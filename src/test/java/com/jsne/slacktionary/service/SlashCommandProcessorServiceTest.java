package com.jsne.slacktionary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jsne.slacktionary.model.Channel;
import com.jsne.slacktionary.repository.ChannelRepository;
import com.jsne.slacktionary.util.MessageBuilder;
import com.jsne.slacktionary.util.PhraseLibrary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

/**
 * Created by brandonmanson on 11/22/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SlashCommandProcessorServiceTest {


    private String CHANNEL_ID = "C2147483705";
    private String USER_ID = "U2147483697";
    private String TOKEN = "gIkuvaNzQIHg97ATvDxqgjtO";
    private String PHRASE = "A horse of a different color";
    private String URL = "https://slack.com/api/chat.postMessage";
    private Channel activeChannel;
    private HttpEntity httpEntity;
    private ResponseEntity responseEntity;
    private MessageBuilder builder;

    @SpyBean
    SlashCommandProcessorService processorService;

    @MockBean
    ChannelStateManagerService stateManagerService;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    MessageBuilderService messageBuilderService;
//
//    @MockBean
//    ChannelRepository repository;

    @Before
    public void setup() {
        builder = new MessageBuilder();
        activeChannel = new Channel(CHANNEL_ID, TOKEN);
        activeChannel.setToActive(PHRASE, USER_ID);
        setupHttpClient();
    }

    @Test
    public void processingNewGameShouldReturnNewGameMessage() {
        Mockito.when(stateManagerService.setChannelToActive(CHANNEL_ID, USER_ID, TOKEN)).thenReturn(activeChannel);
        assertEquals(processorService.processNewGameCommand(CHANNEL_ID, USER_ID, TOKEN), builder.createNewGameMessage());
    }

    @Test
    public void verifyThatAPICallWasMadeWithCorrectData() {
        Mockito.when(restTemplate.postForEntity(URL, httpEntity, JsonNode.class)).thenReturn(responseEntity);
        Mockito.when(stateManagerService.setChannelToActive(CHANNEL_ID, USER_ID, TOKEN)).thenReturn(activeChannel);
        Mockito.when(messageBuilderService.createPhraseMessage(CHANNEL_ID, USER_ID, TOKEN)).thenReturn(builder.createPhraseMessage(CHANNEL_ID, USER_ID, TOKEN));
        processorService.processNewGameCommand(CHANNEL_ID, USER_ID, TOKEN);
        Mockito.verify(restTemplate).postForEntity(URL, httpEntity, JsonNode.class);
    }

    private void setupHttpClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpEntity = new HttpEntity<JsonNode>(builder.createPhraseMessage(CHANNEL_ID, USER_ID, TOKEN), headers);
        responseEntity = new ResponseEntity<JsonNode>(createSlackResponse(), HttpStatus.OK);
    }

    private JsonNode createSlackResponse() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("ok", true);
        node.put("message_ts", "1502210682.580145");
        return node;
    }
}
