package com.jsne.slacktionary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jsne.slacktionary.service.MessageBuilderService;
import com.jsne.slacktionary.service.SlashCommandProcessorService;
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
    MessageBuilderService builderService;

    @Autowired
    SlashCommandProcessorService processorService;

    @RequestMapping(value = "/command", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JsonNode processSlashCommand(WebRequest request) throws JsonProcessingException {
        System.out.println("TEXT PARAMETER: " + request.getParameter("text"));
        if (request.getParameter("command").equals("/slacktionary") && !request.getParameter("text").isEmpty())
        {
            JsonNode node;
            if (request.getParameter("text").equals("new"))
            {
                System.out.println("Text is new. Running processor service.");
                node = processorService.processNewGameCommand(request.getParameter("channel_id"), request.getParameter("user_id"), request.getParameter("token"));
                return node;
            } else if (request.getParameter("text").equals("join"))
            {
                node = builderService.createJoinResponseMessage();
                return node;
            } else if (request.getParameter("text").equals("guess"))
            {
                node = builderService.createGuessResponseMessage();
                return node;
            }
            else if (request.getParameter("text").equals("help"))
            {
                node = builderService.createHelpMessage();
                return node;
            }
        }
        return builderService.createHelpMessage();
    }
}
