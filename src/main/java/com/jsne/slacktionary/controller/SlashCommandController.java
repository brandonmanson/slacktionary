package com.jsne.slacktionary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jsne.slacktionary.service.MessageBuilderService;
import com.jsne.slacktionary.service.SlashCommandProcessorService;
import com.jsne.slacktionary.util.PhraseLibrary;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by brandonmanson on 11/12/17.
 */
@Controller
public class SlashCommandController {

    private Pattern pattern = Pattern.compile("^guess(.*)");

    @Autowired
    MessageBuilderService builderService;

    @Autowired
    SlashCommandProcessorService processorService;

    @RequestMapping(value = "/command", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JsonNode processSlashCommand(WebRequest request) throws JsonProcessingException {
        System.out.println("SLASH COMMAND RECEIVED: \n" + request);
        if (request.getParameter("command").equals("/slacktionary") && !request.getParameter("text").isEmpty())
        {
            JsonNode node;
            if (request.getParameter("text").equals("new"))
            {
                System.out.println("NEW GAME COMMAND RECEIVED");
                node = processorService.processNewGameCommand(request.getParameter("channel_id"), request.getParameter("user_id"), request.getParameter("team_id"));
                return node;
            } else if (request.getParameter("text").equals("join"))
            {
                System.out.println("JOIN COMMAND RECEIVED");
                node = processorService.processJoinCommand(request.getParameter("channel_id"), request.getParameter("user_id"));
                return node;
            } else if (request.getParameter("text").matches("^guess\\W+(.*)"))
            {
                System.out.println("GUESS COMMAND RECEIVED");
                Matcher matcher = pattern.matcher(request.getParameter("text"));
                String phrase = null;
                while (matcher.find())
                {
                    phrase = matcher.group(1);
                    System.out.println("GUESS: " + phrase);
                }
                node = processorService.processGuessCommand(request.getParameter("channel_id"), request.getParameter("user_id"), phrase.trim());
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
