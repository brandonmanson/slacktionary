package com.jsne.slacktionary.repository;

import com.jsne.slacktionary.model.Channel;
import com.jsne.slacktionary.model.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long> {

    public List<Team> findByTeamId(String teamId);
}
