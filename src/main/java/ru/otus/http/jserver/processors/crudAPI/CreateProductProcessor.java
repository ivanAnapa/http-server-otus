package ru.otus.http.jserver.processors.crudAPI;

import com.google.gson.Gson;
import ru.otus.http.jserver.HttpRequest;
import ru.otus.http.jserver.application.Product;
import ru.otus.http.jserver.application.ProductsService;
import ru.otus.http.jserver.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.*;

public class CreateProductProcessor implements RequestProcessor {

    private final ProductsService productsService;

    public CreateProductProcessor(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public String urlPrefix() {
        return "/products";
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Product newProduct = gson.fromJson(request.getBody(), Product.class);

        String responseCode  = productsService.createNewProduct(newProduct) ? "201 Created" : "409 Conflict";
        String response = new MessageFormat("HTTP/1.1 {0}\r\nContent-Type: text/html\r\n\r\n").format(responseCode);
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
