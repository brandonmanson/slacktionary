package com.jsne.slacktionary.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jsne.slacktionary.model.Team;
import com.jsne.slacktionary.repository.TeamRepository;
import org.hibernate.validator.constraints.URL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    private final String URL = "https://slack.com/api/oauth.access";
    private final String TEAM_ID = "T0001";
    private final String CODE = "ABC123456";
    private final String URL_UNDER_TEST = "/auth/redirect";
    private ResponseEntity response;
    private HttpEntity httpEntity;
    private Team team;
    private List<Team> teams;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${redirect.uri}")
    private String redirectUri;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    TeamRepository repository;

    private JsonNode setupJsonResponse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree("{\"access_token\":\"xoxp-XXXXXXXX-XXXXXXXX-XXXXX\",\"scope\":\"incoming-webhook,commands,bot\",\"team_name\":\"TEST TEAM\",\"team_id\":\"T0001\",\"incoming_webhook\":{\"url\":\"https://hooks.slack.com/TXXXXX/BXXXXX/XXXXXXXXXX\",\"channel\":\"#test_channel\",\"configuration_url\":\"https://teamname.slack.com/services/BXXXXX\"},\"bot\":{\"bot_user_id\":\"UTTTTTTTTTTR\",\"bot_access_token\":\"xoxb-XXXXXXXXXXXX-TTTTTTTTTTTTTT\"}}");
        return root;
    }

    private void setupHttpClient() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", CODE);
        formData.add("redirect_uri", String.format(redirectUri + "/auth/redirect"));

        httpEntity = new HttpEntity<MultiValueMap<String, String>>(formData, headers);
        response = new ResponseEntity<JsonNode>(setupJsonResponse(), HttpStatus.OK);
    }

    @Before
    public void setup() throws IOException {
        team = new Team(TEAM_ID, CODE);
        teams = new ArrayList<>();
        teams.add(team);
        setupHttpClient();
        Mockito.when(restTemplate.postForEntity(URL, httpEntity, JsonNode.class)).thenReturn(response);
    }

    @Test
    public void redirectUriShouldGetCodeAndRetrieveApiTokenForExistingTeam() throws Exception {
        Mockito.when(repository.findByTeamId(TEAM_ID)).thenReturn(teams);
        mockMvc.perform(get(URL_UNDER_TEST)
                .param("code", CODE))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    public void redirectUriShouldGetCodeAndRetrieveApiTokenAndCreateNewTeam() throws Exception {
        Mockito.when(repository.findByTeamId(TEAM_ID)).thenReturn(null);
        mockMvc.perform(get(URL_UNDER_TEST)
                .param("code", CODE))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }




}
