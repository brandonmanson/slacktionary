package com.jsne.slacktionary.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jsne.slacktionary.service.MessageBuilderService;
import com.jsne.slacktionary.service.SlashCommandProcessorService;
import com.jsne.slacktionary.util.MessageBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by brandonmanson on 11/11/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SlashCommandController.class)
public class SlashCommandControllerTest {

    private String CHANNEL_ID = "C2147483705";
    private String USER_ID = "U2147483697";
    private String TOKEN = "gIkuvaNzQIHg97ATvDxqgjtO";
    private MessageBuilder builder = new MessageBuilder();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MessageBuilderService messageBuilderService;

    @MockBean
    SlashCommandProcessorService commandProcessorService;


    @Test
    public void controllerShouldPostNewGameMessage() throws Exception {

        when(commandProcessorService.processNewGameCommand(CHANNEL_ID, USER_ID, TOKEN)).thenReturn(builder.createNewGameMessage());
        JsonNode newGameMessage = builder.createNewGameMessage();

        mockMvc.perform(post("/command")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", "gIkuvaNzQIHg97ATvDxqgjtO")
                .param("team_id", "T0001")
                .param("team_domain", "example")
                .param("channel_id", "C2147483705")
                .param("channel_name", "test")
                .param("user_id", "U2147483697")
                .param("command", "/slacktionary")
                .param("text", "new")
                .param("response_url", "https://hooks.slack.com/commands/1234/5678")
                .param("trigger_id", "13345224609.738474920.8088930838d88f008e0"))
                .andExpect(status().isOk())
                .andExpect(content().json(newGameMessage.toString()));
    }

    @Test
    public void controllerShouldPostJoinGameMessage() throws Exception {
        when(messageBuilderService.createJoinResponseMessage()).thenReturn(builder.createJoinResponseMessage());
        JsonNode joinResponseMessage = builder.createJoinResponseMessage();
        mockMvc.perform(post("/command")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", "gIkuvaNzQIHg97ATvDxqgjtO")
                .param("team_id", "T0001")
                .param("team_domain", "example")
                .param("channel_id", "C2147483705")
                .param("channel_name", "test")
                .param("user_id", "U2147483697")
                .param("command", "/slacktionary")
                .param("text", "join")
                .param("response_url", "https://hooks.slack.com/commands/1234/5678")
                .param("trigger_id", "13345224609.738474920.8088930838d88f008e0"))
                .andExpect(status().isOk())
                .andExpect(content().string(joinResponseMessage.toString()));
    }

    @Test
    public void controllerShouldPostGuessResponseMessage() throws Exception {
        when(messageBuilderService.createGuessResponseMessage()).thenReturn(builder.createGuessResponseMessage());
        JsonNode joinResponseMessage = builder.createGuessResponseMessage();
        mockMvc.perform(post("/command")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", "gIkuvaNzQIHg97ATvDxqgjtO")
                .param("team_id", "T0001")
                .param("team_domain", "example")
                .param("channel_id", "C2147483705")
                .param("channel_name", "test")
                .param("user_id", "U2147483697")
                .param("command", "/slacktionary")
                .param("text", "guess")
                .param("response_url", "https://hooks.slack.com/commands/1234/5678")
                .param("trigger_id", "13345224609.738474920.8088930838d88f008e0"))
                .andExpect(status().isOk())
                .andExpect(content().string(joinResponseMessage.toString()));
    }

    @Test
    public void controllerShouldPostHelpMessageWhenTextFieldIsEmpty() throws Exception {
        when(messageBuilderService.createHelpMessage()).thenReturn(builder.createHelpMessage());
        JsonNode joinResponseMessage = builder.createHelpMessage();
        mockMvc.perform(post("/command")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", "gIkuvaNzQIHg97ATvDxqgjtO")
                .param("team_id", "T0001")
                .param("team_domain", "example")
                .param("channel_id", "C2147483705")
                .param("channel_name", "test")
                .param("user_id", "U2147483697")
                .param("command", "/slacktionary")
                .param("text", "")
                .param("response_url", "https://hooks.slack.com/commands/1234/5678")
                .param("trigger_id", "13345224609.738474920.8088930838d88f008e0"))
                .andExpect(status().isOk())
                .andExpect(content().string(joinResponseMessage.toString()));
    }

    @Test
    public void controllerShouldPostHelpMessageWhenCommandIsHelp() throws Exception {
        when(messageBuilderService.createHelpMessage()).thenReturn(builder.createHelpMessage());
        JsonNode joinResponseMessage = builder.createHelpMessage();
        mockMvc.perform(post("/command")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", "gIkuvaNzQIHg97ATvDxqgjtO")
                .param("team_id", "T0001")
                .param("team_domain", "example")
                .param("channel_id", "C2147483705")
                .param("channel_name", "test")
                .param("user_id", "U2147483697")
                .param("command", "/slacktionary")
                .param("text", "help")
                .param("response_url", "https://hooks.slack.com/commands/1234/5678")
                .param("trigger_id", "13345224609.738474920.8088930838d88f008e0"))
                .andExpect(status().isOk())
                .andExpect(content().string(joinResponseMessage.toString()));
    }
}


