package com.jsne.slacktionary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jsne.slacktionary.service.MessageBuilderService;
import com.jsne.slacktionary.util.PhraseLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by brandonmanson on 11/12/17.
 */
@Controller
public class SlashCommandController {

    @Autowired
    MessageBuilderService service;

    @RequestMapping(value = "/command", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JsonNode processSlashCommand(WebRequest request) throws JsonProcessingException {
        JsonNode node;
        if (request.getParameter("command").equals("/slacktionary") && request.getParameter("text") != null)
        {
            if (request.getParameter("text").equals("new"))
            {
                node = service.createNewGameMessage();
                return node;
            } else if (request.getParameter("text").equals("join"))
            {
                node = service.createJoinResponseMessage();
                return node;
            } else if (request.getParameter("text").equals("guess"))
            {
                node = service.createGuessResponseMessage();
                return node;
            }
        }
        node = service.createWinnerNotificationMessage();
        return node;
    }
}
