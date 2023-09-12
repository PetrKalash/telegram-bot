package ru.daniil.telegrambot.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "messages")
public class Message {
    @Id
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private User user;
    private String question;
    private String answer;
}
