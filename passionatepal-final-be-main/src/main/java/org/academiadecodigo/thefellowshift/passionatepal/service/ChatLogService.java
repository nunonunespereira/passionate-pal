package org.academiadecodigo.thefellowshift.passionatepal.service;

import org.academiadecodigo.thefellowshift.passionatepal.model.ChatLog;

import java.util.List;

public interface ChatLogService {

    ChatLog save(ChatLog chatLog);
    ChatLog getById(Integer id);

    List<ChatLog> findAll();
    List<ChatLog> findAllByOrderByIdDesc();
}
