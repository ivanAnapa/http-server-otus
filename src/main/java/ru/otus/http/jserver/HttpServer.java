package ru.otus.http.jserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    private static final int THREAD_POOL_SIZE = 10;
    private int port;

    private final Dispatcher dispatcher;
    private final ExecutorService threadPool;

    public HttpServer(int port) {
        this.dispatcher = new Dispatcher();
        this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту: " + port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                threadPool.execute(() -> handleRequest(socket));
            }
        } catch (IOException e) {
            logger.error(e);
        } finally {
            threadPool.shutdown();
        }
    }

    private void handleRequest(Socket socket) {
        byte[] buffer = new byte[8192];
        try {
            int n = socket.getInputStream().read(buffer);
            logger.info("Подключился новый клиент");
            HttpRequest request = new HttpRequest(new String(buffer, 0, n));
            request.info();
            dispatcher.execute(request, socket.getOutputStream());
            socket.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}

