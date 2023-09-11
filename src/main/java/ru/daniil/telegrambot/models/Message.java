package ru.daniil.telegrambot.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "messages")
public class Message {
    @Id
    @GeneratedValue
    private Integer id;
    private String question;
    private String answer;
}
