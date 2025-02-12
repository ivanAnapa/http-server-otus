package ru.otus.http.jserver.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Record;
import org.jooq.Result;
import ru.otus.http.jserver.generated.tables.records.ProductRecord;
import ru.otus.http.jserver.helperDB.Crud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductsService {
    private static final Logger logger = LogManager.getLogger(ProductsService.class);

    private List<Product> products;

    public ProductsService() {
        getAllProducts();
    }

    public List<Product> getAllProducts() {
        logger.info("Получение всех продуктов");
        this.products = new ArrayList<>();
        Result<Record> allProducts = Crud.getAllProducts();
        allProducts.forEach(product -> {
            Integer id = product.getValue(ru.otus.http.jserver.generated.tables.Product.PRODUCT.ID);
            String name = product.getValue(ru.otus.http.jserver.generated.tables.Product.PRODUCT.NAME);
            products.add(new Product(id, name));
        });
        return Collections.unmodifiableList(products);
    }

    public Product getProductById(int id) {
        logger.info("Получение продукта с id = {}", id);
        ProductRecord product = Crud.getOneProductByCondition(
                ru.otus.http.jserver.generated.tables.Product.PRODUCT,
                ru.otus.http.jserver.generated.tables.Product.PRODUCT.ID.eq(id)
        );
        if (product == null) {
            throw new NullPointerException("Продукта с id = " + id + " в БД нет");
        }
        return new Product(product.getId(), product.getName());
    }

    public boolean createNewProduct(Product product) {
        logger.info("Добавление нового продукта {} с id: {}", product.getTitle(), product.getId());
        try {
            Crud.addNew(product);
            logger.info("Продукта {} с id: {} добавлен успешно", product.getTitle(), product.getId());
            return true;
        } catch (Exception e) {
            logger.error("Ошибка при попытке добавления нового продукта");
            e.printStackTrace();
        }
        return false;

    }

    public void deleteProductById(int id) {
        logger.info("Удаление продукта с id = {}", id);
        try {
            Crud.deleteById(id);
        } catch (NullPointerException e) {
            logger.error(e);
        }
    }

    public void deleteAllProducts() {
        logger.info("Удаление всех продуктов");
        Crud.deleteAll();
    }

    public void modifyProduct(Product product) {
        logger.info("Обновление продукта с id = {}", product.getId());
        if (product.getId() == null) {
            throw new NoSuchElementException("В параметрах продукта отсутствует id");
        }
        if (product.getTitle() == null) {
            throw new NoSuchElementException("В параметрах продукта отсутствует title");
        }
        Crud.update(product);
    }
}
