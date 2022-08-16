package roommateproject.roommatebackend.entity;

import lombok.Data;

@Data
public class Message {

    public enum MessageType{
        ENTER, TALK
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}
