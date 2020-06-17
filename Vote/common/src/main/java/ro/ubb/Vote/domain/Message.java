package ro.ubb.Vote.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Message implements Serializable
{
    public static final String HOST = "localhost";

    private List<Map.Entry<String, Object>> body;

    public Message()
    {

    }

    public Message(List<Map.Entry<String, Object>> body)
    {
        this.body = body;
    }

    public void writeTo(ObjectOutputStream os)
    {
        try
        {
            os.writeObject(this);
        } catch (IOException e)
        {
            throw new RuntimeException("Failed to write object to stream");
        }
    }

    public void readFrom(ObjectInputStream is)
    {
        try
        {
            Message message = (Message) is.readObject();
            setBody(message.getBody());
        } catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to read message from socket.");
        }
    }

    public Optional<Object> getSingleParameter(String paramName)
    {
        return body.stream().filter(entry -> entry.getKey().equals(paramName)).map(Map.Entry::getValue).findFirst();
    }

    public List<Map.Entry<String, Object>> getBody()
    {
        return body;
    }

    public void setBody(List<Map.Entry<String, Object>> body)
    {
        this.body = body;
    }
}

