package ru.daniil.telegrambot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.daniil.telegrambot.models.Messages;

@Repository
public interface MessageRepository extends CrudRepository<Messages, Long> {
}