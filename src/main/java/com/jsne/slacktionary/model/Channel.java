package com.jsne.slacktionary.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandonmanson on 11/18/17.
 */
@Entity
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String channelId;
    private String activePhrase;
    private String activeUserId;
    private String token;

    @ElementCollection
    private List<String> players;
    private boolean hasActiveGame;

    public Channel() {
    }

    public Channel(String channelId, String token) {
        this.channelId = channelId;
        this.token = token;
        this.activePhrase = null;
        this.activeUserId = null;
        this.players = new ArrayList<String>();
        this.hasActiveGame = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getActivePhrase() {
        return activePhrase;
    }

    public void setActivePhrase(String activePhrase) {
        this.activePhrase = activePhrase;
    }

    public String getActiveUserId() {
        return activeUserId;
    }

    public void setActiveUserId(String activeUserId) {
        this.activeUserId = activeUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isHasActiveGame() {
        return hasActiveGame;
    }

    public void setHasActiveGame(boolean hasActiveGame) {
        this.hasActiveGame = hasActiveGame;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void setToActive(String phrase, String userId) {
        this.hasActiveGame = true;
        this.activePhrase = phrase;
        this.activeUserId = userId;
    }

    public void setToInactive() {
        this.hasActiveGame = false;
        this.setActivePhrase(null);
        this.setActiveUserId(null);
        this.setPlayers(new ArrayList<>());
    }

}
