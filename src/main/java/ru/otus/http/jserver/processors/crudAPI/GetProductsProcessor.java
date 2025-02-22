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
import java.util.List;

public class GetProductsProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(GetProductsProcessor.class);
    private final ProductsService productsService;

    public GetProductsProcessor(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public String urlPrefix() {
        return "/products";
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        String jsonResult;
        Product product;
        String response;
        Gson gson = new Gson();
        if (request.containsParameter("id")) {
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                product = productsService.getProductById(id);
                jsonResult = gson.toJson(product);
                response = responseContent() + jsonResult;
            } catch (NullPointerException e) {
                logger.error("Была попытка поиска продукта с несуществующим id = {}", id);
                response = responseContent();
            }
        } else {
            List<Product> products = productsService.getAllProducts();
            jsonResult = gson.toJson(products);
            response = responseContent() + jsonResult;
        }
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

    private static String responseContent() {
        return "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "\r\n";
    }
}
