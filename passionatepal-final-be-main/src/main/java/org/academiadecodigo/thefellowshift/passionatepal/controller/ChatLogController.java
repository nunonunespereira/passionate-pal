package org.academiadecodigo.thefellowshift.passionatepal.controller;

import lombok.extern.slf4j.Slf4j;
import org.academiadecodigo.thefellowshift.passionatepal.Constants;
import org.academiadecodigo.thefellowshift.passionatepal.dto.ChatLogDto;
import org.academiadecodigo.thefellowshift.passionatepal.dto.requests.ChatGtpRequestParameters;
import org.academiadecodigo.thefellowshift.passionatepal.dto.responses.AnswerDataDto;
import org.academiadecodigo.thefellowshift.passionatepal.dto.responses.ChatGptResponseDto;
import org.academiadecodigo.thefellowshift.passionatepal.model.ChatLog;
import org.academiadecodigo.thefellowshift.passionatepal.model.Message;
import org.academiadecodigo.thefellowshift.passionatepal.service.ChatLogService;
import org.academiadecodigo.thefellowshift.passionatepal.service.client.ChatGptServiceClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/conversation")
@Slf4j
public class ChatLogController {

    @Autowired
    private ChatGptServiceClient chatGptServiceClient;

    @Autowired
    private ChatLogService chatLogService;



    @PostMapping("/generate-answer")
    public ResponseEntity<AnswerDataDto> generateAnswer(@RequestBody String sentence, @Nullable @RequestParam Integer id ) {

        ChatGtpRequestParameters chatGtpRequestParameters = ChatGtpRequestParameters.builder()
                .model(Constants.MODEL_NAME)
                .temperature(Constants.TEMPERATURE)
                .maxTokens(Constants.MAX_TOKENS).stream(Constants.STREAM_STRATEGY_DEFAULT).build();
        ChatLog chatLog;
        List<Message> messages;
        String reinforcementPrompt = "";

        if (id == null) {
            chatLog = new ChatLog();
            messages = new ArrayList<>();
            Message messageSystem = new Message(Constants.DEFAULT_SYSTEM_MESSAGE, Message.RoleType.SYSTEM, chatLog);
            messages.add(messageSystem);
            reinforcementPrompt = Constants.DEFAULT_SYSTEM_MESSAGE;
        } else {
            chatLog = chatLogService.getById(id);
            messages = chatLog.getMessages();
        }
        Message messagePrompt = new Message(reinforcementPrompt + sentence, Message.RoleType.USER, chatLog);
        Message messageSave = new Message(sentence, Message.RoleType.USER, chatLog);
        List<Message> messagesToSave = new ArrayList<>(messages);
        messagesToSave.add(messageSave);
        messages.add(messagePrompt);

        ResponseEntity<ChatGptResponseDto> responseEntity = chatGptServiceClient.completion(chatGtpRequestParameters, messages);


        String assistantMessage = responseEntity.getBody().getChoices()[responseEntity.getBody().getChoices().length - 1].getMessage().getContent();
        Message assistantPrompt = new Message(assistantMessage, Message.RoleType.ASSISTANT, chatLog);
        messagesToSave.add(assistantPrompt);

        chatLog.setMessages(messagesToSave);
        chatLog = chatLogService.save(chatLog);

        messages.forEach(message ->
        {

            log.debug("role: {}", message.getRole() );
            log.debug("\ncontent: {}", message.getContent() );
        });
        ChatGptResponseDto.ChoicesDTO[] choicesDTOS = responseEntity.getBody().getChoices();

        AnswerDataDto answerDataDto = new AnswerDataDto(chatLog.getId(), choicesDTOS[choicesDTOS.length - 1].getMessage().getContent());
        return ResponseEntity.ok().headers(new HttpHeaders()).body(answerDataDto);
    }

    @GetMapping("/get-all-conversations")
    public ResponseEntity<List<ChatLogDto>> getAllStories() {
        ModelMapper modelMapper = new ModelMapper();

        log.debug("{}: {}", "chatLogDto", chatLogService
                .findAllByOrderByIdDesc().get(0).getMessages().get(0).getContent());


        List<ChatLogDto> chatLogDtos = chatLogService
                .findAllByOrderByIdDesc()
                .stream()
                .map(v -> modelMapper.map(v, ChatLogDto.class))
                .toList();

        return ResponseEntity.ok().body(chatLogDtos);
    }
}
