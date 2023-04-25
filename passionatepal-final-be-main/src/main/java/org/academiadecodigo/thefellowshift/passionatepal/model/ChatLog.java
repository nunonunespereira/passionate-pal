package org.academiadecodigo.thefellowshift.passionatepal.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "conversation_session")
@NoArgsConstructor
public class ChatLog extends Model {

    @OneToMany(mappedBy = "chatLog",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Message> messages;

    @Override
    public String toString() {
        return "chatLog{" +
                "messages=" + messages +
                '}';
    }
}
