package com.jsne.slacktionary.repository;

import com.jsne.slacktionary.model.Channel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by brandonmanson on 11/20/17.
 */
@Repository
public interface ChannelRepository extends CrudRepository<Channel, Long> {

    public List<Channel> findByChannelId(String channelId);
}
