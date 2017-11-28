package com.jsne.slacktionary.service;

import com.jsne.slacktionary.model.Channel;
import com.jsne.slacktionary.model.Team;
import com.jsne.slacktionary.repository.ChannelRepository;
import com.jsne.slacktionary.repository.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;

/**
 * Created by brandonmanson on 11/20/17.
 */
@RunWith(SpringRunner.class)
public class ChannelStateManagerServiceTest {

    private final String PHRASE = "A horse of a different color";
    private final String USER_ID = "U1234567";
    private final String TEAM_ID = "T0001";
    private final String EMPTY_TEAM_ID = "T0002";
    private final String ACTIVE_CHANNEL_ID = "C2147483705";
    private final String INACTIVE_CHANNEL_ID = "C2147483706";
    private final String TOKEN = "gIkuvaNzQIHg97ATvDxqgjtO";
    private Team team;
    private Team emptyTeam;
    private Channel activeChannel;
    private Channel inactiveChannel;
    List<Team> teams;
    List<Team> emptyTeamsList;
    private List<Channel> activeChannelsList;
    private List<Channel> inactiveChannelsList;
    private List<Channel> emptyChannelsList;

    @SpyBean
    ChannelStateManagerService service;

    @MockBean
    ChannelRepository channelRepository;

    @MockBean
    TeamRepository teamRepository;

    @Before
    public void setup() {
        team = new Team(TEAM_ID, TOKEN);
        teams = new ArrayList<>();
        teams.add(team);
        emptyTeam = new Team(EMPTY_TEAM_ID, TOKEN);
        emptyTeamsList = new ArrayList<>();
        emptyTeamsList.add(emptyTeam);
        activeChannel = new Channel(ACTIVE_CHANNEL_ID, TOKEN);
        activeChannel.setTeam(team);
        activeChannel.setToActive(PHRASE, USER_ID);
        team.getChannels().add(activeChannel);
        inactiveChannel = new Channel(INACTIVE_CHANNEL_ID, TOKEN);
        inactiveChannel.setTeam(team);
        team.getChannels().add(inactiveChannel);
        emptyChannelsList = new ArrayList<>();
        activeChannelsList = new ArrayList<>();
        inactiveChannelsList = new ArrayList<>();
        activeChannelsList.add(activeChannel);
        inactiveChannelsList.add(inactiveChannel);
    }

    @Test
    public void returnShouldBeNullWhenNoTeamCanBeFound() {
        Mockito.when(teamRepository.findByTeamId(TEAM_ID)).thenReturn(null);
        assertThat(service.setChannelToActive(ACTIVE_CHANNEL_ID, USER_ID, TEAM_ID)).isEqualTo(null);
    }

    @Test
    public void channelShouldHaveActiveGame() {
        Mockito.when(teamRepository.findByTeamId(TEAM_ID)).thenReturn(teams);
        assertThat(service.setChannelToActive(ACTIVE_CHANNEL_ID, USER_ID, TEAM_ID).isHasActiveGame()).isEqualTo(true);
    }

    @Test
    public void activeChannelShouldBeReturnedWhenInactiveChannelFound() {
        Mockito.when(teamRepository.findByTeamId(TEAM_ID)).thenReturn(teams);
        assertThat(service.setChannelToActive(INACTIVE_CHANNEL_ID, USER_ID, TEAM_ID).isHasActiveGame()).isEqualTo(true);
    }

    @Test
    public void channelShouldHavePhrase() {
        Mockito.when(teamRepository.findByTeamId(TEAM_ID)).thenReturn(teams);
        assertThat(service.setChannelToActive(ACTIVE_CHANNEL_ID, USER_ID, TEAM_ID).getActivePhrase()).isEqualTo(PHRASE);
    }

    @Test
    public void channelShouldHaveActiveUserId() {
        Mockito.when(teamRepository.findByTeamId(TEAM_ID)).thenReturn(teams);
        assertThat(service.setChannelToActive(ACTIVE_CHANNEL_ID, USER_ID, TEAM_ID).getActiveUserId()).isEqualTo(USER_ID);
    }

    @Test
    public void serviceShouldReturnNewActiveChannelWhenTeamHasNoChannels() {
        Mockito.when(teamRepository.findByTeamId(TEAM_ID)).thenReturn(emptyTeamsList);
        assertThat(service.setChannelToActive(ACTIVE_CHANNEL_ID, USER_ID, TEAM_ID).isHasActiveGame()).isEqualTo(true);
    }

    @Test
    public void channelShouldBecomeInactive() {
        Mockito.when(channelRepository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(activeChannelsList);
        assertThat(service.setChannelToInactive(ACTIVE_CHANNEL_ID).isHasActiveGame()).isEqualTo(false);
    }

    @Test
    public void inactiveChannelShouldBeReturned() {
        Mockito.when(channelRepository.findByChannelId(INACTIVE_CHANNEL_ID)).thenReturn(inactiveChannelsList);
        assertThat(service.setChannelToInactive(INACTIVE_CHANNEL_ID)).isEqualTo(inactiveChannel);
    }

    @Test
    public void setToInactiveShouldReturnNullIfRepositoryReturnsNull() {
        Mockito.when(channelRepository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(emptyChannelsList);
        assertThat(service.setChannelToInactive(ACTIVE_CHANNEL_ID)).isEqualTo(null);
    }

    @Test
    public void playerListShouldBeSizeOf1() {
        Mockito.when(channelRepository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(activeChannelsList);
        assertThat(service.addPlayerToActiveChannel(ACTIVE_CHANNEL_ID, "U123456789").getPlayers().size()).isEqualTo(1);
    }

    @Test
    public void returnShouldBeNullWhenPlayerIsAddedToNonexistentChannel() {
        Mockito.when(channelRepository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(emptyChannelsList);
        assertThat(service.addPlayerToActiveChannel(ACTIVE_CHANNEL_ID, USER_ID)).isEqualTo(null);
    }

    @Test
    public void userShouldNotBeAddedToPlayerListIfTheyAreActiveUser() {
        Mockito.when(channelRepository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(activeChannelsList);
        assertThat(service.addPlayerToActiveChannel(ACTIVE_CHANNEL_ID, USER_ID).getPlayers().size()).isEqualTo(0);
    }

    @Test
    public void nullShouldBeReturnedIfActiveUserTriesToJoin() {
        Mockito.when(channelRepository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(activeChannelsList);
        assertThat(service.addPlayerToActiveChannel(ACTIVE_CHANNEL_ID, USER_ID)).isEqualTo(null);
    }


}
