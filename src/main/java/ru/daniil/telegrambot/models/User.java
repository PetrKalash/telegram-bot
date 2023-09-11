package ru.daniil.telegrambot.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long chatId;
    private String firstName;
    private Date lastMessageAt;
}