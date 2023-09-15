package ru.daniil.telegrambot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.daniil.telegrambot.models.Domain;
import ru.daniil.telegrambot.repository.IDomainRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class DomainService {
    private static final String LINK_TO_DOMAINS =
            "https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&item";
    private final IDomainRepository domainRepository;
    private final List<Domain> domainsList;

    @Autowired
    public DomainService(IDomainRepository domainRepository) {
        this.domainRepository = domainRepository;
        this.domainsList = getDomain();
    }

    public List<Domain> getDomain() {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(LINK_TO_DOMAINS).asString();
        } catch (UnirestException e) {
            log.error("Error: " + e);
        }
        try {
            return new ObjectMapper().readValue(
                    response != null ? response.getBody() : null, new TypeReference<>(){}
            );
        } catch (JsonProcessingException e) {
            log.error("Error: " + e);
        }
        return null;
    }

    public void createDomain() {
        clearDomain();
        domainRepository.saveAll(getDomain());
        log.info("New domains have been uploaded");
    }

    public String mailingDomain() {
        log.info("Domains have been sent to users");
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()) +
                " Cобрано " + domainsList.size() + " доменов";
    }

    private void clearDomain() {
        domainRepository.deleteAll();
        log.info("All domains have been deleted");
    }
}