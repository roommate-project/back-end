package roommateproject.roommatebackend.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {

    private String message;
    private Boolean isMe;
    private Date sendTime;
    private Boolean isImage;
    private String image;

}
