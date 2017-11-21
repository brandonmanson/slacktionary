package com.jsne.slacktionary.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.notIn;


/**
 * Created by brandonmanson on 11/18/17.
 */
@RunWith(SpringRunner.class)
public class ChannelTest {

    private Channel channel;

    private String PHRASE = "A horse of a different color";
    private String USER_ID = "U2147483697";

    @Before
    public  void setup() {
        channel = new Channel("C2147483705", "gIkuvaNzQIHg97ATvDxqgjtO");
        channel.setToActive(PHRASE, USER_ID);
    }

    @Test
    public void channelShouldBeActive() {
        assertThat(channel.isHasActiveGame()).isEqualTo(true);

    }

    @Test
    public void channelShouldHaveActivePhrase() {
        assertThat(channel.getActivePhrase()).isEqualTo(PHRASE);
    }

    @Test
    public void channelShouldHaveActiveUserId() {
        assertThat(channel.getActiveUserId()).isEqualTo(USER_ID);
    }

    @Test
    public void channelShouldBeInactive() {
        channel.setToInactive();
        assertThat(channel.isHasActiveGame()).isEqualTo(false);
    }

    @Test
    public void channelShouldNotHaveActivePhrase() {
        channel.setToInactive();
        assertThat(channel.getActivePhrase()).isEqualTo(null);
    }

    @Test
    public void channelShouldNotHaveActiveUserId() {
        channel.setToInactive();
        assertThat(channel.getActiveUserId()).isEqualTo(null);
    }

    @Test
    public void channelShouldHaveEmptyPlayerList() {
        channel.getPlayers().add("U57163412");
        channel.getPlayers().add("U98765432");
        channel.setToInactive();
        assertThat(channel.getPlayers()).hasSize(0);
    }


}
