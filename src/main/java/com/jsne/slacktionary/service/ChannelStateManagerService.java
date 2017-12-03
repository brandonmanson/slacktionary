package com.jsne.slacktionary.service;

import com.jsne.slacktionary.model.Channel;
import com.jsne.slacktionary.model.Team;
import com.jsne.slacktionary.repository.ChannelRepository;
import com.jsne.slacktionary.repository.TeamRepository;
import com.jsne.slacktionary.util.PhraseLibrary;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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
        System.out.println("RETRIEVING TEAM " + teamId);
        List<Team> teams = teamRepository.findByTeamId(teamId);
        if (teams != null && teams.size() > 0)
        {
            Team team = teams.get(0);
            if (team.getChannels().size() > 0)
            {
                System.out.println("RETREIVING CHANNEL " + channelId);
                Channel channel = getChannelWithChannelId(team.getChannels(), channelId);
                System.out.println("CHANNEL RETRIEVED. CURRENT STATUS: "
                        + "\nACTIVE USER: " + channel.getActiveUserId()
                        + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                        + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                        + "\nPLAYERS: " + channel.getPlayers()
                        + "\nGUESSES: " + channel.getGuesses());
                if (!channel.isHasActiveGame())
                {
                    System.out.println("SETTING CHANNEL AS ACTIVE");
                    channel.setToActive(library.getRandomPhrase(), userId);
                    channelRepository.save(channel);
                    System.out.println("CHANNEL ACTIVATED. CURRENT STATUS: "
                            + "\nACTIVE USER: " + channel.getActiveUserId()
                            + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                            + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                            + "\nPLAYERS: " + channel.getPlayers()
                            + "\nGUESSES: " + channel.getGuesses());
                    return channel;
                } else {
                    System.out.println("CHANNEL IS ALREADY ACTIVE. CURRENT STATUS: "
                            + "\nACTIVE USER: " + channel.getActiveUserId()
                            + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                            + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                            + "\nPLAYERS: " + channel.getPlayers()
                            + "\nGUESSES: " + channel.getGuesses());
                    return channel;
                }
            }
            else
            {
                System.out.println("NO CHANNEL WITH CHANNEL ID " + channelId + ". CREATING NEW CHANNEL.");
                Channel newChannel = new Channel(channelId, team.getToken());
                newChannel.setTeam(team);
                newChannel.setToActive(library.getRandomPhrase(), userId);
                channelRepository.save(newChannel);
                System.out.println("CHANNEL CREATED. CURRENT STATUS: "
                        + "\nCHANNEL ID: " + newChannel.getChannelId()
                        + "\nACTIVE USER: " + newChannel.getActiveUserId()
                        + "\nACTIVE PHRASE: " + newChannel.getActivePhrase()
                        + "\nCURRENTLY ACTIVE: " + newChannel.isHasActiveGame()
                        + "\nPLAYERS: " + newChannel.getPlayers()
                        + "\nGUESSES: " + newChannel.getGuesses());
                return newChannel;
            }
        } else {
            System.out.println("NO TEAM FOUND");
            return null;
        }
    }

    public Channel setChannelToInactive(String channelId) {
        System.out.println("RETRIEVING CHANNEL " + channelId + " TO SET AS INACTIVE");
        List<Channel> channels = channelRepository.findByChannelId(channelId);
        if (channels.size() > 0)
        {
            Channel channel = channels.get(0);
            System.out.println("CHANNEL RETRIEVED. CURRENT STATUS: "
                    + "\nACTIVE USER: " + channel.getActiveUserId()
                    + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                    + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                    + "\nPLAYERS: " + channel.getPlayers()
                    + "\nGUESSES: " + channel.getGuesses());
            if (!channel.isHasActiveGame())
            {
                System.out.println("CHANNEL IS ALREADY INACTIVE. RETURNING CHANNEL");
                return channel;
            } else {
                System.out.println("CHANNEL IS ACTIVE. SETTING TO INACTIVE");
                channel.setToInactive();
                channelRepository.save(channel);
                System.out.println("CHANNEL UPDATED. CURRENT STATUS: "
                        + "\nACTIVE USER: " + channel.getActiveUserId()
                        + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                        + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                        + "\nPLAYERS: " + channel.getPlayers()
                        + "\nGUESSES: " + channel.getGuesses());
                return channel;
            }
        }
        else
        {
            System.out.println("NO CHANNEL FOUND");
            return null;
        }
    }

    public Channel addPlayerToActiveChannel(String channelID, String playerId) {
        System.out.println("RETRIEVING CHANNEL " + channelID+ " TO ADD PLAYER " + playerId + " TO PLAYER LIST");
        List<Channel> channels = channelRepository.findByChannelId(channelID);
        if (channels.size() > 0)
        {
            Channel channel = channels.get(0);
            System.out.println("CHANNEL RETRIEVED. CURRENT STATUS: "
                    + "\nACTIVE USER: " + channel.getActiveUserId()
                    + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                    + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                    + "\nPLAYERS: " + channel.getPlayers()
                    + "\nGUESSES: " + channel.getGuesses());
            if (!channel.getActiveUserId().equals(playerId))
            {
                System.out.println("ADDING PLAYER TO PLAYER LIST");
                channel.getPlayers().add(playerId);
                channelRepository.save(channel);
                System.out.println("CHANNEL UPDATED WITH NEW PLATER. CURRENT STATUS: "
                        + "\nACTIVE USER: " + channel.getActiveUserId()
                        + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                        + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                        + "\nPLAYERS: " + channel.getPlayers()
                        + "\nGUESSES: " + channel.getGuesses());
                return channel;
            }
            System.out.println("PLAYER IS ALREADY THE ACTIVE USER. CANNOT BE ADDED TO PLAYER LIST");
            return null;
        }
        else
        {
            System.out.println("COULD NOT FIND CHANNEL");
            return null;
        }
    }

    public Channel addGuessToGuessList(String channelId, String userId, String phrase) {
        System.out.println("RETRIEVING CHANNEL " + channelId + " TO ADD GUESS " + phrase + " FOR USER " + userId);
        List<Channel> channels = channelRepository.findByChannelId(channelId);
        if (channels.size() > 0)
        {
            Channel channel = channels.get(0);
            System.out.println("CHANNEL RETRIEVED. CURRENT STATUS: "
                    + "\nACTIVE USER: " + channel.getActiveUserId()
                    + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                    + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                    + "\nPLAYERS: " + channel.getPlayers()
                    + "\nGUESSES: " + channel.getGuesses());
            if (channel.isHasActiveGame())
            {
                if (!channel.getGuesses().containsKey(userId) && channel.getPlayers().contains(userId))
                {
                    System.out.println("ADDING GUESS TO GUESS LIST");
                    channel.getGuesses().put(userId, phrase);
                    channelRepository.save(channel);
                    System.out.println("CHANNEL UPDATED WITH NEW GUESS. CURRENT STATUS: "
                            + "\nACTIVE USER: " + channel.getActiveUserId()
                            + "\nACTIVE PHRASE: " + channel.getActivePhrase()
                            + "\nCURRENTLY ACTIVE: " + channel.isHasActiveGame()
                            + "\nPLAYERS: " + channel.getPlayers()
                            + "\nGUESSES: " + channel.getGuesses());
                    return channel;
                }
                System.out.println("USER HAS ALREADY GUESSED. RETURNING CHANNEL");
                return channel;
            }
        }
        System.out.println("COULD NOT FIND CHANNEL");
        return null;
    }

    private Channel getChannelWithChannelId(List<Channel> channels, String channelId) {
        Optional<Channel> matchingChannel = channels.stream()
                .filter(channel -> channel.getChannelId().equals(channelId))
                .findFirst();
        Channel channel = matchingChannel.orElse(null);
        return channel;
    }
}
