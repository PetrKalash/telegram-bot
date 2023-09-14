package ru.daniil.telegrambot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.daniil.telegrambot.models.Domain;

@Repository
public interface IDomainRepository extends CrudRepository<Domain, Long> {
}