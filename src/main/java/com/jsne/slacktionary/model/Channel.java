package com.jsne.slacktionary.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brandonmanson on 11/18/17.
 */
@Entity
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;

    private String channelId;
    private String activePhrase;
    private String activeUserId;
    private String token;

    @ElementCollection
    private List<String> players;
    private HashMap<String, String> guesses;
    private boolean hasActiveGame;

    public Channel() {
    }

    public Channel(String channelId, String token) {
        this.channelId = channelId;
        this.token = token;
        this.activePhrase = null;
        this.activeUserId = null;
        this.players = new ArrayList<String>();
        this.guesses = new HashMap<>();
        this.hasActiveGame = false;
    }

    public String getChannelId() {
        return channelId;
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

    public boolean isHasActiveGame() {
        return hasActiveGame;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public HashMap<String, String> getGuesses() {
        return guesses;
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
