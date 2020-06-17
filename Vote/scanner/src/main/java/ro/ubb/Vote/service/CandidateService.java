package ro.ubb.Vote.service;

import ro.ubb.Vote.domain.Message;
import ro.ubb.Vote.tcp.TCPClient;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class CandidateService {
    private final ExecutorService executorService;
    private final TCPClient tcpClient;
    public int counter;

    public CandidateService(ExecutorService executorService, TCPClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    public void generate(Integer id, String name) {
        CompletableFuture.supplyAsync(() -> {
            while (true) {
                Random random = new Random();
                int a = random.nextInt(2);
                int b = random.nextInt(2);
                int c = random.nextInt(2);
                Message message = new Message(new ArrayList<>());
                message.getBody().add(pair("name", name));
                message.getBody().add(pair("a", a));
                message.getBody().add(pair("b", b));
                message.getBody().add(pair("c", c));
                counter += 1;
                System.out.println(counter + " " + name + " " + a + " " + b + " " + c);
                tcpClient.send(message,id);
                try {
                    Thread.sleep(new Random().nextInt(3000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, this.executorService);
    }

    public static <K, V> Map.Entry<K, V> pair(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
}
