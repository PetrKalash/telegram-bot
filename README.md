Используемые технологии:
Java 11
Maven
Spring Boot 2.7.15
PostgreSQL 15
Liquibase
spring-boot-starter-logging
telegrambots-spring-boot-starter

SpringBot

Функционал:
1. Регистрирует всех обратившихся к нему пользователей в таблице users
и обновляет поле last_message_at при каждом сообщении, структуру
таблицы продумать самостоятельно.
2. Регистрирует все входящие к нему сообщения и ответы который он
отправил в таблице messages (связанной с users), структуру таблицы
продумать самостоятельно.
3. Раз в сутки забирает данные из
https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&item
s=50 и складывает в таблицу daily_domains (данные прошлого дня
удаляются), структуру таблицы продумать самостоятельно.
4. Раз в сутки, после сбора базы, рассылает всем пользователям которые у
него зарегистрированы сообщение "YYY-MM-dd. Собрано N доменов".

Бот установлен на удаленный Linux-сервер и доступен по ссылке https://t.me/SpringSmartBot

![image](https://github.com/PetrKalash/telegram-bot/assets/100221510/0ee7dc08-890f-4054-b5f0-8525bfec60fd)
![image](https://github.com/PetrKalash/telegram-bot/assets/100221510/e887e079-7619-44b5-827d-4ed67128e5de)
![image](https://github.com/PetrKalash/telegram-bot/assets/100221510/9b5bd8a5-e163-4f55-89c4-08ff19bd5842)
![image](https://github.com/PetrKalash/telegram-bot/assets/100221510/57bf77b7-93cd-41d3-b47b-36e931331941)
