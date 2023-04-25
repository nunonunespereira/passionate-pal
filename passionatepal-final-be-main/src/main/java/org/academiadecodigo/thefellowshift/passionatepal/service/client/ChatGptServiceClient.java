package org.academiadecodigo.thefellowshift.passionatepal.service.client;

import org.academiadecodigo.thefellowshift.passionatepal.dto.MessageDto;
import org.academiadecodigo.thefellowshift.passionatepal.dto.requests.ChatGtpRequestParameters;
import org.academiadecodigo.thefellowshift.passionatepal.dto.responses.ChatGptResponseDto;
import org.academiadecodigo.thefellowshift.passionatepal.model.Message;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.List;

public interface ChatGptServiceClient {

    ResponseEntity<ChatGptResponseDto> completion(ChatGtpRequestParameters parameters, List<Message> messages);
    ResponseEntity<Resource> completionStream();


}
