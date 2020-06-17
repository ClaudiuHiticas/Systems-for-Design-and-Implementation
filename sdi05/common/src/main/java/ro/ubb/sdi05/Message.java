package ro.ubb.sdi05;

import java.io.Serializable;
import java.util.Arrays;

public class Message implements Serializable {
    private static final long serialVersionUID = -1964328975658520510L;
    private String header;
    private Object[] body;

    public Message() {
    }

    public Message(String header) {
        this.header = header;
        this.body = "".split("");
    }

    public Message(String header, Object... body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Object[] getBody() {
        return body;
    }

    public void setBody(Object... body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "header='" + header + '\'' +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
