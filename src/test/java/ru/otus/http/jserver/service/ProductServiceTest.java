package ru.otus.http.jserver.service;

import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.http.jserver.generated.tables.Product;
import ru.otus.http.jserver.generated.tables.records.ProductRecord;
import ru.otus.http.jserver.helperDB.Crud;

import static ru.otus.http.jserver.helperDB.Crud.getAllProducts;

public class ProductServiceTest {

    @Test
    void readProductsFromDbAndCheckFirstValue() {
        Result<Record> products = getAllProducts();

        products.forEach(product -> {
            Integer id = product.getValue(Product.PRODUCT.ID);
            String name = product.getValue(Product.PRODUCT.NAME);
            System.out.printf("Продукт %s с id: %d\n", name, id);
        });

        ProductRecord product = Crud.getOneProductByCondition(
                Product.PRODUCT,
                Product.PRODUCT.ID.eq(1)
        );

        Assertions.assertEquals(
                "Milk",
                product.getName(),
                "Название первого продукта не соответствует ожидаемому"
        );
    }
}
