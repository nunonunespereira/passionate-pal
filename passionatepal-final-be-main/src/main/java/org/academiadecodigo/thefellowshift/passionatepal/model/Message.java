package org.academiadecodigo.thefellowshift.passionatepal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message extends Model{

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private ChatLog chatLog;

    public enum RoleType {
        SYSTEM,
        USER,
        ASSISTANT;



        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
