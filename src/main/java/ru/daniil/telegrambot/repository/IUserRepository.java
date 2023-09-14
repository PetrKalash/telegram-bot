package ru.daniil.telegrambot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.daniil.telegrambot.models.User;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {
}