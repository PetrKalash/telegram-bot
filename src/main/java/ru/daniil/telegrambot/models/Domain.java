package ru.daniil.telegrambot.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "daily_domains")
public class Domain {
    @Id
    @GeneratedValue
    private Integer id;
    @JsonProperty(value = "domainname")
    private String domainName;
    private Integer hotness;
    private Integer price;
    @JsonProperty(value = "x_value")
    private Double xValue;
    @JsonProperty(value = "yandex_tic")
    private Integer yandexTic;
    private Integer links;
    private Integer visitors;
    private String registrar;
    private Integer old;
    @JsonProperty(value = "delete_date")
    private Date deleteDate;
    private Boolean rkn;
    private Boolean judicial;
    private Boolean block;
}