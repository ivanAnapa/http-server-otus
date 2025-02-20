# Курсовой проект "Простой веб-сервер" по программе Otus Java Basic

### Описание проекта:
- Язык: Java v21
- Сборщик проекта:Gradle
- Работа с БД: JOOQ + Liquibase + Драйвер Postgres
- Тестирование: JUnit
- Логирование: log4j (только консоль)
- Образ БД: контейнер с Postgres

### Запуск проекта:
1) Запустить Docker Desktop с образом Postgres
2) Выполнить консольную команду для запуска контейнера: docker compose up -d
3) Выполнить консольную команду для создания схемы в БД и необходимых классов: .\gradlew generateJooq
4) Выполнить консольную команду для исполнения скриптов liquibase: .\gradlew update
5) Запустить приложение src/main/java/ru/otus/http/jserver/Application.java

### Функционал:
1) Получение списка всех продуктов в БД: GET http://localhost:8189/products
2) Получение продукта по id в БД: GET http://localhost:8189/products?id={id}
3) Добавление продукта в БД: POST http://localhost:8189/products с телом запроса в виде {"id":11,"title":"Bread"} 
4) Изменение продукта в БД: PUT http://localhost:8189/products с телом запроса в виде {"id":11,"title":"Bread"} 
5) Удаление всех продуктов в БД: DELETE http://localhost:8189/products
6) Удаление продукта по id в БД: DELETE http://localhost:8189/products?id={id}
