package ru.otus.http.jserver.helperDB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import ru.otus.http.jserver.application.Product;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Crud {
    private static final Logger logger = LogManager.getLogger(Crud.class);
    private static final DSLContext context = getConnection();

    private static DSLContext getConnection() {
        Properties props = new Properties();
        java.nio.file.Path envFile = Paths.get(".env");
        try (InputStream inputStream = Files.newInputStream(envFile)) {
            props.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String userName = (String) props.get("OTUS_DB_USER"); // "postgres"
        String password = (String) props.get("OTUS_DB_PASSWORD"); // "postgres"
        String url = (String) props.get("OTUS_DB_URL"); // "jdbc:postgresql://localhost:5433/otus-db-2"
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return DSL.using(conn, SQLDialect.POSTGRES);
    }

    public static Result<Record> getAllProducts() {
        return context.select()
                .from(ru.otus.http.jserver.generated.tables.Product.PRODUCT)
                .fetch();
    }

    public static <R extends Record> R getOneProductByCondition(Table<R> table, Condition condition) {
        return context.fetchOne(table, condition);
    }

    public static void addNew(Product newProduct) {
        context.insertInto(ru.otus.http.jserver.generated.tables.Product.PRODUCT)
                .set(ru.otus.http.jserver.generated.tables.Product.PRODUCT.ID, newProduct.getId())
                .set(ru.otus.http.jserver.generated.tables.Product.PRODUCT.NAME, newProduct.getTitle())
                .execute();
    }

    public static void deleteAll() {
        context.truncate(ru.otus.http.jserver.generated.tables.Product.PRODUCT)
                .execute();
    }

    public static void deleteById(int productId) {
        if (getOneProductByCondition(
                ru.otus.http.jserver.generated.tables.Product.PRODUCT,
                ru.otus.http.jserver.generated.tables.Product.PRODUCT.ID.eq(productId)) == null) {
            throw new NullPointerException("Продукта с указанным id " + productId + " в БД нет");
        }
        context.deleteFrom(ru.otus.http.jserver.generated.tables.Product.PRODUCT)
                .where(ru.otus.http.jserver.generated.tables.Product.PRODUCT.ID.eq(productId))
                .execute();
    }

    public static void update(Product product) {
        if (getOneProductByCondition(
                ru.otus.http.jserver.generated.tables.Product.PRODUCT,
                ru.otus.http.jserver.generated.tables.Product.PRODUCT.ID.eq(product.getId())) == null) {
            throw new NullPointerException("Продукта с указанным id " + product.getId() + " в БД нет");
        }
        context.update(ru.otus.http.jserver.generated.tables.Product.PRODUCT)
                .set(ru.otus.http.jserver.generated.tables.Product.PRODUCT.ID, product.getId())
                .set(ru.otus.http.jserver.generated.tables.Product.PRODUCT.NAME, product.getTitle())
                .where(ru.otus.http.jserver.generated.tables.Product.PRODUCT.ID.eq(product.getId()))
                .execute();
    }
}
