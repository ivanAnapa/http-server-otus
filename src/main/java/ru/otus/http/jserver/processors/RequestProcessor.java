package ru.otus.http.jserver.processors;

import ru.otus.http.jserver.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {

    default String urlPrefix() {
        return "";
    }

    void execute(HttpRequest request, OutputStream output) throws IOException;
}
