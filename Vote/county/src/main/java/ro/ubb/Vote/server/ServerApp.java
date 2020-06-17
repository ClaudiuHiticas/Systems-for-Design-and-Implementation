package ro.ubb.Vote.server;

import ro.ubb.Vote.server.domain.ScannerVote;
import ro.ubb.Vote.server.service.VoteService;
import ro.ubb.Vote.server.tcp.TCPServer;

import javax.xml.validation.Validator;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        TCPServer tcpServer = new TCPServer(executorService);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Enter county id");
        int id = Integer.parseInt(scanner.nextLine());

        VoteService voteService = new VoteService();

        tcpServer.setHandler(message -> {
            System.out.println(message.getBody());
            String scannerName = (String) message.getSingleParameter("name").orElseThrow();
            Integer a = (Integer) message.getSingleParameter("a").orElseThrow();
            Integer b = (Integer) message.getSingleParameter("b").orElseThrow();
            Integer c = (Integer) message.getSingleParameter("c").orElseThrow();
            System.out.println("(" + scannerName + ", " + a + ", " + b + ", " + c + ")");
            ScannerVote scannerVote = new ScannerVote(scannerName,a,b,c);
            voteService.addVote(scannerVote);
            try {
                Thread.sleep(new Random().nextInt(4000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        CompletableFuture.supplyAsync(() -> {
        while(true){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            voteService.sendVotes();
        }},executorService);

        tcpServer.startServer(id);
    }
}
