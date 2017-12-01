package com.jsne.slacktionary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jsne.slacktionary.model.Channel;
import com.jsne.slacktionary.model.Team;
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
    private String CHANNEL_ID_2 = "C216783705";
    private String USER_ID_1 = "U2147483697";
    private String USER_ID_2 = "U1234567890";
    private String USER_ID_3 = "U1799867890";
    private String TEAM_ID = "T001";
    private String TOKEN = "gIkuvaNzQIHg97ATvDxqgjtO";
    private String PHRASE = "A horse of a different color";
    private String OTHER_PHRASE = "Horse hearts";
    private String MESSAGE_URL = "https://slack.com/api/chat.postMessage";
    private String EPHEMERAL_URL = "https://slack.com/api/chat.postEphemeral";
    private Team team;
    private Channel activeChannel;
    private Channel activeChannel2;
    private Channel activeChannel3;
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

    @Before
    public void setup() {
        team = new Team(TEAM_ID, TOKEN);
        builder = new MessageBuilder();
        activeChannel = new Channel(CHANNEL_ID, TOKEN);
        activeChannel.setToActive(PHRASE, USER_ID_1);
        activeChannel.getPlayers().add(USER_ID_2);
        activeChannel.getPlayers().add(USER_ID_3);
        activeChannel.getGuesses().put(USER_ID_2, OTHER_PHRASE);
        activeChannel.getGuesses().put(USER_ID_3, OTHER_PHRASE);
        activeChannel2 = new Channel(CHANNEL_ID_2, TOKEN);
        activeChannel2.setToActive(PHRASE, USER_ID_1);
        activeChannel2.getPlayers().add(USER_ID_2);
        activeChannel2.getGuesses().put(USER_ID_2, PHRASE);
        activeChannel3 = new Channel(CHANNEL_ID, TOKEN);
        activeChannel3.setToActive(PHRASE, USER_ID_1);
        activeChannel3.getPlayers().add(USER_ID_2);
        activeChannel3.getPlayers().add(USER_ID_3);

        setupHttpClient();
    }

    @Test
    public void processingNewGameShouldReturnNewGameMessage() {
        Mockito.when(stateManagerService.setChannelToActive(CHANNEL_ID, USER_ID_1, TEAM_ID)).thenReturn(activeChannel);
        Mockito.when(messageBuilderService.createNewGameMessage()).thenReturn(builder.createNewGameMessage());
        Mockito.when(restTemplate.postForEntity(EPHEMERAL_URL, httpEntity, JsonNode.class)).thenReturn(responseEntity);
        assertEquals(processorService.processNewGameCommand(CHANNEL_ID, USER_ID_1, TEAM_ID), builder.createNewGameMessage());
    }

    @Test
    public void verifyThatAPICallWasMadeWithCorrectData() {
        Mockito.when(restTemplate.postForEntity(EPHEMERAL_URL, httpEntity, JsonNode.class)).thenReturn(responseEntity);
        Mockito.when(stateManagerService.setChannelToActive(CHANNEL_ID, USER_ID_1, TEAM_ID)).thenReturn(activeChannel);
        Mockito.when(messageBuilderService.createPhraseMessage(CHANNEL_ID, USER_ID_1, team.getToken())).thenReturn(builder.createPhraseMessage(CHANNEL_ID, USER_ID_1, TOKEN));
        processorService.processNewGameCommand(CHANNEL_ID, USER_ID_1, TEAM_ID);
        Mockito.verify(restTemplate).postForEntity(EPHEMERAL_URL, httpEntity, JsonNode.class);
    }

    @Test
    public void processingJoinCommandForNonActiveUserShouldReturnJoinMessage() {
        Mockito.when(stateManagerService.addPlayerToActiveChannel(CHANNEL_ID, USER_ID_2)).thenReturn(activeChannel);
        Mockito.when(messageBuilderService.createJoinResponseMessage()).thenReturn(builder.createJoinResponseMessage());
        assertEquals(processorService.processJoinCommand(CHANNEL_ID, USER_ID_2), builder.createJoinResponseMessage());
    }

    @Test
    public void processingJoinCommandForActiveUserShouldReturnJoinMessageForActiveUser() {
        Mockito.when(stateManagerService.addPlayerToActiveChannel(CHANNEL_ID, USER_ID_1)).thenReturn(null);
        Mockito.when(messageBuilderService.createJoinMessageForActiveUser()).thenReturn(builder.createJoinMessageForActiveUser());
        assertEquals(processorService.processJoinCommand(CHANNEL_ID, USER_ID_1), builder.createJoinMessageForActiveUser());
    }

    @Test
    public void processingGuessForNonActiveUserShouldReturnGuessRecordedMessage() {
        Mockito.when(stateManagerService.addGuessToGuessList(CHANNEL_ID, USER_ID_2, OTHER_PHRASE)).thenReturn(activeChannel3);
        Mockito.when(messageBuilderService.createGuessResponseMessage()).thenReturn(builder.createGuessResponseMessage());
        assertEquals(builder.createGuessResponseMessage(), processorService.processGuessCommand(CHANNEL_ID, USER_ID_2, OTHER_PHRASE));
    }

    @Test
    public void returnNoWinnerMessageWhenAllGuessesHaveBeenRecordedAndNoneAreCorrect() {
        Mockito.when(stateManagerService.addGuessToGuessList(CHANNEL_ID, USER_ID_2, OTHER_PHRASE)).thenReturn(activeChannel);
        Mockito.when(messageBuilderService.createNoWinnerMessage()).thenReturn(builder.createNoWinnerMessage());
        assertEquals(builder.createNoWinnerMessage(), processorService.processGuessCommand(CHANNEL_ID, USER_ID_2, OTHER_PHRASE));
    }

    @Test
    public void returnWinnerMessageWhenAllGuessesHaveBeenRecordedAndThereIsAWinner() {
        Mockito.when(stateManagerService.addGuessToGuessList(CHANNEL_ID_2, USER_ID_2, PHRASE)).thenReturn(activeChannel2);
        Mockito.when(messageBuilderService.createWinnerNotificationMessage(CHANNEL_ID_2, USER_ID_2, TOKEN)).thenReturn(builder.createWinnerNotificationMessage(CHANNEL_ID_2, USER_ID_2, TOKEN));
        assertEquals(builder.createWinnerNotificationMessage(CHANNEL_ID_2, USER_ID_2, TOKEN), processorService.processGuessCommand(CHANNEL_ID_2, USER_ID_2, PHRASE));
    }

    private void setupHttpClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpEntity = new HttpEntity<JsonNode>(builder.createPhraseMessage(CHANNEL_ID, USER_ID_1, team.getToken()), headers);
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
