package com.jsne.slacktionary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jsne.slacktionary.util.PhraseLibrary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by brandonmanson on 11/12/17.
 */
@Controller
public class SlashCommandController {

    @RequestMapping(value = "/command", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String processSlashCommand(WebRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        String phrase = PhraseLibrary.getRandomPhrase();
        node.put("response_type", "ephemeral");
        node.put("text", phrase);
        return mapper.writeValueAsString(node);
    }
}
