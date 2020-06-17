package ro.ubb.sdi08;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.sdi08.ui.Console;

public class ClientApp {
    public static void main(String[] args) {
        new ClientApp().run();
    }

    private void run() {
        System.out.println("client started");
        final var appContext = new AnnotationConfigApplicationContext("ro.ubb.sdi08.config");
        Console console = appContext.getBean(Console.class);
        console.runConsole();
        System.out.println("bye client");
    }
}
