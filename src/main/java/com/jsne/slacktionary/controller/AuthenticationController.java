package com.jsne.slacktionary.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsne.slacktionary.model.Team;
import com.jsne.slacktionary.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Controller
public class AuthenticationController {

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${redirect.uri}")
    private String redirectUri;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TeamRepository repository;

    @RequestMapping(value = "/auth/redirect", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getTokenAndSaveTeam(@RequestParam String code) throws IOException {
        String tokenExchangeResponse = exchangeCodeForToken(code);
        String token = getBotAccessToken(tokenExchangeResponse);
        String teamId = getTeamId(tokenExchangeResponse);
        findOrCreateTeam(teamId, token);
        return "Success";
    }

    private String exchangeCodeForToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", code);

        HttpEntity<MultiValueMap<String, String>> postRequest = new HttpEntity<MultiValueMap<String, String>>(formData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://slack.com/api/oauth.access", postRequest, String.class);
        return response.getBody();
    }

    private void findOrCreateTeam(String teamId, String token) {
        List<Team> teams = repository.findByTeamId(teamId);
        if (teams != null && teams.size() > 0)
        {
            Team team = teams.get(0);
            team.setToken(token);
            repository.save(team);
        } else {
            Team team = new Team(teamId, token);
            repository.save(team);
        }
    }

    private String getBotAccessToken(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);
        JsonNode bot = json.get("bot");
        String token = bot.get("bot_access_token").textValue();
        return token;
    }

    private String getTeamId(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);
        String teamId = json.get("team_id").textValue();
        return teamId;
    }
}
