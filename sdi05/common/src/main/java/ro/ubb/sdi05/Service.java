package ro.ubb.sdi05;

import java.util.concurrent.CompletableFuture;

public interface Service {
    CompletableFuture<Message> message(Message request);
}
