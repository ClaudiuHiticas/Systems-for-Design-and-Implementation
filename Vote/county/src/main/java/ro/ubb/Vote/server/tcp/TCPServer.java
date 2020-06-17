package ro.ubb.Vote.server.tcp;

import ro.ubb.Vote.domain.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class TCPServer {
    private ExecutorService executorService;
    private Consumer<Message> handler;
    public TCPServer(ExecutorService executorService)
    {
        this.executorService = executorService;
    }

    public void setHandler(Consumer<Message> handler)
    {
        this.handler = handler;
    }

    public void startServer(int port)
    {
        try (var serverSocket = new ServerSocket(port))
        {
            System.out.println("SERVER UP");
            while (true)
            {
                Socket client = serverSocket.accept();
                executorService.submit(new ClientHandler(client));
            }
        } catch (Exception e)
        {
            throw new RuntimeException("error connecting clients", e);
        }
    }

    private class ClientHandler implements Runnable
    {
        private Socket socket;

        public ClientHandler(Socket client)
        {
            this.socket = client;
        }

        @Override
        public void run()
        {
            try (var is = new ObjectInputStream(socket.getInputStream());
                 var os = new ObjectOutputStream(socket.getOutputStream()))
            {
                Message request = new Message();
                request.readFrom(is);
                handler.accept(request);
            } catch (IOException e)
            {
                throw new RuntimeException("error processing client", e);
            }
        }
    }
}
