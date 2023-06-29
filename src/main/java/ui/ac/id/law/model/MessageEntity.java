package ui.ac.id.law.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "message_entity")
@NoArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "destination")
    private String destination;

    @Column(name = "subject")
    private String subject;

    @Column(name = "message", columnDefinition="TEXT")
    private String message;

    public MessageEntity(String to, String subject, String message){
        this.destination = to;
        this.subject = subject;
        this.message = message;
    }
}
