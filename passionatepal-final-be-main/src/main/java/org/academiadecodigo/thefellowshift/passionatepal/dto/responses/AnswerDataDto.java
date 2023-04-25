package org.academiadecodigo.thefellowshift.passionatepal.dto.responses;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.academiadecodigo.thefellowshift.passionatepal.dto.MessageDto;

@Data
@AllArgsConstructor
public class AnswerDataDto {
    private Integer id;
    private String content;
}
