package org.academiadecodigo.thefellowshift.passionatepal.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.academiadecodigo.thefellowshift.passionatepal.dto.MessageDto;
import org.academiadecodigo.thefellowshift.passionatepal.model.Message;

import java.util.List;

@Data
@Builder
public class ChatGptRequestDto {

    @JsonProperty("messages")
    private List<MessageDto> messageDtoList;

    private String model;

    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Double temperature;
    private Boolean stream;

}
