package com.jsne.slacktionary.service;

import com.jsne.slacktionary.model.Channel;
import com.jsne.slacktionary.repository.ChannelRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by brandonmanson on 11/20/17.
 */
@RunWith(SpringRunner.class)
public class ChannelStateManagerServiceTest {

    // Make channel active
    // Make channel inactive
    // Add a user to the players list
    // Add a guess to the guess list

    private final String PHRASE = "A horse of a different color";
    private final String USER_ID = "U1234567";
    private final String ACTIVE_CHANNEL_ID = "C2147483705";
    private final String INACTIVE_CHANNEL_ID = "C2147483705";
    private final String TOKEN = "gIkuvaNzQIHg97ATvDxqgjtO";
    private Channel activeChannel;
    private Channel inactiveChannel;
    private List<Channel> activeChannelsList;
    private List<Channel> inactiveChannelsList;
    private List<Channel> emptyChannelsList;

    @SpyBean
    ChannelStateManagerService service;

    @MockBean
    ChannelRepository repository;

    @Before
    public void setup() {
        activeChannel = new Channel(ACTIVE_CHANNEL_ID, TOKEN);
        activeChannel.setToActive(PHRASE, USER_ID);
        inactiveChannel = new Channel(INACTIVE_CHANNEL_ID, TOKEN);
        emptyChannelsList = new ArrayList<>();
        activeChannelsList = new ArrayList<>();
        inactiveChannelsList = new ArrayList<>();
        activeChannelsList.add(activeChannel);
        inactiveChannelsList.add(inactiveChannel);
    }

    @Test
    public void channelShouldHaveActiveGame() {
        Mockito.when(repository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(activeChannelsList);
        assertThat(service.setChannelToActive(ACTIVE_CHANNEL_ID, USER_ID, TOKEN).isHasActiveGame()).isEqualTo(true);
    }

    @Test
    public void channelShouldHavePhrase() {
        Mockito.when(repository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(activeChannelsList);
        assertThat(service.setChannelToActive(ACTIVE_CHANNEL_ID, USER_ID, TOKEN).getActivePhrase()).isEqualTo(PHRASE);
    }

    @Test
    public void channelShouldHaveActiveUserId() {
        Mockito.when(repository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(activeChannelsList);
        assertThat(service.setChannelToActive(ACTIVE_CHANNEL_ID, USER_ID, TOKEN).getActiveUserId()).isEqualTo(USER_ID);
    }

    @Test
    public void serviceShouldReturnNewActiveChannelWhenQueryReturnsNull() {
        Mockito.when(repository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(emptyChannelsList);
        assertThat(service.setChannelToActive(ACTIVE_CHANNEL_ID, USER_ID, TOKEN).isHasActiveGame()).isEqualTo(true);
    }

    @Test
    public void channelShouldBecomeInactive() {
        Mockito.when(repository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(activeChannelsList);
        assertThat(service.setChannelToInactive(ACTIVE_CHANNEL_ID).isHasActiveGame()).isEqualTo(false);
    }

    @Test
    public void inactiveChannelShouldBeReturned() {
        Mockito.when(repository.findByChannelId(INACTIVE_CHANNEL_ID)).thenReturn(inactiveChannelsList);
        assertThat(service.setChannelToInactive(INACTIVE_CHANNEL_ID)).isEqualTo(inactiveChannel);
    }

    @Test
    public void setToInactiveShouldReturnNullIfRepositoryReturnsNull() {
        Mockito.when(repository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(emptyChannelsList);
        assertThat(service.setChannelToInactive(ACTIVE_CHANNEL_ID)).isEqualTo(null);
    }

    @Test
    public void playerListShouldBeSizeOf1() {
        Mockito.when(repository.findByChannelId(ACTIVE_CHANNEL_ID)).thenReturn(activeChannelsList);
        assertThat(service.addPlayerToActiveChannel(ACTIVE_CHANNEL_ID, "U123456789").getPlayers().size()).isEqualTo(1);
    }



}
