package com.jsne.slacktionary.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Created by brandonmanson on 11/11/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SlashCommandController.class)
public class SlashCommandControllerTest {

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper mapper;


    @Test
    public void controllerShouldReturnPhraseFromDictionary() throws Exception {
        mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("response_type", "ephemeral");
        node.put("text", "A horse of a different color");

        mockMvc.perform(post("/command")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", "gIkuvaNzQIHg97ATvDxqgjtO")
                .param("team_id", "T0001")
                .param("team_domain", "example")
                .param("channel_id", "C2147483705")
                .param("channel_name", "test")
                .param("user_id", "U2147483697")
                .param("command", "/new")
                .param("text", "")
                .param("response_url", "https://hooks.slack.com/commands/1234/5678")
                .param("trigger_id", "13345224609.738474920.8088930838d88f008e0"))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(node)));
    }
}
