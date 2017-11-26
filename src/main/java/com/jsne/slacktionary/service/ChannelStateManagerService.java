package com.jsne.slacktionary.service;

import com.jsne.slacktionary.model.Channel;
import com.jsne.slacktionary.model.Team;
import com.jsne.slacktionary.repository.ChannelRepository;
import com.jsne.slacktionary.repository.TeamRepository;
import com.jsne.slacktionary.util.PhraseLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by brandonmanson on 11/20/17.
 */
@Service
public class ChannelStateManagerService {

    private PhraseLibrary library = new PhraseLibrary();

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    TeamRepository teamRepository;


    public Channel setChannelToActive(String channelId, String userId, String teamId) {
        List<Team> teams = teamRepository.findByTeamId(teamId);
        if (teams != null && teams.size() > 0)
        {
            Team team = teams.get(0);
            if (team.getChannels().size() > 0)
            {
                Channel channel = getChannelWithChannelId(team.getChannels(), channelId);
                if (!channel.isHasActiveGame())
                {
                    channel.setToActive(library.getRandomPhrase(), userId);
                    channelRepository.save(channel);
                    return channel;
                } else {
                    return channel;
                }
            }
            else
            {
                Channel newChannel = new Channel(channelId, team.getToken());
                newChannel.setTeam(team);
                newChannel.setToActive(library.getRandomPhrase(), userId);
                channelRepository.save(newChannel);
                return newChannel;
            }
        } else {
            return null;
        }
    }

    public Channel setChannelToInactive(String channelId) {
        List<Channel> channels = channelRepository.findByChannelId(channelId);
        if (channels.size() > 0)
        {
            Channel channel = channels.get(0);
            if (!channel.isHasActiveGame())
            {
                return channel;
            } else {
                channel.setToInactive();
                channelRepository.save(channel);
                return channel;
            }
        }
        else
        {
            return null;
        }
    }

    public Channel addPlayerToActiveChannel(String channelID, String playerId) {
        List<Channel> channels = channelRepository.findByChannelId(channelID);
        if (channels.size() > 0)
        {
            Channel channel = channels.get(0);
            channel.getPlayers().add(playerId);
            channelRepository.save(channel);
            return channel;
        }
        else
        {
            return null;
        }
    }

    private Channel getChannelWithChannelId(List<Channel> channels, String channelId) {
        Optional<Channel> matchingChannel = channels.stream()
                .filter(channel -> channel.getChannelId().equals(channelId))
                .findFirst();
        Channel channel = matchingChannel.orElse(null);
        return channel;
    }
}
