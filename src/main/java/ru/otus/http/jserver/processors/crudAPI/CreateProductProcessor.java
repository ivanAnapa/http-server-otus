package ru.otus.http.jserver.processors.crudAPI;

import com.google.gson.Gson;
import ru.otus.http.jserver.HttpRequest;
import ru.otus.http.jserver.application.Product;
import ru.otus.http.jserver.application.ProductsService;
import ru.otus.http.jserver.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CreateProductProcessor implements RequestProcessor {
    private ProductsService productsService;

    public CreateProductProcessor(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Product newProduct = gson.fromJson(request.getBody(), Product.class);
        String response = "";
        if ( productsService.createNewProduct(newProduct)) {
            response = "" +
                    "HTTP/1.1 201 Created\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n";
        } else {
            response = "" +
                    "HTTP/1.1 409 Conflict\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n";
        }
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
