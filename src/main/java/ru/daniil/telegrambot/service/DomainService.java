package ru.daniil.telegrambot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.daniil.telegrambot.models.Domain;
import ru.daniil.telegrambot.repository.DomainRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@Component
public class DomainService {
    private static final String LINK_TO_DOMAINS =
            "https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&item";
    private final DomainRepository domainRepository;
    private List<Domain> domainsList;

    @Autowired
    public DomainService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    public List<Domain> getDomain() {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response;
        try {
            response = Unirest.get(LINK_TO_DOMAINS).asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        try {
            return new ObjectMapper().readValue(response.getBody(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDomain() {
        domainsList = getDomain();
        domainRepository.saveAll(domainsList);
    }

    public void clearDomain() {
        domainRepository.deleteAll();
    }

    public String mailingDomain() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()) +
                " Cобрано " + domainsList.size() + " доменов";
    }
}