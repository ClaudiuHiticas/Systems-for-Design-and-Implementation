package ro.ubb.sdi08;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.sdi08.repo.db.RepoDbHelper;

public class ServerApp {

    public static void main(String[] args) {
        new ServerApp().run();
    }

    private void run() {
        System.out.println("server started");
        new AnnotationConfigApplicationContext("ro.ubb.sdi08.config");
        cleanAllTables();
        System.out.println("Bye");
    }

    private void cleanAllTables() {
        System.out.println("clean all tables");
        RepoDbHelper.cleanAllTables();
    }
}
