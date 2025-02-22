package ru.otus.http.jserver.processors.crudAPI;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.http.jserver.HttpRequest;
import ru.otus.http.jserver.application.Product;
import ru.otus.http.jserver.application.ProductsService;
import ru.otus.http.jserver.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class PutProductProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(PutProductProcessor.class);

    private final ProductsService productsService;

    public PutProductProcessor(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public String urlPrefix() {
        return "/products";
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        String response;

        try {
            Product product = gson.fromJson(request.getBody(), Product.class);
            productsService.modifyProduct(product);
            response = "" +
                    "HTTP/1.1 201 Created\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n";
        } catch (NoSuchElementException | NullPointerException e) {
            logger.error(e.getMessage());
            response = "" +
                    "HTTP/1.1 204 No Content\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n";
        }
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}
