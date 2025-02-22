package ru.otus.http.jserver.processors.crudAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.http.jserver.HttpRequest;
import ru.otus.http.jserver.application.ProductsService;
import ru.otus.http.jserver.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class DeleteProductProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(DeleteProductProcessor.class);
    private final ProductsService productsService;

    public DeleteProductProcessor(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public String urlPrefix() {
        return "/products";
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        if (request.containsParameter("id")) {
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                productsService.deleteProductById(id);
            } catch (NoSuchElementException e) {
                logger.info("Была попытка удаления продукта с несуществующим id = {}", id);
            }
        } else {
            productsService.deleteAllProducts();
        }

        String response = "" +
                "HTTP/1.1 204 No Content\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }


}
