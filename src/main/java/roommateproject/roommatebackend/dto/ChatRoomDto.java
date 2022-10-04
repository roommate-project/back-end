package roommateproject.roommatebackend.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import roommateproject.roommatebackend.entity.User;

@Getter
@Setter
public class ChatRoomDto {

    private Long roomId;
    private UserInfo userInfo;
    @Nullable
    private String lastMessage;
    @Nullable
    private Date sendTime;

    public ChatRoomDto() {
    }

}
