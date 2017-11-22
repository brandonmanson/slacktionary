package com.jsne.slacktionary.service;

import com.jsne.slacktionary.model.Channel;
import com.jsne.slacktionary.repository.ChannelRepository;
import com.jsne.slacktionary.util.PhraseLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by brandonmanson on 11/20/17.
 */
@Service
public class ChannelStateManagerService {

    private PhraseLibrary library = new PhraseLibrary();

    @Autowired
    ChannelRepository repository;


    public Channel setChannelToActive(String channelId, String userId, String token) {
        System.out.println("-----REPOSITORY-----" + repository);
        List<Channel> channels = repository.findByChannelId(channelId);
        if (channels.size() > 0)
        {
            Channel channel = channels.get(0);
            if (!channel.isHasActiveGame())
            {
                channel.setToActive(library.getRandomPhrase(), userId);
                repository.save(channel);
                return channel;
            } else {
                return channel;
            }
        }
        else
        {
            System.out.println("IN ELSE BLOCK");
            Channel newChannel = new Channel(channelId, token);
            newChannel.setToActive(library.getRandomPhrase(), userId);
            repository.save(newChannel);
            return newChannel;
        }
    }

    public Channel setChannelToInactive(String channelId) {
        List<Channel> channels = repository.findByChannelId(channelId);
        if (channels.size() > 0)
        {
            Channel channel = channels.get(0);
            if (!channel.isHasActiveGame())
            {
                return channel;
            } else {
                channel.setToInactive();
                repository.save(channel);
                return channel;
            }
        }
        else
        {
            return null;
        }
    }

    public Channel addPlayerToActiveChannel(String channelID, String playerId) {
        List<Channel> channels = repository.findByChannelId(channelID);
        if (channels.size() > 0)
        {
            Channel channel = channels.get(0);
            channel.getPlayers().add(playerId);
            repository.save(channel);
            return channel;
        }
        else
        {
            return null;
        }
    }
}
