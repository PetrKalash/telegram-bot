package ru.daniil.telegrambot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.daniil.telegrambot.models.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
}