package org.academiadecodigo.thefellowshift.passionatepal.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.academiadecodigo.thefellowshift.passionatepal.dto.MessageDto;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class ChatGptResponseDto {
    private String id;
    private String object;
    private String created;
    private String model;
    private ChatGPTUsageDTO usage;
    @NonNull
    private ChoicesDTO[] choices;

    @Data
    public static class ChatGPTUsageDTO {
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
    @Data
    public static class ChoicesDTO {

        private MessageDto message;
        @JsonProperty("finish_reason")
        private String finishReason;
        private Integer index;
    }

}
