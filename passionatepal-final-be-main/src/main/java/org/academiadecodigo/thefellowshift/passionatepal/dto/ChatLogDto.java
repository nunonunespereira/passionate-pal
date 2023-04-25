package org.academiadecodigo.thefellowshift.passionatepal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatLogDto {

    private Integer id;
    private List<MessageDto> messages;
}
