package ru.otus.http.jserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    private static final int THREAD_POOL_SIZE = 10;

    private final Dispatcher dispatcher;
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;

    public HttpServer(int port) throws IOException {
        this.dispatcher = new Dispatcher();
        this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.serverSocket = new ServerSocket(port);
    }

    public void start() {
        logger.info("Сервер запущен на порту: {}", serverSocket.getLocalPort());

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            logger.error(e);
            threadPool.shutdown();
        }
    }

    private void handleClient(Socket clientSocket) {
        logger.info("Подключился новый клиент");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()), 8192)) {
            String clientInputLine;

            while ((clientInputLine = in.readLine()) != null) {
                if (clientInputLine.isEmpty()) {
                    break;
                }
                logger.info("Запрос клиента: {}", clientInputLine);

                HttpRequest request = new HttpRequest(clientInputLine);
                request.info(true);

                dispatcher.execute(request, clientSocket.getOutputStream());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

