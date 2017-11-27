package com.jsne.slacktionary.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String teamId;
    private String token;

    @OneToMany(mappedBy = "team")
    private List<Channel> channels;

    public Team(String teamId, String token) {
        this.teamId = teamId;
        this.token = token;
        this.channels = new ArrayList<>();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Channel> getChannels() {
        return channels;
    }
}
