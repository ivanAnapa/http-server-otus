package ru.otus.http.jserver;

public class Application {

    /**
     * Проект «Веб-сервер»
     * Сложность: 5-10
     * Примечание: проект является продолжением веб-сервера, реализованного на курсе
     * Цель работы: применить полученные на курсе знания
     * Задание: доработать веб-сервер:
     * •	Подключить базу данных
     * •	Реализовать полный набор CRUD операций над продуктами (все действия должны влиять на продукты в базе данных):
     * o	GET /items – получение всех продуктов
     * o	GET /items?id=1 – получение продукта, с указанным id (или более правильный, но сложный вариант /items/{id} – использование PathVariable)
     * o	POST /items – создание нового продукта
     * o	PUT /items – модификация продукта
     * o	DELETE /items?id=1 – удаление продукта с указанным id
     * •	* При запросе GET /[имя_файла] возвращать в теле ответа указанный файл из папки static
     * •	*** Можно прикрутить простой frontend
     * •	Сделать единую точку обработки исключений, с преобразованием их в 400/500 ответы
     * •	Сделать "красивые страницы" с ответами 400/404/500
     * •	*** Оптимизировать парсинг запроса
     * •	Возможность работать с запросами > 8кб (относительно учебного проекта)
     * •	Возможность ограничивать максимальный размер запроса/ответа
     * •	Можно сделать так, чтобы при запуске было возможно указывать используемый порт
     * •	Возможность конфигурировать сервер через properties файл (стандартный порт, максимальный размер запроса/ответа, размер пула потоков, ..)
     * •	* В заголовке запроса Accept можно указывать тип ожидаемого ответа (например, text/html или application/json, и сервер либо должен вернуть ответ указанного типа, либо ошибку 406 Not Acceptable). Соответственно, каждый Processor должен понимать данными какого типа он отвечает. Если в запросе такой хедер не указан, то считаем что он равен * / *
     * •    Если есть обработчик GET /something, но нет обработчика запроса POST /something, то при POST запрос необходимо кинуть ответ 405 Method Not Allowed(это значит что вы можете впринципе обрабатывать указанный uri, только с другим http-методом)
     * •    ***** Сделать поддержку сервлетов
     */

    public static void main(String[] args) {
        new HttpServer(8189).start();
    }
}
