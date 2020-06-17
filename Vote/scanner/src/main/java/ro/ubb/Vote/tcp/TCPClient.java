package ro.ubb.Vote.tcp;

import ro.ubb.Vote.domain.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPClient {
    public void send(Message request, int port) {
        try (var socket = new Socket(Message.HOST, port);
             var os = new ObjectOutputStream(socket.getOutputStream())) {
            request.writeTo(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
