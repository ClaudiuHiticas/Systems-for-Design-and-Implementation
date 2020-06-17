package ro.ubb.Vote;

import ro.ubb.Vote.service.CandidateService;
import ro.ubb.Vote.tcp.TCPClient;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        TCPClient tcpClient = new TCPClient();
        CandidateService sensorService = new CandidateService(executorService, tcpClient);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Enter id");
        int id = Integer.parseInt(scanner.nextLine());
        if (id <= 1023) {
            System.out.println("Id should be greater than 1023");
        }
        sensorService.generate(id, name);

    }
}
