package ru.otus.http.jserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.http.jserver.application.ProductsService;
import ru.otus.http.jserver.processors.Default400Processor;
import ru.otus.http.jserver.processors.Default500Processor;
import ru.otus.http.jserver.processors.RequestProcessor;
import ru.otus.http.jserver.processors.ReturnFileProcessor;
import ru.otus.http.jserver.processors.crudAPI.CreateProductProcessor;
import ru.otus.http.jserver.processors.crudAPI.DeleteProductProcessor;
import ru.otus.http.jserver.processors.crudAPI.GetProductsProcessor;
import ru.otus.http.jserver.processors.crudAPI.PutProductProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static ru.otus.http.jserver.HttpMethod.*;

public class Dispatcher {
    private static final Logger logger = LogManager.getLogger(Dispatcher.class);

    private final Map<HttpMethod, Set<RequestProcessor>> processorRoutes;

    private Default400Processor default400Processor;
    private Default500Processor default500Processor;

    public Dispatcher() {
        ProductsService productsService = new ProductsService();

        this.processorRoutes = Map.of(
                GET, Set.of(
                        new GetProductsProcessor(productsService),
                        new ReturnFileProcessor()
                ),
                POST, Set.of(
                        new CreateProductProcessor(productsService)
                ),
                PUT, Set.of(
                        new PutProductProcessor(productsService)
                ),
                DELETE, Set.of(
                        new DeleteProductProcessor(productsService)
                )
        );

        this.default400Processor = new Default400Processor();
        this.default500Processor = new Default500Processor();
    }

    public void execute(HttpRequest request, OutputStream output) throws IOException {
        try {
            var processors = processorRoutes.getOrDefault(request.getMethod(), Collections.emptySet());
            for (RequestProcessor processor : processors) {
                if (request.getUri().startsWith(processor.urlPrefix())) {
                    processor.execute(request, output);
                }
            }
        } catch (BadRequestException ex) {
            logger.error("Ошибка ввода данных", ex);
            request.setErrorCause(ex);
            default400Processor.execute(request, output);
        } catch (Exception ex) {
            logger.error("Серверная ошибка при обработке запроса", ex);
            default500Processor.execute(request, output);
        }
    }

}
