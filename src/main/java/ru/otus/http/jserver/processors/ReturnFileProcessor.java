package ru.otus.http.jserver.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.http.jserver.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ReturnFileProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(ReturnFileProcessor.class);
    private static final String filesDir = "src/main/resources/static";

    @Override
    public String urlPrefix() {
        return "/files";
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        File folder = new File(filesDir);

        File[] listOfFiles = folder.listFiles();
        Objects.requireNonNull(listOfFiles, "Каталог 'static' на сервере пуст");

        String requestPath = request.getUri().replaceFirst(urlPrefix(), "");
        String fileName = requestPath.substring(requestPath.lastIndexOf("/"));

        try {
            Path filePath = Paths.get(filesDir, requestPath);

            if (!filePath.toFile().exists()) {
                logger.error("Файла с названием = {} в каталоге нет", fileName);
                new Default404Processor().execute(request, output);
            }

            byte[] fileStream = Files.readAllBytes(filePath);

            String contentType = getContentType(fileName);
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "\r\n";
            byte[] responseBytes = response.getBytes();
            output.write(responseBytes);
            output.write(fileStream);

        } catch (NullPointerException e) {
            logger.error("При отправке файла с названием = {} произошла ошибка", fileName);
            new Default500Processor().execute(request, output);
        }
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith("html")) {
            return ("text/html");
        } else if (fileName.endsWith("jpg")) {
            return ("image/jpg");
        } else if (fileName.endsWith("jpeg")) {
            return ("image/jpeg");
        } else if (fileName.endsWith("gif")) {
            return ("image/gif");
        } else {
            return ("application/octet-stream");
        }
    }

}