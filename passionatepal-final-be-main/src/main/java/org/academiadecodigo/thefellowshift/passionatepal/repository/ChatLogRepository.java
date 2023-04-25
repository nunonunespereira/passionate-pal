package org.academiadecodigo.thefellowshift.passionatepal.repository;

import org.academiadecodigo.thefellowshift.passionatepal.model.ChatLog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Integer> {

    @Query("select r from ChatLog r")
    List<ChatLog> findAllByOrderByIdDesc();
}
