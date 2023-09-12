package ru.daniil.telegrambot.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity(name = "users")
public class User {
    @Id
    private Long chatId;
    private String firstName;
    private Date lastMessageAt;
}