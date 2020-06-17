package ro.ubb.sdi05.tcp;


import ro.ubb.sdi05.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.util.Collections.singletonList;
import static ro.ubb.sdi05.CommonHelper.*;

public class TcpClient {
    public Object sendAndReceive(Message sendMsg) {
        try (var socket = new Socket(HOST, PORT);
             var objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             var objectInputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            objectOutputStream.writeObject(sendMsg);
            return objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return new Message(BAD_MSG, singletonList("something went wrong in tcpClient"));
    }
}
