Курсовой проект "Простой веб-сервер" по программе Otus Java Basic

Описание проекта:
- Язык: Java v21
- Сборщик проекта:Graddle
- Работа с БД: JOOQ + Liquibase + Драйвер Postgres
- Тестирование: JUnit
- Логирование: log4j (только консоль)
- Образ БД: контейнер с Postgres

Запуск проекта:
1) Запустить Docker Desktop с образом Postgres
2) Выполнить консольную команду для запуска контейнера: docker compose up -d
3) Выполнить консольную команду для создания схемы в БД и необходимых классов: .\gradlew generateJooq
4) Выполнить консольную команду для исполнения скриптов liquibase: .\gradlew update
5) Запустить приложение src/main/java/ru/otus/http/jserver/Application.java
