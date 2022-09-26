package roommateproject.roommatebackend.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomDto {

    private Long roomId;
    private String sender; //TODO: 상대방 이름? 상대방 아이디?
    private String lastMessage;
    private Date sendTime;
    private Long representImageId;

}
